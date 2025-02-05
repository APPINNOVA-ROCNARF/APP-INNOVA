package com.rocnarf.rocnarf.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PedidosPendienteResponse  {

    @SerializedName("totalItems")
    public String totalItems;

    @SerializedName("items")
    public List<PedidosPendiente> items;


}
