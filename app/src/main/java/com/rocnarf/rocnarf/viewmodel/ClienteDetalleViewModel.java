package com.rocnarf.rocnarf.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import android.content.Context;
import androidx.annotation.NonNull;

import com.rocnarf.rocnarf.models.Clientes;
import com.rocnarf.rocnarf.repository.ClientesRepository;

public class ClienteDetalleViewModel extends AndroidViewModel {

    public LiveData<Clientes> cliente;
    private ClientesRepository clientesRepository;
    private Context context;
    private String idUsuario;

    public ClienteDetalleViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();

    }

    public void setIdUsuario(String idUsuario){
        this.idUsuario = idUsuario;
    }

    public LiveData<Clientes> getByid(String idCliente){
        clientesRepository = new ClientesRepository(context, idUsuario );
        cliente=  this.clientesRepository.getById(idCliente);
        return cliente;
    }

}
