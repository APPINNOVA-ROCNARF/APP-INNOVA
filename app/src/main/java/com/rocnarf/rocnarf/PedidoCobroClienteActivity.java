package com.rocnarf.rocnarf;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.ClientesAdapter;
import com.rocnarf.rocnarf.models.Clientes;
import com.rocnarf.rocnarf.repository.ClientesRepository;

import java.util.List;

public class PedidoCobroClienteActivity extends AppCompatActivity {
    private String  seccion, rol, secciones;
    private String idUsuario ;
    private int destino;



    private SearchView searchView;
    private RecyclerView lstPaneles;



    private ClientesRepository clientesRepository;
    private ClientesAdapter clientesAdapter;
    private ClientesAdapter.OnClienteClickListener listener = new ClientesAdapter.OnClienteClickListener() {
        @Override
        public void onClienteClick(Clientes cliente) {

            final boolean esKAM = idUsuario.equalsIgnoreCase("SBC") ||
                    idUsuario.equalsIgnoreCase("JRP") ||
                    idUsuario.equalsIgnoreCase("COG");
            try {
                Log.d("pedido","pedido de cobros --->"+ destino);
                if (destino == 3) // Pedidos
                {
                    if (esKAM) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PedidoCobroClienteActivity.this);
                        builder.setTitle("¿Qué precios desea usar?")
                                .setMessage("Seleccione si desea usar precios normales o precios especiales para este cliente.")
                                .setPositiveButton("Precios especiales", (dialog, which) -> {
                                    abrirProductosActivity(cliente, true);
                                })
                                .setNegativeButton("Precios normales", (dialog, which) -> {
                                    abrirProductosActivity(cliente, false);
                                })
                                .setCancelable(true)
                                .show();
                    } else {
                        abrirProductosActivity(cliente, false); // usuarios normales
                    }
                }

                if (destino == 5)//Pedidos
                {
                    Intent in = new Intent(getApplicationContext(), ClientesFacturasActivity.class);
                    in.putExtra(Common.ARG_IDCLIENTE, cliente.getIdCliente());
                    in.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    in.putExtra(Common.ARG_SECCIOM, seccion);
                    in.putExtra(Common.ARG_NOMBRE_CLIENTE, cliente.getNombreCliente());
                    in.putExtra(Common.ARG_TIPO_PEDIDO, Common.TIPO_PEDIDO_COBRO);
                    in.putExtra(Common.ARG_FACTURAS_SELECCION, true);
                    startActivity(in);
                }
                if (destino == 6)//Pedidos
                {
                    Intent in = new Intent();
                    in.putExtra("argCliente", cliente.getNombreCliente());
                    in.putExtra("argIdCliente", cliente.getIdCliente());
                    in.putExtra("argOrigen", cliente.getOrigen());
                    in.putExtra("argEstadoVisita", cliente.getEstadoVisita());
                    in.putExtra("argTipoObser", cliente.getTipoObserv());
                    in.putExtra("argClienteModel",  (Parcelable) cliente);
                    setResult(RESULT_OK, in);
                    finish();
                }
                if (destino == 7)//proforma
                {
                    Intent in = new Intent(getApplicationContext(), ProductosActivity.class);
                    in.putExtra(Common.ARG_IDCLIENTE, cliente.getIdCliente());
                    in.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    in.putExtra(Common.ARG_DESTINO_PEDIDO, 7);
                    in.putExtra(Common.ARG_SECCIOM, seccion);
                    in.putExtra(Common.ARG_NOMBRE_CLIENTE, cliente.getNombreCliente());
                    startActivity(in);
                }
                if (destino == 8)// quejas
                {
                    Intent in = new Intent(getApplicationContext(), DetalleQuejaActivity.class);
                    in.putExtra(Common.ARG_IDCLIENTE, cliente.getIdCliente());
                    in.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    in.putExtra(Common.ARG_DESTINO_PEDIDO, 7);
                    in.putExtra(Common.ARG_SECCIOM, seccion);
                    in.putExtra(Common.ARG_NOMBRE_CLIENTE, cliente.getNombreCliente());
                    startActivity(in);
                }
            }catch(Exception ex)
            {
                Toast.makeText(getApplicationContext(), "A ocurrido un error al intentar agregar el cliente: " + cliente.getNombreCliente()
                        + "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_pedido_cliente);
        setContentView(R.layout.activity_pedido_cobro_clientes);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i = getIntent();
        destino = i.getIntExtra(Common.ARG_DESTINO_PEDIDO, 3);
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion =  i.getStringExtra(Common.ARG_SECCIOM);
        secciones = i.getStringExtra(Common.ARG_SECCIONES);
        rol=  i.getStringExtra(Common.ARG_ROL);

        CargarPanel();

        /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ClientesCriteriosFragment detalleCliente = ClientesCriteriosFragment.newInstance(destino, idUsuario, seccion);
        ft.replace(R.id.fm_criterios_activity_pedido_cliente, detalleCliente);
        ft.commit();*/

    }

    private void abrirProductosActivity(Clientes cliente, boolean usarPrecioEspecial) {
        Intent in = new Intent(getApplicationContext(), ProductosActivity.class);
        in.putExtra(Common.ARG_IDCLIENTE, cliente.getIdCliente());
        in.putExtra(Common.ARG_IDUSUARIO, idUsuario);
        in.putExtra(Common.ARG_SECCIOM, seccion);
        in.putExtra(Common.ARG_NOMBRE_CLIENTE, cliente.getNombreCliente());
        in.putExtra(Common.ARG_USAR_PRECIO_ESPECIAL, usarPrecioEspecial);
        startActivity(in);
    }


    public void CargarPanel() {
        clientesRepository = new ClientesRepository(this, idUsuario);
        Log.d("Hola","rol ped"+rol);

        List<Clientes> clientes = clientesRepository.getClientes(seccion, idUsuario, "FARMA", rol);
        for (int i=0; i < clientes.size(); i++) {
            if ("CLI Z".equals(clientes.get(i).getTipoObserv())) {
                clientes.remove(i);
                i--;//decrease the counter by one
            }
        }
        clientesAdapter =  new ClientesAdapter(this, listener, clientes, idUsuario, seccion, secciones);
        lstPaneles = (RecyclerView) findViewById(R.id.list);
        lstPaneles.setLayoutManager(new LinearLayoutManager(this));
        lstPaneles.setAdapter(clientesAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.clientes, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                clientesAdapter.filtrarPorTexto(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                clientesAdapter.filtrarPorTexto(query);
                return false;
            }
        });

        return true;
    }


}
