package com.rocnarf.rocnarf.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class FichaMedico {
    private String idCliente;
    private Date fechaNacimiento ;
    private String hobbies ;
    private String laboraEn;
    private String segurosPrivados;
    private String actividadesAcademicas;
    private String direccion2;
    private String celular;
    private String nombreSecretaria;
    private String actividadRocnarf;
    private String especialidades;
    private String email;
    private String telefono1;
    private String cedula;
    private String direccion;
    private String clase;
    private Boolean auspiciado;
    private String conceptoPlan;
    private String marca;
    private Date fechaDesdeAuspicio;
    private Date fechaHastaAuspicio;

    public void setFechaDesdeAuspicio(Date fechaDesdeAuspicio) {
        this.fechaDesdeAuspicio = fechaDesdeAuspicio;
    }

    public void setFechaHastaAuspicio(Date fechaHastaAuspicio) {
        this.fechaHastaAuspicio = fechaHastaAuspicio;
    }

    public Boolean getAuspiciado() {
        return auspiciado;
    }

    public String getConceptoPlan() {
        return conceptoPlan;
    }

    public void setConceptoPlan(String conceptoPlan) {
        this.conceptoPlan = conceptoPlan;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Date getFechaDesdeAuspicio() {
        return fechaDesdeAuspicio;
    }

    public Date getFechaHastaAuspicio() {
        return fechaHastaAuspicio;
    }

    public void setAuspiciado(Boolean auspiciado) {
        this.auspiciado = auspiciado;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getLaboraEn() {
        return laboraEn;
    }

    public void setLaboraEn(String laboraEn) {
        this.laboraEn = laboraEn;
    }

    public String getSegurosPrivados() {
        return segurosPrivados;
    }

    public void setSegurosPrivados(String segurosPrivados) {
        this.segurosPrivados = segurosPrivados;
    }

    public String getActividadesAcademicas() {
        return actividadesAcademicas;
    }

    public void setActividadesAcademicas(String actividadesAcademicas) {
        this.actividadesAcademicas = actividadesAcademicas;
    }

    public String getDireccion2() {
        return direccion2;
    }

    public void setDireccion2(String direccion2) {
        this.direccion2 = direccion2;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getNombreSecretaria() {
        return nombreSecretaria;
    }

    public void setNombreSecretaria(String nombreSecretaria) {
        this.nombreSecretaria = nombreSecretaria;
    }

    public String getActividadRocnarf() {
        return actividadRocnarf;
    }

    public void setActividadRocnarf(String actividadRocnarf) {
        this.actividadRocnarf = actividadRocnarf;
    }

    public String getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(String especialidades) {
        this.especialidades = especialidades;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }
}
