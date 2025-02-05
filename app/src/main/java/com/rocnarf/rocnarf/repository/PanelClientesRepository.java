package com.rocnarf.rocnarf.repository;

import android.content.Context;

import com.rocnarf.rocnarf.api.ApiClient;

import java.util.List;

import rx.Completable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.rocnarf.rocnarf.dao.RocnarfDatabase;
import com.rocnarf.rocnarf.models.PanelClientes;
import com.rocnarf.rocnarf.dao.PanelClientesDao;

public class PanelClientesRepository {

    PanelClientesDao panelClientesDao;

    public PanelClientesRepository(Context context ) {
        panelClientesDao = RocnarfDatabase.getDatabase(context).PanelClientesDao();
    }

    public void addPanelCliente(PanelClientes panel){
        this.panelClientesDao.add(panel);
    }

    public  List<PanelClientes> getPanelClientes(String idUsuario ){
        return this.panelClientesDao.getPanelClientes(idUsuario);
    }

    public  List<PanelClientes> getPanelClientesXOrigen(String idUsuario, String tipoObserv ){
        return this.panelClientesDao.getPanelClientesXOrigen(idUsuario, tipoObserv);
    }

    public  List<PanelClientes> getPanelClientesXTipo(String idUsuario  ){
        return this.panelClientesDao.getPanelClientesXTipo(idUsuario);
    }

    public void delete(PanelClientes panelClientes) {
        this.panelClientesDao.delete(panelClientes);
    }
    public void deletePanelCliente(String idCliente) {
        this.panelClientesDao.deletePanelClientes(idCliente);
    }
}
