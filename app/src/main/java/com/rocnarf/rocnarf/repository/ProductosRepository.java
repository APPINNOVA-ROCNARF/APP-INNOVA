package com.rocnarf.rocnarf.repository;

import android.content.Context;
import android.util.Log;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.ClienteService;
import com.rocnarf.rocnarf.api.ProductoService;
import com.rocnarf.rocnarf.dao.ProductosDao;
import com.rocnarf.rocnarf.dao.RocnarfDatabase;
import com.rocnarf.rocnarf.dao.SincronizacionDao;
import com.rocnarf.rocnarf.models.Producto;
import com.rocnarf.rocnarf.models.Sincronizacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Completable;

public class ProductosRepository {
    ProductosDao productosDao;
    SincronizacionDao sincronizacionDao;
    private Date fechaLimiteSincronizacion;
    private Sincronizacion sincronizacion;

    public ProductosRepository(Context context, String idUsuario ){
        this.productosDao = RocnarfDatabase.getDatabase(context).ProductosDao();
        this.sincronizacionDao = RocnarfDatabase.getDatabase(context).SincronizacionDao();
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK, -1);
        fechaLimiteSincronizacion = cal.getTime();
        sincronizacion = sincronizacionDao.get(idUsuario, Common.ENT_PRODDUCTOS);
    }



    public Completable sincronizar(final String idUsuario){

        return Completable.create(new Completable.CompletableOnSubscribe() {
            @Override
            public void call(final Completable.CompletableSubscriber completableSubscriber) {
                productosDao.deleteAll();
                ProductoService service = ApiClient.getClient().create(ProductoService.class);
                Call<List<Producto>> call  = service.GetAll();
                call.enqueue(new Callback<List<Producto>>() {
                    @Override
                    public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                        if (response.isSuccessful()){
                            List<Producto> proddutos = response.body();

                            productosDao.addProductos(proddutos);
                            Sincronizacion sincronizacion = sincronizacionDao.get( idUsuario, Common.ENT_PRODDUCTOS);
                            if (sincronizacion == null) {
                                sincronizacion = new Sincronizacion();
                                sincronizacion.setIdUsuario(idUsuario);
                                sincronizacion.setEntidad(Common.ENT_PRODDUCTOS);
                                sincronizacionDao.insert(sincronizacion);
                            }
                            sincronizacion.setFechaSincronizacion(new Date());
                            sincronizacionDao.update(sincronizacion);
                            completableSubscriber.onCompleted();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Producto>> call, Throwable t) {
                        Log.d("sincronizar Clientes", t.getMessage());
                        completableSubscriber.onError(t);
                    }
                });
            }
        });
    }

    public List<Producto> getProductos(){
        List<Producto> lista = new ArrayList<>();
        lista = this.productosDao.get();
        return lista;
    }


    public Boolean isFueraSincronizacion (){
        if (sincronizacion == null) return true;
        return fechaLimiteSincronizacion.after(sincronizacion.getFechaSincronizacion());
    }

}
