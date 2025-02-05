package com.rocnarf.rocnarf.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private DataBaseHelper Database;
    private SQLiteDatabase sqLiteDatabase;

    public DataBaseHelper(Context context) {
        super(context,
                context.getExternalFilesDir(null).getAbsolutePath() + "/RocnarfDBH.db",
                null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE panelclientes (idPanelCliente INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "idCliente TEXT, NombreCliente TEXT , representante TEXT , ciudad TEXT" +
                ",sector TEXT, idUsuario TEXT)");


        db.execSQL("CREATE TABLE PedidoDetalle (IdPedidoetalle INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "IdLocalPedido TEXT, IdPedido TEXT , IdProducto TEXT , Tipo TEXT" +
                ",Cantidad TEXT, Bono TEXT, Nombre TEXT, Precio TEXT, Descuento TEXT" +
                ",PrecioTotal TEXT,PrecioFinal TEXT, IdCliente TEXT, FechaPedido TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS panelclientes");

        db.execSQL("DROP TABLE IF EXISTS PedidoDetalle");

        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE clientes ADD COLUMN revisita INTEGER DEFAULT NULL");
            db.execSQL("ALTER TABLE panelclientes ADD COLUMN revisita INTEGER DEFAULT NULL");
        }

    }
}
