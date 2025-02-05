package com.rocnarf.rocnarf.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import androidx.annotation.NonNull;

import com.rocnarf.rocnarf.models.EscalaBonificacion;
import com.rocnarf.rocnarf.models.Pedido;
import com.rocnarf.rocnarf.models.PedidoDetalle;
import com.rocnarf.rocnarf.models.Producto;
import com.rocnarf.rocnarf.repository.EscalaBonificacionRepository;
import com.rocnarf.rocnarf.repository.PedidosRepository;
import com.rocnarf.rocnarf.repository.ProductosRepository;


import java.util.Date;
import java.util.List;

import rx.Completable;
import rx.Subscription;

import static com.rocnarf.rocnarf.Utils.Common.PED_PENDIENTE;

public class ProductosViewModel extends AndroidViewModel {
    private  String idUsuario;
    private Context context;
    private ProductosRepository productosRepository;
    private PedidosRepository pedidosRepository;
    private EscalaBonificacionRepository escalaBonificacionRepository;

    public MutableLiveData<Pedido> pedido = new MutableLiveData<>();
    public MutableLiveData<List<Producto>> listaProductos = new MutableLiveData<>();
    public MutableLiveData<List<EscalaBonificacion>> listaEscalasBonificaciones = new MutableLiveData<>();

    public ProductosViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
    }

    public void setIdUsuario(String idUsuario){
        this.idUsuario = idUsuario;
        productosRepository = new ProductosRepository(context, idUsuario);
        pedidosRepository = new PedidosRepository(context);

    }

    public void getListaEscalasBonificaciones(){
        escalaBonificacionRepository = new EscalaBonificacionRepository(this.context, idUsuario);
        if (escalaBonificacionRepository.isFueraSincronizacion()){
            escalaBonificacionRepository.sincronizar(idUsuario).subscribe(new Completable.CompletableSubscriber() {
                @Override
                public void onCompleted() {
                    listaEscalasBonificaciones.setValue(escalaBonificacionRepository.getEscalas());
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onSubscribe(Subscription d) {

                }
            });
        }else  {
            listaEscalasBonificaciones.setValue(escalaBonificacionRepository.getEscalas());
        }
    }

    public void getListaProductos(){
        productosRepository = new ProductosRepository(this.context, idUsuario);
        if (this.productosRepository.isFueraSincronizacion()){
            productosRepository.sincronizar(idUsuario).subscribe(new Completable.CompletableSubscriber() {
                @Override
                public void onCompleted() {
                    listaProductos.setValue(productosRepository.getProductos());
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onSubscribe(Subscription d) {

                }
            });
        }else  {
            listaProductos.setValue(productosRepository.getProductos());
        }
    }


    public Completable getPedido(final String idCliente, final String nombreCliente, final String tipoPedido ){

        return Completable.create(new Completable.CompletableOnSubscribe() {
            @Override
            public void call(Completable.CompletableSubscriber completableSubscriber) {
                // Se consulta si el cliente tiene un pedido en progeso (Pendiente)
                // de tenerlo se sigue con el mismo, caso contrario se crea un pedido
                Pedido pedidoExistente =  pedidosRepository.getPedidoPendiente(idCliente, idUsuario);
                if (pedidoExistente == null) {
                    pedidoExistente = new Pedido();
                    pedidoExistente.setTipoPedido(tipoPedido);
                    pedidoExistente.setIdPedido(0);
                    pedidoExistente.setFechaPedido(new Date());
                    pedidoExistente.setIdCliente(idCliente);
                    pedidoExistente.setNombreCliente(nombreCliente);
                    pedidoExistente.setIdAsesor(idUsuario);
                    pedidoExistente.setPrecioTotal(0.00);
                    pedidoExistente.setPorcentajeDescuento(0.00);
                    pedidoExistente.setDescuento(0.00);
                    pedidoExistente.setPrecioFinal(0.00);
                    pedidoExistente.setEstado(PED_PENDIENTE);
                    pedidoExistente.setPendienteSync(true);
                    pedidosRepository.addPedido(pedidoExistente);
                    pedidoExistente =  pedidosRepository.getPedidoPendiente(idCliente, idUsuario);
                }
                pedido.setValue(pedidoExistente);
                completableSubscriber.onCompleted();
            }
        });
    }

    public void addPedido (String idCliente){
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setIdAsesor(this.idUsuario);
        nuevoPedido.setIdCliente(idCliente);
        nuevoPedido.setFechaPedido(new Date());
        this.pedidosRepository.addPedido(nuevoPedido);
    }


    public void addDetallePedido(PedidoDetalle pedidoDetalle){
        int idLocal = pedido.getValue().getIdLocalPedido();
        this.pedidosRepository.addPediodDetalle(this.pedido.getValue(),pedidoDetalle );
        pedido.setValue(this.pedidosRepository.getPedidoByIdLocal(idLocal));
    }

}
