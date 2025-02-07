package com.rocnarf.rocnarf.models;


import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import android.os.Parcel;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rocnarf.rocnarf.dao.TimestampConverter;

import java.util.Date;

@Entity(tableName = "visita_clientes",
        indices = {@Index(value="codigoAsesor")})
public class VisitaClientes extends VisitaClientesList {

    public final static String PLANIFICADO = "PLANI";
    public final static String PEFECTIVA = "PEFECT";
    public final static String EFECTIVA = "EFECT";
    public final static String VISITADO = "NOEFE";

    public Clientes getClientes() {
        return cliente;
    }

    @SerializedName("cliente")
    @Embedded(prefix = "clien_")
    public Clientes cliente;

    @PrimaryKey(autoGenerate = true)
    private int idLocal;

    private  int  idVisitaCliente;

    @SerializedName("idAsesor")
    @Expose
    private String codigoAsesor;

    @SerializedName("seccion")
    @Expose
    private String seccion;

    @SerializedName("idCliente")
    @Expose
    private String codigoCliente;

    @SerializedName("nombreCliente")
    @Expose
    private String nombreCliente;

    @SerializedName("direccion")
    @Expose
    private String direccion;

    @SerializedName("latitud")
    @Expose
    private Double latitud;

    @SerializedName("longitud")
    @Expose
    private Double longitud;

    @TypeConverters({TimestampConverter.class})
    @Nullable
    private Date fechaVisitaPlanificada;

    @SerializedName("visitaXCajasVacias")
    @Expose
    private boolean visitaXCajasVacias;

    @SerializedName("visitaXSiembraProducto")
    @Expose
    private boolean visitaXSiembraProducto;

    @SerializedName("visitaPromocional")
    @Expose
    private boolean visitaPromocional;

    @SerializedName("visitaXEntregaPremios")
    @Expose
    private boolean visitaXEntregaPremios;

    @SerializedName("visitaXDevolucion")
    @Expose
    private boolean visitaXDevolucion;


    @SerializedName("visitaXPedido")
    @Expose
    private boolean visitaXPedido;

    @SerializedName("valorPedidoF2")
    @Expose
    private Double valorPedidoF2;

    @SerializedName("valorPedidoF3")
    @Expose
    private Double valorPedidoF3;

    @SerializedName("valorPedidoF4")
    @Expose
    private Double valorPedidoF4;

    @SerializedName("valorPedidoGEN")
    @Expose
    private Double valorPedidoGEN;


    @SerializedName("visitaXCobro")
    @Expose
    private boolean visitaXCobro;

    @SerializedName("valorXCobrar")
    @Expose
    private Double valorXCobrar;


    @SerializedName("efectivaXCajasVacias")
    @Expose
    private boolean efectivaXCajasVacias;

    @SerializedName("efectivaXSiembraProducto")
    @Expose
    private boolean efectivaXSiembraProducto;

    @SerializedName("efectivaPromocional")
    @Expose
    private boolean efectivaPromocional;

    @SerializedName("efectivaXPedido")
    @Expose
    private boolean efectivaXPedido;

    @SerializedName("efectivaXCobro")
    @Expose
    private boolean efectivaXCobro;

    @SerializedName("efectivaXEntregaPremios")
    @Expose
    private boolean efectivaXEntregaPremios;

    @SerializedName("efectivaXDevolucion")
    @Expose
    private boolean efectivaXDevolucion;

    @SerializedName("visitaAcompanado")
    @Expose
    private boolean visitaAcompanado;


    @SerializedName("acompanate")
    @Expose
    private String acompanate;

    @SerializedName("visitaDocEfec")
    @Expose
    private boolean visitaDocEfec;

    @SerializedName("visitaSinGestion")
    @Expose
    private boolean visitaSinGestion;

    @SerializedName("visitaPuntoContacto")
    @Expose
    private boolean visitaPuntoContacto;

    @SerializedName("visitaReVisita")
    @Expose
    private boolean visitaReVisita;

    @SerializedName("estado")
    @Expose
    private String estado;

    @TypeConverters({TimestampConverter.class})
    private Date fechaVisita;

    @SerializedName("observacion")
    @Expose
    private String observacion;

    @SerializedName("queja")
    @Expose
    private String queja;

    @TypeConverters({TimestampConverter.class})
    private Date fechaCreacion;

    @TypeConverters({TimestampConverter.class})
    private Date fechaModificacion ;

    @SerializedName("tipoCliente")
    @Expose
    private String tipoCliente;

    private boolean pendienteSync ;

    // Es para identificar la fecha de la ultima sincronizacion
    @TypeConverters({TimestampConverter.class})
    private Date  fechaSincronizacion;



    public VisitaClientes(){}

