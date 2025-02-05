package com.rocnarf.rocnarf.api;

import com.rocnarf.rocnarf.models.Producto;
import com.rocnarf.rocnarf.models.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductoService {

    @GET("api/productos/")
    Call<List<Producto>> GetAll(
    );

}
