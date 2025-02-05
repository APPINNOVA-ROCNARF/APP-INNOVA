package com.rocnarf.rocnarf.models;
import java.util.Date;

public class Quejas {
    private int idQueja;
    private String idCliente;
    private String motivo;
    private String observacion;
    private Boolean opcionCliente;
    private Boolean opcionFactura;
    private Date fecha;
    private String idUsuario;
    private Boolean estado;
    private String facturas;

    public String getFacturas() {
        return facturas;
    }

    public void setFacturas(String facturas) {
        this.facturas = facturas;
    }

    public int getIdQueja() {
        return idQueja;
    }

    public void setIdQueja(int idQueja) {
        this.idQueja = idQueja;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Boolean getOpcionCliente() {
        return opcionCliente;
    }

    public void setOpcionCliente(Boolean opcionCliente) {
        this.opcionCliente = opcionCliente;
    }

    public Boolean getOpcionFactura() {
        return opcionFactura;
    }

    public void setOpcionFactura(Boolean opcionFactura) {
        this.opcionFactura = opcionFactura;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

}
