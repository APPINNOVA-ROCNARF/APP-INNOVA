package com.rocnarf.rocnarf.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;

import com.rocnarf.rocnarf.models.VisitaClientes;
import com.rocnarf.rocnarf.repository.VisitaClientesRespository;

import rx.Completable;
import rx.Subscription;

public class PlanificacionCrearViewModel extends AndroidViewModel {

    private VisitaClientesRespository visitaClientesRespository;
    public MutableLiveData<VisitaClientes> visitaClientes ;
    public MutableLiveData<VisitaClientes> visitaAnterior ;

    public PlanificacionCrearViewModel(@NonNull Application application) {
        super(application);
        visitaClientesRespository = new VisitaClientesRespository(application.getApplicationContext());

        visitaAnterior = new MutableLiveData<>();
    }

    public LiveData<VisitaClientes> get() {
        visitaClientes = new MutableLiveData<VisitaClientes>();
        return visitaClientes;
    }

    public void getVisitaAnterior(int idLocal) {
        visitaAnterior.setValue(this.visitaClientesRespository.getById(idLocal));
    }

    public VisitaClientes getByIdCliente(String idCliente){
        return visitaClientesRespository.getByIdCliente(idCliente);
    }

    public void add(final VisitaClientes visitaClientesAgregar){

        this.visitaClientesRespository.add(visitaClientesAgregar).subscribe(new Completable.CompletableSubscriber() {
            @Override
            public void onCompleted() {
                visitaClientes.setValue(visitaClientesRespository.getById(visitaClientesAgregar.getIdLocal()));
            }

            @Override
            public void onError(Throwable e) {
                visitaClientes.setValue(visitaClientesRespository.getById(visitaClientesAgregar.getIdLocal()));
            }

            @Override
            public void onSubscribe(Subscription d) {

            }
        });


    }

}
