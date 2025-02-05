package com.rocnarf.rocnarf;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.CarteraClienteAdapter;
import com.rocnarf.rocnarf.adapters.PedidosPendienteAdapter;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.models.CarteraCliente;
import com.rocnarf.rocnarf.models.Clientes;
import com.rocnarf.rocnarf.models.FacturasNotaDebitos;
import com.rocnarf.rocnarf.models.PedidosPendiente;
import com.rocnarf.rocnarf.models.PedidosPendienteResponse;
import com.rocnarf.rocnarf.repository.ClientesRepository;
import com.rocnarf.rocnarf.viewmodel.ClientesFacturasViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarteraClienteActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, nombreCliente, seccion, rol;
    private List<CarteraCliente> listaPedidosPendiente;
    private List<Clientes> clientes;
    private Context context;
    private CarteraClienteAdapter adapter ;
    SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar pgsBar;
    private ClientesFacturasViewModel clientesFacturasViewModel;
    Chip op1,op2,op3,op4;
    ChipGroup Fitros;
    private ClientesRepository clientesRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartera_cliente);
        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion =  i.getStringExtra(Common.ARG_SECCIOM);
        rol = i.getStringExtra(Common.ARG_ROL);
        context =this;


        op1 = findViewById(R.id.op1_cartera);
        op2 = findViewById(R.id.op2_cartera);
        op3 = findViewById(R.id.op3_cartera);
        op4 = findViewById(R.id.op4_cartera);

        Fitros = findViewById(R.id.id_chip_group_cartera);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        pgsBar = (ProgressBar) findViewById(R.id.pBarCarteraCliente);
        final ActionBar actionBar = getSupportActionBar();

        swipeRefreshLayout =  (SwipeRefreshLayout) findViewById(R.id.swipeRefreshCarteraCliente);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CargarData();
                swipeRefreshLayout.setRefreshing(false);

            }

        });
        recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_cartera_cliente);

        clientesRepository = new ClientesRepository(this, idUsuario);
        clientes = clientesRepository.getClientes(seccion, idUsuario, null, rol);

        CargarData();

        Fitros.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                List<CarteraCliente> filtered = new ArrayList<>();


                Chip chip = findViewById(i);
                if(chip != null){

                    if(chip.getText().toString().equals("Valor Vcdo")){
                        Collections.sort(listaPedidosPendiente, new Comparator<CarteraCliente>() {
                            @Override
                            public int compare(CarteraCliente u2, CarteraCliente u1) {
                                return u1.getVencido().compareTo(u2.getVencido());
                            }
                        });

                    }else if(chip.getText().toString().equals("Dias Vcdo")){
                        Collections.sort(listaPedidosPendiente, new Comparator<CarteraCliente>() {
                            @Override
                            public int compare(CarteraCliente u1, CarteraCliente u2) {
                                return Integer.compare(u2.getDiazVencido(), u1.getDiazVencido());
                                //return u1.getDiazVencido();
                            }
                        });
                    }else if(chip.getText().toString().equals("Ciudad")){
                        Collections.sort(listaPedidosPendiente, new Comparator<CarteraCliente>() {
                            @Override
                            public int compare(CarteraCliente u1, CarteraCliente u2) {
                                return u1.getCiudad().compareTo(u2.getCiudad());
                            }
                        });
                    }else if(chip.getText().toString().equals("Visitas")){
                        Collections.sort(listaPedidosPendiente, new Comparator<CarteraCliente>() {
                            @Override
                            public int compare(CarteraCliente u1, CarteraCliente u2) {
                                if (u1.getEstadoVisita() == null) {
                                    return (u2.getEstadoVisita() == null) ? 0 : -1;
                                }
                                if (u2.getEstadoVisita() == null) {
                                    return 1;
                                }
                                return u1.getEstadoVisita().compareTo(u2.getEstadoVisita());

                            }
                        });
                    }

                }else{
                    Collections.sort(listaPedidosPendiente, new Comparator<CarteraCliente>() {
                        @Override
                        public int compare(CarteraCliente u1, CarteraCliente u2) {
                            return u1.getNombre().compareTo(u2.getNombre());
                        }
                    });
                }


                recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_cartera_cliente);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                final CarteraClienteAdapter.CarteraClienteListener listener =  new CarteraClienteAdapter.CarteraClienteListener() {
                    @Override
                    public void CarteraClienteListener(CarteraCliente carteraCliente) {

//                            Intent i = new Intent(context, PedidosPendienteDetalleActivity.class);
//                            i.putExtra(Common.ARG_IDCLIENTE, idCliente);
//                            i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
//                            i.putExtra(Common.ARG_SECCIOM, seccion);
//                            context.startActivity(i);

                    }
                };
                recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_cartera_cliente);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                adapter = new CarteraClienteAdapter(context,listaPedidosPendiente, listener, idUsuario);
                recyclerView.setAdapter(adapter);

            }
        });

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
        Call<List<CarteraCliente>> call  = service.GetCarteraCliente(seccion,rol);
        call.enqueue(new Callback<List<CarteraCliente>>() {
            @Override
            public void onResponse(Call<List<CarteraCliente>> call, Response<List<CarteraCliente>> response) {
                if (response.isSuccessful()){
                    listaPedidosPendiente = response.body();
                    for(int indicex = 0;indicex<listaPedidosPendiente.size();indicex++) {

                        for (int indice = 0; indice < clientes.size(); indice++) {
                            if(listaPedidosPendiente.get(indicex).getIdCliente().equals(clientes.get(indice).getIdCliente())){
                                listaPedidosPendiente.get(indicex).setEstadoVisita(clientes.get(indice).getEstadoVisita());
                            }

                        }
                    }
                    final CarteraClienteAdapter.CarteraClienteListener listener =  new CarteraClienteAdapter.CarteraClienteListener() {
                        @Override
                        public void CarteraClienteListener(CarteraCliente carteraCliente) {

//                            Intent i = new Intent(context, PedidosPendienteDetalleActivity.class);
//                            i.putExtra(Common.ARG_IDCLIENTE, idCliente);
//                            i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
//                            i.putExtra(Common.ARG_SECCIOM, seccion);
//                            context.startActivity(i);

                        }
                    };
                    recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_cartera_cliente);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    adapter = new CarteraClienteAdapter(context,listaPedidosPendiente, listener, idUsuario);
                    recyclerView.setAdapter(adapter);
                    pgsBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<List<CarteraCliente>> call, Throwable t) {
                //Log.d("sincronizar Clientes", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
