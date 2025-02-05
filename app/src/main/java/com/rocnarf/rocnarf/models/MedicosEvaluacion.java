package com.rocnarf.rocnarf.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Entity(tableName = "medicosEvaluacion")
public class MedicosEvaluacion {
    @Expose
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    private int id ;

    @Expose
    @SerializedName("codigo")
    private String codigo;

    @Expose
    @SerializedName("campo5")
    private String campo5;

    @Expose
    @SerializedName("codigoClose")
    private String codigoClose;

    @Expose
    @SerializedName("medico")
    private String medico;

    @Expose
    @SerializedName("region")
    private String region;

    @Expose
    @SerializedName("provincia")
    private String provincia;

    @Expose
    @SerializedName("prestadora")
    private String prestadora;



    @Expose
    @SerializedName("compra")
    private String compra;

    @Expose
    @SerializedName("rxRocnarf")
    private BigDecimal rxRocnarf;

    @Expose
    @SerializedName("afinidad")
    private int afinidad;

    @Expose
    @SerializedName("quintil")
    private int quintil;

    @Expose
    @SerializedName("quiltelClose")
    private BigDecimal quiltelClose;

    @Expose
    @SerializedName("pxRocnarf")
    private BigDecimal pxRocnarf;

    @Expose
    @SerializedName("afinidadRelacion")
    private BigDecimal afinidadRelacion;

    @Expose
    @SerializedName("compraNo")
    private BigDecimal compraNo;

    @Expose
    @SerializedName("prestadoraSalud")
    private BigDecimal prestadoraSalud;

    @Expose
    @SerializedName("total")
    private BigDecimal total;

    @Expose
    @SerializedName("categoriaMedico")
    private String categoriaMedico;

    @Expose
    @SerializedName("division")
    private int division;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompra() {
        return compra;
    }

    public void setCompra(String compra) {
        this.compra = compra;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCampo5() {
        return campo5;
    }

    public void setCampo5(String campo5) {
        this.campo5 = campo5;
    }

    public String getCodigoClose() {
        return codigoClose;
    }

    public void setCodigoClose(String codigoClose) {
        this.codigoClose = codigoClose;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getPrestadora() {
        return prestadora;
    }

    public void setPrestadora(String prestadora) {
        this.prestadora = prestadora;
    }

    public BigDecimal getRxRocnarf() {
        return rxRocnarf;
    }

    public void setRxRocnarf(BigDecimal rxRocnarf) {
        this.rxRocnarf = rxRocnarf;
    }

    public int getAfinidad() {
        return afinidad;
    }

    public void setAfinidad(int afinidad) {
        this.afinidad = afinidad;
    }

    public int getQuintil() {
        return quintil;
    }

    public void setQuintil(int quintil) {
        this.quintil = quintil;
    }

    public BigDecimal getQuiltelClose() {
        return quiltelClose;
    }

    public void setQuiltelClose(BigDecimal quiltelClose) {
        this.quiltelClose = quiltelClose;
    }

    public BigDecimal getPxRocnarf() {
        return pxRocnarf;
    }

    public void setPxRocnarf(BigDecimal pxRocnarf) {
        this.pxRocnarf = pxRocnarf;
    }

    public BigDecimal getAfinidadRelacion() {
        return afinidadRelacion;
    }

    public void setAfinidadRelacion(BigDecimal afinidadRelacion) {
        this.afinidadRelacion = afinidadRelacion;
    }

    public BigDecimal getCompraNo() {
        return compraNo;
    }

    public void setCompraNo(BigDecimal compraNo) {
        this.compraNo = compraNo;
    }

    public BigDecimal getPrestadoraSalud() {
        return prestadoraSalud;
    }

    public void setPrestadoraSalud(BigDecimal prestadoraSalud) {
        this.prestadoraSalud = prestadoraSalud;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getCategoriaMedico() {
        return categoriaMedico;
    }

    public void setCategoriaMedico(String categoriaMedico) {
        this.categoriaMedico = categoriaMedico;
    }

    public int getDivision() {
        return division;
    }

    public void setDivision(int division) {
        this.division = division;
    }




}
