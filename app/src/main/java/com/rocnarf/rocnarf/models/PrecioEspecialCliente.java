package com.rocnarf.rocnarf.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "precio_especial_cliente")
public class PrecioEspecialCliente {

    @PrimaryKey(autoGenerate = true)
    private int id; // Puedes usar otro campo si tienes uno único del backend

    @SerializedName("codigoCliente")
    @Expose
    private String codigoCliente;

    @SerializedName("codigoProducto")
    @Expose
    private String codigoProducto;

    @SerializedName("fechaDesde")
    @Expose
    private Date fechaDesde;

    @SerializedName("fechaHasta")
    @Expose
    private Date fechaHasta;

    @SerializedName("precioPVF")
    @Expose
    private Double precioPVF;

    @SerializedName("precioPVP")
    @Expose
    private Double precioPVP;

    @SerializedName("precioDesc")
    @Expose
    private Double precioDesc;

    @SerializedName("cantidad")
    @Expose
    private Integer cantidad;

    @SerializedName("tipo")
    @Expose
    private String tipo;

    // Getters y Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigoCliente() { return codigoCliente; }
    public void setCodigoCliente(String codigoCliente) { this.codigoCliente = codigoCliente; }

    public String getCodigoProducto() { return codigoProducto; }
    public void setCodigoProducto(String codigoProducto) { this.codigoProducto = codigoProducto; }

    public Double getPrecioPVF() { return precioPVF; }
    public void setPrecioPVF(Double precioPVF) { this.precioPVF = precioPVF; }

    public Double getPrecioPVP() { return precioPVP; }
    public void setPrecioPVP(Double precioPVP) { this.precioPVP = precioPVP; }

    public Double getPrecioDesc() { return precioDesc; }
    public void setPrecioDesc(Double precioDesc) { this.precioDesc = precioDesc; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }
}
