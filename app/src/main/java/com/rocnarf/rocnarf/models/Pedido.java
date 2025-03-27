package com.rocnarf.rocnarf.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.rocnarf.rocnarf.dao.TimestampConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(tableName = "pedido")
public class Pedido {
    @PrimaryKey(autoGenerate = true)
    private int idLocalPedido;

    private int idPedido;

    private String idCliente;

    private String nombreCliente;

    private String idAsesor;

    @TypeConverters({TimestampConverter.class})
    private Date fechaPedido;

    private Double precioTotal;

    private boolean conBonos;

    private Double descuento;

    private Double porcentajeDescuento;

    private Double precioFinal;

    private String observaciones;

    private String ordenCompra;

    private String reciboCobro;

    private String factura;

    private String cheques;

    @TypeConverters({TimestampConverter.class})
    private String fechaCheque;

    private Double valorCheque;

    private Double efectvo;

    private String notaCredito;

    private String estado;

    private String reciboCobro1;
    private String reciboCobro2;
    private String reciboCobro3;
    private String reciboCobro4;
    private String factura1;
    private String factura2;
    private String factura3;
    private String factura4;
    private String cheque1;
    private String cheque2;
    private String cheque3;
    private String cheque4;
    private String fechaCheque1;
    private String fechaCheque2;
    private String fechaCheque3;
    private String fechaCheque4;
    private Double valorCheque1;
    private Double valorCheque2;
    private Double valorCheque3;
    private Double valorCheque4;
    private String tipoPedido;

    private String tipoPrecio;

    private boolean pendienteSync ;

    // Es para identificar la fecha de la ultima sincronizacion
    @TypeConverters({TimestampConverter.class})
    private Date  fechaSync;

   @ColumnInfo(name = "imagen1",typeAffinity = ColumnInfo.BLOB)
    private  byte[] imagen1;
//
    @ColumnInfo(name = "imagen2",typeAffinity = ColumnInfo.BLOB)
    private  byte[] imagen2;



    @Ignore
    private List<PedidoDetalle> pedidoDetalleResource;

    public Pedido(){
        pedidoDetalleResource = new ArrayList<>();
    }

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

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getIdAsesor() {
        return idAsesor;
    }

    public void setIdAsesor(String idAsesor) {
        this.idAsesor = idAsesor;
    }

