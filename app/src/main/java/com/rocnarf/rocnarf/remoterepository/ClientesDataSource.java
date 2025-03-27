package com.rocnarf.rocnarf.remoterepository;

import androidx.paging.PageKeyedDataSource;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.ClienteService;
import com.rocnarf.rocnarf.models.Clientes;
import com.rocnarf.rocnarf.models.ClientesResponse;
import com.rocnarf.rocnarf.repository.ClientesRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientesDataSource extends PageKeyedDataSource<Integer, Clientes> {
    public static final int PAGE_SIZE = 10;
    private static final int FIRST_PAGE = 1;


    private ClientesRepository clientesRepository;
    private String idUsuario, codigoCliente, nombre, tipo , sector, representante, ciudad,rol;
    private int tipoConsulta;
    private  Context context;
    public void setQueryFilter(Context context, String idUsuario, String codigoCliente, String nombre, String tipo , String sector, String representante, String ciudad, int tipoConsulta, String rol){
        this.context = context;
        this.idUsuario = idUsuario;
        this.codigoCliente = codigoCliente;
        this.nombre = nombre;
        this.tipo = tipo;
        this.sector = sector;
        this.representante = representante;
        this.ciudad = ciudad;
        this.tipoConsulta = tipoConsulta;
        this.rol = rol;
    }



    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Clientes> callback) {
        List<Clientes> lista = new ArrayList<>();
        if (tipoConsulta == 0) {
            ClientesRepository clientesRepository = new ClientesRepository(this.context, idUsuario);
            lista = clientesRepository.getClientes(sector, idUsuario, null, rol);
        }
        if (!lista.isEmpty()){
            callback.onResult(lista, null, FIRST_PAGE + 1);
        }else {
            // Esta parte deberia esta en el repository
            ClienteService service = ApiClient.getClient().create(ClienteService.class);
            Call<ClientesResponse> call = service.GetClientes(FIRST_PAGE, PAGE_SIZE,tipoConsulta,  codigoCliente, nombre, tipo, sector, representante,ciudad,rol,idUsuario);

            call.enqueue(new Callback<ClientesResponse>() {
                @Override
                public void onResponse(Call<ClientesResponse> call, Response<ClientesResponse> response) {
                    if (response.body() != null){
                        ClientesResponse clientesResponse = response.body();
                        callback.onResult(clientesResponse.items, null, FIRST_PAGE + 1);
                    }
                }

                @Override
                public void onFailure(Call<ClientesResponse> call, Throwable t) {
                    call.cancel();
                }
            });
        }
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Clientes> callback) {

        }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Clientes> callback) {
        ClienteService service = ApiClient.getClient().create(ClienteService.class);
        Call<ClientesResponse> call = service.GetClientes(params.key, PAGE_SIZE, tipoConsulta, codigoCliente, nombre, tipo, sector, representante,ciudad,rol,idUsuario);
        call.enqueue(new Callback<ClientesResponse>() {
            @Override
            public void onResponse(Call<ClientesResponse> call, Response<ClientesResponse> response) {
                if (response.body() != null){
                    Integer key = params.key +1;
                    ClientesResponse clientesResponse = response.body();
                    callback.onResult(clientesResponse.items, key );
                }
            }

            @Override
            public void onFailure(Call<ClientesResponse> call, Throwable t) {
                call.cancel();
            }
        });

    }
}
