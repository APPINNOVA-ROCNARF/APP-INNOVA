package com.rocnarf.rocnarf.models;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "escala_bonificacion",
        indices = {@Index(value="idEscalaBonificacion")})
public class EscalaBonificacion {

    @PrimaryKey
    @NonNull
    @SerializedName("idEscalaBonificacion")
    @Expose
    private int idEscalaBonificacion;

    @SerializedName("idEscala")
    @Expose
    private int idEscala;

    @SerializedName("cantidad")
    @Expose
    private int cantidad;

    @SerializedName("bonificacion")
    @Expose
    private int bonificacion;

    @SerializedName("porcentaje")
    @Expose
    private Double porcentaje;

    @SerializedName("nombre")
    @Expose
    private String nombre;

    public int getIdEscalaBonificacion() {
        return idEscalaBonificacion;
    }

    public void setIdEscalaBonificacion(int idEscalaBonificacion) {
        this.idEscalaBonificacion = idEscalaBonificacion;
    }

    public int getIdEscala() {
        return idEscala;
    }

    public void setIdEscala(int idEscala) {
        this.idEscala = idEscala;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getBonificacion() {
        return bonificacion;
    }

    public void setBonificacion(int bonificacion) {
        this.bonificacion = bonificacion;
    }

    public Double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getNombre() {
         return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