    public Date getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public Double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(Double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public boolean isConBonos() {
        return conBonos;
    }

    public void setConBonos(boolean conBonos) {
        this.conBonos = conBonos;
    }

    public Double getDescuento() {
        return descuento;
    }

    public void setDescuento(Double descuento) {
        this.descuento = descuento;
    }

    public Double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(Double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public Double getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(Double precioFinal) {
        this.precioFinal = precioFinal;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    public String getOrdenCompra() {
        return ordenCompra;
    }

    public void setOrdenCompra(String ordenCompra) {
        this.ordenCompra = ordenCompra;
    }

    public String getReciboCobro() {
        return reciboCobro;
    }

    public void setReciboCobro(String reciboCobro) {
        this.reciboCobro = reciboCobro;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public Double getValorCheque() {
        return valorCheque;
    }

    public void setValorCheque(Double valorCheque) {
        this.valorCheque = valorCheque;
    }

    public String getCheques() {
        return cheques;
    }

    public void setCheques(String cheques) {
        this.cheques = cheques;
    }

    public String getFechaCheque() {
        return fechaCheque;
    }

    public void setFechaCheque(String fechaCheque) {
        this.fechaCheque = fechaCheque;
    }

    public Double getEfectvo() {
        return efectvo;
    }

    public void setEfectvo(Double efectvo) {
        this.efectvo = efectvo;
    }

    public String getNotaCredito() {
        return notaCredito;
    }

    public void setNotaCredito(String notaCredito) {
        this.notaCredito = notaCredito;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isPendienteSync() {
        return pendienteSync;
    }

    public void setPendienteSync(boolean pendienteSync) {
        this.pendienteSync = pendienteSync;
    }

    public Date getFechaSync() {
        return fechaSync;
    }

    public void setFechaSync(Date fechaSync) {
        this.fechaSync = fechaSync;
    }

    public List<PedidoDetalle> getPedidoDetalleResource() {
        return pedidoDetalleResource;
    }

    public void setPedidoDetalleResource(List<PedidoDetalle> pedidoDetalleResource) {
        this.pedidoDetalleResource = pedidoDetalleResource;
    }

    public String getReciboCobro1() {
        return reciboCobro1;
    }

    public void setReciboCobro1(String reciboCobro1) {
        this.reciboCobro1 = reciboCobro1;
    }

    public String getReciboCobro2() {
        return reciboCobro2;
    }

    public void setReciboCobro2(String reciboCobro2) {
        this.reciboCobro2 = reciboCobro2;
    }

    public String getReciboCobro3() {
        return reciboCobro3;
    }

    public void setReciboCobro3(String reciboCobro3) {
        this.reciboCobro3 = reciboCobro3;
    }

    public String getReciboCobro4() {
        return reciboCobro4;
    }

    public void setReciboCobro4(String reciboCobro4) {
        this.reciboCobro4 = reciboCobro4;
    }

    public String getFactura1() {
        return factura1;
    }

    public void setFactura1(String factura1) {
        this.factura1 = factura1;
    }

    public String getFactura2() {
        return factura2;
    }

    public void setFactura2(String factura2) {
        this.factura2 = factura2;
    }

    public String getFactura3() {
        return factura3;
    }

    public void setFactura3(String factura3) {
        this.factura3 = factura3;
    }

    public String getFactura4() {
        return factura4;
    }

    public void setFactura4(String factura4) {
        this.factura4 = factura4;
    }

    public String getFechaCheque1() {
        return fechaCheque1;
    }

    public void setFechaCheque1(String fechaCheque1) {
        this.fechaCheque1 = fechaCheque1;
    }

    public String getFechaCheque2() {
        return fechaCheque2;
    }

    public void setFechaCheque2(String fechaCheque2) {
        this.fechaCheque2 = fechaCheque2;
    }

    public String getFechaCheque3() {
        return fechaCheque3;
    }

    public void setFechaCheque3(String fechaCheque3) {
        this.fechaCheque3 = fechaCheque3;
    }

    public String getFechaCheque4() {
        return fechaCheque4;
    }

    public void setFechaCheque4(String fechaCheque4) {
        this.fechaCheque4 = fechaCheque4;
    }

    public String getCheque1() {
        return cheque1;
    }

    public void setCheque1(String cheque1) {
        this.cheque1 = cheque1;
    }

    public String getCheque2() {
        return cheque2;
    }

    public void setCheque2(String cheque2) {
        this.cheque2 = cheque2;
    }

    public String getCheque3() {
        return cheque3;
    }

    public void setCheque3(String cheque3) {
        this.cheque3 = cheque3;
    }

    public String getCheque4() {
        return cheque4;
    }

    public void setCheque4(String cheque4) {
        this.cheque4 = cheque4;
    }

    public Double getValorCheque1() {
        return valorCheque1;
    }

    public void setValorCheque1(Double valorCheque1) {
        this.valorCheque1 = valorCheque1;
    }

    public Double getValorCheque2() {
        return valorCheque2;
    }

    public void setValorCheque2(Double valorCheque2) {
        this.valorCheque2 = valorCheque2;
    }

    public Double getValorCheque3() {
        return valorCheque3;
    }

    public void setValorCheque3(Double valorCheque3) {
        this.valorCheque3 = valorCheque3;
    }

    public Double getValorCheque4() {
        return valorCheque4;
    }

    public void setValorCheque4(Double valorCheque4) {
        this.valorCheque4 = valorCheque4;
    }

    public String getTipoPedido() {
        return tipoPedido;
    }

    public void setTipoPedido(String tipoPedido) {
        this.tipoPedido = tipoPedido;
    }

   public byte[] getImagen1() {
       return imagen1;
   }
//
   public void setImagen1(byte[] imagen1) {
       this.imagen1 = imagen1;
   }
//
    public byte[] getImagen2() {
        return imagen2;
    }
//
    public void setImagen2(byte[] imagen2) {
        this.imagen2 = imagen2;
    }

    public String getTipoPrecio() {
        return tipoPrecio;
    }

    public void setTipoPrecio(String tipoPrecio) {
        this.tipoPrecio = tipoPrecio;
    }

    @NonNull
    @Override
    public String toString() {
        return "Pedido{" +
                "idLocalPedido=" + idLocalPedido +
                ", idPedido=" + idPedido +
                ", idCliente='" + idCliente + '\'' +
                ", nombreCliente='" + nombreCliente + '\'' +
                ", idAsesor='" + idAsesor + '\'' +
                ", fechaPedido=" + fechaPedido +
                ", precioTotal=" + precioTotal +
                ", descuento=" + descuento +
                ", porcentajeDescuento=" + porcentajeDescuento +
                ", precioFinal=" + precioFinal +
                ", observaciones='" + observaciones + '\'' +
                ", tipoPedido='" + tipoPedido + '\'' +
                ", tipoPrecio='" + tipoPrecio + '\'' +
                ", pendienteSync=" + pendienteSync +
                ", pedidoDetalleResource count=" + (pedidoDetalleResource != null ? pedidoDetalleResource.size() : 0) +
                '}';
    }

}

