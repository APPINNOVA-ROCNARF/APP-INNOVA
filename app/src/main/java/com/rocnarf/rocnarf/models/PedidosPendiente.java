package com.rocnarf.rocnarf.models;

import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PedidosPendiente implements Serializable {

    @PrimaryKey
    @SerializedName("ID")
    @Expose
    private  int ID ;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFactura() {
        return Factura;
    }

    public void setFactura(String factura) {
        Factura = factura;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date fecha) {
        Fecha = fecha;
    }

    public String getNomcli() {
        return Nomcli;
    }

    public void setNomcli(String nomcli) {
        Nomcli = nomcli;
    }

    public String getCiucli() {
        return Ciucli;
    }

    public void setCiucli(String ciucli) {
        Ciucli = ciucli;
    }

    public String getVendedor() {
        return Vendedor;
    }

    public void setVendedor(String vendedor) {
        Vendedor = vendedor;
    }
    public BigDecimal getValor() {
        return Valor;
    }

    public void setValor(BigDecimal valor) {
        Valor = valor;
    }
    public String getTipoObserv() {
        return TipoObserv;
    }

    public void setTipoObserv(String tipoObserv) {
        TipoObserv = tipoObserv;
    }

    public String getOrigen() {
        return Origen;
    }

    public void setOrigen(String origen) {
        Origen = origen;
    }
    @SerializedName("factura")
    @Expose
    private  String Factura;

    @SerializedName("codigo")
    @Expose
    private  String Codigo;


    @SerializedName("fecha")
    @Expose
    private Date Fecha;

    @SerializedName("nomcli")
    @Expose
    private  String Nomcli;

    @SerializedName("ciucli")
    @Expose
    private  String Ciucli;


    @SerializedName("vendedor")
    @Expose
    private  String Vendedor;



    @SerializedName("valor")
    @Expose
    private BigDecimal Valor;

    public String getAprobado() {
        return Aprobado;
    }

    public void setAprobado(String aprobado) {
        Aprobado = aprobado;
    }

    public Boolean getOkboni() {
        return Okboni;
    }

    public void setOkboni(Boolean okboni) {
        Okboni = okboni;
    }

    public Boolean getNegado() {
        return Negado;
    }

    public void setNegado(Boolean negado) {
        Negado = negado;
    }

    public Boolean getNegaboni() {
        return Negaboni;
    }

    public void setNegaboni(Boolean negaboni) {
        Negaboni = negaboni;
    }

    public String getDespachado() {
        return despachado;
    }

    public void setDespachado(String despachado) {
        this.despachado = despachado;
    }

    @SerializedName("aprobado")
    @Expose
    private  String Aprobado;

    @SerializedName("okboni")
    @Expose
    private  Boolean Okboni;

    @SerializedName("negado")
    @Expose
    private  Boolean Negado;

    @SerializedName("negaboni")
    @Expose
    private  Boolean Negaboni;

    @SerializedName("tipoObserv")
    @Expose
    private String TipoObserv;

    @SerializedName("origen")
    @Expose
    private String Origen;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @SerializedName("tipo")
    @Expose
    private String tipo;

    @SerializedName("despachado")
    @Expose
    private String despachado;

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    @SerializedName("seccion")
    @Expose
    private String seccion;

    @SerializedName("transporte")
    @Expose
    private String transporte;

    public String getTransporte() {
        return transporte;
    }

}
