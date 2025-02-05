package com.rocnarf.rocnarf;

import com.google.gson.annotations.SerializedName;
import com.rocnarf.rocnarf.models.HistorialComentarios;
import com.rocnarf.rocnarf.models.Planes;

import java.util.List;

public class HistorialComentariosResponse {

    @SerializedName("totalItems")
    public String totalItems;

    @SerializedName("items")
    public List<HistorialComentarios> items;


}
