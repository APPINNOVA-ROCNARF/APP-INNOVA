package com.rocnarf.rocnarf.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.rocnarf.rocnarf.models.EscalaBonificacion;


import java.util.List;

@Dao
public interface EscalaBonificacionDao {


    @Query("select * from escala_bonificacion")
    List<EscalaBonificacion> get();

    @Query("delete from escala_bonificacion")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(List<EscalaBonificacion> escalasBonificacion);

}
