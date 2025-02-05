package com.rocnarf.rocnarf.models;

import android.app.Application;

public class Enviroment  extends Application {
    public String modalCumple = "N";

    private static final Enviroment ourInstance = new Enviroment();
    public static Enviroment getInstance() {
        return ourInstance;
    }
    private Enviroment() {
    }

    public String getModalCumple() {
        return modalCumple;
    }

    public void setModalCumple(String modalCumple) {
        this.modalCumple = modalCumple;
    }
}
