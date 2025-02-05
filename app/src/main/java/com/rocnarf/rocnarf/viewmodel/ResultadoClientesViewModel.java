package com.rocnarf.rocnarf.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import android.content.Context;

import com.rocnarf.rocnarf.models.Clientes;
import com.rocnarf.rocnarf.remoterepository.ClienteDataSourceFactory;
import com.rocnarf.rocnarf.remoterepository.ClientesDataSource;

public class ResultadoClientesViewModel extends AndroidViewModel {

    ClienteDataSourceFactory clienteDataSourceFactory;


    public LiveData<PagedList<Clientes>> listaClientes;
    private Context context;
    //LiveData<PageKeyedDataSource<Integer, Clientes>> liveDataSource;

    public ResultadoClientesViewModel (Application application){
        super( application);
        this.context = application.getApplicationContext();
        clienteDataSourceFactory = new ClienteDataSourceFactory();
        //liveDataSource = clienteDataSourceFactory.getClientesLiveDataSources();
        PagedList.Config pageListConfig = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(ClientesDataSource.PAGE_SIZE).build();
        listaClientes = (new LivePagedListBuilder(clienteDataSourceFactory, pageListConfig)).build();
    }


    public void setQueryFilter(String idUsuario, String codigoCliente, String nombre, String tipo , String sector, String representante, String ciudad, int tipoConsulta, String rol){
        clienteDataSourceFactory.setQueryFilter(context, idUsuario, codigoCliente, nombre, tipo , sector, representante, ciudad, tipoConsulta, rol);
    }


}