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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.QuejaAdapter;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.models.Cobro;
import com.rocnarf.rocnarf.models.HistorialComentarios;
import com.rocnarf.rocnarf.models.PedidosPendiente;
import com.rocnarf.rocnarf.models.QuejasConsulta;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuejaActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, nombreCliente, seccion;
    private List<PedidosPendiente> listaPedidosPendiente;
    private Context context;
    private QuejaAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar pgsBar;
    private List<QuejasConsulta> listaQueja;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queja);
        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion = i.getStringExtra(Common.ARG_SECCIOM);

        context = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        pgsBar = (ProgressBar) findViewById(R.id.pBarQueja);

        final ActionBar actionBar = getSupportActionBar();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.buttom_add_queja);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LlamarClientes(8); //Quejas
            }
        });


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshQueja);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CargaData();
                swipeRefreshLayout.setRefreshing(false);

            }

        });
        recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_queja);


        CargaData();


    }

    public void LlamarClientes (int destino){
        Intent i  = new Intent( getApplicationContext(), PedidoCobroClienteActivity.class );
        i.putExtra(Common.ARG_DESTINO_PEDIDO, destino);
        i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
        i.putExtra(Common.ARG_SECCIOM, seccion);


        startActivity(i);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.politicas, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
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


    final void CargaData() {

        PlanesService service = ApiClient.getClient().create(PlanesService.class);
        Call<List<QuejasConsulta>> call = service.GetQuejasUsuario(idUsuario);
        call.enqueue(new Callback<List<QuejasConsulta>>() {
            @Override
            public void onResponse(Call<List<QuejasConsulta>> call, Response<List<QuejasConsulta>> response) {
                if (response.isSuccessful()) {
                    List<QuejasConsulta> historialComentariosResponse = response.body();
                    listaQueja = historialComentariosResponse;
                    final QuejaAdapter.QuejaListener listener = new QuejaAdapter.QuejaListener() {
                        @Override
                        public void QuejaListener(QuejasConsulta quejas) {


                        }
                    };
                    recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_queja);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    adapter = new QuejaAdapter(context, listaQueja, listener);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<QuejasConsulta>> call, Throwable t) {
                //Log.d("sincronizar Clientes", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
