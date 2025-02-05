package com.rocnarf.rocnarf.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "guiaProductos")
public class GuiaProductos {

    @PrimaryKey
    @SerializedName("idGuia")
    @Expose
    private  int IdGuia ;

    @SerializedName("codigo")
    @Expose
    private  String Codigo;

    @SerializedName("nombre")
    @Expose
    private  String Nombre;


    @SerializedName("status")
    @Expose
    private  Boolean Status;

    @SerializedName("fuerza")
    @Expose
    private  String Fuerza;

    @SerializedName("marca")
    @Expose
    private  String Marca;

    @SerializedName("urlVideo")
    @Expose
    private  String UrlVideo;

    private List<ArchivosGuiaProducto> archivosGuiaProducto;


    public int getIdGuia() {
        return IdGuia;
    }

//    public void setIdCodigo(int idCodigo) {
//        IdCodigo = idCodigo;
//    }

    public String getCodigo() {
        return Codigo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }


    public boolean getStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public String getFuerza() {
        return Fuerza;
    }

    public void setFuerza(String fuerza) {
        Fuerza = fuerza;
    }

    public String getMarca() {
        return Marca;
    }

    public void setMarca(String marca) {
        Marca = marca;
    }

    public String getUrlVideo() {
        return UrlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        UrlVideo = urlVideo;
    }

    public List<ArchivosGuiaProducto> getArchivosGuiaProducto() {
        return archivosGuiaProducto;
    }

    public void setArchivosGuiaProducto(List<ArchivosGuiaProducto> archivosGuiaProducto) {
        this.archivosGuiaProducto = archivosGuiaProducto;
    }
}
