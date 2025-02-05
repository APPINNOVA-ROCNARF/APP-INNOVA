package com.rocnarf.rocnarf.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "planes")
public class Planes {

    @PrimaryKey
    @SerializedName("idCodigo")
    @Expose
    private  int IdCodigo ;

    @SerializedName("nombre")
    @Expose
    private  String Nombre;

    @SerializedName("descripcion")
    @Expose
    private  String Descripcion;

    @SerializedName("status")
    @Expose
    private  Boolean Status;

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

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public boolean getStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }


}
