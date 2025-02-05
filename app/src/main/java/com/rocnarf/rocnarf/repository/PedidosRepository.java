package com.rocnarf.rocnarf.repository;

import android.content.Context;

import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PedidoService;
import com.rocnarf.rocnarf.dao.PedidosDao;
import com.rocnarf.rocnarf.dao.RocnarfDatabase;
import com.rocnarf.rocnarf.models.Pedido;
import com.rocnarf.rocnarf.models.PedidoDetalle;
import com.rocnarf.rocnarf.models.PedidoResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Completable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.rocnarf.rocnarf.Utils.Common.PED_PENDIENTE;
import static com.rocnarf.rocnarf.Utils.Common.PED_SINCRONIZADO;


public class PedidosRepository {
    PedidosDao pedidosDao;



    public PedidosRepository(Context context){
        this.pedidosDao= RocnarfDatabase.getDatabase(context).PedidosDAo();
    }

    public List<Pedido> getPedidos(String idAsesor, String estado ){
        return this.pedidosDao.getPedidos(idAsesor, estado);
    }

    public Pedido getPedidoPendiente (String idCliente, String idAsesor){
        return this.pedidosDao.getPedidoPendiente(idCliente, idAsesor, PED_PENDIENTE);
    }

    public void addPedido(Pedido pedido){
        this.pedidosDao.add(pedido);
    }

    public void updatePedido(Pedido pedido){
        this.pedidosDao.update(pedido);
    }

    public void updatePedidoDetalle(PedidoDetalle pedidoDetalle){
        this.pedidosDao.updateDetalle(pedidoDetalle);
    }

    public void  deletePedido(Pedido pedido){
        this.pedidosDao.deleteTodosDetallesDelPedido(pedido.getIdLocalPedido());
        this.pedidosDao.deletePedido(pedido);
    }

    public Pedido getPedidoByIdLocal(int idLocalPedido){
        return this.pedidosDao.getPedidoByIdLocal(idLocalPedido);
    }



    //detalles
    public List<PedidoDetalle> getDetallesPedido(int idLocalPedido){
        return this.pedidosDao.getDetallesPedido(idLocalPedido);
    }

    public  void addPediodDetalle(Pedido pedido, PedidoDetalle pedidoDetalle){
        pedido.setPrecioTotal(pedido.getPrecioTotal() + pedidoDetalle.getPrecioTotal());
        if (pedidoDetalle.getBono() != 0) {
            pedido.setPorcentajeDescuento(0.00);
            pedido.setDescuento(0.00);
            pedido.setConBonos(true);
        }
        pedido.setDescuento(pedido.getPrecioTotal() * (pedido.getPorcentajeDescuento()/100));
        pedido.setPrecioFinal(pedido.getPrecioTotal() - pedido.getDescuento());
        this.pedidosDao.addPedidoDetalle(pedidoDetalle);
        this.pedidosDao.update(pedido);
    }

    public List<PedidoDetalle> deletePediodDetalle(Pedido pedido, PedidoDetalle pedidoDetalle){
        pedido.setPrecioTotal(pedido.getPrecioTotal() - pedidoDetalle.getPrecioTotal());
        // si ningun producto tiene bono entones se permite dar descucent (propiedad conBono - false)
        this.pedidosDao.deletePedidoDetalle(pedidoDetalle);
        List<PedidoDetalle> detalles = this.pedidosDao.getDetallesPedido(pedido.getIdLocalPedido());
        for (PedidoDetalle detalle: detalles) {
            if (detalle.getBono() != 0) {
                pedido.setPorcentajeDescuento(0.00);
                pedido.setDescuento(0.00);
                pedido.setConBonos(true);
            }
        }
        pedido.setDescuento(pedido.getPrecioTotal()*(pedido.getPorcentajeDescuento()/100));
        pedido.setPrecioFinal(pedido.getPrecioTotal() - pedido.getDescuento());

        this.pedidosDao.update(pedido);
        return detalles;
    }


    // envio de pedido
    public Completable sync(final int idLocalPedido){
        return Completable.create(new Completable.CompletableOnSubscribe() {
            @Override
            public void call(final Completable.CompletableSubscriber completableSubscriber) {
                final Pedido pedido = pedidosDao.getPedidoByIdLocal(idLocalPedido);
                List<PedidoDetalle> detalles = pedidosDao.getDetallesPedido(pedido.getIdLocalPedido());
                for (PedidoDetalle detalle: detalles) {
                    detalle.setIdPedidoDetalle(0);
                }
                pedido.setPedidoDetalleResource(detalles);


                PedidoService pedidoService = ApiClient.getClient().create(PedidoService.class);
                pedidoService.Post(pedido)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Pedido>() {
                            @Override
                            public void onCompleted() {
                                pedido.setEstado(PED_SINCRONIZADO);
                                pedidosDao.update(pedido);
                                completableSubscriber.onCompleted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                completableSubscriber.onError(e);
                            }

                            @Override
                            public void onNext(Pedido pedido) {

                            }
                        });
            }
        });

    }


    public Completable syncDescargaPerdidos(final String idAsesor){

        return Completable.create(new Completable.CompletableOnSubscribe() {
            @Override
            public void call(final Completable.CompletableSubscriber completableSubscriber) {
                PedidoService pedidoService = ApiClient.getClient().create(PedidoService.class);
                Call<PedidoResponse> call = pedidoService.Get(idAsesor, true, "FechaPedido");
                call.enqueue(new Callback<PedidoResponse>() {
                    @Override
                    public void onResponse(Call<PedidoResponse> call, Response<PedidoResponse> response) {
                        if (response.isSuccessful()) {
                            PedidoResponse pedidoResponse = response.body();
                            pedidosDao.deletePedidosDetallesXUsuario(idAsesor);
                            pedidosDao.deletePedidosXUsuario(idAsesor);
                            pedidosDao.addPedidos(pedidoResponse.items);
                            completableSubscriber.onCompleted();
                        }
                    }

                    @Override
                    public void onFailure(Call<PedidoResponse> call, Throwable t) {
                        completableSubscriber.onError(t);
                    }
                });
            }
        });



    }

}
