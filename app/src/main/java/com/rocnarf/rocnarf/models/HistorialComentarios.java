package com.rocnarf.rocnarf.models;

import java.util.Date;

public class HistorialComentarios {
    private  int  id;
    private String usuario;
    private Date fecha ;
    private String seccion ;
    private String comentarios;
    private String idAsesor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentario(String comentario) {
        this.comentarios = comentario;
    }

    public String getIdAsesor() {
        return idAsesor;
    }

    public void setIdAsesor(String idAsesor) {
        this.idAsesor = idAsesor;
    }




}