    public VisitaClientes(int idLocal,  int  IdVisitaCliente, String CodigoAsesor,
                          String CodigoCliente,
                          String NombreCliente,
                          String direccion, Double latitud, Double longitud,
                          Date fechaVisitaPlanificadad,
                          Boolean visitaXCajasVacias,
                          Boolean VisitaXSiembraProducto,
                          Boolean VisitaPromocional,
                          Boolean VisitaAcompanado,
                          Boolean VisitaDocEfec,
                          Boolean VisitaSinGestion,
                          Boolean VisitaPuntoContacto,
                          Boolean VisitaReVisita,
                          Boolean VisitaXPedido,
                          Double ValorPedidoF2,
                          Double ValorPedidoF3,
                          Double ValorPedidoF4,
                          Double ValorPedidoGEN,
                          Boolean VisitaXCobro ,
                          Double ValorXCobrar ,
                          String Estado ,
                          Date fechaVisita,
                          String observacion,
                          String tipoCliente,
                          String queja,
                          Date FechaCreacion,
                          Date FechaModificacion ,
                          String acompanate,
                          Boolean pendienteSync, Date fechaSincronizacion){
        this.idLocal = idLocal;
        this.idVisitaCliente =IdVisitaCliente;
        this.codigoAsesor =CodigoAsesor;
        this.codigoCliente =CodigoCliente;
        this.nombreCliente =NombreCliente;
        this.direccion = direccion; this.latitud = latitud; this.longitud = longitud;
        this.fechaVisitaPlanificada =fechaVisitaPlanificadad;
        this.visitaXCajasVacias = visitaXCajasVacias;
        this.visitaXSiembraProducto =VisitaXSiembraProducto;
        this.visitaPromocional =VisitaPromocional;
        this.visitaAcompanado = VisitaAcompanado;
        this.visitaDocEfec = VisitaDocEfec;
        this.visitaSinGestion = VisitaSinGestion;
        this.visitaPuntoContacto = VisitaPuntoContacto;
        this.visitaReVisita = visitaReVisita;
        this.visitaXPedido =VisitaXPedido;
        this.valorPedidoF2 =ValorPedidoF2;
        this.valorPedidoF3 =ValorPedidoF3;
        this.valorPedidoF4 =ValorPedidoF4;
        this.valorPedidoGEN =ValorPedidoGEN;
        this.visitaXCobro = VisitaXCobro;
        this.valorXCobrar =ValorXCobrar;
        this.estado =Estado;
        this.fechaVisita = fechaVisita;
        this.observacion = observacion;
        this.tipoCliente = tipoCliente;
        this.queja = queja;
        this.fechaCreacion =FechaCreacion;
        this.fechaModificacion =FechaModificacion;
        this.acompanate = acompanate;
        this.pendienteSync =pendienteSync;
        this.fechaSincronizacion = fechaSincronizacion;
    }

    public VisitaClientes(Parcel in){
        this.idLocal = in.readInt();
        this.idVisitaCliente = in.readInt();
        this.codigoAsesor = in.readString();
        this.codigoCliente = in.readString();
        this.nombreCliente = in.readString();
        this.direccion = in.readString();
        this.latitud = in.readDouble();
        this.longitud = in.readDouble();
        this.fechaVisitaPlanificada = new Date (in.readLong()) ;
        this.visitaXCajasVacias= in.readByte() != 0;
        this.visitaXSiembraProducto= in.readByte() != 0;
        this.visitaPromocional= in.readByte() != 0;
        this.visitaAcompanado = in.readByte() !=0;
        this.visitaDocEfec = in.readByte() !=0;
        this.visitaSinGestion = in.readByte() !=0;
        this.visitaPuntoContacto = in.readByte() !=0;
        this.visitaReVisita = in.readByte() !=0;
        this.visitaXPedido= in.readByte() != 0;
        this.valorPedidoF2 = in.readDouble();
        this.valorPedidoF3 = in.readDouble();
        this.valorPedidoF4 = in.readDouble();
        this.valorPedidoGEN = in.readDouble();
        this.visitaXCobro =in.readByte() != 0;
        this.valorXCobrar = in.readDouble();
        this.estado =in.readString();
        this.fechaVisita = new Date (in.readLong()) ;
        this.observacion = in.readString();
        this.tipoCliente = in.readString();
        this.queja = in.readString();
        this.fechaCreacion = new Date (in.readLong()) ;
        this.fechaModificacion = new Date(in.readLong());
        this.acompanate = in.readString();
        this.pendienteSync = in.readByte()!= 0;
        this.fechaSincronizacion = new Date(in.readLong());
    }

