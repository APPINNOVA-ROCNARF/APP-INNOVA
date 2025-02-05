package com.rocnarf.rocnarf.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
@Entity(tableName = "clientes_cupo_credito")
public class ClientesCupoCredito {

    @PrimaryKey
    @NonNull
    @SerializedName("idCliente")
    @Expose
    private String idCliente;

    @SerializedName("cupoCredito")
    @Expose
    private BigDecimal cupoCredito;

    @SerializedName("facturas")
    @Expose
    private BigDecimal facturas;

    @SerializedName("protestos")
    @Expose
    private BigDecimal protestos;

    @SerializedName("chequesAFechas")
    @Expose
    private BigDecimal chequesAFechas;

    public ClientesCupoCredito(
            String idCliente,
            BigDecimal cupoCredito,
            BigDecimal facturas,
            BigDecimal protestos,
            BigDecimal chequesAFechas){

        this.idCliente = idCliente;
        this.cupoCredito =cupoCredito;
        this.facturas = facturas;
        this.protestos =protestos;
        this.chequesAFechas = chequesAFechas;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        idCliente = idCliente;
    }

    public BigDecimal getChequesAFechas() {
        return chequesAFechas;
    }

    public void setChequesAFechas(BigDecimal chequesAFechas) {
        chequesAFechas = chequesAFechas;
    }

    public BigDecimal getCupoCredito() {
        return cupoCredito;
    }

    public void setCupoCredito(BigDecimal cupoCredito) {
        cupoCredito = cupoCredito;
    }

    public BigDecimal getFacturas() {
        return facturas;
    }

    public void setFacturas(BigDecimal facturas) {
        facturas = facturas;
    }

    public BigDecimal getProtestos() {
        return protestos;
    }

    public void setProtestos(BigDecimal protestos) {
        protestos = protestos;
    }
}
