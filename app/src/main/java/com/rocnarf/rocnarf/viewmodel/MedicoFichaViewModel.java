package com.rocnarf.rocnarf.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import android.content.Context;
import androidx.annotation.NonNull;

import com.rocnarf.rocnarf.models.FichaMedico;
import com.rocnarf.rocnarf.repository.ClientesRepository;

import rx.Completable;

public class MedicoFichaViewModel extends AndroidViewModel {
    private Context context;
    private ClientesRepository clientesRepository;
    private  String idUsuario;
    public LiveData<FichaMedico> fichaMedicoLiveData;

    public MedicoFichaViewModel(@NonNull Application application) {
        super(application);
    }

    public void setIdUsuario(String idUsuario){
        this.idUsuario = idUsuario;
        clientesRepository = new ClientesRepository(context, idUsuario );
    }

    public LiveData<FichaMedico> getFichaMedico(String idCliente) {
        fichaMedicoLiveData = clientesRepository.getFichaMedica(idCliente);
        return fichaMedicoLiveData;
    }

    public void update(final FichaMedico fichaMedico, Completable.CompletableSubscriber resultado){
        clientesRepository.updateFichaMedico(fichaMedico).subscribe(resultado);
    }


}
