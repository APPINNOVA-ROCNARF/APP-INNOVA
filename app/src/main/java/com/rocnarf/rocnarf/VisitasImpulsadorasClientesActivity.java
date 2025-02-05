package com.rocnarf.rocnarf;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import com.rocnarf.rocnarf.Utils.Common;

public class VisitasImpulsadorasClientesActivity extends AppCompatActivity {
    private String idUsuario, seccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitas_impulsadoras_clientes);


        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion =  i.getStringExtra(Common.ARG_SECCIOM);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ClientesCriteriosFragment detalleCliente = ClientesCriteriosFragment.newInstance(ClientesCriteriosFragment.DEST_VISITAS_IMPULSADORAS, idUsuario, seccion);
        ft.replace(R.id.fm_clientes_activity_geolocalizacion, detalleCliente);
        ft.commit();

    }

}
