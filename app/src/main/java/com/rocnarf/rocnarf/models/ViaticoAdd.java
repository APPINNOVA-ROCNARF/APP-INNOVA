package com.rocnarf.rocnarf.models;

import androidx.room.ColumnInfo;

import java.io.Serializable;
import java.util.Date;

public class ViaticoAdd implements Serializable {

    private int idViatico;
    private int idCiclo;
    private int idCatalogo;
    private String idUsuario;
    private Date fecha;
    private String ruc;
    private String razonSocial;
    private String numeroFactura;
    private double subTotal= 0;;
    private double subTotalIva= 0;;
    private double total= 0;;
    private String placa;
    private String rutaImagen;
    private int estadoViatico;
    private boolean estatus;
    private String comentario;
    private String usuarioComenta;
    private Date fechaFactura;

    @ColumnInfo(name = "imagen",typeAffinity = ColumnInfo.BLOB)
    private  byte[] imagen;
    public String getNombreCiclo() {
        return nombreCiclo;
    }

    public void setNombreCiclo(String nombreCiclo) {
        this.nombreCiclo = nombreCiclo;
    }

    public String getNombreCatalogo() {
        return nombreCatalogo;
    }

    public void setNombreCatalogo(String nombreCatalogo) {
        this.nombreCatalogo = nombreCatalogo;
    }

    private String nombreCiclo;
    private String nombreCatalogo;

    public int getIdViatico() {
        return idViatico;
    }

    public void setIdViatico(int idViatico) {
        this.idViatico = idViatico;
    }

    public int getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(int idCiclo) {
        this.idCiclo = idCiclo;
    }

    public int getIdCatalogo() {
        return idCatalogo;
    }

    public void setIdCatalogo(int idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }
    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getSubTotalIva() {
        return subTotalIva;
    }

    public void setSubTotalIva(double subTotalIva) {
        this.subTotalIva = subTotalIva;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public int getEstadoViatico() {
        return estadoViatico;
    }

    public void setEstadoViatico(int estadoViatico) {
        this.estadoViatico = estadoViatico;
    }

    public boolean isEstatus() {
        return estatus;
    }

    public void setEstatus(boolean estatus) {
        this.estatus = estatus;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getUsuarioComenta() {
        return usuarioComenta;
    }

    public void setUsuarioComenta(String usuarioComenta) {
        this.usuarioComenta = usuarioComenta;
    }

    public byte[] getImagen() {
        return imagen;
    }
    //
    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
}
