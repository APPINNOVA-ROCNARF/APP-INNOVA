package com.rocnarf.rocnarf.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class PedidosPendienteComentarios implements Serializable {

    @SerializedName("comentarioVen")
    @Expose
    private  String ComentarioVen;

    public String getComentarioVen() {
        return ComentarioVen;
    }

    public void setComentarioVen(String comentarioVen) {
        ComentarioVen = comentarioVen;
    }

    public String getComentarioCar() {
        return ComentarioCar;
    }

    public void setComentarioCar(String comentarioCar) {
        ComentarioCar = comentarioCar;
    }

    @SerializedName("comentarioCar")
    @Expose
    private  String ComentarioCar;

}
