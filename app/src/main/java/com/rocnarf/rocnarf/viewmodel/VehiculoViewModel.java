package com.rocnarf.rocnarf.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rocnarf.rocnarf.models.Vehiculo;
import com.rocnarf.rocnarf.repository.VehiculoRepository;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VehiculoViewModel extends ViewModel {
    private MutableLiveData<List<Vehiculo>> listaVehiculos = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> registroExitoso = new MutableLiveData<>();
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private VehiculoRepository vehiculoRepository;
    public VehiculoViewModel(){
        vehiculoRepository = new VehiculoRepository();
    }
    public LiveData<List<Vehiculo>> getVehiculos() {
        return listaVehiculos;
    }
    public LiveData<String> getErrorMessage(){
        return errorMessage;
    }
    public LiveData<Boolean> getRegistroExitoso(){
        return registroExitoso;
    }

    public void cargarVehiculos(String idUsuario){
        executorService.execute(() -> {
            vehiculoRepository.obtenerVehiculosPorUsuario(idUsuario, new VehiculoRepository.VehiculoCallback() {
                @Override
                public void onSuccess(List<Vehiculo> vehiculos) {
                    if(vehiculos.isEmpty()){
                        listaVehiculos.postValue(Collections.singletonList(new Vehiculo(idUsuario,"No tiene placas registradas")));
                    }else{
                         listaVehiculos.postValue(vehiculos);
                    }
                }

                @Override
                public void onError(String mensaje) {
                    errorMessage.postValue(mensaje);
                    listaVehiculos.postValue(Collections.singletonList(new Vehiculo(idUsuario,"No tiene placas registradas")));
                }
            });
        });
    }

    public void registrarVehiculo(String idUsuario, String placa) {
        executorService.execute(() -> {
            vehiculoRepository.registrarVehiculo(idUsuario, placa, new VehiculoRepository.RegistroCallback() {
                @Override
                public void onSuccess() {
                    registroExitoso.postValue(true);
                    cargarVehiculos(idUsuario); // Refrescar la lista después de registrar
                }

                @Override
                public void onError(String mensaje) {
                    errorMessage.postValue(mensaje);
                }
            });
        });
    }


}
