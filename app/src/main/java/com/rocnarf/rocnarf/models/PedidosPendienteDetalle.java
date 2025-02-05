package com.rocnarf.rocnarf.models;

import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PedidosPendienteDetalle implements Serializable {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private  int ID ;

    @SerializedName("factura")
    @Expose
    private  String Factura;

    @SerializedName("codigo")
    @Expose
    private  String Codigo;

    @SerializedName("producto")
    @Expose
    private  String Producto;

    @SerializedName("cantidad")
    @Expose
    private  Number Cantidad;


    @SerializedName("bonific")
    @Expose
    private  Number Bonific;

    @SerializedName("pedibon")
    @Expose
    private  Number Pedibon;

    @SerializedName("precio")
    @Expose
    private BigDecimal Precio;



    @SerializedName("descuento")
    @Expose
    private String Descuento;

    @SerializedName("fecha")
    @Expose
    private Date Fecha;

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

    public String getProducto() {
        return Producto;
    }

    public void setProducto(String producto) {
        Producto = producto;
    }

    public Number getCantidad() {
        return Cantidad;
    }

    public void setCantidad(Number cantidad) {
        Cantidad = cantidad;
    }

    public Number getBonific() {
        return Bonific;
    }

    public void setBonific(Number bonific) {
        Bonific = bonific;
    }

    public Number getPedibon() {
        return Pedibon;
    }

    public void setPedibon(Number pedibon) {
        Pedibon = pedibon;
    }

    public BigDecimal getPrecio() {
        return Precio;
    }

    public void setPrecio(BigDecimal precio) {
        Precio = precio;
    }

    public BigDecimal getValor() {
        return Valor;
    }

    public void setValor(BigDecimal valor) {
        Valor = valor;
    }

    public String getDescuento() {
        return Descuento;
    }

    public void setDescuento(String descuento) {
        Descuento = descuento;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date fecha) {
        Fecha = fecha;
    }

    @SerializedName("valor")
    @Expose
    private BigDecimal Valor;

    public String getTipoPrecio() {
        return TipoPrecio;
    }

    public void setTipoPrecio(String tipoPrecio) {
        TipoPrecio = tipoPrecio;
    }

    @SerializedName("tipoPrecio")
    @Expose
    private String TipoPrecio;


}
