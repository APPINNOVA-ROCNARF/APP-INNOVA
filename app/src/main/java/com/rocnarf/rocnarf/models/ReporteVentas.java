package com.rocnarf.rocnarf.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "repoteVentas")
public class ReporteVentas {

    @PrimaryKey
    @SerializedName("idCodigo")
    @Expose
    private  int IdCodigo ;



    public int getIdCodigo() {
        return IdCodigo;
    }

    public void setIdCodigo(int idCodigo) {
        IdCodigo = idCodigo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean status) {
        Status = status;
    }

    public String getCilco() {
        return Cilco;
    }

    public void setCilco(String cilco) {
        Cilco = cilco;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date fecha) {
        Fecha = fecha;
    }

    @SerializedName("nombre")
    @Expose
    private  String Nombre;


    @SerializedName("status")
    @Expose
    private  Boolean Status;

    @SerializedName("ciclo")
    @Expose
    private  String Cilco;

    @SerializedName("fecha")
    @Expose
    private  Date Fecha;


}
