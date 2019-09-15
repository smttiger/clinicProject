package com.itstep.fabiyanski.service;

import com.itstep.fabiyanski.Main;
import com.itstep.fabiyanski.model.Doctor;
import com.itstep.fabiyanski.model.Patient;
import com.itstep.fabiyanski.model.Room;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class Clinic {
    private Collection<Room> rooms;
    private Queue<Patient> patients;
    private Set<Doctor> doctors;

    public Clinic(int amountOfRooms, int lineSize) {
        this.rooms = new ArrayList<>(amountOfRooms);
        this.patients = new ArrayBlockingQueue<>(lineSize);
        doctors = new HashSet<>();
        for (int i = 0; i < amountOfRooms; i++) {
            rooms.add(new Room());
        }
        for (Room room : rooms) {
            Doctor doctor = new Doctor(this);
            doctors.add(doctor);
            doctor.setRoom(room);
            doctor.start();
        }
        Main.LOG.info("Clinic is open");
    }

    public Collection<Room> getRooms() {
        return rooms;
    }

    public Queue<Patient> getPatients() {
        return patients;
    }

    public void toTakeAPatient(Patient patient) {
        patient.start();
    }

    public Set<Doctor> getDoctors() {
        return doctors;
    }

}
