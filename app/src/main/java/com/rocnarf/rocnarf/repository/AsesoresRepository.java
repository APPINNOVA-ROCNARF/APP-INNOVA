package com.rocnarf.rocnarf.repository;

import android.content.Context;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.AsesorService;
import com.rocnarf.rocnarf.api.VisitaClientesService;
import com.rocnarf.rocnarf.dao.AsesorLocationDao;
import com.rocnarf.rocnarf.dao.RocnarfDatabase;
import com.rocnarf.rocnarf.models.AsesorLocation;
import com.rocnarf.rocnarf.models.AsesorLocationBulk;
import com.rocnarf.rocnarf.models.VisitaClientes;
import com.rocnarf.rocnarf.models.VisitaClientesResponse;

import java.io.Console;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Completable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AsesoresRepository {
    AsesorService asesorService;
    AsesorLocationDao asesorLocationDao;


    public AsesoresRepository(Context context){
        this.asesorLocationDao = RocnarfDatabase.getDatabase(context).AsesorLocationDao();
    }


    public void add (final AsesorLocation asesorLocation){
        asesorService = ApiClient.getClient().create(AsesorService.class);
        asesorService.Post(asesorLocation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AsesorLocation>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof IOException) {
                            // Si es falso quiere decir que es la primera vez que se envia, si es true quiere decir que esta en la base local
                            if (!asesorLocation.isPendienteSync()) {
                                asesorLocation.setPendienteSync(true);
                                asesorLocationDao.add(asesorLocation);
                            }
                        }

                    }

                    @Override
                    public void onNext(AsesorLocation asesorLocation) {


                    }
                });
    }


    public void SincronizarUbicaciones(final String idAsesor){

        List<AsesorLocation> pendientes =  this.asesorLocationDao.getById(idAsesor);
        //for (AsesorLocation asesorLocation: pendientes) {
        AsesorLocationBulk bulk = new AsesorLocationBulk();
        bulk.setLocations(pendientes);
            asesorService = ApiClient.getClient().create(AsesorService.class);
            //asesorService.Post(asesorLocation)
            asesorService.PostBulk(bulk)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ResponseBody>() {
                        @Override
                        public void onCompleted() {
                            asesorLocationDao.deleteByIdAsesor(idAsesor);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(ResponseBody responseBody) {

                        }
                    });
//                    .subscribe(new Subscriber<AsesorLocation>()
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            if (e instanceof IOException) {
//
//                            }
//                        }
//
//                        @Override
//                        public void onNext(AsesorLocation asesorLocation) {
//                            asesorLocationDao.delete(asesorLocation);
//                        }
//                    });

        //}


    }


}

