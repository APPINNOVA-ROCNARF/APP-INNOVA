package com.rocnarf.rocnarf.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class CarteraCliente {
    @SerializedName("idCliente")
    @Expose
    private String IdCliente;
    @SerializedName("nombre")
    @Expose
    private String Nombre;
    @SerializedName("tipo")
    @Expose
    private String Tipo;
    @SerializedName("seccion")
    @Expose
    private  String Seccion;

    @SerializedName("seccion2")
    @Expose
    private  String Seccion2;

    @SerializedName("seccion3")
    @Expose
    private  String Seccion3;

    @SerializedName("seccion4")
    @Expose
    private  String Seccion4;
    @SerializedName("dueno")
    @Expose
    private String Dueno;

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

    @SerializedName("especialidades")
    @Expose
    private String Especialidades;

    @SerializedName("idEspecialidades")
    @Expose
    private String IdEspecialidades;

    @SerializedName("clase")
    @Expose
    private String Clase;

    private String estadoVisita  = "";;

    @SerializedName("auspiciado")
    @Expose
    private Boolean Auspiciado;
    @SerializedName("noVencido")
    @Expose
    private BigDecimal NoVencido = BigDecimal.ZERO;
    @SerializedName("vencido")
    @Expose
    private BigDecimal Vencido = BigDecimal.ZERO;
    @SerializedName("totalCartera")
    @Expose
    private BigDecimal TotalCartera = BigDecimal.ZERO;
    @SerializedName("diasPlazo")
    @Expose
    private int DiasPlazo;
    @SerializedName("diazVencido")
    @Expose
    private int DiazVencido;
    @SerializedName("saldoCliente")
    @Expose
    private BigDecimal SaldoCliente = BigDecimal.ZERO;

    private String claseMedico;
    // Getters and Setters (properties) for the private fields
    public String getEstadoVisita() {
        return estadoVisita;
    }

    public void setEstadoVisita(String estadoVisita) {
        this.estadoVisita = estadoVisita;
    }


    public String getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(String IdCliente) {
        this.IdCliente = IdCliente;
    }

    public String getNombre() {
        return Nombre;
    }
    public String getClaseMedico() {
        return TipoObserv + " " + (Clase != null ? Clase :"");
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String Tipo) {
        this.Tipo = Tipo;
    }

    public String getSeccion() {
        return Seccion;
    }

    public void setSeccion(String Seccion) {
        this.Seccion = Seccion;
    }

    public String getSeccion2() {
        return Seccion2;
    }

    public void setSeccion2(String Seccion2) {
        this.Seccion2 = Seccion2;
    }

    public String getSeccion3() {
        return Seccion3;
    }

    public void setSeccion3(String Seccion3) {
        this.Seccion3 = Seccion3;
    }

    public String getSeccion4() {
        return Seccion4;
    }

    public void setSeccion4(String Seccion4) {
        this.Seccion4 = Seccion4;
    }

    public String getDueno() {
        return Dueno;
    }

    public void setDueno(String Dueno) {
        this.Dueno = Dueno;
    }

    public String getCiudad() {
        return Ciudad;
    }

    public void setCiudad(String Ciudad) {
        this.Ciudad = Ciudad;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    public String getOrigen() {
        return Origen;
    }

    public void setOrigen(String Origen) {
        this.Origen = Origen;
    }

    public String getTipoObserv() {
        return TipoObserv;
    }

    public void setTipoObserv(String TipoObserv) {
        this.TipoObserv = TipoObserv;
    }

    public String getEspecialidades() {
        return Especialidades;
    }

    public void setEspecialidades(String Especialidades) {
        this.Especialidades = Especialidades;
    }

    public String getIdEspecialidades() {
        return IdEspecialidades;
    }

    public void setIdEspecialidades(String IdEspecialidades) {
        this.IdEspecialidades = IdEspecialidades;
    }

    public String getClase() {
        return Clase;
    }

    public void setClase(String Clase) {
        this.Clase = Clase;
    }

    public Boolean getAuspiciado() {
        return Auspiciado;
    }

    public void setAuspiciado(Boolean Auspiciado) {
        this.Auspiciado = Auspiciado;
    }

    public BigDecimal getNoVencido() {
        return NoVencido;
    }

    public void setNoVencido(BigDecimal NoVencido) {
        this.NoVencido = NoVencido;
    }

    public BigDecimal getVencido() {
        return Vencido;
    }

    public void setVencido(BigDecimal Vencido) {
        this.Vencido = Vencido;
    }

    public BigDecimal getTotalCartera() {
        return TotalCartera;
    }

    public void setTotalCartera(BigDecimal TotalCartera) {
        this.TotalCartera = TotalCartera;
    }

    public int getDiasPlazo() {
        return DiasPlazo;
    }

    public void setDiasPlazo(int DiasPlazo) {
        this.DiasPlazo = DiasPlazo;
    }

    public int getDiazVencido() {
        return DiazVencido;
    }

    public void setDiazVencido(int DiazVencido) {
        this.DiazVencido = DiazVencido;
    }

    public BigDecimal getSaldoCliente() {
        return SaldoCliente;
    }

    public void setSaldoCliente(BigDecimal SaldoCliente) {
        this.SaldoCliente = SaldoCliente;
    }
}
