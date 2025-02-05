package com.rocnarf.rocnarf.api;

import com.rocnarf.rocnarf.models.Usuario;
import com.rocnarf.rocnarf.models.VisitaClientesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UsuarioService {


    @GET("api/usuarios/{id}/{clave}")
    Call<Usuario> Get(
            @Path("id") String idAsesor,
            @Path("clave") String clave
    );
}
