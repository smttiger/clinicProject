package com.itstep.fabiyanski.model;

import com.itstep.fabiyanski.Main;
import com.itstep.fabiyanski.service.Clinic;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

public class Patient extends Thread {

    private int queueNumber;
    private int timeToGetHealthy;
    private Clinic clinic;
    private static AtomicInteger count = new AtomicInteger(0);

    public Patient(Clinic clinic) {
        this.clinic = clinic;
        this.timeToGetHealthy = 500 * (10 + new Random().nextInt(9));
        this.queueNumber = count.incrementAndGet();
        Main.LOG.info("A patient number " + this.queueNumber + " has come");
    }

    @Override
    public void run() {
        if (!this.clinic.getDoctors().stream().filter(Thread::isAlive).collect(Collectors.toList()).isEmpty()) {
            if (this.clinic.getPatients().isEmpty()) {
                if (comeToRoom()) {
                } else {
                    tryToGetInLine();
                }
            } else {
                tryToGetInLine();
            }
        }
    }

    private void tryToGetInLine() {
        if (getInLine()) {
            if (this.timeToGetHealthy != 0) {
                this.clinic.getPatients().remove(this);
                Main.LOG.info("A patient number " + this.queueNumber + " was waiting too much");
            }
        }
    }


    private boolean comeToRoom() {
        for (Room room : this.clinic.getRooms()) {
            if (room.isAvailable()) {
                room.setPatient(this);
                room.setAvailable(false);
                Main.LOG.info("A patient number " + this.queueNumber + "has come to room number " +
                        room.getRoomNumber());
                Lock lock = room.getLock();
                synchronized (lock) {
                    lock.notify();
                }
                try {
                    this.sleep(this.timeToGetHealthy);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    private boolean getInLine() {
        boolean isInLine;
        try {
            if (isInLine = this.clinic.getPatients().add(this)) {
                Main.LOG.info("A patient number " + this.queueNumber + " got in line");
            }
            try {
                sleep(1000 * (10 + new Random().nextInt(30)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IllegalStateException ex) {
            Main.LOG.info("Clinic is full, a patient number " + this.queueNumber + " could not get in line and went home");
            return false;
        }
        return isInLine;
    }

    public int getQueueNumber() {
        return queueNumber;
    }

    public int getTimeToGetHealthy() {
        return timeToGetHealthy;
    }

    public void setTimeToGetHealthy(int timeToGetHealthy) {
        this.timeToGetHealthy = timeToGetHealthy;
    }
}
