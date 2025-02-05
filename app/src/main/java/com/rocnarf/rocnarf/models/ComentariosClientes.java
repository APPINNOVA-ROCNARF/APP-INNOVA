package com.rocnarf.rocnarf.models;

import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ComentariosClientes {

    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @Expose
    private  int Id ;

    @SerializedName("usuario")
    @Expose
    private  String Usuario;

    @SerializedName("fecha")
    private Date Fecha;

    @SerializedName("seccion")
    @Expose
    private  String Seccion;

    @SerializedName("idCliente")
    @Expose
    private  String IdCliente;

    @SerializedName("comentarios")
    @Expose
    private  String Comentarios;

    @SerializedName("status")
    @Expose
    private boolean  Status;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date fecha) {
        Fecha = fecha;
    }

    public String getSeccion() {
        return Seccion;
    }

    public void setSeccion(String seccion) {
        Seccion = seccion;
    }

    public String getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(String idCliente) {
        IdCliente = idCliente;
    }

    public String getComentarios() {
        return Comentarios;
    }

    public void setComentarios(String comentarios) {
        Comentarios = comentarios;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }
}
