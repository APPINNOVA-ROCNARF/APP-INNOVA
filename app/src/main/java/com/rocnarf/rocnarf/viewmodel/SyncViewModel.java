package com.rocnarf.rocnarf.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import androidx.annotation.NonNull;
import android.widget.ProgressBar;

import com.rocnarf.rocnarf.repository.ClientesRepository;
import com.rocnarf.rocnarf.repository.EscalaBonificacionRepository;
import com.rocnarf.rocnarf.repository.ProductosRepository;
import com.rocnarf.rocnarf.repository.VisitaClientesRespository;

import rx.Completable;
import rx.Subscription;

public class SyncViewModel extends AndroidViewModel {

    private VisitaClientesRespository visitaClientesRespository;
    private ClientesRepository clientesRepository;
    private ProductosRepository productosRepository;
    private EscalaBonificacionRepository escalaBonificacionRepository;
    public MutableLiveData<String> estadoCliente = new MutableLiveData<>();
    public MutableLiveData<String> estadoProductos = new MutableLiveData<>();
    private Context context;
    private ProgressBar pgsBar;

    public SyncViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        visitaClientesRespository = new VisitaClientesRespository(application.getApplicationContext());

    }

    public LiveData<String> getEstadoCliente(){
        return estadoCliente;
    }

    public void SincronizarClientes(String idUsuario, String seccion, String rolUsuario){
        clientesRepository = new ClientesRepository(context, idUsuario);
         clientesRepository.sincronizar(idUsuario, seccion, rolUsuario).subscribe(new Completable.CompletableSubscriber() {
             @Override
             public void onCompleted() {
                 estadoCliente.postValue("Clientes Sincronizados existosamente");

             }

             @Override
             public void onError(Throwable e) {
                 estadoCliente.postValue("Error: " + e.getMessage() );
             }

             @Override
             public void onSubscribe(Subscription d) {

             }
         });
    }

    public void SincronizarVisitas(String idUsuario){
        visitaClientesRespository.SincronizarVisitasClientes(idUsuario, true);
        estadoCliente.postValue("Clientes Visitas existosamente");
    }


    public void  SincronizarProductos(String idUsuario){
        productosRepository = new ProductosRepository(this.context, idUsuario);
        productosRepository.sincronizar(idUsuario).subscribe(new Completable.CompletableSubscriber() {
            @Override
            public void onCompleted() {
                estadoProductos.postValue("Productos Sincronizados existosamente");
            }

            @Override
            public void onError(Throwable e) {
                estadoProductos.postValue("Error: " + e.getMessage() );
            }

            @Override
            public void onSubscribe(Subscription d) {

            }
        });

    }

    public void  SincronizarEscalasBonificacion(String idUsuario){
        escalaBonificacionRepository = new EscalaBonificacionRepository(this.context, idUsuario);
        escalaBonificacionRepository.sincronizar(idUsuario).subscribe(new Completable.CompletableSubscriber() {
            @Override
            public void onCompleted() {
                estadoProductos.postValue("Escalas Sincronizadas existosamente");
            }

            @Override
            public void onError(Throwable e) {
                estadoProductos.postValue("Error: " + e.getMessage() );
            }

            @Override
            public void onSubscribe(Subscription d) {

            }
        });

    }


    public void SincronizarPedidos(String idUsuario){

    }

}
