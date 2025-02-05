package com.rocnarf.rocnarf;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.PedidosPendienteAdapter;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.models.PedidosPendiente;
import com.rocnarf.rocnarf.models.PedidosPendienteResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidosPendienteActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, nombreCliente, seccion;
    private List<PedidosPendiente> listaPedidosPendiente;
    private Context context;
    private PedidosPendienteAdapter adapter ;
    SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar pgsBar;
    Chip FiltroC,FiltroB,FiltroD;
    ChipGroup Fitros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_pendiente);
        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion =  i.getStringExtra(Common.ARG_SECCIOM);

        context =this;

        FiltroC = findViewById(R.id.bC);
        FiltroB = findViewById(R.id.bB);
        FiltroD = findViewById(R.id.bD);

        Fitros = findViewById(R.id.id_chip_group);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        pgsBar = (ProgressBar) findViewById(R.id.pBarPedidosPendiente);
        final ActionBar actionBar = getSupportActionBar();

        swipeRefreshLayout =  (SwipeRefreshLayout) findViewById(R.id.swipeRefreshPedidosPendiente);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CargarPedidoPendiente();
                swipeRefreshLayout.setRefreshing(false);

            }

        });
        recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_pedidos_pendiente);



        CargarPedidoPendiente();

        Fitros.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                List<PedidosPendiente> filtered = new ArrayList<>();


                Chip chip = findViewById(i);
                if(chip != null){

                    if(chip.getText().toString().equals("Despacho")){

                        for (int x = 0; x <listaPedidosPendiente.size(); x ++) {
                            if(listaPedidosPendiente.get(x).getDespachado() != null){
                                filtered.add(listaPedidosPendiente.get(x));
                            }
                        }

                        Collections.sort(filtered, new Comparator<PedidosPendiente>() {
                            @Override
                            public int compare(PedidosPendiente u1, PedidosPendiente u2) {
                                return u2.getFecha().compareTo(u1.getFecha());
                            }
                        });


                    }else if(chip.getText().toString().equals("Bonificación")){

                        for (int x = 0; x <listaPedidosPendiente.size(); x ++) {
                            if(!listaPedidosPendiente.get(x).getAprobado().toUpperCase(Locale.ROOT).equals("X")){
                                filtered.add(listaPedidosPendiente.get(x));
                            }
                        }

                        Collections.sort(filtered, new Comparator<PedidosPendiente>() {
                            @Override
                            public int compare(PedidosPendiente u1, PedidosPendiente u2) {
                                return u1.getNegaboni().compareTo(u2.getNegaboni());
                            }
                        });


                    }else  if(chip.getText().toString().equals("Crédito")){
                        for (int x = 0; x <listaPedidosPendiente.size(); x ++) {
                            if(listaPedidosPendiente.get(x).getAprobado().toUpperCase(Locale.ROOT).equals("") ||listaPedidosPendiente.get(x).getAprobado().toUpperCase(Locale.ROOT).equals("A") || listaPedidosPendiente.get(x).getAprobado().toUpperCase(Locale.ROOT).equals("N")){
                                filtered.add(listaPedidosPendiente.get(x));
                            }
                        }


                        Collections.sort(filtered, new Comparator<PedidosPendiente>() {
                            @Override
                            public int compare(PedidosPendiente u1, PedidosPendiente u2) {
                                return u2.getAprobado().compareTo(u1.getAprobado());
                            }
                        });

                    }

                }else{
                    filtered = listaPedidosPendiente;
//                    Collections.sort(listaPedidosPendiente, new Comparator<PedidosPendiente>() {
//                        @Override
//                        public int compare(PedidosPendiente u1, PedidosPendiente u2) {
//                            return u1.getNomcli().compareTo(u2.getNomcli());
//                        }
//                    });

                }


                recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_pedidos_pendiente);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                final PedidosPendienteAdapter.PedidosPendienteListener listener =  new PedidosPendienteAdapter.PedidosPendienteListener() {
                    @Override
                    public void PedidosPendienteListener(PedidosPendiente pedidosPendiente) {

                        Intent i = new Intent(context, PedidosPendienteDetalleActivity.class);
                        i.putExtra(Common.ARG_IDCLIENTE, idCliente);
                        i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                        i.putExtra(Common.ARG_SECCIOM, seccion);
                        i.putExtra("pedidosPendiente", (Serializable) pedidosPendiente);
                        context.startActivity(i);

                    }
                };
                adapter = new PedidosPendienteAdapter(context,filtered, listener, idUsuario);
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


    public void CargarPedidoPendiente(){

        pgsBar.setVisibility(View.VISIBLE);

        PlanesService service = ApiClient.getClient().create(PlanesService.class);
        Call<PedidosPendienteResponse> call  = service.GetPedidosPendiente(idUsuario);
        call.enqueue(new Callback<PedidosPendienteResponse>() {
            @Override
            public void onResponse(Call<PedidosPendienteResponse> call, Response<PedidosPendienteResponse> response) {
                if (response.isSuccessful()){
                    PedidosPendienteResponse pedidosPendienteResponse = response.body();
                    List<PedidosPendiente> pedidosPendiente = pedidosPendienteResponse.items;
                    listaPedidosPendiente =  pedidosPendienteResponse.items;

                    final PedidosPendienteAdapter.PedidosPendienteListener listener =  new PedidosPendienteAdapter.PedidosPendienteListener() {
                        @Override
                        public void PedidosPendienteListener(PedidosPendiente pedidosPendiente) {

                            Intent i = new Intent(context, PedidosPendienteDetalleActivity.class);
                            i.putExtra(Common.ARG_IDCLIENTE, idCliente);
                            i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                            i.putExtra(Common.ARG_SECCIOM, seccion);
                            i.putExtra("pedidosPendiente", (Serializable) pedidosPendiente);
                            context.startActivity(i);

                        }
                    };
                    recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_pedidos_pendiente);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    adapter = new PedidosPendienteAdapter(context,pedidosPendiente, listener, idUsuario);
                    recyclerView.setAdapter(adapter);
                    pgsBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<PedidosPendienteResponse> call, Throwable t) {
                //Log.d("sincronizar Clientes", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
