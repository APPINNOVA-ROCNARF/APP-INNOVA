package com.rocnarf.rocnarf.models;
import java.util.Date;
public class Recetas {
    private int id ;
    private String codigo;
    private String medico;
    private String producto;
    private String laboratorio;
    private String Mercado;
    private int sem1;
    private int ytd1;
    private int sem2;
    private int ytd2;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }

    public String getMercado() {
        return Mercado;
    }

    public void setMercado(String mercado) {
        Mercado = mercado;
    }

    public int getSem1() {
        return sem1;
    }

    public void setSem1(int sem1) {
        sem1 = sem1;
    }

    public int getYtd1() {
        return ytd1;
    }

    public void setYtd1(int ytd1) {
        ytd1 = ytd1;
    }

    public int getSem2() {
        return sem2;
    }

    public void setSem2(int sem2) {
        sem2 = sem2;
    }

    public int getYtd2() {
        return ytd2;
    }

    public void setYtd2(int ytd2) {
        ytd2 = ytd2;
    }



}
