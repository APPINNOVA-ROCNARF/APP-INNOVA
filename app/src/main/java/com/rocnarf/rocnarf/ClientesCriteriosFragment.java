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
import android.widget.Button;
import android.widget.Spinner;

import com.rocnarf.rocnarf.Utils.Common;

public class ClientesCriteriosFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_DESTINO = "destino";
    private Context mContext;

    // estas variables serviran para llevarlas al resultado y al dar click en un resultado definir
    // si se va a actualizar la geolocalizacion o al resultado de la visita
    public static final int DEST_GEOLOCALIZACION = 1;
    public static final int DEST_PLANIFICACION = 2;
    public static final int DEST_PEDIDO = 3;
    public static final int DEST_VISITAS_IMPULSADORAS = 4;
    public static final int DEST_COBROS = 5;

    private int destino;
    private String idUsuario, seccion;

    public ClientesCriteriosFragment() {

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ClientesCriteriosFragment newInstance(int destino, String idUsuario, String seccion) {
        ClientesCriteriosFragment fragment = new ClientesCriteriosFragment();
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
        final View rootView = inflater.inflate(R.layout.content_clientes_criterios, container, false);

        TextInputEditText etSeccion = (TextInputEditText)rootView.findViewById(R.id.ti_sector_content_clientes_criterios);
        etSeccion.setText(this.seccion);
        etSeccion.setFocusable(false);
        Button btnBuscar = (Button) rootView.findViewById(R.id.bt_buscar_content_clientes_criterio);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText codigoView = (TextInputEditText)rootView.findViewById(R.id.ti_codigo_content_clientes_criterios);
                TextInputEditText localView = (TextInputEditText)rootView.findViewById(R.id.ti_local_content_clientes_criterios);
                TextInputEditText propietarioView = (TextInputEditText)rootView.findViewById(R.id.ti_propietario_content_clientes_criterios);
                TextInputEditText sectorView = (TextInputEditText)rootView.findViewById(R.id.ti_sector_content_clientes_criterios);
                TextInputEditText ciudadView = (TextInputEditText)rootView.findViewById(R.id.ti_ciudad_content_clientes_criterios);
                Spinner tipoView = (Spinner)rootView.findViewById(R.id.sp_tipo_content_clientes_criterios);

                // Se pasan los criterios a la pantalla de consulta
                Intent i = new Intent(mContext, ResultadoClientesActivity.class);

                i.putExtra("idUsuario",  idUsuario );
                i.putExtra("codigoCliente",  codigoView.getText().toString().equals("") ? null : codigoView.getText().toString() );
                i.putExtra("nombre", localView.getText().toString().equals("") ? null : localView.getText().toString() );
                i.putExtra("tipo", tipoView.getSelectedItem().toString().equals("TODOS") ? null : tipoView.getSelectedItem().toString() );
                i.putExtra("sector", sectorView.getText().toString().equals("") ? null : sectorView.getText().toString() );
                i.putExtra("representante", propietarioView.getText().toString().equals("") ? null : propietarioView.getText().toString() );
                i.putExtra("ciudad", ciudadView.getText().toString().equals("") ? null : ciudadView.getText().toString() );
                i.putExtra("destino", destino);
                startActivity(i);



            }
        });




        //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_DESTINO)));
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
