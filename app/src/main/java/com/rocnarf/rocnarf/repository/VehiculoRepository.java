package com.rocnarf.rocnarf.repository;

import android.util.Log;
import com.rocnarf.rocnarf.models.Vehiculo;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.VehiculoService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehiculoRepository {

    private VehiculoService vehiculoService;

    public VehiculoRepository() {
        vehiculoService = ApiClient.getClient().create(VehiculoService.class);
    }

    /**
     * Obtiene la lista de vehículos de un usuario
     */
    public void obtenerVehiculosPorUsuario(String idUsuario, final VehiculoCallback callback) {
        vehiculoService.obtenerVehiculosPorUsuario(idUsuario).enqueue(new Callback<List<Vehiculo>>() {
            @Override
            public void onResponse(Call<List<Vehiculo>> call, Response<List<Vehiculo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("No tiene vehiculos registrados.");
                    Log.e("VehiculoRepository", "Error al obtener vehículos: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Vehiculo>> call, Throwable t) {
                callback.onError("Error de conexión: " + t.getMessage());
                Log.e("VehiculoRepository", "Error de red: ", t);
            }
        });
    }

    /**
     * Registra un nuevo vehículo para un usuario
     */
    public void registrarVehiculo(String idUsuario, String placa, final RegistroCallback callback) {
        Vehiculo nuevoVehiculo = new Vehiculo(idUsuario, placa); // Crear objeto Vehiculo con usuario y placa

        vehiculoService.insertarVehiculo(idUsuario, nuevoVehiculo).enqueue(new Callback<Vehiculo>() {
            @Override
            public void onResponse(Call<Vehiculo> call, Response<Vehiculo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess();
                    Log.i("VehiculoRepository", "Vehículo registrado correctamente.");
                } else {
                    // 🔹 Manejo de errores basado en códigos HTTP
                    switch (response.code()) {
                        case 409: // HTTP 409 Conflict (Placa duplicada)
                            callback.onError("Error: La placa ya está registrada.");
                            break;
                        case 400: // HTTP 400 Bad Request (Clave foránea no válida)
                            callback.onError("Error: El ID del asesor no es válido.");
                            break;
                        default: // Otro error no identificado
                            callback.onError("Error desconocido: " + response.message());
                            break;
                    }
                    Log.e("VehiculoRepository", "Error al registrar: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Vehiculo> call, Throwable t) {
                callback.onError("Error de conexión al registrar: " + t.getMessage());
                Log.e("VehiculoRepository", "Error de red al registrar: ", t);
            }
        });
    }


    // Interfaz para manejar la respuesta
    public interface VehiculoCallback {
        void onSuccess(List<Vehiculo> vehiculos);
        void onError(String mensaje);
    }

    // Interfaz para manejar la respuesta del registro de vehículo
    public interface RegistroCallback {
        void onSuccess();
        void onError(String mensaje);
    }
}
