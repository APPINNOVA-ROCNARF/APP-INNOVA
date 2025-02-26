package com.rocnarf.rocnarf.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Vehiculo {
    @SerializedName("idVehiculo")
    private int idVehiculo;

    @SerializedName("idAsesor")
    private String idAsesor;

    @SerializedName("placa")
    private String placa;

    @SerializedName("fechaRegistro")
    private Date fechaRegistro;

    // 🔹 Constructor vacío necesario para Retrofit, Firebase y deserialización JSON
    public Vehiculo() {}

    // 🔹 Constructor con parámetros
    public Vehiculo(String idAsesor, String placa) {
        this.idAsesor = idAsesor;
        this.placa = placa;
    }

    // Getters y Setters
    public int getIdVehiculo() { return idVehiculo; }
    public void setIdVehiculo(int idVehiculo) { this.idVehiculo = idVehiculo; }

    public String getIdAsesor() { return idAsesor; }
    public void setIdAsesor(String idAsesor) { this.idAsesor = idAsesor; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}

