package com.rocnarf.rocnarf.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.annotation.NonNull;

import com.rocnarf.rocnarf.dao.TimestampConverter;

import java.util.Date;

@Entity(tableName = "sincronizacion", primaryKeys = {"idUsuario","entidad"})
public class Sincronizacion {

    @NonNull
    private  String idUsuario ;

    @NonNull
    private  String entidad;

    @TypeConverters({TimestampConverter.class})
    private Date fechaSincronizacion;

    @NonNull
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(@NonNull String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date getFechaSincronizacion() {
        return fechaSincronizacion;
    }

    public void setFechaSincronizacion(Date fechaSincronizacion) {
        this.fechaSincronizacion = fechaSincronizacion;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }
}
