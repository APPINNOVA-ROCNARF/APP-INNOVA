package com.rocnarf.rocnarf;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.CarteraClienteAdapter;
import com.rocnarf.rocnarf.adapters.MedicosCumpleanyosAdapter;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.models.CarteraCliente;
import com.rocnarf.rocnarf.models.Clientes;
import com.rocnarf.rocnarf.models.MedicosCumpleanyos;
import com.rocnarf.rocnarf.models.MedicosCumpleanyosResponse;
import com.rocnarf.rocnarf.models.Recetas;
import com.rocnarf.rocnarf.repository.ClientesRepository;
import com.rocnarf.rocnarf.viewmodel.ClientesFacturasViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CumpleanyosActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, nombreCliente, seccion, rol;
    private List<MedicosCumpleanyos> listaPedidosPendiente;
    private List<Clientes> clientes;
    private Context context;
    private MedicosCumpleanyosAdapter adapter ;
    SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar pgsBar;
    private ClientesFacturasViewModel clientesFacturasViewModel;
    private ClientesRepository clientesRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicos_cumpleanyos);
        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion =  i.getStringExtra(Common.ARG_SECCIOM);
        rol = i.getStringExtra(Common.ARG_ROL);
        context =this;


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        pgsBar = (ProgressBar) findViewById(R.id.pBarcumpleanyos);
        final ActionBar actionBar = getSupportActionBar();

        swipeRefreshLayout =  (SwipeRefreshLayout) findViewById(R.id.swipeRefreshcumpleanyos);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CargarData();
                swipeRefreshLayout.setRefreshing(false);

            }

        });
        recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_cumpleanyos);

        clientesRepository = new ClientesRepository(this, idUsuario);
        clientes = clientesRepository.getClientes(seccion, idUsuario, null, rol);

        CargarData();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.politicas, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    public void CargarData(){

        pgsBar.setVisibility(View.VISIBLE);

        PlanesService service = ApiClient.getClient().create(PlanesService.class);
        Call<MedicosCumpleanyosResponse> call  = service.GetCumpleanyos(seccion);
        call.enqueue(new Callback<MedicosCumpleanyosResponse>() {
            @Override
            public void onResponse(Call<MedicosCumpleanyosResponse> call, Response<MedicosCumpleanyosResponse> response) {
                if (response.isSuccessful()){

                    MedicosCumpleanyosResponse medicosCumpleanyosResponse = response.body();
                    for(int indicex = 0;indicex<medicosCumpleanyosResponse.items.size();indicex++) {

                        for (int indice = 0; indice < clientes.size(); indice++) {
                            if (medicosCumpleanyosResponse.items.get(indicex).getIdCliente().equals(clientes.get(indice).getIdCliente())) {
                                medicosCumpleanyosResponse.items.get(indicex).setEstadoVisita(clientes.get(indice).getEstadoVisita());
                            }

                        }
                    }
                    listaPedidosPendiente = medicosCumpleanyosResponse.items;
                    final MedicosCumpleanyosAdapter.MedicosCumpleanyosListener listener =  new MedicosCumpleanyosAdapter.MedicosCumpleanyosListener() {
                        @Override
                        public void MedicosCumpleanyosListener(MedicosCumpleanyos medicosCumpleanyos) {

                            Intent in = new Intent(getApplicationContext(), PlanificacionCrearActivity.class);
                            in.putExtra(Common.ARG_IDCLIENTE, medicosCumpleanyos.getIdCliente());
                            in.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                            in.putExtra(Common.ARG_SECCIOM, seccion);
                            //in.putExtra(Common.ARG_ESTADOVISTA,  clientes.getEstadoVisita());
                            in.putExtra(Common.ARG_ORIGEN_PLANIFICACION_VISITA, Common.VISITA_DESDE_PANEL);
                            startActivity(in);

                        }
                    };
                    recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_cumpleanyos);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    adapter = new MedicosCumpleanyosAdapter(context,listaPedidosPendiente, listener, idUsuario);
                    recyclerView.setAdapter(adapter);
                    pgsBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<MedicosCumpleanyosResponse> call, Throwable t) {
                //Log.d("sincronizar Clientes", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
