package com.rocnarf.rocnarf;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.VisitasImpulsadorasReciclerViewAdapter;
import com.rocnarf.rocnarf.models.VisitasImpulsadoras;
import com.rocnarf.rocnarf.viewmodel.VisitasImpulsadorasViewModel;

import java.util.List;

public class VisitasImpulsadorasActivity extends AppCompatActivity {
    private String idUsuario, sector;
    private RecyclerView recyclerView;
    private TextView mensajeVacio;
    private VisitasImpulsadorasReciclerViewAdapter adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitas_impulsadoras);

        context = this;
        Intent intent = getIntent();
        idUsuario= intent.getStringExtra(Common.ARG_IDUSUARIO);
        sector = intent.getStringExtra(Common.ARG_SECCIOM);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent( getApplicationContext(), VisitasImpulsadorasClientesActivity.class );
                i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                i.putExtra(Common.ARG_SECCIOM, sector);
                startActivity(i);
            }
        });


        mensajeVacio = (TextView)findViewById(R.id.tv_vacio_activity_visitas_impuladoras);
        recyclerView = (RecyclerView) findViewById(R.id.rv_lista_visitas_impuladoras);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


//        final VisitasImpulsadorasReciclerViewAdapter.VisitasImpulsadorasListaListener listener = new PedidosRecyclerViewAdapter.PedidoListaListener() {
//            @Override
//            public void PedidoListaListener(Pedido pedido) {
//                Intent i = new Intent(context, PedidoActivity.class);
//                i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
//                i.putExtra(Common.ARG_SECCIOM, sector);
//                i.putExtra(Common.ARG_IDCLIENTE, pedido.getIdCliente());
//                i.putExtra(Common.ARG_IDPEDIDO, pedido.getIdLocalPedido());
//                startActivity(i);
//
//            }
//        };

        final VisitasImpulsadorasReciclerViewAdapter.VisitasImpulsadorasListaListener listener =  new VisitasImpulsadorasReciclerViewAdapter.VisitasImpulsadorasListaListener() {
            @Override
            public void VisitasImpulsadorasListaListener(VisitasImpulsadoras visitasImpulsadoras) {
                Intent i = new Intent(context, VisitasImpulsadorasResultadoActivity.class);
                i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                i.putExtra(Common.ARG_SECCIOM, sector);
                i.putExtra(Common.ARG_IDCLIENTE, visitasImpulsadoras.getCodigoCliente());
                //i.putExtra(Common.ARG_IDPEDIDO, pedido.getIdLocalPedido());
                startActivity(i);
            }
        };

        final VisitasImpulsadorasViewModel model = ViewModelProviders.of( this ).get(VisitasImpulsadorasViewModel.class);
        //model.setQueryFilter(idUsuario, codigoCliente,nombre, tipo, sector, representante, ciudad);
        model.getVisitas();
        model.visitas.observe(this, new Observer<List<VisitasImpulsadoras>>() {
            @Override
            public void onChanged(@Nullable List<VisitasImpulsadoras> visitasImpulsadoras) {
                if (visitasImpulsadoras != null) {
                    if (visitasImpulsadoras.size() > 0) mensajeVacio.setVisibility(View.GONE);
                    adapter = new VisitasImpulsadorasReciclerViewAdapter(model.visitas.getValue(), listener);
                    recyclerView.setAdapter(adapter);

                }
            }
        });


    }
}