    public static Creator<VisitaClientes> CREATOR = new Creator<VisitaClientes>() {
        public VisitaClientes createFromParcel(Parcel in) {
            return new VisitaClientes(in);
        }

        public VisitaClientes[] newArray(int size) {
            return new VisitaClientes[size];
        }
    };


    @Override
    public int getType() {
        return TYPE_PLANIFICACION;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.idLocal);
        parcel.writeInt(this.idVisitaCliente);
        parcel.writeString(this.codigoAsesor);
        parcel.writeString(this.codigoCliente);
        parcel.writeString(this.nombreCliente);
        parcel.writeString(this.direccion);
        parcel.writeDouble(this.latitud);
        parcel.writeDouble(this.longitud);
        parcel.writeLong(this.fechaVisitaPlanificada.getTime());
        parcel.writeByte((byte) (this.visitaXCajasVacias ? 1 : 0));
        parcel.writeByte((byte) (this.visitaXSiembraProducto ? 1 : 0));
        parcel.writeByte((byte) (this.visitaPromocional ? 1 : 0));
        parcel.writeByte((byte) (this.visitaAcompanado ? 1 : 0));
        parcel.writeByte((byte) (this.visitaDocEfec ? 1 : 0));
        parcel.writeByte((byte) (this.visitaSinGestion ? 1 : 0));
        parcel.writeByte((byte) (this.visitaPuntoContacto ? 1 : 0));
        parcel.writeByte((byte) (this.visitaReVisita ? 1 : 0));
        parcel.writeByte((byte) (this.visitaXPedido ? 1 : 0));

        parcel.writeDouble(this.valorPedidoF2);
        parcel.writeDouble(this.valorPedidoF3);
        parcel.writeDouble(this.valorPedidoF4);
        parcel.writeDouble(this.valorPedidoGEN);

        parcel.writeByte((byte) (this.visitaXCobro ? 1 : 0));
        parcel.writeDouble(this.valorXCobrar);
        parcel.writeString(this.estado);
        parcel.writeLong(this.fechaVisita.getTime());
        parcel.writeString(this.observacion);
        parcel.writeString(this.queja);
        parcel.writeString(this.tipoCliente);

