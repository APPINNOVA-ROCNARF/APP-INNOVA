package com.rocnarf.rocnarf.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.rocnarf.rocnarf.models.Clientes;


import java.util.List;

@Dao
public interface ClientesDao {

    @Query("select clientes.*, clientes.revisita, " +
            "( select visita_clientes.estado from visita_clientes where clientes.idCliente = visita_clientes.codigoCliente and visita_clientes.codigoAsesor = :idAsesor  order by fechaVisitaPlanificada desc limit 1) as estadoVisita, " +
            "( select pedido.tipoPedido from pedido where clientes.idCliente = pedido.idCliente and pedido.idAsesor = :idAsesor and pedido.tipoPedido = 'PEDID' order by fechaPedido desc limit 1) as pedido, " +
            "( select pedido.tipoPedido from pedido where clientes.idCliente = pedido.idCliente and pedido.idAsesor = :idAsesor and pedido.tipoPedido = 'COBRO' order by fechaPedido desc limit 1) as cobro" +
            " from clientes " +
            " where  " +
            "(clientes.seccion = :sector or clientes.seccion2 = :sector or clientes.seccion3 = :sector or clientes.seccion4 = :sector  or clientes.seccion5 = :sector or clientes.seccion6 = :sector or clientes.seccion7 = :sector or clientes.seccion8 = :sector or clientes.seccion9 = :sector) and " +
            "(:codigoCliente is null or clientes.idCliente like :codigoCliente ) and " +
            "(:nombre is null or clientes.nombreCliente like :nombre) and " +
            "(:origen is null or clientes.origen = :origen) and " +
            "(:representante is null or clientes.representante like :representante) and " +
            "(:ciudad is null or clientes.ciudad like :ciudad)  " )
    List<Clientes> get(String codigoCliente, String nombre, String origen, String sector, String representante, String ciudad, String idAsesor);

    @Query("select clientes.*, clientes.revisita, " +
            "( select visita_clientes.estado from visita_clientes where clientes.idCliente = visita_clientes.codigoCliente and visita_clientes.codigoAsesor = :idAsesor  order by fechaVisitaPlanificada desc limit 1) as estadoVisita, " +
            "( select pedido.tipoPedido from pedido where clientes.idCliente = pedido.idCliente and pedido.idAsesor = :idAsesor and pedido.tipoPedido = 'PEDID' order by fechaPedido desc limit 1) as pedido, " +
            "( select pedido.tipoPedido from pedido where clientes.idCliente = pedido.idCliente and pedido.idAsesor = :idAsesor and pedido.tipoPedido = 'COBRO' order by fechaPedido desc limit 1) as cobro" +
            " from clientes " +
            " where  " +
            "(clientes.seccion = :sector or clientes.seccion2 = :sector or clientes.seccion3 = :sector or clientes.seccion4 = :sector  or clientes.seccion5 = :sector or clientes.seccion6 = :sector or clientes.seccion7 = :sector or clientes.seccion8 = :sector or clientes.seccion9 = :sector) and " +
            "(clientes.longitud is null  ) and " +
            "(:codigoCliente is null or clientes.idCliente like :codigoCliente ) and " +
            "(:nombre is null or clientes.nombreCliente like :nombre) and " +
            "(:origen is null or clientes.origen = :origen) and " +
            "(:representante is null or clientes.representante like :representante) and " +
            "(:ciudad is null or clientes.ciudad like :ciudad)  " )
    List<Clientes> getSinGeo(String codigoCliente, String nombre, String origen, String sector, String representante, String ciudad, String idAsesor);

    @Query("select clientes.*, clientes.revisita," +
            "( select visita_clientes.estado from visita_clientes where clientes.idCliente = visita_clientes.codigoCliente and visita_clientes.codigoAsesor = :idAsesor  order by fechaVisitaPlanificada desc limit 1) as estadoVisita, " +
            "( select pedido.tipoPedido from pedido where clientes.idCliente = pedido.idCliente and pedido.idAsesor = :idAsesor and pedido.tipoPedido = 'PEDID' order by fechaPedido desc limit 1) as pedido, " +
            "( select pedido.tipoPedido from pedido where clientes.idCliente = pedido.idCliente and pedido.idAsesor = :idAsesor and pedido.tipoPedido = 'COBRO' order by fechaPedido desc limit 1) as cobro" +
            " from clientes " +
            " where  " +
            "(:codigoCliente is null or clientes.idCliente like :codigoCliente ) and " +
            "(:nombre is null or clientes.nombreCliente like :nombre) and " +
            "(:origen is null or clientes.origen = :origen) and " +
            "(:representante is null or clientes.representante like :representante) and " +
            "(:ciudad is null or clientes.ciudad like :ciudad)  " )
    List<Clientes> getByJefes(String codigoCliente, String nombre, String origen, String representante, String ciudad, String idAsesor);

    @Query("SELECT clientes.*, clientes.revisita FROM clientes WHERE idCliente = :idCliente")
    Clientes getById(String idCliente);


    @Query("select * from clientes where " +
            "(clientes.seccion = :seccion or clientes.seccion2 = :seccion or clientes.seccion3 = :seccion or clientes.seccion4 = :seccion  or clientes.seccion5 = :seccion or clientes.seccion6 = :seccion or clientes.seccion7 = :seccion or clientes.seccion8 = :seccion or clientes.seccion9 = :seccion) and " +
            " cumpleAnyos = 1")
    List<Clientes> getByCumple(String seccion);

    @Query("delete from clientes")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addClientes(List<Clientes> clientes);


    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Clientes cliente);
}
