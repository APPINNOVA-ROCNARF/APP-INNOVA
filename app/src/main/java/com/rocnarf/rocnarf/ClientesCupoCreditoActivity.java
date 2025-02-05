package com.rocnarf.rocnarf;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.models.ClientesCupoCredito;
import com.rocnarf.rocnarf.viewmodel.ClientesCupoCreditoViewModel;

import java.math.BigDecimal;

public class ClientesCupoCreditoActivity extends AppCompatActivity {

    private ClientesCupoCreditoViewModel clientesCupoCreditoViewModel;
    private String idCliente, idUsuario, idFactura;
    private TextView codigo, cupo, facturas, protestos, chequesAFechas, cupoDisponible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes_cupo_credito);

        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        idCliente =  i.getStringExtra(Common.ARG_IDCLIENTE);

        codigo = (TextView)findViewById(R.id.tv_codigo_activity_cliente_cupo_credito);

        cupo = (TextView)findViewById(R.id.tv_cupo_activity_cliente_cupo_credito);
        facturas = (TextView)findViewById(R.id.tv_facturas_activity_cliente_cupo_credito);
        protestos = (TextView)findViewById(R.id.tv_protestos_activity_cliente_cupo_credito);
        chequesAFechas = (TextView)findViewById(R.id.tv_cheque_activity_cliente_cupo_credito);
        cupoDisponible = (TextView)findViewById(R.id.tv_disponible_activity_cliente_cupo_credito);


        clientesCupoCreditoViewModel = ViewModelProviders.of(this).get(ClientesCupoCreditoViewModel.class);
        clientesCupoCreditoViewModel.setIdUsuario(idUsuario);
        clientesCupoCreditoViewModel.getCupoCredito(idCliente).observe(this, new Observer<ClientesCupoCredito>() {
            @Override
            public void onChanged(@Nullable ClientesCupoCredito clientesCupoCredito) {
                codigo.setText(clientesCupoCredito.getIdCliente());
                cupo.setText(clientesCupoCredito.getCupoCredito().toString());
                facturas.setText( clientesCupoCredito.getFacturas().toString());
                protestos.setText( clientesCupoCredito.getProtestos().toString());
                chequesAFechas.setText(clientesCupoCredito.getChequesAFechas().toString());
                BigDecimal disponible = clientesCupoCredito.getCupoCredito().subtract(clientesCupoCredito.getFacturas());
                disponible = disponible.subtract(clientesCupoCredito.getProtestos());
                disponible = disponible.subtract(clientesCupoCredito.getChequesAFechas());
                disponible.setScale(2);
                cupoDisponible .setText(disponible.toString());
            }
        });

    }

}
