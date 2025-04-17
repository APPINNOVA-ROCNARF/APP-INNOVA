package com.rocnarf.rocnarf.models;

import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Cobro {

    @PrimaryKey
    @NonNull
    @SerializedName("idCobro")
    @Expose
    private int idCobro;

    @SerializedName("idFactura")
    @Expose
    private String idFactura;

    @SerializedName("idCliente")
    @Expose
    private String idCliente ;

    @SerializedName("fecha")
    @Expose
    private Date fecha ;

    @SerializedName("idCobrador")
    @Expose
    private String idCobrador ;

    @SerializedName("cobrador")
    @Expose
    private String cobrador;

    @SerializedName("recibo")
    @Expose
    private String recibo;

    @SerializedName("valor")
    @Expose
    private BigDecimal valor;

    @SerializedName("banco")
    @Expose
    private String banco;

    @SerializedName("cuenta")
    @Expose
    private String cuenta;

    @SerializedName("numeroCheque")
    @Expose
    private String numeroCheque;

    private List<Cobro> pedidosRelacionados;

    public List<Cobro> getPedidosRelacionados() {
        return pedidosRelacionados;
    }

    public void setPedidosRelacionados(List<Cobro> pedidosRelacionados) {
        this.pedidosRelacionados = pedidosRelacionados;
    }

    @NonNull
    public int getIdCobro() {
        return idCobro;
    }

    public void setIdCobro(@NonNull int idCobro) {
        this.idCobro = idCobro;
    }

    public void setIdFactura(String idFactura) {
        this.idFactura = idFactura;
    }

    public String getIdFactura() {
        return idFactura;
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

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getIdCobrador() {
        return idCobrador;
    }

    public void setIdCobrador(String idCobrador) {
        this.idCobrador = idCobrador;
    }

    public String getCobrador() {
        return cobrador;
    }

    public void setCobrador(String cobrador) {
        this.cobrador = cobrador;
    }

    public String getRecibo() {
        return recibo;
    }

    public void setRecibo(String recibo) {
        this.recibo = recibo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getNumeroCheque() {
        return numeroCheque;
    }

    public void setNumeroCheque(String numeroCheque) {
        this.numeroCheque = numeroCheque;
    }
}
