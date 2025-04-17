package com.rocnarf.rocnarf.repository;

import android.content.Context;

import com.rocnarf.rocnarf.dao.RocnarfDatabase;
import com.rocnarf.rocnarf.dao.UsuariosDao;
import com.rocnarf.rocnarf.models.Usuario;

public class UsuarioRepository {

    private UsuariosDao usuariosDao;

    public UsuarioRepository(Context context){
        this.usuariosDao = RocnarfDatabase.getDatabase(context).UsuariosDao();
    }

    public Usuario getUsuario(){
        return this.usuariosDao.get();
    }
}
