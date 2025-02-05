package com.rocnarf.rocnarf.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.rocnarf.rocnarf.models.Sincronizacion;

@Dao
public interface SincronizacionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Sincronizacion sincronizacion);

    @Query("select * from sincronizacion where idUsuario = :idUsuario and entidad = :entidad")
    Sincronizacion get(String idUsuario, String entidad);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Sincronizacion sincronizacion);
}
