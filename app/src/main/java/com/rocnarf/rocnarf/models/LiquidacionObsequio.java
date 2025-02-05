package com.rocnarf.rocnarf.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "liquidacionObsequio")
public class LiquidacionObsequio implements Serializable {

    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @Expose
    private  int Id ;

    private  int  idPlan;

    @SerializedName("nombrePlan")
    @Expose
    private  String NombrePlan;

    @SerializedName("idCliente")
    @Expose
    private  String IdCliente;

    @SerializedName("nombreCliente")
    @Expose
    private  String NombreCliente;

    @SerializedName("obsequio")
    @Expose
    private  String Obsequio;

    @SerializedName("idAsesor")
    @Expose
    private  String IdAsesor;

    @SerializedName("fechaCreacion")
    private Date FechaCreacion;

    @SerializedName("estadoSolicitud")
    @Expose
    private  String EstadoSolicitud;

    @SerializedName("observacion")
    @Expose
    private  String Observacion;

    public int getId() {
        return Id;
    }

//    public void setIdCodigo(int id) {
//        Id = id;
//    }

    public int getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(int idPlan) {
        this.idPlan = idPlan;
    }

    public String getNombrePlan() {
        return NombrePlan;
    }

    public void setNombrePlan(String nombrePlan) {
        NombrePlan = nombrePlan;
    }

    public String getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(String idCliente) {
        IdCliente = idCliente;
    }

    public String getNombreCliente() {
        return NombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        NombreCliente = nombreCliente;
    }

    public String getObsequio() {
        return Obsequio;
    }

    public void setObsequio(String obsequio) {
        Obsequio = obsequio;
    }

    public String getIdAsesor() {
        return IdAsesor;
    }

    public void setIdAsesor(String idAsesor) {
        IdAsesor = idAsesor;
    }

    public Date getFechaCreacion() {
        return FechaCreacion;
    }

//    public void setFechaCreacion(Date fechaCreacion) {
//        FechaCreacion = fechaCreacion;
//    }

    public String getEstadoSolicitud() {
        return EstadoSolicitud;
    }

    public void setEstadoSolicitud(String estadoSolicitud) {
        EstadoSolicitud = estadoSolicitud;
    }

    public String getObservacion() {
        return Observacion;
    }

    public void setObservacion(String observacion) {
        Observacion = observacion;
    }


}
