package com.rocnarf.rocnarf.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity(tableName = "usuarios")
public class Usuario {

    @PrimaryKey
    @NonNull
    @SerializedName("idUsuario")
    @Expose
    private  String idUsuario ;

    @SerializedName("clave")
    @Expose
    private  String Clave;

    @SerializedName("nombre")
    @Expose
    private  String Nombre;

    @SerializedName("seccion")
    @Expose
    private  String Seccion;

    @SerializedName("rol")
    @Expose
    private  String Rol;

    @SerializedName("email")
    @Expose
    private  String Email;

    @SerializedName("estado")
    @Expose
    private  String Estado;

    @SerializedName("secciones")
    @Expose
    private String Secciones;

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String clave) {
        Clave = clave;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getSeccion() {
        return Seccion;
    }

    public void setSeccion(String seccion) {
        Seccion = seccion;
    }

    public String getRol() {
        return Rol;
    }

    public void setRol(String rol) {
        Rol = rol;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }
    public String getSecciones() { return Secciones;}
    public void setSecciones(String secciones) { Secciones = secciones;}

}
