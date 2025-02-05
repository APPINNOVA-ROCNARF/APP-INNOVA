package com.rocnarf.rocnarf.models;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class FcmTokenDivice {


    @PrimaryKey
    @NonNull
    @SerializedName("idCodigo")
    @Expose
    private  int IdCodigo ;


    @SerializedName("token")
    @Expose
    private  String Token;

    @SerializedName("idUsuario")
    @Expose
    private  String IdUsuario;


    @SerializedName("fechaCreacion")
    private Date FechaCreacion;

    @SerializedName("estado")
    @Expose
    private  Boolean Estado;

    public int getIdCodigo() {
        return IdCodigo;
    }

    public void setIdCodigo(int idCodigo) {
        IdCodigo = idCodigo;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        IdUsuario = idUsuario;
    }

    public Date getFechaCreacion() {
        return FechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        FechaCreacion = fechaCreacion;
    }

    public Boolean getEstado() {
        return Estado;
    }

    public void setEstado(Boolean estado) {
        Estado = estado;
    }



}
