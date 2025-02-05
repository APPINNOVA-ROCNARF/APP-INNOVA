package com.rocnarf.rocnarf.models;

import java.util.Date;

public class Periodo {
    private int idPeriodo;
    private String descripcion;
    private Date fechaPeriodo;
    private String estado;
    private Date finPeriodo;

    public Integer getEstadoCiclo() {
        return estadoCiclo;
    }

    public void setEstadoCiclo(Integer estadoCiclo) {
        this.estadoCiclo = estadoCiclo;
    }

    private Integer estadoCiclo;

    public Date getFinPeriodo() {
        return finPeriodo;
    }

    public void setFinPeriodo(Date finPeriodo) {
        this.finPeriodo = finPeriodo;
    }

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
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


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
