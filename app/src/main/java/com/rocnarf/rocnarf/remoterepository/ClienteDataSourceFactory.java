package com.rocnarf.rocnarf.remoterepository;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;
import android.content.Context;

import com.rocnarf.rocnarf.models.Clientes;

public class ClienteDataSourceFactory extends DataSource.Factory {


    //private MutableLiveData<PageKeyedDataSource<Integer, Clientes>> clientesLiveDataSources = new MutableLiveData<>();
    private ClientesDataSource dataSource  = new ClientesDataSource();

    @Override
    public DataSource<Integer, Clientes> create() {

        //clientesLiveDataSources.postValue(dataSource);
        return dataSource;
    }

//    public MutableLiveData<PageKeyedDataSource<Integer, Clientes>> getClientesLiveDataSources() {
//        return clientesLiveDataSources;
//    }

    public void setQueryFilter(Context context, String idUsuario, String codigoCliente, String nombre, String tipo , String sector, String representante, String ciudad, int tipoConsulta, String rol){
        dataSource.setQueryFilter(context, idUsuario, codigoCliente, nombre, tipo , sector, representante, ciudad, tipoConsulta, rol);
    }

}
