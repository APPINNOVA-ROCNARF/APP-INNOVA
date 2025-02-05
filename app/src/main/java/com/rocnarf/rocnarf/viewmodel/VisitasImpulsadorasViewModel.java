package com.rocnarf.rocnarf.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import androidx.annotation.NonNull;

import com.rocnarf.rocnarf.models.VisitasImpulsadoras;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VisitasImpulsadorasViewModel extends AndroidViewModel {
    private Context context;
    //private PedidosRepository pedidosRepository;
    public MutableLiveData<List<VisitasImpulsadoras>> visitas =  new MutableLiveData<>();
    //public MutableLiveData<List<PedidoDetalle>> detallesPedido;
    //public MutableLiveData<String> sincronizado = new MutableLiveData<>();

    public VisitasImpulsadorasViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        //this.pedidosRepository= new PedidosRepository(context);
    }

    public void getVisitas(){
        List<VisitasImpulsadoras> lista = new ArrayList<>();
        VisitasImpulsadoras visitasImpulsadoras = new VisitasImpulsadoras();
        visitasImpulsadoras.setCodigoAsesor("RLA");
        visitasImpulsadoras.setCodigoCliente("101049");
        visitasImpulsadoras.setNombreCliente("FARMACIA GENESIS");
        visitasImpulsadoras.setDireccion("EL RECREO AV.LAURICE DE SALEM SOLAR 18");
        visitasImpulsadoras.setEstado("PEND");
        visitasImpulsadoras.setFechaInicioVisitaPlanificada(new Date());
        visitasImpulsadoras.setFechaFinVisitaPlanificada(new Date());
        lista.add(visitasImpulsadoras);
        visitasImpulsadoras = new VisitasImpulsadoras();
        visitasImpulsadoras.setCodigoAsesor("RLA");
        visitasImpulsadoras.setCodigoCliente("102008");
        visitasImpulsadoras.setNombreCliente("FARMACIA VILLACRES    ");
        visitasImpulsadoras.setDireccion("GUAYAQUIL 916 Y PICHINCHA");
        visitasImpulsadoras.setEstado("PEND");
        visitasImpulsadoras.setFechaInicioVisitaPlanificada(new Date());
        visitasImpulsadoras.setFechaFinVisitaPlanificada(new Date());
        lista.add(visitasImpulsadoras);

        visitas = new MutableLiveData<>();
        visitas.setValue(lista);
    }

}
