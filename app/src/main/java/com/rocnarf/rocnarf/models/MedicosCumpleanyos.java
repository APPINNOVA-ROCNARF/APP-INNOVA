package com.rocnarf.rocnarf.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Date;

public class MedicosCumpleanyos {
    @SerializedName("idCliente")
    @Expose
    private String IdCliente;
    @SerializedName("nombre")
    @Expose
    private String Nombre;

    @SerializedName("tipo")
    @Expose
    private String Tipo;


    @SerializedName("ciudad")
    @Expose
    private String Ciudad;

    @SerializedName("direccion")
    @Expose
    private String Direccion;
    @SerializedName("origen")
    @Expose
    private String Origen;

    @SerializedName("tipoObserv")
    @Expose
    private String TipoObserv;

    @SerializedName("idEspecialidades")
    @Expose
    private String IdEspecialidades;

    @SerializedName("clase")
    @Expose
    private String Clase;

    @SerializedName("fechaNacimiento")
    @Expose
    private Date FechaNacimiento;

    @SerializedName("anyo")
    @Expose
    private int Anyo;

    private String estadoVisita  = "";;

    public String getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(String idCliente) {
        IdCliente = idCliente;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getCiudad() {
        return Ciudad;
    }

    public void setCiudad(String ciudad) {
        Ciudad = ciudad;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getOrigen() {
        return Origen;
    }

    public void setOrigen(String origen) {
        Origen = origen;
    }

    public String getTipoObserv() {
        return TipoObserv;
    }

    public void setTipoObserv(String tipoObserv) {
        TipoObserv = tipoObserv;
    }

    public String getIdEspecialidades() {
        return IdEspecialidades;
    }

    public void setIdEspecialidades(String idEspecialidades) {
        IdEspecialidades = idEspecialidades;
    }

    public String getClase() {
        return Clase;
    }

    public void setClase(String clase) {
        Clase = clase;
    }

    public Date getFechaNacimiento() {
        return FechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        FechaNacimiento = fechaNacimiento;
    }

    public int getAnyo() {
        return Anyo;
    }

    public void setAnyo(int anyo) {
        Anyo = anyo;
    }

    public String getClaseMedico() {
        return TipoObserv + " " + (Clase != null ? Clase :"");
    }

    public String getEstadoVisita() {
        return estadoVisita;
    }

    public void setEstadoVisita(String estadoVisita) {
        this.estadoVisita = estadoVisita;
    }

}
