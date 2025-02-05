package com.rocnarf.rocnarf.models;

import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Date;

public class NotaCredito {

    @PrimaryKey
    @NonNull

    @SerializedName("numero")
    @Expose
    private int numero;

    @SerializedName("codigo")
    @Expose
    private String codigo ;

    @SerializedName("factura")
    @Expose
    private String factura ;

    @SerializedName("numnota")
    @Expose
    private String numNota ;

    @SerializedName("comprob")
    @Expose
    private String comprob ;

    @SerializedName("fecha")
    @Expose
    private Date fecha ;


    @SerializedName("valor")
    @Expose
    private BigDecimal valor;

    @SerializedName("concepto")
    @Expose
    private String concepto;


    @NonNull
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getFactura() {
        return factura;
    }

    public String getNumNota() {
        return numNota;
    }

    public String getComprob() {
        return comprob;
    }

    public Date getFecha() {
        return fecha;
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

    public String getConcepto() {
        return concepto;
    }

}
