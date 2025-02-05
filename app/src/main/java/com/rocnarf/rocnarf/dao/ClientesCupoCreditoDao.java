package com.rocnarf.rocnarf.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.rocnarf.rocnarf.models.ClientesCupoCredito;

import java.util.List;

@Dao
public interface ClientesCupoCreditoDao {

    @Query("select * from clientes_cupo_credito where idCliente = :idCliente")
    ClientesCupoCredito get(String idCliente);

    @Insert(onConflict = OnConflictStrategy.REPLACE )
    void addCuposCreditos(List<ClientesCupoCredito> clientesCupoCredito);
}
