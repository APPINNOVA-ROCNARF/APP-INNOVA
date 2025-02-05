package com.rocnarf.rocnarf.repository;

import android.content.Context;
import android.util.Log;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.EscalaBonificacionService;
import com.rocnarf.rocnarf.api.ProductoService;
import com.rocnarf.rocnarf.dao.EscalaBonificacionDao;
import com.rocnarf.rocnarf.dao.RocnarfDatabase;
import com.rocnarf.rocnarf.dao.SincronizacionDao;
import com.rocnarf.rocnarf.models.EscalaBonificacion;
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

public class EscalaBonificacionRepository {
    EscalaBonificacionDao escalaBonificacionDao;
    SincronizacionDao sincronizacionDao;
    private Date fechaLimiteSincronizacion;
    private Sincronizacion sincronizacion;

    public EscalaBonificacionRepository(Context context, String idUsuario ){
        this.escalaBonificacionDao = RocnarfDatabase.getDatabase(context).EscalaBonificacionDao();
        this.sincronizacionDao = RocnarfDatabase.getDatabase(context).SincronizacionDao();
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK, -1);
        fechaLimiteSincronizacion = cal.getTime();
        sincronizacion = sincronizacionDao.get(idUsuario, Common.ENT_ESCALA_BONIFICACION);
    }


    public Completable sincronizar(final String idUsuario){

        return Completable.create(new Completable.CompletableOnSubscribe() {
            @Override
            public void call(final Completable.CompletableSubscriber completableSubscriber) {
                escalaBonificacionDao.deleteAll();
                EscalaBonificacionService service = ApiClient.getClient().create(EscalaBonificacionService.class);
                Call<List<EscalaBonificacion>> call  = service.GetAll();
                call.enqueue(new Callback<List<EscalaBonificacion>>() {
                    @Override
                    public void onResponse(Call<List<EscalaBonificacion>> call, Response<List<EscalaBonificacion>> response) {
                        if (response.isSuccessful()){
                            List<EscalaBonificacion> escalas = response.body();

                            escalaBonificacionDao.add(escalas);
                            Sincronizacion sincronizacion = sincronizacionDao.get( idUsuario, Common.ENT_ESCALA_BONIFICACION);
                            if (sincronizacion == null) {
                                sincronizacion = new Sincronizacion();
                                sincronizacion.setIdUsuario(idUsuario);
                                sincronizacion.setEntidad(Common.ENT_ESCALA_BONIFICACION);
                                sincronizacionDao.insert(sincronizacion);
                            }
                            sincronizacion.setFechaSincronizacion(new Date());
                            sincronizacionDao.update(sincronizacion);
                            completableSubscriber.onCompleted();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<EscalaBonificacion>> call, Throwable t) {
                        Log.d("sincronizar Escalas", t.getMessage());
                        completableSubscriber.onError(t);
                    }
                });
            }
        });
    }

    public List<EscalaBonificacion> getEscalas(){
        List<EscalaBonificacion> lista = new ArrayList<>();
        lista = this.escalaBonificacionDao.get();
        return lista;
    }


    public Boolean isFueraSincronizacion (){
        if (sincronizacion == null) return true;
        return fechaLimiteSincronizacion.after(sincronizacion.getFechaSincronizacion());
    }



}
