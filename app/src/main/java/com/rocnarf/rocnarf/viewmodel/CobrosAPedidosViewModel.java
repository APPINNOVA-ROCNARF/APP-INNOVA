package com.rocnarf.rocnarf.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import androidx.annotation.NonNull;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.models.Pedido;
import com.rocnarf.rocnarf.repository.PedidosRepository;

import java.io.File;
import java.util.Date;

import rx.Completable;
import rx.Subscription;

import static com.rocnarf.rocnarf.Utils.Common.PED_PENDIENTE;

public class CobrosAPedidosViewModel extends AndroidViewModel {
    private  String idUsuario;
    private Context context;
    private PedidosRepository pedidosRepository;
    public MutableLiveData<String> sincronizado = new MutableLiveData<>();

    public MutableLiveData<Pedido> pedido = new MutableLiveData<>();

    public MutableLiveData<File> photoReciboFile ;
    public MutableLiveData<File> photoFormaPagoFile;

    public CobrosAPedidosViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
    }

    public void setIdUsuario(String idUsuario){
        this.idUsuario = idUsuario;
        pedidosRepository = new PedidosRepository(context);

    }


    public Completable getPedido(final String idCliente, final String nombreCliente ){

        return Completable.create(new Completable.CompletableOnSubscribe() {
            @Override
            public void call(Completable.CompletableSubscriber completableSubscriber) {
                // Se consulta si el cliente tiene un pedido en progeso (Pendiente)
                // de tenerlo se sigue con el mismo, caso contrario se crea un pedido
                Pedido pedidoExistente =  pedidosRepository.getPedidoPendiente(idCliente, idUsuario);
                if (pedidoExistente != null) {
                    pedido.setValue(pedidoExistente);
                }
                completableSubscriber.onCompleted();
            }
        });
    }

    public Pedido addPedido (String idCliente, String nombreCliente ){
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setTipoPedido(Common.TIPO_PEDIDO_COBRO);
        nuevoPedido.setIdAsesor(this.idUsuario);
        nuevoPedido.setIdCliente(idCliente);
        nuevoPedido.setNombreCliente(nombreCliente);
        nuevoPedido.setFechaPedido(new Date());
        nuevoPedido.setPrecioTotal(0.00);
        nuevoPedido.setPorcentajeDescuento(0.00);
        nuevoPedido.setDescuento(0.00);
        nuevoPedido.setPrecioFinal(0.00);
        nuevoPedido.setEstado(PED_PENDIENTE);
        nuevoPedido.setPendienteSync(true);

        this.pedidosRepository.addPedido(nuevoPedido);
        nuevoPedido =  pedidosRepository.getPedidoPendiente(idCliente, idUsuario);
        this.pedido.setValue(nuevoPedido);
        return nuevoPedido;
    }

    public Pedido updatePedido(Pedido pedido){
        this.pedidosRepository.updatePedido(pedido);
        this.pedido.setValue(pedido);
        return pedido;
    }

    public void Sync (){
        //pedidosRepository.addPedido(pedido.getValue());
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

}
