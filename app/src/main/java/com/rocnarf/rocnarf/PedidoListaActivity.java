package com.rocnarf.rocnarf;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.PedidosRecyclerViewAdapter;
import com.rocnarf.rocnarf.models.Pedido;
import com.rocnarf.rocnarf.viewmodel.PedidoListaViewModel;

import java.util.List;
import java.util.Objects;

public class PedidoListaActivity extends AppCompatActivity {
    private String idUsuario, sector, idAsesor, rolUsuario;
    private RecyclerView recyclerView;
    private TextView mensajeVacio;
    private PedidosRecyclerViewAdapter adapter;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_lista);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        Intent intent = getIntent();
        idUsuario= intent.getStringExtra(Common.ARG_IDUSUARIO);
        sector = intent.getStringExtra(Common.ARG_SECCIOM);
        idAsesor = intent.getStringExtra(Common.ARG_IDUSUARIO);
        rolUsuario = intent.getStringExtra(Common.ARG_ROL);
        mensajeVacio = (TextView)findViewById(R.id.tv_vacio_activity_pedido_lista);
        recyclerView = (RecyclerView) findViewById(R.id.rv_lista_activity_pedido_lista);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        final PedidosRecyclerViewAdapter.PedidoListaListener listener
                = new PedidosRecyclerViewAdapter.PedidoListaListener() {
            @Override
            public void PedidoListaListener(Pedido pedido) {
                if (pedido.getTipoPedido().equals(Common.TIPO_PEDIDO_PEDIDO)) {
                    //Se realiza un pequeño cambio se llama a un activity con 2 framelayouts
                    //Intent i = new Intent(context, PedidoActivity.class);
                    //Pequeño cambio de la forma de pantalla por solicitud del cliente
                    Intent i = new Intent(context, PedidoSimpleActivity.class);
                    i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    i.putExtra(Common.ARG_SECCIOM, sector);
                    i.putExtra(Common.ARG_IDCLIENTE, pedido.getIdCliente());
                    i.putExtra(Common.ARG_IDPEDIDO, pedido.getIdLocalPedido());
                    if (Objects.equals(pedido.getTipoPrecio(), "ESP")){
                        i.putExtra(Common.ARG_USAR_PRECIO_ESPECIAL, true);
                    }
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(context, CobroActivity.class);
                    i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    i.putExtra(Common.ARG_SECCIOM, sector);
                    i.putExtra(Common.ARG_IDCLIENTE, pedido.getIdCliente());
                    i.putExtra(Common.ARG_IDPEDIDO, pedido.getIdLocalPedido());
                    i.putExtra(Common.ARG_NOMBRE_CLIENTE, pedido.getNombreCliente());
                    i.putExtra(Common.ARG_TIPO_PEDIDO, pedido.getTipoPedido());
                    startActivity(i);
                }

            }
        };


        PedidoListaViewModel model = ViewModelProviders.of( this ).get(PedidoListaViewModel.class);
        //model.setQueryFilter(idUsuario, codigoCliente,nombre, tipo, sector, representante, ciudad);
        model.getPedidos(idUsuario).observe(this, new Observer<List<Pedido>>() {
                    @Override
                    public void onChanged(@Nullable List<Pedido> pedidos) {
                        if (pedidos != null) {
                            if (pedidos.size() > 0) mensajeVacio.setVisibility(View.GONE);
                            adapter = new PedidosRecyclerViewAdapter(pedidos, listener);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });



        LinearLayout btnPedido = findViewById(R.id.btn_pedido);
        btnPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LlamarClientes(3); // Pedidos
            }
        });

        LinearLayout btnCobro = findViewById(R.id.btn_cobro);
        btnCobro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LlamarClientes(5); // Cobros
            }
        });


//        FloatingActionButton fabProforma = (FloatingActionButton) findViewById(R.id.fab_proforma);
//        fabProforma.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(getApplicationContext(), ProductosActivity.class);
//                i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
//                i.putExtra(Common.ARG_SECCIOM, sector);
//                i.putExtra(Common.ARG_ROL, rolUsuario);
//                startActivity(i);
//            }
//        });

    }

   public void LlamarClientes (int destino){
        //Intent i  = new Intent( getApplicationContext(), PedidoClienteActivity.class );
       Log.d("pedido","pedido --->"+ destino);
       Intent i  = new Intent( getApplicationContext(), PedidoCobroClienteActivity.class );
        i.putExtra(Common.ARG_DESTINO_PEDIDO, destino);
        i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
        i.putExtra(Common.ARG_SECCIOM, sector);
       i.putExtra(Common.ARG_ROL, rolUsuario);

       startActivity(i);

    }

}
