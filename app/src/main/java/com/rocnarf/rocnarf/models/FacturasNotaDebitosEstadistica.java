package com.rocnarf.rocnarf.models;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

public class FacturasNotaDebitosEstadistica {
    @SerializedName("cupoTotal")
    private BigDecimal cupoTotal;

    @SerializedName("valorNoVencido")
    private BigDecimal valorNoVencido;

    @SerializedName("valorVencido")
    private BigDecimal valorVencido;

    @SerializedName("totalChequesFecha")
    private BigDecimal totalChequesFecha;

    @SerializedName("carteraTotal")
    private BigDecimal carteraTotal;

    @SerializedName("cupoDisponible")
    private BigDecimal cupoDisponible;

    @SerializedName("promedioDias")
    private int promedioDias;

    public BigDecimal getCupoTotal() {
        return cupoTotal;
    }

    public void setCupoTotal(BigDecimal cupoTotal) {
        this.cupoTotal = cupoTotal;
    }

    public BigDecimal getValorNoVencido() {
        return valorNoVencido;
    }

    public void setValorNoVencido(BigDecimal valorNoVencido) {
        this.valorNoVencido = valorNoVencido;
    }

    public BigDecimal getValorVencido() {
        return valorVencido;
    }

    public void setValorVencido(BigDecimal valorVencido) {
        this.valorVencido = valorVencido;
    }

    public BigDecimal getTotalChequesFecha() {
        return totalChequesFecha;
    }

    public void setTotalChequesFecha(BigDecimal totalChequesFecha) {
        this.totalChequesFecha = totalChequesFecha;
    }

    public BigDecimal getCarteraTotal() {
        return carteraTotal;
    }

    public void setCarteraTotal(BigDecimal carteraTotal) {
        this.carteraTotal = carteraTotal;
    }

    public BigDecimal getCupoDisponible() {
        return cupoDisponible;
    }

    public void setCupoDisponible(BigDecimal cupoDisponible) {
        this.cupoDisponible = cupoDisponible;
    }

    public int getPromedioDias() {
        return promedioDias;
    }

    public void setPromedioDias(int promedioDias) {
        this.promedioDias = promedioDias;
    }
}