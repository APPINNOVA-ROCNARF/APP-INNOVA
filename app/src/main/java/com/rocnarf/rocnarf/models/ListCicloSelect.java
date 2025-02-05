package com.rocnarf.rocnarf.models;

import java.util.Date;

public class ListCicloSelect {
    private int id;
    private String descripcion;
    private Date finPeriodo;

    private Integer estadoCiclo;


    public ListCicloSelect(int id, String descripcion, Date finPeriodo, Integer estadoCiclo) {
        this.id = id;
        this.descripcion = descripcion;
        this.finPeriodo = finPeriodo;
        this.estadoCiclo = estadoCiclo;
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public Date getFinPeriodo() {
        return finPeriodo;
    }

    public Integer getEstadoCiclo() {
        return estadoCiclo;
    }
    @Override
    public String toString() {
        // Esto es lo que se mostrará en el Spinner
        return descripcion;
    }
}

