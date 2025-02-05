package com.rocnarf.rocnarf.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MedicosCumpleanyosResponse {

    @SerializedName("totalItems")
    public int totalItems;

    @SerializedName("items")
    public List<MedicosCumpleanyos> items;


}
