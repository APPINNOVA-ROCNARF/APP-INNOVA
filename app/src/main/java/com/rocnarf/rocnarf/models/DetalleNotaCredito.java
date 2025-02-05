package com.rocnarf.rocnarf.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity(tableName = "DetalleNotaCredito")
public class DetalleNotaCredito implements Serializable  {

    @NonNull
    @SerializedName("numero")
    @Expose
    private String numero;

    @SerializedName("producto")
    @Expose
    private String producto ;

    @SerializedName("cantidad")
    @Expose
    private BigDecimal cantidad;

    @SerializedName("bonific")
    @Expose
    private BigDecimal bonific;


    @SerializedName("precio")
    @Expose
    private BigDecimal precio;



    @NonNull
    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }


    public BigDecimal getBonificacion() {
        return bonific;
    }

    public void setBonificacion(BigDecimal bonificacion) {
        this.bonific = bonificacion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }


}
