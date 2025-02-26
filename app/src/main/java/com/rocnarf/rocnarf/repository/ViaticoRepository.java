package com.rocnarf.rocnarf.repository;

import android.util.Log;

import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.models.Viatico;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViaticoRepository {
    private final PlanesService service;

    public ViaticoRepository(){
        service = ApiClient.getClient().create(PlanesService.class);
    }

    public void registrarViatico(Viatico viatico, final RegistroCallback callback) {
        service.AddViatico(viatico).enqueue(new Callback<Viatico>() {
            @Override
            public void onResponse(Call<Viatico> call, Response<Viatico> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess();
                    Log.i("ViaticoRepository", "Viático registrado correctamente.");
                } else {
                    // 🔹 Manejo de errores basado en códigos HTTP
                    switch (response.code()) {
                        case 400: // HTTP 400 Bad Request (Datos inválidos)
                            callback.onError("Error: Datos inválidos en la solicitud.");
                            break;
                        case 409: // HTTP 409 Conflict (Viático duplicado)
                            callback.onError("Error: Ya existe un viático con estos datos.");
                            break;
                        default: // Otro error no identificado
                            callback.onError("Error desconocido: " + response.message());
                            break;
                    }
                    Log.e("ViaticoRepository", "Error al registrar: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Viatico> call, Throwable t) {
                callback.onError("Error de conexión al registrar viático: " + t.getMessage());
                Log.e("ViaticoRepository", "Error de red al registrar: ", t);
            }
        });
    }

    public void actualizarViatico(int idViatico, Viatico viatico, final RegistroCallback callback) {
        service.updateViatico(idViatico, viatico).enqueue(new Callback<Viatico>() {
            @Override
            public void onResponse(Call<Viatico> call, Response<Viatico> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess();
                    Log.i("ViaticoRepository", "Viático actualizado correctamente.");
                } else {
                    // 🔹 Manejo de errores basado en códigos HTTP
                    switch (response.code()) {
                        case 400: // HTTP 400 Bad Request
                            callback.onError("Error: Datos inválidos en la actualización.");
                            break;
                        case 404: // HTTP 404 Not Found (Viático no encontrado)
                            callback.onError("Error: No se encontró el viático.");
                            break;
                        default: // Otro error no identificado
                            callback.onError("Error desconocido: " + response.message());
                            break;
                    }
                    Log.e("ViaticoRepository", "Error al actualizar: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Viatico> call, Throwable t) {
                callback.onError("Error de conexión al actualizar viático: " + t.getMessage());
                Log.e("ViaticoRepository", "Error de red al actualizar: ", t);
            }
        });
    }

    public interface RegistroCallback {
        void onSuccess();
        void onError(String mensaje);
    }

}
