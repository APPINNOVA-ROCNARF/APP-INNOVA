package com.rocnarf.rocnarf.api;

import com.rocnarf.rocnarf.HistorialComentariosResponse;
import com.rocnarf.rocnarf.models.GuiaProductosResponse;
import com.rocnarf.rocnarf.models.LiquidacionObsequio;
import com.rocnarf.rocnarf.models.LiquidacionObsequioResponse;
import com.rocnarf.rocnarf.models.PlanesResponse;
import com.rocnarf.rocnarf.models.PreguntasFrecuentesResponse;
import com.rocnarf.rocnarf.models.Rutas;
import com.rocnarf.rocnarf.models.RutasResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface RutasService {

    @POST("api/planes/addObsequio")
    Observable<Rutas> CrearRuta(
            @Body Rutas rutas
    );

    @GET("api/planes")
    Call<PlanesResponse> Get(
            @Query("solicitudPremio") Boolean solicitudPremio
    );

    @GET("api/visitas?")
    Call<HistorialComentariosResponse> GetComentarios(
            @Query("idCliente") String idCliente,
            @Query("estado") String estado
    );

    @GET("api/planes/getAll")
    Call<LiquidacionObsequioResponse> GetObsequios(
            @Query("idAsesor") String idAsesor
    );

    @PUT("api/planes/liquidacionObsequio/{id}")
    Observable<LiquidacionObsequio> updateObsequio(
            @Path("id") int id,
            @Body LiquidacionObsequio liquidacionObsequio
    );

    @GET("api/planes/getGuiaProductos")
    Call<GuiaProductosResponse> GetGuiaProductos(
            @Query("status") Boolean status
    );

    @GET("api/planes/getPreguntas")
    Call<RutasResponse> GetRutas(
//            @Query("status") Boolean status
    );
}
