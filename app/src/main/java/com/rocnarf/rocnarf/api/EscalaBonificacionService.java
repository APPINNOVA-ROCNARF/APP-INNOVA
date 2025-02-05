package com.rocnarf.rocnarf.api;

import com.rocnarf.rocnarf.models.EscalaBonificacion;
import com.rocnarf.rocnarf.models.Producto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EscalaBonificacionService {

    @GET("api/escalabonificacion/")
    Call<List<EscalaBonificacion>> GetAll(
    );

}
