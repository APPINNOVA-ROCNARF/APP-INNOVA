package com.rocnarf.rocnarf;

import com.google.gson.annotations.SerializedName;
import com.rocnarf.rocnarf.models.HistorialComentarios;
import com.rocnarf.rocnarf.models.Recetas;

import java.util.List;

public class RecetasXResponse {

    @SerializedName("totalItems")
    public int totalItems;

    @SerializedName("items")
    public List<Recetas> items;


}
