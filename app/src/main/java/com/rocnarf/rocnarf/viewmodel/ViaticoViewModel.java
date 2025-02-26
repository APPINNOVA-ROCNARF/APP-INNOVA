package com.rocnarf.rocnarf.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.rocnarf.rocnarf.models.Viatico;
import com.rocnarf.rocnarf.repository.ViaticoRepository;

public class ViaticoViewModel extends ViewModel {
    private final ViaticoRepository repository;
    private final MutableLiveData<Boolean> viaticoGuardado = new MutableLiveData<>();
    private final MutableLiveData<String> errorMensaje = new MutableLiveData<>();

    public ViaticoViewModel(){
        repository = new ViaticoRepository();
    }

    public void guardarViatico(Viatico viatico){
        repository.registrarViatico(viatico, new ViaticoRepository.RegistroCallback() {
            @Override
            public void onSuccess() {
                viaticoGuardado.postValue(true);
            }

            @Override
            public void onError(String mensaje) {
                errorMensaje.postValue(mensaje);
            }
        });
    }

    public void actualizarViatico(int idViatico, Viatico viatico) {
        repository.actualizarViatico(idViatico, viatico, new ViaticoRepository.RegistroCallback() {
            @Override
            public void onSuccess() {
                viaticoGuardado.postValue(true);
            }

            @Override
            public void onError(String mensaje) {
                errorMensaje.postValue(mensaje);
            }
        });
    }

    public LiveData<Boolean> getViaticoGuardado() {
        return viaticoGuardado;
    }

    public LiveData<String> getErrorMensaje() {
        return errorMensaje;
    }
}
