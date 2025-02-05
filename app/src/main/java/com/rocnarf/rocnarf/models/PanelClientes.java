package com.rocnarf.rocnarf.models;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "panel_clientes")
public class PanelClientes {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @SerializedName("idPanelCliente")
    private int idPanelCliente;

    @SerializedName("idCliente")
    @Expose
    private String idCliente;

    @SerializedName("NombreCliente")
    @Expose
    private String NombreCliente ;

    @SerializedName("representante")
    @Expose
    private String representante ;


    @SerializedName("ciudad")
    private String ciudad ;

    @SerializedName("direccion")
    @Expose
    private String direccion;

    @SerializedName("origen")
    @Expose
    private String origen;

    @SerializedName("tipoObserv")
    @Expose
    private String tipoObserv;

    @SerializedName("idUsuario")
    @Expose
    private String idUsuario ;

    @SerializedName("revisita")
    @Expose
    private Integer revisita ;

    private String estadoSeleccion;

    private String estadoVisita;

    private String claseMedico;
    private Boolean cumpleAnyos;
    public String getIdEspecialidades() {
        return idEspecialidades;
    }

    public void setIdEspecialidades(String idEspecialidades) {
        this.idEspecialidades = idEspecialidades;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    private String idEspecialidades;
    private String tipo;
    public String getClaseMedico() {
        return claseMedico;
    }

    public void setClaseMedico(String claseMedico) {
        this.claseMedico = claseMedico;
    }

    public int getIdPanelCliente() {return idPanelCliente;}
    public void setIdPanelCliente(int idPanelCliente) {
        this.idPanelCliente = idPanelCliente;
    }
    public String getIdCliente() {return idCliente;}
    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }
    public String getNombreCliente() {return NombreCliente;}
    public void setNombreCliente(String NombreCliente) {
        this.NombreCliente = NombreCliente;
    }
    public String getRepresentante() {return representante;}
    public void setRepresentante(String representante) {
        this.representante = representante;
    }
    public String getCiudad() {return ciudad;}
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    public String getIdUsuario() {return idUsuario;}
    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getRevisita() { return revisita; }

    public void setRevisita(Integer revisita) { this.revisita = revisita; }
    public String getEstadoSeleccion() {
        return estadoSeleccion;
    }

    public void setEstadoSeleccion(String estadoSeleccion) {
        this.estadoSeleccion = estadoSeleccion;
    }

    public String getTipoObserv() {
        return tipoObserv;
    }

    public void setTipoObserv(String tipoObserv) {
        this.tipoObserv = tipoObserv;
    }

    public String getEstadoVisita() {
        return estadoVisita;
    }

    public void setEstadoVisita(String estadoVisita) {
        this.estadoVisita = estadoVisita;
    }
    public Boolean getCumpleAnyos() {
        return cumpleAnyos;
    }

    public void setCumpleAnyos(Boolean cumpleAnyos) {
        this.cumpleAnyos = cumpleAnyos;
    }
}
