package com.rocnarf.rocnarf.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "promocionado")
public class Promocionado  {

    @PrimaryKey
    @NonNull
    @SerializedName("idPromocionado")
    @Expose
    private  int IdPromocionado ;

    @SerializedName("idVisitaCliente")
    @Expose
    private  int IdVisitaCliente ;

    @SerializedName("producto")
    @Expose
    private  String Producto;

    @SerializedName("item")
    @Expose
    private  String Item;



    public int getIdPromocionado() {
        return IdPromocionado; };

    public void setIdPromocionado(int idPromocionado) {
        this.IdPromocionado = idPromocionado;
    }

    public int getIiVisitaCliente() {
        return IdVisitaCliente; };

    public void setIdVisitaCliente(int idVisitaCliente) {
        this.IdVisitaCliente = idVisitaCliente;
    }

    public String getProducto() {
        return Producto;
    }

    public void setProducto(String producto) {
        Producto = producto;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }



}
