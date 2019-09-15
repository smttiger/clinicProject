package com.itstep.fabiyanski;

import com.itstep.fabiyanski.model.Patient;
import com.itstep.fabiyanski.service.Clinic;

import java.util.logging.Logger;

public class Main {
    final static public Logger LOG = Logger.getLogger("logger");

    public static void main(String[] args) {
        Clinic clinic = new Clinic(5, 10);
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Patient patient = new Patient(clinic);
            clinic.toTakeAPatient(patient);
        }
    }
}

