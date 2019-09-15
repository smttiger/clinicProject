package com.itstep.fabiyanski.model;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Room {
    private Doctor doctor;
    private boolean isAvailable;
    private Patient patient;
    private int roomNumber;
    private Lock lock;
    private static AtomicInteger counter = new AtomicInteger(0);


    public Room() {
        this.isAvailable = true;
        this.doctor = null;
        this.patient = null;
        this.lock = new ReentrantLock();
        this.roomNumber = counter.incrementAndGet();

    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }


    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public Lock getLock() {
        return lock;
    }

}
