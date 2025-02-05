package com.rocnarf.rocnarf;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.ClientesAdapter;
import com.rocnarf.rocnarf.models.Clientes;
import com.rocnarf.rocnarf.repository.ClientesRepository;

import java.util.List;

public class GeoLocalizacionActivity extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView lstPaneles;



    private ClientesRepository clientesRepository;
    private ClientesAdapter clientesAdapter;
    private ClientesAdapter.OnClienteClickListener listener = new ClientesAdapter.OnClienteClickListener() {
        @Override
        public void onClienteClick(Clientes cliente) {
            try {
                Intent i = new Intent(getApplicationContext(), GeoLocalizacionMapsActivity.class);
                i.putExtra("cliente", cliente);
                startActivity(i);

            }catch(Exception ex)
            {
                Toast.makeText(getApplicationContext(), "A ocurrido un error al intentar agregar el cliente: " + cliente.getNombreCliente()
                        + "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    };

    private String idUsuario, seccion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geolocalizacion);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion =  i.getStringExtra(Common.ARG_SECCIOM);

        CargarPanel();
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ClientesCriteriosFragment detalleCliente = ClientesCriteriosFragment.newInstance(ClientesCriteriosFragment.DEST_GEOLOCALIZACION, idUsuario, seccion);
//        ft.replace(R.id.fm_clientes_activity_geolocalizacion, detalleCliente);
//        ft.commit();

    }


    public void CargarPanel() {
        clientesRepository = new ClientesRepository(this, idUsuario);

        List<Clientes> clientes = clientesRepository.getClientesSinGeo(seccion, idUsuario, null);
        clientesAdapter =  new ClientesAdapter(this, listener, clientes, idUsuario);
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
                clientesAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                clientesAdapter.getFilter().filter(query);
                return false;
            }
        });

        return true;
    }

}
