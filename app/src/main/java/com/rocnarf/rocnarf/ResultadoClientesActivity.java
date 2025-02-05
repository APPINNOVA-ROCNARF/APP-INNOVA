package com.rocnarf.rocnarf;

import android.app.SearchManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.models.Clientes;
import com.rocnarf.rocnarf.adapters.ClientesAdapter;
import com.rocnarf.rocnarf.viewmodel.ResultadoClientesViewModel;

import java.util.ArrayList;
import java.util.List;

public class ResultadoClientesActivity extends AppCompatActivity  {

    private Context mContext;
    private ArrayList<Clientes> lista = new ArrayList<>() ;
    private RecyclerView recyclerView;
    private String idUsuario, codigoCliente, nombre, tipo , sector, representante, ciudad,rol;
    private int destino, tipoConsulta;
    private ClientesAdapter.OnClienteClickListener listener;
    private SearchView searchView;
    private ClientesAdapter adapter ;
    private List<Clientes> ListClientes;
    private List<Clientes> ListClientesResult;
    //private ResultadoClientesViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_clientes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ResultadoClientesFragment tabla = ResultadoClientesFragment.newInstance(1);
//        ft.replace(R.id.fl_fragment_activity_resultado_clientes, tabla);
//        ft.commit();


        Intent intent = getIntent();
        idUsuario= intent.getStringExtra("idUsuario");
        codigoCliente = intent.getStringExtra("codigoCliente");
        nombre = intent.getStringExtra("nombre");
        tipo = intent.getStringExtra("tipo");
        sector = intent.getStringExtra("sector");
        representante = intent.getStringExtra("representante");
        ciudad = intent.getStringExtra("ciudad");
        destino = intent.getIntExtra("destino", 0);
        tipoConsulta = intent.getIntExtra("tipoConsulta", 0);
        rol= intent.getStringExtra("rol");
        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        if (destino == ClientesCriteriosFragment.DEST_GEOLOCALIZACION) {
            listener = new ClientesAdapter.OnClienteClickListener() {
                @Override
                public void onClienteClick(Clientes cliente) {
                    Intent i = new Intent(getApplicationContext(), GeoLocalizacionMapsActivity.class);
                    i.putExtra("cliente", cliente);
                    startActivity(i);
                }
            };
        }else if (destino == ClientesCriteriosFragment.DEST_PLANIFICACION) {
            listener = new ClientesAdapter.OnClienteClickListener() {
                @Override
                public void onClienteClick(Clientes cliente) {
                    Intent i = new Intent(getApplicationContext(), PlanificacionCrearActivity.class);
                    i.putExtra(Common.ARG_IDCLIENTE, cliente.getIdCliente());
                    i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    i.putExtra(Common.ARG_SECCIOM, sector);
                    i.putExtra(Common.ARG_ORIGEN_PLANIFICACION_VISITA, Common.VISITA_DESDE_MAIN);
                    startActivity(i);
                }
            };
        }else if (destino == ClientesCriteriosFragment.DEST_PEDIDO) {
            listener = new ClientesAdapter.OnClienteClickListener() {
                @Override
                public void onClienteClick(Clientes cliente) {
                    Intent i = new Intent(getApplicationContext(), ProductosActivity.class);
                    i.putExtra(Common.ARG_IDCLIENTE , cliente.getIdCliente());
                    i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    i.putExtra(Common.ARG_SECCIOM, sector);
                    i.putExtra(Common.ARG_NOMBRE_CLIENTE, cliente.getNombreCliente());
                    startActivity(i);
                }
            };
        }else if (destino == ClientesCriteriosFragment.DEST_VISITAS_IMPULSADORAS) {
            listener = new ClientesAdapter.OnClienteClickListener() {
                @Override
                public void onClienteClick(Clientes cliente) {
                    Intent i = new Intent(getApplicationContext(), VisitaImpulsadoraCrearActivity.class);
                    i.putExtra(Common.ARG_IDCLIENTE, cliente.getIdCliente());
                    i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    startActivity(i);
                }
            };
        }else if (destino == ClientesCriteriosFragment.DEST_COBROS) {
            listener = new ClientesAdapter.OnClienteClickListener() {
                @Override
                public void onClienteClick(Clientes cliente) {
                    Intent i = new Intent(getApplicationContext(), ClientesFacturasActivity.class);
                    i.putExtra(Common.ARG_IDCLIENTE, cliente.getIdCliente());
                    i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    i.putExtra(Common.ARG_SECCIOM, sector);
                    i.putExtra(Common.ARG_NOMBRE_CLIENTE, cliente.getNombreCliente());
                    i.putExtra(Common.ARG_TIPO_PEDIDO, Common.TIPO_PEDIDO_COBRO);
                    i.putExtra(Common.ARG_FACTURAS_SELECCION, true);
                    startActivity(i);
                }
            };

        }else  { // En caso de que no llego el valor
            listener = new ClientesAdapter.OnClienteClickListener() {
                @Override
                public void onClienteClick(Clientes cliente) {
                }
            };
        }


            //final ClientesAdapter adapter = new ClientesAdapter(this, listener);
            //adapter = new ClientesAdapter(this, listener);
            recyclerView.setAdapter(adapter);
        ViewModelProviders.of(this).get(ResultadoClientesViewModel.class);
        ResultadoClientesViewModel model = ViewModelProviders.of(this).get(ResultadoClientesViewModel.class);
            model.setQueryFilter(idUsuario, codigoCliente, nombre, tipo, sector, representante, ciudad, tipoConsulta,rol);
            model.listaClientes.observe(this, new Observer<PagedList<Clientes>>() {
                @Override
                public void onChanged(@Nullable PagedList<Clientes> clientes) {
                    //adapter.submitList(clientes);
                    ListClientes = clientes;
                }

            });

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
                //adapter.getFilter(ListClientes).filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //ListClientesResult = adapter.getFilter(ListClientes, query);

                return false;
            }
        });

        return true;
    }

}
