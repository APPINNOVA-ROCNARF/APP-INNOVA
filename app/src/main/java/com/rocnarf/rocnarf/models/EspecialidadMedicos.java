package com.rocnarf.rocnarf.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "especialidadMedicos")
public class EspecialidadMedicos {

    @PrimaryKey
    @NonNull
    @SerializedName("idCliente")
    @Expose
    private  int idCliente ;

    @SerializedName("f1")
    @Expose
    private  String f1;

    @SerializedName("f2")
    @Expose
    private  String f2;

    @SerializedName("f3")
    @Expose
    private  String f3;

    @SerializedName("f4")
    @Expose
    private  String f4;

    public int idCliente() {
        return idCliente; };

    public String getF1() {
        return f1;
    }

    public String getF2() {
        return f2;
    }

    public String getF3() {
        return f3;
    }

    public String getF4() {
        return f4;
    }



}
