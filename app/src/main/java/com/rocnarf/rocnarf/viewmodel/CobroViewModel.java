package com.rocnarf.rocnarf.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import android.content.Context;
import androidx.annotation.NonNull;

import com.rocnarf.rocnarf.models.Cobro;
import com.rocnarf.rocnarf.models.NotaCredito;
import com.rocnarf.rocnarf.repository.ClientesRepository;

import java.util.List;

public class CobroViewModel extends AndroidViewModel {
    private  String idUsuario;
    private Context context;
    private ClientesRepository clientesRepository;
    private LiveData<List<Cobro>> listaFacturaDetalle;
    private LiveData<List<NotaCredito>> listaNotaCredito;

    public CobroViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
    }

    public void setIdUsuario(String idUsuario){
        this.idUsuario = idUsuario;
        clientesRepository = new ClientesRepository(context, idUsuario );
    }


    public LiveData<List<Cobro>> geGetCobrosXFactura(final String idFactura){
        listaFacturaDetalle = clientesRepository.geGetCobrosXFactura(idFactura);
        return listaFacturaDetalle;
    }

    public LiveData<List<Cobro>> getChequeFechaXFactura(final String idFactura) {
        listaFacturaDetalle = clientesRepository.GetChequeFechaXFactura(idFactura);
        return listaFacturaDetalle;
    }

    public LiveData<List<Cobro>> getCobrosXCliente(final String idCliente){
        listaFacturaDetalle = clientesRepository.getCobrosXCliente(idCliente);
        return listaFacturaDetalle;
    }

    public LiveData<List<Cobro>> getChequeFechaXCliente(final String idCliente){
        listaFacturaDetalle = clientesRepository.getChequeFechaXCliente(idCliente);
        return listaFacturaDetalle;
    }

    public LiveData<List<NotaCredito>> getNcXCliente(final String idFactura,final String idCliente){
        listaNotaCredito = clientesRepository.getNcXCliente(idFactura, idCliente);
        return listaNotaCredito;
    }
}
