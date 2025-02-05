package com.rocnarf.rocnarf.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "rutas")
public class Rutas implements Serializable {

    @PrimaryKey
    @NonNull
    @SerializedName("idRuta")
    @Expose
    private  int IdRuta ;


    @SerializedName("nombre")
    @Expose
    private  String Nombre;

    @SerializedName("descripcion")
    @Expose
    private  String Descripcion;


    @SerializedName("fechaCreacion")
    private Date FechaCreacion;



    public int getIdRuta() {
        return IdRuta; };

    public void setIdRuta(int idRuta) {
        this.IdRuta = idRuta;
    }


    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }


    public Date getFechaCreacion() {
        return FechaCreacion;
    }


}
