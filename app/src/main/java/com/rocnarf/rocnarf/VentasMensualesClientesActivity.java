package com.rocnarf.rocnarf;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.VentasMensualesClientesReciclerViewAdapter;
import com.rocnarf.rocnarf.models.VentaMensualXCliente;
import com.rocnarf.rocnarf.viewmodel.VentasMensualesClientesViewModel;

import java.util.List;

public class VentasMensualesClientesActivity extends AppCompatActivity {
    private VentasMensualesClientesViewModel ventasMensualesClientesViewModel;
    private String idCliente, idUsuario;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Context context;
    private VentasMensualesClientesReciclerViewAdapter ventasMensualesClientesReciclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ventas_mensuales_clientes);
        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        idCliente =  i.getStringExtra(Common.ARG_IDCLIENTE);
        context = this;
        progressBar = (ProgressBar)findViewById(R.id.pr_list_activity_ventas_mensuales_clientes);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView)findViewById(R.id.rv_list_activity_ventas_mensuales_clientes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        ventasMensualesClientesViewModel = ViewModelProviders.of(this).get(VentasMensualesClientesViewModel.class);
        ventasMensualesClientesViewModel.setIdUsuario(idUsuario);
        ventasMensualesClientesViewModel.getVentasMensualesXCliente(idCliente).observe(this, new Observer<List<VentaMensualXCliente>>() {
            @Override
            public void onChanged(@Nullable List<VentaMensualXCliente> ventaMensualXClientes) {
                progressBar.setVisibility(View.GONE);
                if (ventaMensualXClientes.size() == 0) Toast.makeText(context, "Cliente no historial de ventas", Toast.LENGTH_LONG).show();
                ventasMensualesClientesReciclerViewAdapter = new VentasMensualesClientesReciclerViewAdapter(context, idUsuario, ventaMensualXClientes);
                recyclerView.setAdapter(ventasMensualesClientesReciclerViewAdapter);
            }
        });

    }
}
