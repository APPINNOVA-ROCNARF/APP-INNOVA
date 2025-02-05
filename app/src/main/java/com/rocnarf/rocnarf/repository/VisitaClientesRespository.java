package com.rocnarf.rocnarf.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.VisitaClientesService;
import com.rocnarf.rocnarf.dao.RocnarfDatabase;
import com.rocnarf.rocnarf.dao.VisitaClientesDao;
import com.rocnarf.rocnarf.models.Promocionado;
import com.rocnarf.rocnarf.models.VisitaClientes;
import com.rocnarf.rocnarf.models.VisitaClientesResponse;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Completable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Singleton
public class VisitaClientesRespository {



    private VisitaClientesDao visitaClientesDao ;
    private VisitaClientesService visitaClientesService;
    private MutableLiveData<List<VisitaClientes>> listaVisitas = new MutableLiveData<>();
    private Date desde;
    Executor executor;

    public VisitaClientesRespository(Context context){
        this.visitaClientesDao = RocnarfDatabase.getDatabase(context).VisitaClientesDao();
        executor = Executors.newSingleThreadExecutor();
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add( Calendar.DAY_OF_WEEK, -(cal.get(Calendar.DAY_OF_WEEK)-1));
        desde = cal.getTime();

    }

    public VisitaClientes getById(int idLocal){
        return this.visitaClientesDao.getById(idLocal);
    }

    public VisitaClientes getByIdCliente(String idCliente){
        return this.visitaClientesDao.getByIdCliente(idCliente);
    }

    public void SincronizarVisitasClientes(final String idAsesor, boolean forzarSincronizacion){
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK, -1);
        Date fechaLimiteSincronizacion = cal.getTime();


        Completable.CompletableSubscriber resultado = new Completable.CompletableSubscriber() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onSubscribe(Subscription d) {
            }
        };
        List<VisitaClientes> pendientes =  this.visitaClientesDao.getPendientesSincronizar(idAsesor);
        for (VisitaClientes visita: pendientes) {
            if (visita.getIdVisitaCliente() == 0) {
                add(visita).subscribe(resultado );
            }else {
                update(visita).subscribe(resultado);
            }
        }

        // actualiza la base local solo si no hay registros pendientes de sincronizar
        if (pendientes.size() == 0) {
            int registroFueraCache =  this.visitaClientesDao.getFueraRangoSincronizacion(idAsesor, fechaLimiteSincronizacion);
            int cantidadVisitas = this.visitaClientesDao.getCantidadVisitas(idAsesor);
            if (cantidadVisitas == 0 || registroFueraCache > 0 || forzarSincronizacion){
                VisitaClientesService service = ApiClient.getClient().create(VisitaClientesService.class);
                Call<VisitaClientesResponse> call  = service.Get(idAsesor, true, "FechaVisitaPlanificada");
                call.enqueue(new Callback<VisitaClientesResponse>() {
                    @Override
                    public void onResponse(Call<VisitaClientesResponse> call, Response<VisitaClientesResponse> response) {
                        if (response.isSuccessful()) {
                            VisitaClientesResponse visitaClientesResponse = response.body();
                            visitaClientesDao.deleteAll(idAsesor);
                            visitaClientesDao.addVisitas(visitaClientesResponse.items);
                            visitaClientesDao.updateSincronizacion(idAsesor, new Date());
                            //listaVisitas.setValue(visitaClientesResponse.items);
                        }
                    }

                    @Override
                    public void onFailure(Call<VisitaClientesResponse> call, Throwable t) {
                        //listaVisitas.setValue(visitaClientesDao.getVisitasXAsesor());
                    }
                });
            }
        }
    }

    public LiveData<List<VisitaClientes>> getVisitasXAsesor(final String idAsesor) {

        listaVisitas.setValue(visitaClientesDao.getVisitasXAsesor(idAsesor, desde ));
        return listaVisitas;
    }




    public Completable add(final VisitaClientes visitaClientes){
        return Completable.create(new Completable.CompletableOnSubscribe() {
            @Override
            public void call(final Completable.CompletableSubscriber completableSubscriber) {
                visitaClientes.setIdVisitaCliente(0);
                visitaClientesService = ApiClient.getClient().create(VisitaClientesService.class);
                visitaClientesService.Post(visitaClientes)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new  Subscriber<VisitaClientes>() {
                            @Override
                            public void onCompleted() {
                                completableSubscriber.onCompleted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                executor.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        visitaClientes.setPendienteSync(true);
                                        visitaClientesDao.add(visitaClientes);
                                    }
                                });
                                completableSubscriber.onError(e);
                            }

                            @Override
                            public void onNext(VisitaClientes visitaClientesResponse) {
                                // Si esta pendiente de sincronizar es porque fue agregado a la base local, pero no al servidor remoto
                                // Ahora se esta modificando y la suncronizacion debe agregarlo
                                if (visitaClientes.isPendienteSync()) {
                                    visitaClientes.setIdVisitaCliente(visitaClientesResponse.getIdVisitaCliente());
                                    visitaClientes.setPendienteSync(false);
                                    visitaClientesDao.update(visitaClientes);
                                }else {
                                    visitaClientesDao.add(visitaClientesResponse);
                                }
                            }
                        });
            }
        });
    }



    public Completable update(final VisitaClientes visitaClientes){
        return Completable.create(new Completable.CompletableOnSubscribe() {
            @Override
            public void call(final Completable.CompletableSubscriber completableSubscriber) {
                visitaClientesService = ApiClient.getClient().create(VisitaClientesService.class);
                visitaClientesService.Put(visitaClientes.getIdVisitaCliente(), visitaClientes)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<VisitaClientes>() {
                            @Override
                            public void onCompleted() {
                                completableSubscriber.onCompleted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                visitaClientes.setPendienteSync(true);
                                visitaClientesDao.update(visitaClientes);
                                completableSubscriber.onError(e);
                            }

                            @Override
                            public void onNext(VisitaClientes visitaClientesResponse) {
                                visitaClientes.setPendienteSync(false);
                                visitaClientesDao.update(visitaClientes);
                            }
                        });
            }
        });
    }




    public void delete (VisitaClientes visitaClientes){
        VisitaClientesService service = ApiClient.getClient().create(VisitaClientesService.class);
        Call<Void> call  = service.Delete(visitaClientes.getIdVisitaCliente());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                //listaVisitas.setValue(visitaClientesDao.getVisitasXAsesor());
            }
        });
        this.visitaClientesDao.delete(visitaClientes.getIdLocal());
    }

    public Completable addPromocionado(final List<Promocionado> promocionado){
        return Completable.create(new Completable.CompletableOnSubscribe() {
            @Override
            public void call(final Completable.CompletableSubscriber completableSubscriber) {

                visitaClientesService = ApiClient.getClient().create(VisitaClientesService.class);
                visitaClientesService.PostPromocionado(promocionado)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new  Subscriber<Promocionado>() {
                            @Override
                            public void onCompleted() {
                                completableSubscriber.onCompleted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                executor.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                    }
                                });
                                completableSubscriber.onError(e);
                            }

                            @Override
                            public void onNext(Promocionado promocionado) {
                                // Si esta pendiente de sincronizar es porque fue agregado a la base local, pero no al servidor remoto
                                // Ahora se esta modificando y la suncronizacion debe agregarlo

                            }


                        });
            }
        });
    }

}
