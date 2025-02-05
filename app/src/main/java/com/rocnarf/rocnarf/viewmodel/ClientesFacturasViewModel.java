package com.rocnarf.rocnarf.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import androidx.annotation.NonNull;

import com.rocnarf.rocnarf.models.Factura;
import com.rocnarf.rocnarf.models.FacturasNotaDebitos;
import com.rocnarf.rocnarf.repository.ClientesRepository;

import java.util.List;

public class ClientesFacturasViewModel extends AndroidViewModel{

    public LiveData<List<Factura>> listaFacturas;
    public LiveData<List<FacturasNotaDebitos>> listaFacturasND;
    private ClientesRepository clientesRepository;
    private String idUsuario;
    private Context context;

    public ClientesFacturasViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        listaFacturas =  new MutableLiveData<>();
        listaFacturasND = new MutableLiveData<>();
    }

    public void setIdUsuario(String idUsuario){
        this.idUsuario = idUsuario;
    }

    public LiveData<List<Factura>> getFacturas(String idCliente, String seccion){
        clientesRepository = new ClientesRepository(context, idUsuario );
        listaFacturas = clientesRepository.getFacturas(idCliente, seccion);
        return listaFacturas;
    }

    public LiveData<List<FacturasNotaDebitos>> getFacturasNotaDebitos(String idCliente, String seccion){
        clientesRepository = new ClientesRepository(context, idUsuario );
        listaFacturasND = clientesRepository.getFacturasNotaDebitos(idCliente, seccion);
        return listaFacturasND;
    }

}
