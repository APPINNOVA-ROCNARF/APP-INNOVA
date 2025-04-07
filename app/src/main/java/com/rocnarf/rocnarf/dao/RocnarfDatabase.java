package com.rocnarf.rocnarf.dao;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;

import androidx.annotation.NonNull;
import android.util.Log;

import com.rocnarf.rocnarf.models.AsesorLocation;
import com.rocnarf.rocnarf.models.Clientes;
import com.rocnarf.rocnarf.models.EscalaBonificacion;
import com.rocnarf.rocnarf.models.PanelClientes;
import com.rocnarf.rocnarf.models.Pedido;
import com.rocnarf.rocnarf.models.PedidoDetalle;
import com.rocnarf.rocnarf.models.PrecioEspecialCliente;
import com.rocnarf.rocnarf.models.Producto;
import com.rocnarf.rocnarf.models.Sincronizacion;
import com.rocnarf.rocnarf.models.Usuario;
import com.rocnarf.rocnarf.models.VisitaClientes;

@Database(entities = {VisitaClientes.class, Usuario.class, Clientes.class, Sincronizacion.class, Producto.class,
        Pedido.class, PedidoDetalle.class, EscalaBonificacion.class, AsesorLocation.class , PanelClientes.class, PrecioEspecialCliente.class}, version =3 )
@TypeConverters({TimestampConverter.class})
public abstract class RocnarfDatabase extends RoomDatabase{
    private static RocnarfDatabase INSTANCE;
    private static final String DB_NAME = "RocnarfDatabase.db";

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE clientes ADD COLUMN revisita INTEGER DEFAULT 0");
        }
    };

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `precio_especial_cliente` (" +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`codigoCliente` TEXT, " +
                            "`codigoProducto` TEXT, " +
                            "`fechaDesde` TEXT, " +
                            "`fechaHasta` TEXT, " +
                            "`precioPVF` REAL, " +
                            "`precioPVP` REAL, " +
                            "`precioDesc` REAL, " +
                            "`cantidad` INTEGER, " +
                            "`tipo` TEXT)"
            );
        }
    };

    public static RocnarfDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VisitaClientes.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), RocnarfDatabase.class, DB_NAME)
                            .allowMainThreadQueries() // SHOULD NOT BE USED IN PRODUCTION !!!
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);

                                    Log.d("RocnarfDatabase", "populating with data...");
                                }
                            })
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    public  abstract  ClientesDao ClientesDao();
    public abstract VisitaClientesDao VisitaClientesDao();

    public abstract UsuariosDao UsuariosDao();
    public abstract SincronizacionDao SincronizacionDao();
    public abstract ProductosDao ProductosDao();
    public abstract PedidosDao PedidosDAo();
    public abstract EscalaBonificacionDao EscalaBonificacionDao();
    public abstract AsesorLocationDao AsesorLocationDao();
    public abstract PanelClientesDao PanelClientesDao();
    public abstract PrecioEspecialClienteDao PrecioEspecialClienteDao();
}
