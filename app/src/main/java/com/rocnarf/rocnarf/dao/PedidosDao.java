package com.rocnarf.rocnarf.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.rocnarf.rocnarf.models.Pedido;
import com.rocnarf.rocnarf.models.PedidoDetalle;

import java.util.List;

@Dao
public interface PedidosDao {

    @Query("select * from pedido where idCliente = :idCliente and idAsesor = :idAsesor and estado = :estado ")
    Pedido getPedidoPendiente (String idCliente, String idAsesor, String estado);
    

    @Query("select * from pedido where idAsesor = :idAsesor and estado = :estado ")
    List<Pedido> getPedidos(String idAsesor, String estado);

    @Query("select * from pedido where idLocalPedido = :idLocalPedido")
    Pedido getPedidoByIdLocal(int idLocalPedido);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(Pedido pedido);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Pedido pedido);

    @Delete()
    void deletePedido(Pedido pedido);


    @Query("select * from pedidoDetalle where idLocalPedido = :idLocalPedido")
    List<PedidoDetalle> getDetallesPedido(int idLocalPedido);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addPedidoDetalle(PedidoDetalle pedidoDetalle);

    @Delete()
    void deletePedidoDetalle(PedidoDetalle pedidoDetalle);

    @Query("delete from pedidoDetalle where idLocalPedido = :idLocalPedido")
    void deleteTodosDetallesDelPedido(int idLocalPedido);


    @Query("delete from pedidoDetalle where idLocalPedido  in (select idLocalPedido from pedido where idAsesor = :idUsuario) ")
    void deletePedidosDetallesXUsuario(String idUsuario);

    @Query("delete from pedido where idAsesor = :idUsuario ")
    void deletePedidosXUsuario(String idUsuario);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addPedidos(List<Pedido> pedidos);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateDetalle(PedidoDetalle pedidoDetalle);
}
