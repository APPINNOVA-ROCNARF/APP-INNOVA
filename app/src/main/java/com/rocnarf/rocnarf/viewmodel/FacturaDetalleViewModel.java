package com.rocnarf.rocnarf.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import androidx.annotation.NonNull;

import com.rocnarf.rocnarf.models.FacturaDetalle;
import com.rocnarf.rocnarf.repository.ClientesRepository;

import java.util.List;

public class FacturaDetalleViewModel extends AndroidViewModel {
    private Context context;
    private ClientesRepository clientesRepository;
    private  String idUsuario;
    private LiveData<List<FacturaDetalle>> listaFacturaDetalle;

    public FacturaDetalleViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
    }

    public void setIdUsuario(String idUsuario){
        this.idUsuario = idUsuario;
        clientesRepository = new ClientesRepository(context, idUsuario );
    }

    public LiveData<List<FacturaDetalle>> getFacturaDetalles(final String idFactura){
        listaFacturaDetalle = clientesRepository.getFacturaDetalles(idFactura);
        return listaFacturaDetalle;
    }

    public LiveData<List<FacturaDetalle>> getDetallesFacturasXCliente(final String idCliente){
        listaFacturaDetalle = clientesRepository.getDetallesFacturasXCliente(idCliente);
        return listaFacturaDetalle;
    }

}
