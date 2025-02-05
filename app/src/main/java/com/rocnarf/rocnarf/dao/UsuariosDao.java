package com.rocnarf.rocnarf.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.rocnarf.rocnarf.models.Usuario;

import java.util.List;


@Dao
public interface UsuariosDao {

    @Query("select * from usuarios")
    Usuario get();

    @Query("select * from usuarios")
    List<Usuario> getAllUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Usuario usuario);

    @Query("delete from usuarios")
    void deleteAll();
}
