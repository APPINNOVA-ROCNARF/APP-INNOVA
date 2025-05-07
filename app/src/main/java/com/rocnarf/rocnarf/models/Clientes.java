package com.rocnarf.rocnarf.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "clientes")
public class Clientes  implements Parcelable {

    @PrimaryKey
    @NonNull
    @SerializedName("idCliente")
    @Expose
    private String idCliente;

    @SerializedName("nombre")
    @Expose
    private String nombreCliente;

    @SerializedName("tipo")
    @Expose
    private String tipo;

    @SerializedName("seccion")
    @Expose
    private  String seccion;

    @SerializedName("seccion2")
    @Expose
    private  String seccion2;

    @SerializedName("seccion3")
    @Expose
    private  String seccion3;

    @SerializedName("seccion4")
    @Expose
    private  String seccion4;

    @SerializedName("seccion5")
    @Expose
    private  String seccion5;

    @SerializedName("seccion6")
    @Expose
    private  String seccion6;

    @SerializedName("seccion7")
    @Expose
    private  String seccion7;

    @SerializedName("seccion8")
    @Expose
    private  String seccion8;

    @SerializedName("seccion9")
    @Expose
    private  String seccion9;

    @SerializedName("dueno")
    @Expose
    private String representante;

    @SerializedName("ciudad")
    @Expose
    private String ciudad;

    @SerializedName("direccion")
    @Expose
    private String direccion;

    @SerializedName("telefono1")
    @Expose
    private String telefono1;

    @SerializedName("telefono2")
    @Expose
    private String telefono2;

    @SerializedName("cedula")
    @Expose
    private String cedula;

    @SerializedName("ruc")
    @Expose
    private String ruc;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("latitud")
    @Expose
    private Double latitud;

    @SerializedName("longitud")
    @Expose
    private Double longitud;

    @SerializedName("rangoTolerancia")
    @Expose
    private int rangoTolerancia;

    @SerializedName("origen")
    @Expose
    private String origen;

    @SerializedName("tipoObserv")
    @Expose
    private String tipoObserv;

    @SerializedName("especialidades")
    @Expose
    private String especialidades;

    @SerializedName("idEspecialidades")
    @Expose
    private String idEspecialidades;

    @SerializedName("clase")
    @Expose
    private String clase;

    @SerializedName("clase3")
    @Expose
    private String clase3;

    @SerializedName("clase4")
    @Expose
    private String clase4;


    @SerializedName("fechaDesdeAuspicio")
    @Expose
    private Date fechaDesdeAuspicio;

    @SerializedName("fechaHastaAuspicio")
    @Expose
    private Date fechaHastaAuspicio;

    @SerializedName("conceptoPlan")
    @Expose
    private String conceptoPlan;

    @SerializedName("marca")
    @Expose
    private String marca;

    @SerializedName("cumpleAnyos")
    @Expose
    private Boolean cumpleAnyos;

    @SerializedName("revisita")
    @Expose
    private Integer revisita;
    @SerializedName("revisita3")
    @Expose
    private Integer revisita3;

    @SerializedName("revisit4")
    @Expose
    private Integer revisita4;

    private String estadoVisita;
    private String pedido;
    private String cobro;

    private String estadoSeleccion;


    @SerializedName("auspiciado")
    @Expose
    private Boolean auspiciado;

    private String claseMedico;

    public Clientes(String idCliente, String nombreCliente, String representante,
                    String tipo, String seccion, String seccion2, String seccion3, String seccion4, String seccion5,
                    String seccion6, String seccion7, String seccion8, String seccion9, String ciudad, String direccion,
                    String telefono1, String telefono2, String cedula, String ruc, String email, Double longitud,
                    Double latitud, int rangoTolerancia, String origen, String tipoObserv, String especialidades,
                    String idEspecialidades, String clase, String claseMedico, Boolean auspiciado, Boolean cumpleAnyos,
                    Date fechaDesdeAuspicio, Date fechaHastaAuspicio, String conceptoPlan, String marca, Integer revisita,
                    String clase3, String clase4) {

        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.representante = representante;
        this.tipo = tipo;
        this.seccion = seccion;
        this.seccion2 = seccion2;
        this.seccion3 = seccion3;
        this.seccion4 = seccion4;
        this.seccion5 = seccion5;
        this.seccion6 = seccion6;
        this.seccion7 = seccion7;
        this.seccion8 = seccion8;
        this.seccion9 = seccion9;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.telefono1 = telefono1;
        this.telefono2 = telefono2;
        this.cedula = cedula;
        this.ruc = ruc;
        this.email = email;
        this.longitud = longitud;
        this.latitud = latitud;
        this.rangoTolerancia = rangoTolerancia;
        this.origen = origen;
        this.tipoObserv = tipoObserv;
        this.especialidades = especialidades;
        this.idEspecialidades = idEspecialidades;
        this.clase = clase;
        this.claseMedico = claseMedico;
        this.auspiciado = auspiciado;
        this.cumpleAnyos = cumpleAnyos;
        this.fechaDesdeAuspicio = fechaDesdeAuspicio;
        this.fechaHastaAuspicio = fechaHastaAuspicio;
        this.conceptoPlan = conceptoPlan;
        this.marca = marca;
        this.revisita = revisita;
        this.clase3 = clase3;
        this.clase4 = clase4;
    }




    //private String nombreCliente, representante, direccion, estado ;


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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getSeccion2() {
        return seccion2;
    }

