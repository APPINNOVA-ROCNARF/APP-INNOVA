package com.rocnarf.rocnarf.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;


import com.rocnarf.rocnarf.models.VisitaClientes;
import com.rocnarf.rocnarf.models.VisitaClientesFecha;
import com.rocnarf.rocnarf.models.VisitaClientesList;
import com.rocnarf.rocnarf.repository.AsesoresRepository;
import com.rocnarf.rocnarf.repository.VisitaClientesRespository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Completable;


public class PlanificacionViewModel extends AndroidViewModel {

    public MutableLiveData<List<VisitaClientesList>> visitasLiveData;
    private LiveData<List<VisitaClientes>> visitas;
    private List<VisitaClientesList> visitasList =  new ArrayList<>();
    private VisitaClientesRespository visitaClientesRespository;
    private AsesoresRepository asesoresRepository;
    private Calendar mCalendar = Calendar.getInstance();


    public PlanificacionViewModel(@NonNull Application application) {
        super(application);
        this.visitaClientesRespository =  new VisitaClientesRespository(application.getApplicationContext());
        this.asesoresRepository = new AsesoresRepository(application.getApplicationContext());
        visitasLiveData =  new MutableLiveData<>();
    }

    public Completable SincronizarVisitas(String idAsesor){
        this.visitaClientesRespository.SincronizarVisitasClientes(idAsesor, false);
        return Completable.complete();
    }

    public  void SincronizarUbicaciones (String idAsesor) {
        this.asesoresRepository.SincronizarUbicaciones(idAsesor);
    }


    public LiveData<List<VisitaClientesList>> getVisitasPlanificadasList(String idAsesor) {
        visitas = this.visitaClientesRespository.getVisitasXAsesor(idAsesor);
        mCalendar.set(2000,1,1);
        Date fecha = mCalendar.getTime();
        visitasList.clear();
//        Log.d("RocnarfDatabase", "------------>" + visitas.getValue() );
//        Log.d("RocnarfDatabase", "------------>" + visitas.getValue().size() );
//        Log.d("RocnarfDatabase", "------------>" + visitas.getValue().getClass().getName() );
//        Log.d("RocnarfDatabase", "------------>" + getZeroTimeDate(fecha) );
        for (VisitaClientes visita:visitas.getValue()) {
            if (getZeroTimeDate(fecha).before(getZeroTimeDate(visita.getFechaVisitaPlanificada()))){
                VisitaClientesFecha visitaClientesFecha = new VisitaClientesFecha(visita.getFechaVisitaPlanificada());
                visitasList.add((VisitaClientesList)visitaClientesFecha);
                fecha = visita.getFechaVisitaPlanificada();
            }
            visitasList.add((VisitaClientesList)visita);
        }
        //this.visitasLiveData =  new MutableLiveData<>();
        this.visitasLiveData.setValue(visitasList);

        return visitasLiveData;
    }


    private static Date getZeroTimeDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date = calendar.getTime();
        return date;
    }
}
