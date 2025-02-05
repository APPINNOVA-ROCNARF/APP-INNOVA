package com.rocnarf.rocnarf.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VisitaClientesFecha extends VisitaClientesList implements Parcelable {

    private boolean isExpanded = false;
    private long fecha;

    // Nuevos campos para datos expandibles
    private String nombreCliente;
    private String codigoCliente;
    private String direccionCliente;
    private String motivoVisita;

    private List<VisitaClientes> visitas = new ArrayList<>(); // Inicializa con una lista vacía

    public VisitaClientesFecha(Date fecha) {
        this.fecha = fecha.getTime();
    }

    public VisitaClientesFecha(Parcel in) {
        this.fecha = in.readLong();
        this.nombreCliente = in.readString();
        this.codigoCliente = in.readString();
        this.direccionCliente = in.readString();
        this.motivoVisita = in.readString();
        this.isExpanded = in.readByte() != 0; // Leer boolean como byte
    }

    public static final Creator<VisitaClientesFecha> CREATOR = new Creator<VisitaClientesFecha>() {
        @Override
        public VisitaClientesFecha createFromParcel(Parcel in) {
            return new VisitaClientesFecha(in);
        }

        @Override
        public VisitaClientesFecha[] newArray(int size) {
            return new VisitaClientesFecha[size];
        }
    };

    @Override
    public int getType() {
        return TYPE_FECHA;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(this.fecha);
        parcel.writeString(this.nombreCliente);
        parcel.writeString(this.codigoCliente);
        parcel.writeString(this.direccionCliente);
        parcel.writeString(this.motivoVisita);
        parcel.writeByte((byte) (this.isExpanded ? 1 : 0)); // Escribir boolean como byte
    }

    // Getters y setters
    public Date getFecha() {
        return new Date(fecha);
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha.getTime();
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public List<VisitaClientes> getVisitas() {
        return visitas;
    }

    public void setVisitas(List<VisitaClientes> visitas) {
        this.visitas = visitas != null ? visitas : new ArrayList<>();
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public String getMotivoVisita() {
        return motivoVisita;
    }

    public void setMotivoVisita(String motivoVisita) {
        this.motivoVisita = motivoVisita;
    }


}