    public void setSeccion2(String seccion2) {
        this.seccion2 = seccion2;
    }

    public String getSeccion3() {
        return seccion3;
    }

    public void setSeccion3(String seccion3) {
        this.seccion3 = seccion3;
    }

    public String getSeccion4() {
        return seccion4;
    }

    public void setSeccion4(String seccion4) {
        this.seccion4 = seccion4;
    }

    public String getSeccion5() {
        return seccion5;
    }

    public void setSeccion5(String seccion5) {
        this.seccion5 = seccion5;
    }

    public String getSeccion6() {
        return seccion6;
    }

    public void setSeccion6(String seccion6) {
        this.seccion6 = seccion6;
    }

    public String getSeccion7() {
        return seccion7;
    }

    public void setSeccion7(String seccion7) {
        this.seccion7 = seccion7;
    }

    public String getSeccion8() {
        return seccion8;
    }

    public void setSeccion8(String seccion8) {
        this.seccion8 = seccion8;
    }

    public String getSeccion9() {
        return seccion9;
    }

    public void setSeccion9(String seccion9) {
        this.seccion9 = seccion9;
    }

    public String getRepresentante() {
        return representante;
    }

    public void setRepresentante(String representante) {
        this.representante = representante;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getRangoTolerancia() {
        return rangoTolerancia;
    }

    public void setRangoTolerancia(int rangoTolerancia) {
        this.rangoTolerancia = rangoTolerancia;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getEstadoVisita() {
        return estadoVisita;
    }

    public void setEstadoVisita(String estadoVisita) {
        this.estadoVisita = estadoVisita;
    }

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

    public String getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(String especialidades) {
        this.especialidades = especialidades;
    }

    public String getIdEspecialidades() {
        return idEspecialidades;
    }

    public void setIdEspecialidades(String idEspecialidades) {
        this.idEspecialidades = idEspecialidades;
    }

    public String getClase() {
        return clase;
    }

    public String getClase3() {
        return clase3;
    }

    public void setClase3(String clase3) {
        this.clase3 = clase3;
    }

    public String getClase4() {
        return clase4;
    }

    public void setClase4(String clase4) {
        this.clase4 = clase4;
    }


    public void setClase(String clase) {
        this.clase = clase;
    }
    public String getClaseMedico() {
        return tipoObserv + " " + (clase != null ?clase :"");
    }

    public String getPedido() {
        return pedido;
    }

    public void setPedido(String pedido) {
        this.pedido = pedido;
    }

    public String getCobro() {
        return cobro;
    }

    public void setCobro(String cobro) {
        this.cobro = cobro;
    }

    public Boolean getCumpleAnyos() {
        return cumpleAnyos;
    }

    public void setCumpleAnyos(Boolean cumpleAnyos) {
        this.cumpleAnyos = cumpleAnyos;
    }

    public Boolean getAuspiciado() {
        return auspiciado;
    }

    public Date getFechaDesdeAuspicio() {
        return fechaDesdeAuspicio;
    }

    public Date getFechaHastaAuspicio() {
        return fechaHastaAuspicio;
    }

    public String getConceptoPlan() {
        return conceptoPlan;
    }

    public String getMarca() {
        return marca;
    }
    public Integer getRevisita() {return revisita;}
    public void setRevisita(Integer revisita) {
        this.revisita  = revisita;
    }

    public Integer getRevisita3() {
        return revisita3;
    }

    public void setRevisita3(Integer revisita3) {
        this.revisita3 = revisita3;
    }

    public Integer getRevisita4() {
        return revisita4;
    }

    public void setRevisita4(Integer revisita4) {
        this.revisita4 = revisita4;
    }

    public void setAuspiciado(Boolean auspiciado) {
        auspiciado = auspiciado;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(idCliente);
        parcel.writeString(nombreCliente);
        parcel.writeString (tipo);
        parcel.writeString (seccion);
        parcel.writeString (seccion2);
        parcel.writeString (seccion3);
        parcel.writeString (seccion4);
        parcel.writeString (representante);
        parcel.writeString (ciudad);
        parcel.writeString (direccion);
        parcel.writeString (telefono1);
        parcel.writeString (telefono2);
        parcel.writeString (cedula);
        parcel.writeString (ruc);
        parcel.writeString (email);
        if (longitud == null) { parcel.writeDouble(0); } else {parcel.writeDouble(longitud);}
        if (latitud == null) { parcel.writeDouble(0);} else {parcel.writeDouble(latitud);}

    }

    public Clientes(Parcel parcel){

        readToParcel(parcel);
    }

    public void readToParcel(Parcel parcel){
        idCliente = parcel.readString();
        nombreCliente = parcel.readString();
        tipo = parcel.readString();
        seccion = parcel.readString();
        seccion2 = parcel.readString();
        seccion3 = parcel.readString();
        seccion4 = parcel.readString();
        representante = parcel.readString();
        ciudad = parcel.readString();
        direccion = parcel.readString();
        telefono1 = parcel.readString();
        telefono2 = parcel.readString();
        cedula = parcel.readString();
        ruc = parcel.readString();
        email = parcel.readString();
        longitud = parcel.readDouble();
        latitud = parcel.readDouble();

    }

    public static final Parcelable.Creator<Clientes> CREATOR = new Parcelable.Creator<Clientes>() {

        public Clientes createFromParcel(final Parcel parcel) {
            return new Clientes(parcel);
        }

        public Clientes[] newArray(final int size) {
            return new Clientes[size];
        }
    };


}
