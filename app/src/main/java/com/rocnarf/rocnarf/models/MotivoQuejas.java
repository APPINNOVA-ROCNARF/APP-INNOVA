package com.rocnarf.rocnarf.models;

public class MotivoQuejas {
    private int id;
    private String descripcion;

    public MotivoQuejas(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        // Esto es lo que se mostrará en el Spinner
        return descripcion;
    }
}

