package com.rocnarf.rocnarf.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "parametros")
public class Parametros {

    @PrimaryKey
    @SerializedName("idParametro")
    @Expose
    private  int IdParametro ;

    @SerializedName("tipo")
    @Expose
    private  String Tipo;

    @SerializedName("descripcion")
    @Expose
    private  String Descripcion;

    @SerializedName("valor")
    @Expose
    private  String Valor;

    @SerializedName("status")
    @Expose
    private  Boolean Status;


    public int getIdParametro() {
        return IdParametro;
    }

    public void setIdParametro(int idParametro) {
        IdParametro = idParametro;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getValor() {
        return Valor;
    }

    public void setValor(String valor) {
        Valor = valor;
    }

    public boolean getStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }


}
