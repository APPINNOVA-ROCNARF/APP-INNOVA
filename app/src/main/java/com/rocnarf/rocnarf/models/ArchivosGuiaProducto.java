package com.rocnarf.rocnarf.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ArchivosGuiaProducto {

    @PrimaryKey
    @SerializedName("idFile")
    @Expose
    private  int IdFile ;

    @SerializedName("idGuia")
    @Expose
    private  String IdGuia;

    @SerializedName("nombre")
    @Expose
    private  String Nombre;

    @SerializedName("fechaCreacion")
    @Expose
    private Date FechaCreacion;

    public int getIdFile() {
        return IdFile;
    }

    public void setIdFile(int idFile) {
        IdFile = idFile;
    }

    public String getIdGuia() {
        return IdGuia;
    }

    public void setIdGuia(String idGuia) {
        IdGuia = idGuia;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public Date getFechaCreacion() {
        return FechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        FechaCreacion = fechaCreacion;
    }

}
