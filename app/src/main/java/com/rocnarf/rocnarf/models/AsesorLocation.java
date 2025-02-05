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

@Entity(tableName = "asesores_location")
public class AsesorLocation {

    @PrimaryKey
    @TypeConverters({TimestampConverter.class})
    @Nullable
    private Date fechaRegistro;


    @SerializedName("idAsesor")
    @Expose
    private String idAsesor;

    @SerializedName("latitud")
    @Expose
    private Double latitud;

    @SerializedName("longitud")
    @Expose
    private Double longitud;

    private Boolean Inicio ;

    private boolean pendienteSync ;

    @Nullable
    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(@Nullable Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getIdAsesor() {
        return idAsesor;
    }

    public void setIdAsesor(String idAsesor) {
        this.idAsesor = idAsesor;
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

    public boolean isPendienteSync() {
        return pendienteSync;
    }

    public Boolean getInicio() {
        return Inicio;
    }

    public void setInicio(Boolean inicio) {
        Inicio = inicio;
    }

    public void setPendienteSync(boolean pendienteSync) {
        this.pendienteSync = pendienteSync;
    }
}
