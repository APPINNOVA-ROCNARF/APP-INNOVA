package com.rocnarf.rocnarf.adapters;

public class PanelProductoObjeto {

    private String IdProducto = "";
    private String NombreProducto = "";
    private String Cantidad = "";
    private String Bonificacion = "";
    private String Precio = "";
    private String Total = "";

    public String getIdProducto() {
        return IdProducto;
    }
    public void setIdProducto(String IdProducto) {
        this.IdProducto = IdProducto;
    }

    public String getNombreProducto() {
        return NombreProducto;
    }
    public void setNombreProducto(String NombreProducto) {
        this.NombreProducto = NombreProducto;
    }

    public String getCantidad() {
        return Cantidad;
    }
    public void setCantidad(String Cantidad) {
        this.Cantidad = Cantidad;
    }

    public String getBonificacion() {
        return Bonificacion;
    }
    public void setBonificacion(String Bonificacion) {
        this.Bonificacion = Bonificacion;
    }

    public String getPrecio() {
        return Precio;
    }
    public void setPrecio(String Precio) {
        this.Precio = Precio;
    }

    public String getTotal() {
        return Total;
    }
    public void setTotal(String Total) {
        this.Total = Total;
    }

}
