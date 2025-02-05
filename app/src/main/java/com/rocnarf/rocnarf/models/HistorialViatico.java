package com.rocnarf.rocnarf.models;

import java.io.Serializable;
import java.util.Date;

public class HistorialViatico implements Serializable {

    private int idCiclo;
    private String descripcion;
    private Date fechaPeriodo;
    private int estadoViatico;

    public int getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(int idCiclo) {
        this.idCiclo = idCiclo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaPeriodo() {
        return fechaPeriodo;
    }

    public void setFechaPeriodo(Date fechaPeriodo) {
        this.fechaPeriodo = fechaPeriodo;
    }

    public int getEstadoViatico() {
        return estadoViatico;
    }

    public void setEstadoViatico(int estadoViatico) {
        this.estadoViatico = estadoViatico;
    }




}
