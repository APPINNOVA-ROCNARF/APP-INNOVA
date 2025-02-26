package com.rocnarf.rocnarf.api;

import com.rocnarf.rocnarf.models.Vehiculo;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface VehiculoService {

    // Obtener todos los vehículos
    @GET("api/vehiculos")
    Call<List<Vehiculo>> obtenerVehiculos();

    // Obtener un vehículo por ID
    @GET("api/vehiculos/{idVehiculo}")
    Call<Vehiculo> obtenerVehiculoPorId(@Path("idVehiculo") int idVehiculo);

    // Obtener todos los vehículos de un usuario (asesor) específico
    @GET("api/vehiculos/usuario/{idAsesor}")
    Call<List<Vehiculo>> obtenerVehiculosPorUsuario(@Path("idAsesor") String idAsesor);

    // Registrar un nuevo vehículo
    @POST("api/vehiculos/{idAsesor}")
    Call<Vehiculo> insertarVehiculo(@Path("idAsesor") String idAsesor, @Body Vehiculo vehiculo);

    // Actualizar los datos de un vehículo
    @PUT("api/vehiculos/{idVehiculo}")
    Call<Void> actualizarVehiculo(@Path("idVehiculo") int idVehiculo, @Body Vehiculo vehiculo);

    // Eliminar un vehículo por ID
    @DELETE("api/vehiculos/{idVehiculo}")
    Call<Void> eliminarVehiculo(@Path("idVehiculo") int idVehiculo);
}
