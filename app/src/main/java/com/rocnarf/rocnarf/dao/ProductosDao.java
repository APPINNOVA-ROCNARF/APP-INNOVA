package com.rocnarf.rocnarf.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.rocnarf.rocnarf.models.Producto;

import java.util.List;

@Dao
public interface ProductosDao {

    @Query("select * from producto ")
    List<Producto> get();

    @Query("delete from producto")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addProductos(List<Producto> productos);

}
