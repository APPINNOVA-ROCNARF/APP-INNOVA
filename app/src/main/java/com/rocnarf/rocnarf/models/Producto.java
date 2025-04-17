package com.rocnarf.rocnarf.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "producto")
public class Producto {

    @PrimaryKey
    @NonNull
    @SerializedName("idProducto")
    @Expose
    private String idProducto;

    @SerializedName("idProducto2")
    @Expose
    private String idProducto2;

    @SerializedName("nombre")
    @Expose
    private String nombre;

    @SerializedName("tipo")
    @Expose
    private String tipo;

    @SerializedName("jefe")
    @Expose
    private String jefe;

    @SerializedName("precio")
    @Expose
    private Double precio;

    @SerializedName("unitario")
    @Expose
    private Double unitario;

    @SerializedName("saldo")
    @Expose
    private Double saldo ;

    @SerializedName("vademecun")
    @Expose
    public int vademecun;

    @SerializedName("idEscala")
    @Expose
    public int idEscala;

    @SerializedName("porcentajePvp")
    @Expose
    private Double porcentajePvp ;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("marca")
    @Expose
    private String marca;

    @SerializedName("pvp")
    @Expose
    private Double pvp;
    @SerializedName("precioEspecial")
    @Expose
    private Double precioEspecial;

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getIdProducto2() {
        return idProducto2;
    }

    public void setIdProducto2(String idProducto2) {
        this.idProducto2 = idProducto2;
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

    public String getJefe() {
        return jefe;
    }

    public void setJefe(String jefe) {
        this.jefe = jefe;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getUnitario() {
        return unitario;
    }

    public void setUnitario(Double unitario) {
        this.unitario = unitario;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public int getVademecun() {
        return vademecun;
    }

    public void setVademecun(int vademecun) {
        this.vademecun = vademecun;
    }

    public int getIdEscala() {
        return idEscala;
    }

    public void setIdEscala(int idEscala) {
        this.idEscala = idEscala;
    }

    public Double getPorcentajePvp() {
        return porcentajePvp;
    }

    public void setPorcentajePvp(Double porcentajePvp) {
        this.porcentajePvp = porcentajePvp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Double getPvp() {
        return pvp;
    }

    public void setPvp(Double pvp) {
        this.pvp = pvp;
    }
    public Double getPrecioEspecial() {
        return precioEspecial;
    }

    public void setPrecioEspecial(Double precioEspecial) {
        this.precioEspecial = precioEspecial;
    }
}
