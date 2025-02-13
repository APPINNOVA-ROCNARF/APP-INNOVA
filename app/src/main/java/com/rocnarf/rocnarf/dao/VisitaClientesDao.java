package com.rocnarf.rocnarf.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.rocnarf.rocnarf.models.VisitaClientes;

import java.util.Date;
import java.util.List;

@Dao
public interface VisitaClientesDao {
    @Query("select * from visita_clientes where Estado not in ('ELIMI') and CodigoAsesor = :idAsesor and  FechaVisitaPlanificada >= :desde order by FechaVisitaPlanificada asc")
    List<VisitaClientes> getVisitasXAsesor(String idAsesor, Date desde);

    @Query("select * from visita_clientes where idLocal = :idLocal")
    VisitaClientes getById(int idLocal);

    @Query("select * from visita_clientes where codigoCliente = :idCliente")
    VisitaClientes getByIdCliente(String idCliente);

    @Query("select count(*) from visita_clientes where CodigoASesor = :idAsesor and FechaSincronizacion < :fechaLimiteUltimaSincronizacion")
    int getFueraRangoSincronizacion(String idAsesor, Date fechaLimiteUltimaSincronizacion);

    @Query("select count(*) from visita_clientes where CodigoASesor = :idAsesor")
    int getCantidadVisitas(String idAsesor);

    @Query("select * from visita_clientes where pendienteSync = 1 and codigoAsesor = :idAsesor")
    List<VisitaClientes> getPendientesSincronizar(String idAsesor);

    @Query("SELECT MAX(fechaVisita) FROM visita_clientes WHERE codigoCliente = :idCliente AND CodigoAsesor = :idAsesor AND (Estado = 'PEFECT' OR Estado = 'EFECT')")
    Date getUltimaFechaVisitaPEFECT(String idCliente, String idAsesor);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(VisitaClientes visitaClientes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addVisitas(List<VisitaClientes> visitas);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update(VisitaClientes visitaClientes);

    @Query("update visita_clientes set FechaSincronizacion = :fecha where CodigoASesor = :idAsesor")
    void updateSincronizacion(String idAsesor, Date fecha);


    @Query("delete from visita_clientes where idLocal = :idLocal")
    void delete(int idLocal);


    @Query("DELETE FROM visita_clientes where codigoAsesor = :idAsesor")
    void deleteAll(String idAsesor);

}
