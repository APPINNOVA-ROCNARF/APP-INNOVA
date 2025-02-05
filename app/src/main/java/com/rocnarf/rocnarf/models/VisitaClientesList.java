package com.rocnarf.rocnarf.models;

import android.os.Parcelable;

public abstract class VisitaClientesList implements Parcelable {
    public static final int TYPE_FECHA = 0;
    public static final int TYPE_PLANIFICACION = 1;

    abstract public int getType();
}
