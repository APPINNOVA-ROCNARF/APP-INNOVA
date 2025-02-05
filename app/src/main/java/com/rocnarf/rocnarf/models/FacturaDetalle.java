package com.rocnarf.rocnarf.models;

import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Date;

public class FacturaDetalle {

    @PrimaryKey
    @NonNull
    @SerializedName("idFacturaDetalle")
    @Expose
    private int IdFacturaDetalle;

    @SerializedName("idFactura")
    @Expose
    private String IdFactura ;

    @SerializedName("fecha")
    @Expose
    private Date Fecha ;

    @SerializedName("idProducto")
    @Expose
    private String IdProducto;

    @SerializedName("descripcionProducto")
    @Expose
    private String DescripcionProducto ;

    @SerializedName("cantidad")
    @Expose
    private BigDecimal Cantidad ;

    @SerializedName("bonificacion")
    @Expose
    private BigDecimal Bonificacion ;

    @SerializedName("seccion")
    @Expose
    private String Seccion ;

    @SerializedName("precio")
    @Expose
    private BigDecimal Precio ;

    @SerializedName("idCliente")
    @Expose
    private String IdCliente ;

    @SerializedName("descuento")
    @Expose
    private BigDecimal Descuento ;

    @SerializedName("ciclo")
    @Expose
    private int Ciclo;

    @SerializedName("lote")
    @Expose
    private String Lote;


    @NonNull
    public int getIdFacturaDetalle() {
        return IdFacturaDetalle;
    }

    public void setIdFacturaDetalle(@NonNull int idFacturaDetalle) {
        IdFacturaDetalle = idFacturaDetalle;
    }

    public String getIdFactura() {
        return IdFactura;
    }

    public void setIdFactura(String idFactura) {
        IdFactura = idFactura;
    }

    public String getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(String idCliente) {
        IdCliente = idCliente;
    }

    public String getIdProducto() {
        return IdProducto;
    }

    public void setIdProducto(String idProducto) {
        IdProducto = idProducto;
    }

    public String getDescripcionProducto() {
        return DescripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        DescripcionProducto = descripcionProducto;
    }

    public String getSeccion() {
        return Seccion;
    }

    public void setSeccion(String seccion) {
        Seccion = seccion;
    }

    public BigDecimal getCantidad() {
        return Cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        Cantidad = cantidad;
    }

    public BigDecimal getBonificacion() {
        return Bonificacion;
    }

    public void setBonificacion(BigDecimal bonificacion) {
        Bonificacion = bonificacion;
    }


    public BigDecimal getPrecio() {
        return Precio;
    }

    public void setPrecio(BigDecimal precio) {
        Precio = precio;
    }

    public BigDecimal getDescuento() {
        return Descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        Descuento = descuento;
    }

    public int getCiclo() {
        return Ciclo;
    }

    public void setCiclo(int ciclo) {
        Ciclo = ciclo;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date fecha) {
        Fecha = fecha;
    }

    public String getLote() {
        return Lote;
    }

    public void setLote(String lote) {
        Lote   = lote;
    }
}
