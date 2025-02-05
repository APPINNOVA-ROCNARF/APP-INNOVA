package com.rocnarf.rocnarf.models;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.List;

public class ClientesResponse {

    @SerializedName("totalItems")
    public String totalItems;

    @SerializedName("items")
    public List<Clientes> items;


}
