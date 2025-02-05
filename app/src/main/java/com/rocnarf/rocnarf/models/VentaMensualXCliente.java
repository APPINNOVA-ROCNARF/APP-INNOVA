package com.rocnarf.rocnarf.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Date;

public class VentaMensualXCliente {


    @SerializedName("fecha")
    @Expose
    private Date fecha;

    @SerializedName("monto")
    @Expose
    private BigDecimal monto ;

    @SerializedName("abono")
    @Expose
    private  BigDecimal abono ;

    @SerializedName("notaCredito")
    @Expose
    private  BigDecimal notaCredito;

    @SerializedName("cobrado")
    @Expose
    private  BigDecimal cobrado ;

    @SerializedName("saldo")
    @Expose
    private  BigDecimal saldo ;

    @SerializedName("ventaNeta")
    @Expose
    private  BigDecimal ventaNeta;

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getAbono() {
        return abono;
    }

    public void setAbono(BigDecimal abono) {
        this.abono = abono;
    }

    public BigDecimal getNotaCredito() {
        return notaCredito;
    }

    public void setNotaCredito(BigDecimal notaCredito) {
        this.notaCredito = notaCredito;
    }

    public BigDecimal getCobrado() {
        return cobrado;
    }

    public void setCobrado(BigDecimal cobrado) {
        this.cobrado = cobrado;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getVentaNeta() {
        return ventaNeta;
    }

    public void setVentaNeta(BigDecimal ventaNeta) {
        this.ventaNeta = ventaNeta;
    }
}
