package com.rocnarf.rocnarf.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.rocnarf.rocnarf.viewmodel.PlanificacionViewModel;


import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;


@Module
public interface ViewModelModule {

    @Binds
    ViewModelProvider.Factory bindViewModelFactory(AppViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(PlanificacionViewModel.class)
    ViewModel bindVisitasClientesViewModel(PlanificacionViewModel planificacionViewModel);


}


