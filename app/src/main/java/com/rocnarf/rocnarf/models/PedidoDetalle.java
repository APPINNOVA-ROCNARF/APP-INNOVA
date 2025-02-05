package com.rocnarf.rocnarf.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pedidoDetalle")
public class PedidoDetalle {

    @PrimaryKey(autoGenerate = true)
    private int idPedidoDetalle;

    private int idLocalPedido;

    private int idPedido;

    private String idProducto;

    private String nombre;

    private String tipo;


    private int cantidad;

    private int bono;

    private Double precio;

    private Double precioTotal;

    private Double pvp;

    private Double pvf;

    public int getIdLocalPedido() {
        return idLocalPedido;
    }

    public void setIdLocalPedido(int idLocalPedido) {
        this.idLocalPedido = idLocalPedido;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdPedidoDetalle() {
        return idPedidoDetalle;
    }

    public void setIdPedidoDetalle(int idPedidoDetalle) {
        this.idPedidoDetalle = idPedidoDetalle;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getBono() {
        return bono;
    }

    public void setBono(int bono) {
        this.bono = bono;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(Double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public Double getPvp() {
        return pvp;
    }

    public void setPvf(Double pvf) {
        this.pvf = pvf;
    }

    public Double getPvf() {
        return pvf;
    }

    public void setPvp(Double pvp) {
        this.pvp = pvp;
    }
}
