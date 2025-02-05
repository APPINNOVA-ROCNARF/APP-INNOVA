package com.rocnarf.rocnarf.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import android.content.Context;
import androidx.annotation.NonNull;

import com.rocnarf.rocnarf.models.PanelClientes;
import com.rocnarf.rocnarf.repository.PanelClientesRepository;


public class PanelClientesViewModel extends AndroidViewModel {

    private Context context;
    private PanelClientesRepository panelRepository;

    public PanelClientesViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
    }

    public int addPanel (final PanelClientes panelClientes){
        int grabado;
        PanelClientes nuevoPanel = new PanelClientes();
        nuevoPanel.setIdCliente(panelClientes.getIdCliente());
        nuevoPanel.setCiudad(panelClientes.getCiudad());
        nuevoPanel.setIdUsuario(panelClientes.getIdUsuario());
        nuevoPanel.setNombreCliente(panelClientes.getNombreCliente());
        nuevoPanel.setRepresentante(panelClientes.getRepresentante());
        this.panelRepository.addPanelCliente(nuevoPanel);

        grabado = 1;
        return grabado;


    }
}
