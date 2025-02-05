package com.rocnarf.rocnarf;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rocnarf.rocnarf.Utils.Common;

public class PedidoClienteActivity extends AppCompatActivity {
    private String idUsuario, seccion;
    private int destino;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_cliente);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent i = getIntent();
        destino = i.getIntExtra(Common.ARG_DESTINO_PEDIDO, 3);
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion =  i.getStringExtra(Common.ARG_SECCIOM);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ClientesCriteriosFragment detalleCliente = ClientesCriteriosFragment.newInstance(destino, idUsuario, seccion);
        ft.replace(R.id.fm_criterios_activity_pedido_cliente, detalleCliente);
        ft.commit();

    }

}
