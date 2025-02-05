package com.rocnarf.rocnarf.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import androidx.annotation.NonNull;

import com.rocnarf.rocnarf.models.ClientesCupoCredito;
import com.rocnarf.rocnarf.models.Factura;
import com.rocnarf.rocnarf.models.Pedido;
import com.rocnarf.rocnarf.models.PedidoDetalle;
import com.rocnarf.rocnarf.repository.ClientesRepository;
import com.rocnarf.rocnarf.repository.PedidosRepository;

import java.util.List;

import rx.Completable;
import rx.Subscription;


public class PedidoViewModel extends AndroidViewModel {
    private Context context;
    private PedidosRepository pedidosRepository;
    private ClientesRepository clientesRepository;
    private  String idUsuario;

    public MutableLiveData<Pedido> pedido ;
    public MutableLiveData<List<PedidoDetalle>> detallesPedido;
    public MutableLiveData<String> sincronizado = new MutableLiveData<>();
    private LiveData<ClientesCupoCredito> cupoCreditoLiveData;
    public LiveData<List<Factura>> listaFacturas;

    public PedidoViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        this.pedidosRepository= new PedidosRepository(context);
    }

    public void setIdUsuario(String idUsuario){
        this.idUsuario = idUsuario;
        clientesRepository = new ClientesRepository(context, idUsuario );
    }

    public void init(int idLocalPedido){
        MutableLiveData pedidoCliente = new MutableLiveData<>();
        pedidoCliente.setValue(pedidosRepository.getPedidoByIdLocal(idLocalPedido));
        pedido = pedidoCliente;

        MutableLiveData detalesPedidoCliente = new MutableLiveData();
        detalesPedidoCliente.setValue(pedidosRepository.getDetallesPedido(idLocalPedido));
        detallesPedido = detalesPedidoCliente;
    }

    public LiveData<Pedido> getPedidoByIdLocal(int idLocalPedido){
        return pedido;
    }

    public void  updatePedido(Pedido pedido){
        this.pedidosRepository.updatePedido(pedido);
        this.pedido.setValue(pedido);
    }

    public void  deletePedido(){
        this.pedidosRepository.deletePedido(pedido.getValue());
    }


    public LiveData<List<PedidoDetalle>> getDetallesPedido(int idLocalPedido) {
        return detallesPedido;
    }

    public void deletePedidoDetalle(PedidoDetalle pedidoDetalle){
        //MutableLiveData detalesPedidoCliente = new MutableLiveData();
        detallesPedido.setValue(this.pedidosRepository.deletePediodDetalle(pedido.getValue(), pedidoDetalle));
        //detallesPedido. = detalesPedidoCliente;

    }

    public void updatePedidoDetalle(PedidoDetalle pedidoDetalle){
        this.pedidosRepository.updatePedidoDetalle(pedidoDetalle);
    }

    public void updatePedidoDetalleIngresada(PedidoDetalle pedidoDetalle){
        this.pedidosRepository.updatePedidoDetalle(pedidoDetalle);
    }

    public void Sync (){
        this.pedidosRepository.sync(pedido.getValue().getIdLocalPedido()).subscribe(new Completable.CompletableSubscriber() {
            @Override
            public void onCompleted() {

                sincronizado.setValue("OK");

            }

            @Override
            public void onError(Throwable e) {
                sincronizado.setValue(e.getMessage());
            }

            @Override
            public void onSubscribe(Subscription d) {

            }
        });
    }


    public LiveData<ClientesCupoCredito> getCupoCredito(String idCliente) {
        cupoCreditoLiveData = clientesRepository.getCupoCredito(idCliente);
        return cupoCreditoLiveData;
    }

    public LiveData<List<Factura>> getFacturas(String idCliente, String seccion){
        clientesRepository = new ClientesRepository(context, idUsuario );
        listaFacturas = clientesRepository.getFacturas(idCliente, seccion);
        return listaFacturas;
    }


}
