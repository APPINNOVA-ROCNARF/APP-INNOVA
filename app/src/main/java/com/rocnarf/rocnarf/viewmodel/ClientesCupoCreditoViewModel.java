package com.rocnarf.rocnarf.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import android.content.Context;
import androidx.annotation.NonNull;

import com.rocnarf.rocnarf.models.ClientesCupoCredito;
import com.rocnarf.rocnarf.repository.ClientesRepository;

public class ClientesCupoCreditoViewModel extends AndroidViewModel {
    private LiveData<ClientesCupoCredito> cupoCreditoLiveData;
    private  String idUsuario;
    private Context context;
    private ClientesRepository clientesRepository;

    public ClientesCupoCreditoViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
    }

    public void setIdUsuario(String idUsuario){
        this.idUsuario = idUsuario;
        clientesRepository = new ClientesRepository(context, idUsuario );
    }

    public LiveData<ClientesCupoCredito> getCupoCredito(String idCliente) {
        cupoCreditoLiveData = clientesRepository.getCupoCredito(idCliente);
        return cupoCreditoLiveData;
    }
}
