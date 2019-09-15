package com.itstep.fabiyanski.model;

import com.itstep.fabiyanski.Main;
import com.itstep.fabiyanski.service.Clinic;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

public class Doctor extends Thread {
    private int numberOfCuredPatients;
    private final int MAX_AMOUNT_OF_PATIENTS_PER_DAY = 5;
    private int numberOfDoctor;
    private Room room;
    private Clinic clinic;
    private static final AtomicInteger COUNT = new AtomicInteger(0);

    public Doctor(Clinic clinic) {
        this.numberOfCuredPatients = 0;
        this.numberOfDoctor = COUNT.incrementAndGet();
        this.clinic = clinic;
        Main.LOG.info("A doctor number " + this.numberOfDoctor + " began to work");
    }

    @Override
    public void run() {
        while (numberOfCuredPatients < MAX_AMOUNT_OF_PATIENTS_PER_DAY) {
            if (this.room.isAvailable() && (this.clinic.getPatients().size() == 0)) {
                doctorToWait();
            } else {
                if (this.room.isAvailable()) {
                    getQueue();
                }
            }
            System.out.println(currentThread());
            System.out.println(getNumberOfCuredPatients());
            continue;
        }
        this.room.setAvailable(false);
        Main.LOG.info("A doctor number " + this.numberOfDoctor + " has finished to work");
    }


    public void doctorToWait() {
        Lock lock = this.room.getLock();
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            toCureAPatient();
        }
    }

    public void getQueue() {
        Patient patient = this.clinic.getPatients().poll();
        if (patient != null) {
            this.room.setPatient(patient);
            Main.LOG.info("A doctor number " + numberOfDoctor + " took a patient " +
                    room.getPatient().getQueueNumber() + " from line in room number "
                    + this.room.getRoomNumber());
            this.room.setAvailable(false);
            toCureAPatient();
        }
    }

    private void toCureAPatient() {
        int time = this.room.getPatient().getTimeToGetHealthy();
        this.room.getPatient().setTimeToGetHealthy(0);
        try {
            this.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.numberOfCuredPatients++;
        this.room.setAvailable(true);
        Main.LOG.info("A patient number " + this.room.getPatient().getQueueNumber() + " was cured");
    }

    public int getNumberOfCuredPatients() {
        return numberOfCuredPatients;
    }

    public void setRoom(Room room) {
        this.room = room;
    }


}
