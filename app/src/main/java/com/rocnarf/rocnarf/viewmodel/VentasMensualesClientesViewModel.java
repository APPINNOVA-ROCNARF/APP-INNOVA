package com.rocnarf.rocnarf.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import android.content.Context;
import androidx.annotation.NonNull;

import com.rocnarf.rocnarf.models.VentaMensualXCliente;
import com.rocnarf.rocnarf.repository.ClientesRepository;

import java.util.List;

public class VentasMensualesClientesViewModel extends AndroidViewModel {
    private LiveData<List<VentaMensualXCliente>> ventaMensualXClienteMutableLiveData ;

    private  String idUsuario;
    private Context context;
    private ClientesRepository clientesRepository;

    public VentasMensualesClientesViewModel(@NonNull Application application) {
        super(application);
    }


    public void setIdUsuario(String idUsuario){
        this.idUsuario = idUsuario;
        clientesRepository = new ClientesRepository(context, idUsuario );
    }

    public LiveData<List<VentaMensualXCliente>> getVentasMensualesXCliente(String idCliente) {
        ventaMensualXClienteMutableLiveData = clientesRepository.getVentasMensualesXCliente(idCliente);
        return ventaMensualXClienteMutableLiveData;
    }

}
