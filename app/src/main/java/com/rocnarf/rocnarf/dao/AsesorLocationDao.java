package com.rocnarf.rocnarf.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.rocnarf.rocnarf.models.AsesorLocation;

import java.util.List;

@Dao
public interface AsesorLocationDao {

    @Query("select * from asesores_location where idAsesor = :idAsesor")
    List<AsesorLocation> getById(String idAsesor);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(AsesorLocation asesorLocation);


    @Delete()
    void delete(AsesorLocation asesorLocation);

    @Query("delete from asesores_location where idAsesor = :idAsesor")
    void deleteByIdAsesor(String idAsesor);
}
