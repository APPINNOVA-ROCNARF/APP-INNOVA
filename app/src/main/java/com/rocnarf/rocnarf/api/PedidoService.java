package com.rocnarf.rocnarf.api;

import com.rocnarf.rocnarf.models.Pedido;
import com.rocnarf.rocnarf.models.PedidoResponse;
import com.rocnarf.rocnarf.models.VisitaClientesResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface PedidoService {

    @POST("api/pedidos")
    Observable<Pedido> Post(
            @Body Pedido pedido
    );

    @GET("api/pedidos")
    Call<PedidoResponse> Get(
            @Query("idAsesor") String idAsesor,
            @Query("periodo") Boolean periodo,
            @Query("SortBy") String sortBy
    );

}