        parcel.writeLong(this.fechaCreacion.getTime());
        parcel.writeLong(this.fechaModificacion.getTime());
        parcel.writeString(this.acompanate);
        parcel.writeByte((byte)(this.pendienteSync? 1 : 0));
        parcel.writeLong(this.fechaSincronizacion.getTime());
    }

    public int getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(int idLocal) {
        this.idLocal = idLocal;
    }

    public int getIdVisitaCliente() {
        return idVisitaCliente;
    }

    public void setIdVisitaCliente(int idVisitaCliente) {
        this.idVisitaCliente = idVisitaCliente;
    }

    public String getCodigoAsesor() {
        return codigoAsesor;
    }

    public void setCodigoAsesor(String codigoAsesor) {
        this.codigoAsesor = codigoAsesor;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Date getFechaVisitaPlanificada() {
        return fechaVisitaPlanificada;
    }

    public void setFechaVisitaPlanificada(Date fechaVisitaPlanificada) {
        this.fechaVisitaPlanificada = fechaVisitaPlanificada;
    }

    public boolean isVisitaXCajasVacias() {
        return visitaXCajasVacias;
    }

    public void setVisitaXCajasVacias(boolean visitaXCajasVacias) {
        this.visitaXCajasVacias = visitaXCajasVacias;
    }

    public boolean isVisitaXSiembraProducto() {
        return visitaXSiembraProducto;
    }

    public void setVisitaXSiembraProducto(boolean visitaXSiembraProducto) {
        this.visitaXSiembraProducto = visitaXSiembraProducto;
    }

    public boolean isVisitaPromocional() {
        return visitaPromocional;
    }

    public void setVisitaPromocional(boolean visitaPromocional) {
        this.visitaPromocional = visitaPromocional;
    }

    public boolean isVisitaXEntregaPremios() {
        return visitaXEntregaPremios;
    }

    public void setVisitaXEntregaPremios(boolean visitaXEntregaPremios) {
        this.visitaXEntregaPremios = visitaXEntregaPremios;
    }

    public boolean isVisitaXDevolucion() {
        return visitaXDevolucion;
    }

    public void setVisitaXDevolucion(boolean visitaXDevolucion) {
        this.visitaXDevolucion = visitaXDevolucion;
    }

    public boolean isVisitaXPedido() {
        return visitaXPedido;
    }

    public void setVisitaXPedido(boolean visitaXPedido) {
        this.visitaXPedido = visitaXPedido;
    }

    public Double getValorPedidoF2() {
        return valorPedidoF2;
    }

    public void setValorPedidoF2(Double valorPedidoF2) {
        this.valorPedidoF2 = valorPedidoF2;
    }

    public Double getValorPedidoF3() {
        return valorPedidoF3;
    }

    public void setValorPedidoF3(Double valorPedidoF3) {
        this.valorPedidoF3 = valorPedidoF3;
    }

    public Double getValorPedidoF4() {
        return valorPedidoF4;
    }

    public void setValorPedidoF4(Double valorPedidoF4) {
        this.valorPedidoF4 = valorPedidoF4;
    }

    public Double getValorPedidoGEN() {
        return valorPedidoGEN;
    }

    public void setValorPedidoGEN(Double valorPedidoGEN) {
        this.valorPedidoGEN = valorPedidoGEN;
    }

    public boolean isVisitaXCobro() {
        return visitaXCobro;
    }

    public void setVisitaXCobro(boolean visitaXCobro) {
        this.visitaXCobro = visitaXCobro;
    }

    public Double getValorXCobrar() {
        return valorXCobrar;
    }

    public void setValorXCobrar(Double valorXCobrar) {
        this.valorXCobrar = valorXCobrar;
    }





    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaVisita() {
        return fechaVisita;
    }

    public void setFechaVisita(Date fechaVisita) {
        this.fechaVisita = fechaVisita;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getQueja() {
        return queja;
    }

    public void setQueja(String queja) {
        this.queja = queja;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getAcompanate() {
        return acompanate;
    }

    public void setAcompanate(String acompanate) {
        this.acompanate = acompanate;
    }

    public boolean isPendienteSync() {
        return pendienteSync;
    }

    public void setPendienteSync(boolean pendienteSync) {
        this.pendienteSync = pendienteSync;
    }

    public Date getFechaSincronizacion() {
        return fechaSincronizacion;
    }

    public void setFechaSincronizacion(Date fechaSincronizacion) {
        this.fechaSincronizacion = fechaSincronizacion;
    }


    public boolean isEfectivaXCajasVacias() {
        return efectivaXCajasVacias;
    }

    public void setEfectivaXCajasVacias(boolean efectivaXCajasVacias) {
        this.efectivaXCajasVacias = efectivaXCajasVacias;
    }

    public boolean isEfectivaXSiembraProducto() {
        return efectivaXSiembraProducto;
    }

    public void setEfectivaXSiembraProducto(boolean efectivaXSiembraProducto) {
        this.efectivaXSiembraProducto = efectivaXSiembraProducto;
    }

    public boolean isEfectivaPromocional() {
        return efectivaPromocional;
    }

    public void setEfectivaPromocional(boolean efectivaPromocional) {
        this.efectivaPromocional = efectivaPromocional;
    }

    public boolean isEfectivaXPedido() {
        return efectivaXPedido;
    }

    public void setEfectivaXPedido(boolean efectivaXPedido) {
        this.efectivaXPedido = efectivaXPedido;
    }

    public boolean isEfectivaXCobro() {
        return efectivaXCobro;
    }

    public void setEfectivaXCobro(boolean efectivaXCobro) {
        this.efectivaXCobro = efectivaXCobro;
    }

    public boolean isEfectivaXEntregaPremios() {
        return efectivaXEntregaPremios;
    }

    public void setEfectivaXEntregaPremios(boolean efectivaXEntregaPremios) {
        this.efectivaXEntregaPremios = efectivaXEntregaPremios;
    }

    public boolean isEfectivaXDevolucion() {
        return efectivaXDevolucion;
    }

    public void setEfectivaXDevolucion(boolean efectivaXDevolucion) {
        this.efectivaXDevolucion = efectivaXDevolucion;
    }

    public boolean isVisitaAcompanado() {
        return visitaAcompanado;
    }

    public void setVisitaAcompanado(boolean visitaAcompanado) {
        this.visitaAcompanado = visitaAcompanado;
    }

    public boolean isVisitaDocEfec() {
        return visitaDocEfec;
    }

    public void setVisitaDocEfec(boolean visitaDocEfec) {
        this.visitaDocEfec = visitaDocEfec;
    }

    public boolean isVisitaSinGestion() {
        return visitaSinGestion;
    }

    public void setVisitaSinGestion(boolean visitaSinGestion) {
        this.visitaSinGestion = visitaSinGestion;
    }

    public boolean isVisitaPuntoContacto() {
        return visitaPuntoContacto;
    }

    public void setVisitaPuntoContacto(boolean visitaPuntoContacto) {
        this.visitaPuntoContacto = visitaPuntoContacto;
    }

    public boolean isVisitaReVisita() {
        return visitaReVisita;
    }

    public void setVisitaReVisita(boolean visitaReVisita) {
        this.visitaReVisita = visitaReVisita;
    }
}
