package com.rocnarf.rocnarf.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import com.rocnarf.rocnarf.models.PanelClientes;

@Dao
public interface PanelClientesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(PanelClientes panelcliente);

    @Query("select * from panel_clientes where idUsuario = :idUsuario")
    List<PanelClientes> getPanelClientes (String idUsuario);

    @Query("select * from panel_clientes where idUsuario = :idUsuario and origen = :tipoObserv and idCliente" + " not like('Z%')")
    List<PanelClientes> getPanelClientesXOrigen (String idUsuario, String tipoObserv);

    @Query("select * from panel_clientes where idUsuario = :idUsuario and idCliente" + " like('Z%')")
    List<PanelClientes> getPanelClientesXTipo (String idUsuario );

    @Delete()
    void delete(PanelClientes panelClientes);

    @Query("delete from panel_clientes where idCliente = :idCliente")
    void deletePanelClientes (String idCliente);

}
