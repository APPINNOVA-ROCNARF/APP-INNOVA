package com.rocnarf.rocnarf.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.rocnarf.rocnarf.models.PrecioEspecialCliente;
import com.rocnarf.rocnarf.models.Producto;

import java.util.List;

@Dao
public interface PrecioEspecialClienteDao {

    @Query("select * from precio_especial_cliente")
    List<PrecioEspecialCliente> get();

    @Query("SELECT * FROM precio_especial_cliente WHERE codigoCliente = :idCliente")
    List<PrecioEspecialCliente> getTodosPreciosEspecialesPorCliente(String idCliente);

    @Query("delete from precio_especial_cliente")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addPrecioEspecial(List<PrecioEspecialCliente> precioEspecialClientes);
}
