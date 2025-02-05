package com.rocnarf.rocnarf.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import androidx.annotation.NonNull;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.models.Pedido;
import com.rocnarf.rocnarf.repository.PedidosRepository;

import java.util.List;

public class PedidoListaViewModel extends AndroidViewModel {
    private  Context context;
    private PedidosRepository pedidosRepository;

    public LiveData<List<Pedido>> listaPedidos ;

    public PedidoListaViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        pedidosRepository = new PedidosRepository(this.context );

    }


    public LiveData<List<Pedido>>  getPedidos(String idAsesor){
        if (listaPedidos == null) {
            listaPedidos = new MutableLiveData<>();
            MutableLiveData pedidos = new MutableLiveData<>();
            pedidos.setValue(pedidosRepository.getPedidos(idAsesor, Common.PED_PENDIENTE));
            listaPedidos = pedidos;
        }
        return listaPedidos;
    }
}
