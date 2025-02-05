package com.rocnarf.rocnarf.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "preguntasFrecuentes")
public class PreguntasFrecuentes {

    @PrimaryKey
    @SerializedName("idPregunta")
    @Expose
    private  int IdPregunta ;

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

    public int getIdPregunta() {
        return IdPregunta;
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
