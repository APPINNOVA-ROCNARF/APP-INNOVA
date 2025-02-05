package com.rocnarf.rocnarf.api;


import com.rocnarf.rocnarf.models.Promocionado;
import com.rocnarf.rocnarf.models.VisitaClientes;
import com.rocnarf.rocnarf.models.VisitaClientesResponse;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface VisitaClientesService {



    @GET("api/visitas")
    Call<VisitaClientesResponse> Get(
            @Query("idAsesor") String idAsesor,
            //@Query("FechaVisitaPlanificada") Date FechaVisitaPlanificada,
            @Query("periodo") Boolean periodo,
            @Query("SortBy") String sortBy
    );


    @POST("api/visitas")
    Observable<VisitaClientes> Post(
            @Body VisitaClientes visitaClientes
    );

    @PUT("api/visitas/{id}")
    Observable<VisitaClientes> Put(
            @Path("id") int idVisitaCliente,
            @Body VisitaClientes visitaClientes
    );

    @DELETE("api/visitas/{id}")
    Call<Void> Delete (
            @Path("id") int id
    );

    @POST("api/visitas/addPromocionado")
    Observable<Promocionado> PostPromocionado(
            @Body List<Promocionado> promocionado
    );

}
