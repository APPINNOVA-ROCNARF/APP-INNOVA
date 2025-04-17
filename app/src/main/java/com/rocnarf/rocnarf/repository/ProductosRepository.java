package com.rocnarf.rocnarf.repository;

import android.content.Context;
import android.util.Log;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.ClienteService;
import com.rocnarf.rocnarf.api.ProductoService;
import com.rocnarf.rocnarf.dao.PrecioEspecialClienteDao;
import com.rocnarf.rocnarf.dao.ProductosDao;
import com.rocnarf.rocnarf.dao.RocnarfDatabase;
import com.rocnarf.rocnarf.dao.SincronizacionDao;
import com.rocnarf.rocnarf.models.PrecioEspecialCliente;
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
    PrecioEspecialClienteDao precioEspecialClienteDao;
    SincronizacionDao sincronizacionDao;
    private Date fechaLimiteSincronizacion;
    private Sincronizacion sincronizacion;

    public ProductosRepository(Context context, String idUsuario ){
        this.productosDao = RocnarfDatabase.getDatabase(context).ProductosDao();
        this.sincronizacionDao = RocnarfDatabase.getDatabase(context).SincronizacionDao();
        this.precioEspecialClienteDao = RocnarfDatabase.getDatabase(context).PrecioEspecialClienteDao();
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK, -1);
        fechaLimiteSincronizacion = cal.getTime();
        sincronizacion = sincronizacionDao.get(idUsuario, Common.ENT_PRODDUCTOS);
    }



    public Completable sincronizar(final String idUsuario) {
        return Completable.create(completableSubscriber -> {
            productosDao.deleteAll();

            ProductoService service = ApiClient.getClient().create(ProductoService.class);
            Call<List<Producto>> callProductos = service.GetAll();

            // Bandera para controlar la finalización en caso de SBC
            final boolean esSBC = idUsuario.equalsIgnoreCase("SBC");
            final boolean[] preciosCargados = { !esSBC }; // true si no es SBC
            final boolean[] productosCargados = { false };

            callProductos.enqueue(new Callback<List<Producto>>() {
                @Override
                public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                    if (response.isSuccessful()) {
                        List<Producto> productos = response.body();
                        productosDao.addProductos(productos);

                        // Registrar sincronización productos
                        Sincronizacion sincronizacion = sincronizacionDao.get(idUsuario, Common.ENT_PRODDUCTOS);
                        if (sincronizacion == null) {
                            sincronizacion = new Sincronizacion();
                            sincronizacion.setIdUsuario(idUsuario);
                            sincronizacion.setEntidad(Common.ENT_PRODDUCTOS);
                            sincronizacionDao.insert(sincronizacion);
                        }
                        sincronizacion.setFechaSincronizacion(new Date());
                        sincronizacionDao.update(sincronizacion);

                        productosCargados[0] = true;
                        if (preciosCargados[0]) {
                            completableSubscriber.onCompleted(); // solo completa si ya cargaron precios (o no son SBC)
                        }

                    } else {
                        completableSubscriber.onError(new Exception("Error al sincronizar productos"));
                    }
                }

                @Override
                public void onFailure(Call<List<Producto>> call, Throwable t) {
                    Log.e("SyncProductos", "Error: " + t.getMessage());
                    completableSubscriber.onError(t);
                }
            });

            if (esSBC) {
                precioEspecialClienteDao.deleteAll();

                Call<List<PrecioEspecialCliente>> callPrecios = service.GetPrecioEspecial();
                callPrecios.enqueue(new Callback<List<PrecioEspecialCliente>>() {
                    @Override
                    public void onResponse(Call<List<PrecioEspecialCliente>> call, Response<List<PrecioEspecialCliente>> response) {
                        if (response.isSuccessful()) {
                            List<PrecioEspecialCliente> precios = response.body();
                            precioEspecialClienteDao.addPrecioEspecial(precios);
                            preciosCargados[0] = true;

                            if (productosCargados[0]) {
                                completableSubscriber.onCompleted(); // completa si ya cargaron productos
                            }
                        } else {
                            completableSubscriber.onError(new Exception("Error al sincronizar precios especiales"));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<PrecioEspecialCliente>> call, Throwable t) {
                        Log.e("SyncPreciosEspeciales", "Error: " + t.getMessage());
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

    public List<PrecioEspecialCliente> getPrecioEspecial(){
        List<PrecioEspecialCliente> lista = new ArrayList<>();
        lista = this.precioEspecialClienteDao.get();
        return  lista;
    }

    public List<PrecioEspecialCliente> getPreciosEspecialesPorCliente(String idCliente) {
        return precioEspecialClienteDao.getTodosPreciosEspecialesPorCliente(idCliente);
    }


    public Boolean isFueraSincronizacion (){
        if (sincronizacion == null) return true;
        return fechaLimiteSincronizacion.after(sincronizacion.getFechaSincronizacion());
    }

}
