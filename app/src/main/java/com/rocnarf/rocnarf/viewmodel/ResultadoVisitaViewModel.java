package com.rocnarf.rocnarf.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;
import android.util.Log;

import com.rocnarf.rocnarf.models.Promocionado;
import com.rocnarf.rocnarf.models.VisitaClientes;
import com.rocnarf.rocnarf.repository.VisitaClientesRespository;

import java.util.List;

import rx.Completable;
import rx.Subscription;

public class ResultadoVisitaViewModel extends AndroidViewModel {

    public MutableLiveData<VisitaClientes> visitaClientes ;
    private VisitaClientesRespository visitaClientesRespository;

    public ResultadoVisitaViewModel(@NonNull Application application) {
        super(application);
        visitaClientesRespository = new VisitaClientesRespository(application.getApplicationContext());
        visitaClientes = new MutableLiveData<>();
    }

    public void getByid(int idLocal){
        visitaClientes.setValue(this.visitaClientesRespository.getById(idLocal));
    }

    public void update (final  VisitaClientes visitaClientesActualizar){

        Completable.CompletableSubscriber resultado = new Completable.CompletableSubscriber() {
            @Override
            public void onCompleted() {
                visitaClientes.setValue(visitaClientesRespository.getById(visitaClientesActualizar.getIdLocal()));
            }

            @Override
            public void onError(Throwable e) {
                visitaClientes.setValue(visitaClientesRespository.getById(visitaClientesActualizar.getIdLocal()));
            }

            @Override
            public void onSubscribe(Subscription d) {

            }
        };
        if (visitaClientesActualizar.getIdVisitaCliente() == 0){
            this.visitaClientesRespository.add(visitaClientesActualizar).subscribe(resultado);
        }   else {
            this.visitaClientesRespository.update(visitaClientesActualizar).subscribe(resultado);
        }


    }

    public void delete (int idLocal){
        this.visitaClientesRespository.delete(visitaClientes.getValue());
    }

    public void addPromocionado(final List<Promocionado> promocionado){
        Log.d("myTag", "regresoo.... entra" );
        this.visitaClientesRespository.addPromocionado(promocionado);

    }

}
