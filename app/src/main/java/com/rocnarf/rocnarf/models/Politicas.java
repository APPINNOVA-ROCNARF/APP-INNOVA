package com.rocnarf.rocnarf.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "politicas")
public class Politicas {

    @PrimaryKey
    @SerializedName("idPolitica")
    @Expose
    private  int IdPolitica ;

    @SerializedName("pregunta")
    @Expose
    private  String Pregunta;

    @SerializedName("respuesta")
    @Expose
    private  String Respuesta;


    @SerializedName("status")
    @Expose
    private  Boolean Status;

    @SerializedName("expandir")
    @Expose
    private  Boolean Expandir=false;

    public int getIdPolitica() {
        return IdPolitica;
    }

    public String getPregunta() {
        return Pregunta;
    }

    public String getRespuesta() {
        return Respuesta;
    }

    public boolean getStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public boolean getExpadir() {
        return Expandir;
    }

    public void setExpandir(boolean expandir) {
        Expandir = expandir;
    }


}
