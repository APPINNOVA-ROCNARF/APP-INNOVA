package com.rocnarf.rocnarf.models;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rocnarf.rocnarf.dao.TimestampConverter;

import java.util.Date;

@Entity(tableName = "visita_impulsadora",
        indices = {@Index(value="codigoAsesor")})
public class VisitasImpulsadoras {
    public final static String PLANIFICADO = "PLANI";
    public final static String EFECTIVA = "EFECT";
    public final static String VISITADO = "NOEFE";

    @PrimaryKey(autoGenerate = true)
    private int idLocal;

    private  int  idVisitaImpulsadora;

    @SerializedName("idAsesor")
    @Expose
    private String codigoAsesor;

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
    private Date fechaInicioVisitaPlanificada;

    @TypeConverters({TimestampConverter.class})
    @Nullable
    private Date fechaFinVisitaPlanificada;

    @SerializedName("estado")
    @Expose
    private String estado;

    @TypeConverters({TimestampConverter.class})
    private Date fechaInicioVisita;

    @TypeConverters({TimestampConverter.class})
    private Date fechaFinVisita;

    @SerializedName("observacion")
    @Expose
    private String observacion;


    @TypeConverters({TimestampConverter.class})
    private Date fechaCreacion;

    @TypeConverters({TimestampConverter.class})
    private Date fechaModificacion ;


    private boolean pendienteSync ;

    // Es para identificar la fecha de la ultima sincronizacion
    @TypeConverters({TimestampConverter.class})
    private Date  fechaSincronizacion;


    public int getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(int idLocal) {
        this.idLocal = idLocal;
    }

    public int getIdVisitaImpulsadora() {
        return idVisitaImpulsadora;
    }

    public void setIdVisitaImpulsadora(int idVisitaImpulsadora) {
        this.idVisitaImpulsadora = idVisitaImpulsadora;
    }

    public String getCodigoAsesor() {
        return codigoAsesor;
    }

    public void setCodigoAsesor(String codigoAsesor) {
        this.codigoAsesor = codigoAsesor;
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

    @Nullable
    public Date getFechaInicioVisitaPlanificada() {
        return fechaInicioVisitaPlanificada;
    }

    public void setFechaInicioVisitaPlanificada(@Nullable Date fechaInicioVisitaPlanificada) {
        this.fechaInicioVisitaPlanificada = fechaInicioVisitaPlanificada;
    }

    @Nullable
    public Date getFechaFinVisitaPlanificada() {
        return fechaFinVisitaPlanificada;
    }

    public void setFechaFinVisitaPlanificada(@Nullable Date fechaFinVisitaPlanificada) {
        this.fechaFinVisitaPlanificada = fechaFinVisitaPlanificada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaInicioVisita() {
        return fechaInicioVisita;
    }

    public void setFechaInicioVisita(Date fechaInicioVisita) {
        this.fechaInicioVisita = fechaInicioVisita;
    }

    public Date getFechaFinVisita() {
        return fechaFinVisita;
    }

    public void setFechaFinVisita(Date fechaFinVisita) {
        this.fechaFinVisita = fechaFinVisita;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isPendienteSync() {
        return pendienteSync;
    }

    public void setPendienteSync(boolean pendienteSync) {
        this.pendienteSync = pendienteSync;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Date getFechaSincronizacion() {
        return fechaSincronizacion;
    }

    public void setFechaSincronizacion(Date fechaSincronizacion) {
        this.fechaSincronizacion = fechaSincronizacion;
    }
}
