package com.rocnarf.rocnarf;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.rocnarf.rocnarf.Utils.Common;

import java.util.ArrayList;
import java.util.List;

public class ClientesCriteriosEstadoFragment extends Fragment {

    private static final String ARG_DESTINO = "destino";
    private Context mContext;

    private int destino;
    private String idUsuario, seccion;
    Spinner etSeccion;

    public ClientesCriteriosEstadoFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ClientesCriteriosEstadoFragment newInstance(int destino, String idUsuario, String seccion) {
        ClientesCriteriosEstadoFragment fragment = new ClientesCriteriosEstadoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_DESTINO, destino);
        args.putString(Common.ARG_IDUSUARIO, idUsuario);
        args.putString(Common.ARG_SECCIOM, seccion);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mContext = getContext();
        final View rootView = inflater.inflate(R.layout.content_clientes_criterios_estado, container, false);

        etSeccion = (Spinner) rootView.findViewById(R.id.sp_seccion_content_clientes_criterio_estado);

        List<String> list = new ArrayList<String>();
        list.add(seccion);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etSeccion.setAdapter(dataAdapter);



        Button btnBuscar = (Button) rootView.findViewById(R.id.bt_pagos_content_clientes_criterio_estado);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText ciudadView = (TextInputEditText)rootView.findViewById(R.id.ti_ciudad_content_clientes_criterios_estado);
                Spinner tipoView = (Spinner)rootView.findViewById(R.id.sp_tipo_content_clientes_criterios_estado);

                // Se pasan los criterios a la pantalla de consulta
                Intent i = new Intent(mContext, ResultadoClientesActivity.class);

                i.putExtra("idUsuario",  idUsuario );
                i.putExtra("tipo", tipoView.getSelectedItem().toString().equals("TODOS") ? null : tipoView.getSelectedItem().toString() );
                i.putExtra("sector", etSeccion.getSelectedItem().toString().equals("") ? null : etSeccion.getSelectedItem().toString() );
                i.putExtra("ciudad", ciudadView.getText().toString().equals("") ? null : ciudadView.getText().toString() );
                i.putExtra("destino", destino);
                i.putExtra("tipoConsulta", 1);
                startActivity(i);
            }
        });


        Button btnVisitas = (Button) rootView.findViewById(R.id.bt_visitas_content_clientes_criterio_estado);
        btnVisitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText ciudadView = (TextInputEditText)rootView.findViewById(R.id.ti_ciudad_content_clientes_criterios_estado);
                Spinner tipoView = (Spinner)rootView.findViewById(R.id.sp_tipo_content_clientes_criterios_estado);

                // Se pasan los criterios a la pantalla de consulta
                Intent i = new Intent(mContext, ResultadoClientesActivity.class);

                i.putExtra("idUsuario",  idUsuario );
                i.putExtra("tipo", tipoView.getSelectedItem().toString().equals("TODOS") ? null : tipoView.getSelectedItem().toString() );
                i.putExtra("sector", etSeccion.getSelectedItem().toString().equals("") ? null : etSeccion.getSelectedItem().toString() );
                i.putExtra("ciudad", ciudadView.getText().toString().equals("") ? null : ciudadView.getText().toString() );
                i.putExtra("destino", destino);
                i.putExtra("tipoConsulta", 2);
                startActivity(i);
            }
        });


        return rootView;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.destino = getArguments().getInt(ARG_DESTINO);
            this.idUsuario = getArguments().getString(Common.ARG_IDUSUARIO);
            this.seccion = getArguments().getString(Common.ARG_SECCIOM);
        }
    }
}
