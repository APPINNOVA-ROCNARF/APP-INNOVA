package com.rocnarf.rocnarf.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PoliticasResponse {

    @SerializedName("totalItems")
    public String totalItems;

    @SerializedName("items")
    public List<Politicas> items;


}
