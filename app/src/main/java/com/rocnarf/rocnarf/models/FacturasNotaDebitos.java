package com.rocnarf.rocnarf.models;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Date;

public class FacturasNotaDebitos {

    @PrimaryKey
    @NonNull
    @SerializedName("idFactura")
    @Expose
    private String idFactura ;

    @SerializedName("idCliente")
    @Expose
    private String idCliente ;

    @SerializedName("fecha")
    @Expose
    private Date fecha;

    @SerializedName("valor")
    @Expose
    private BigDecimal valor;

    @SerializedName("abonos")
    @Expose
    private BigDecimal abonos;

    @SerializedName("notaCredito")
    @Expose
    private BigDecimal notaCredito ;

    @SerializedName("descuento")
    @Expose
    private BigDecimal descuento ;

    @SerializedName("idVendedor")
    @Expose
    private String idVendedor ;

    @SerializedName("cobrador")
    @Expose
    private String cobrador ;

    @SerializedName("numeroFactura")
    @Expose
    private String numeroFactura ;

    @SerializedName("zona")
    @Expose
    private String zona ;

    @SerializedName("vencimiento")
    @Expose
    private int vencimiento ;

    @SerializedName("estado")
    @Expose
    private String estado;

    @SerializedName("fechaAsentada")
    @Expose
    private Date fechaAsentada;

    @SerializedName("tipo")
    @Expose
    private String tipo;

    @SerializedName("numcheq")
    @Expose
    private String numcheq;

    @SerializedName("cuenta")
    @Expose
    private String cuenta;

    @SerializedName("banco")
    @Expose
    private String banco;

    @SerializedName("tipoDoc")
    @Expose
    private String tipoDoc;

    public BigDecimal getCobrosAbonos() {
        return cobrosAbonos;
    }

    public void setCobrosAbonos(BigDecimal cobrosAbonos) {
        this.cobrosAbonos = cobrosAbonos;
    }

    public Date getFechaCobro() {
        return fechaCobro;
    }

    public void setFechaCobro(Date fechaCobro) {
        this.fechaCobro = fechaCobro;
    }

    public Date getFechaNotaCredito() {
        return fechaNotaCredito;
    }

    public void setFechaNotaCredito(Date fechaNotaCredito) {
        this.fechaNotaCredito = fechaNotaCredito;
    }

    @SerializedName("cobrosAbonos")
    @Expose
    private BigDecimal cobrosAbonos;

    @SerializedName("fechaCobro")
    @Expose
    private Date fechaCobro;

    @SerializedName("fechaNotaCredito")
    @Expose
    private Date fechaNotaCredito;


    public String getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(String idFactura) {
        this.idFactura = idFactura;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public Date getFecha() {
        return fecha;
    }

    public Date getFechaAsentada() {
        return fechaAsentada;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public BigDecimal getAbonos() {
        return abonos;
    }

    public void setAbonos(BigDecimal abonos) {
        this.abonos = abonos;
    }

    public BigDecimal getNotaCredito() {
        return notaCredito;
    }

    public void setNotaCredito(BigDecimal notaCredito) {
        this.notaCredito = notaCredito;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public String getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getCobrador() {
        return cobrador;
    }

    public void setCobrador(String cobrador) {
        this.cobrador = cobrador;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public int getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(int vencimiento) {
        this.vencimiento = vencimiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNumcheq() {
        return numcheq;
    }

    public void setNumcheq(String numcheq) {
        this.numcheq = numcheq;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(String tipoDoc) {
        this.tipoDoc = tipoDoc;
    }
}
