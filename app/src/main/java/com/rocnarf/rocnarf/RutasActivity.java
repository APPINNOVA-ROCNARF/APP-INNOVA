package com.rocnarf.rocnarf;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.RutasAdapter;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.RutasService;
import com.rocnarf.rocnarf.models.Planes;
import com.rocnarf.rocnarf.models.Rutas;
import com.rocnarf.rocnarf.models.RutasResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RutasActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, nombreCliente, seccion;
    private ProgressBar progressBar;
    private Context context;
    private Planes planes;
    private List<Rutas> listaRutas;
    private SearchView searchView;
    private RutasAdapter adapter ;
    ListView ListViewPlanes;
    private boolean expadir;
    private com.rocnarf.rocnarf.dao.DataBaseHelper DataBaseHelper;
    private SQLiteDatabase sQLiteDatabase;
    SwipeRefreshLayout swipeRefreshLayout;
    FloatingActionButton crearRuta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rutas);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
        swipeRefreshLayout =  (SwipeRefreshLayout) findViewById(R.id.swipeRefreshRutas);
        crearRuta = (FloatingActionButton) findViewById(R.id.crear_ruta);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RutasService service = ApiClient.getClient().create(RutasService.class);
                Call<RutasResponse> call  = service.GetRutas();
                call.enqueue(new Callback<RutasResponse>() {
                    @Override
                    public void onResponse(Call<RutasResponse> call, Response<RutasResponse> response) {
                        if (response.isSuccessful()){
                            RutasResponse rutasResponse = response.body();
                            List<Rutas> rutas = rutasResponse.items;
                            listaRutas =  rutasResponse.items;
                            swipeRefreshLayout.setRefreshing(false);

                        }
                    }

                    @Override
                    public void onFailure(Call<RutasResponse> call, Throwable t) {
                        //Log.d("sincronizar Clientes", t.getMessage());
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

        });

        recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_rutas);
        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion =  i.getStringExtra(Common.ARG_SECCIOM);
        //idCliente =  i.getStringExtra(Common.ARG_IDCLIENTE);
        //nombreCliente =  i.getStringExtra(Common.ARG_NOMBRE_CLIENTE);
        context =this;
        RutasService service = ApiClient.getClient().create(RutasService.class);
        Call<RutasResponse> call  = service.GetRutas();
        call.enqueue(new Callback<RutasResponse>() {
            @Override
            public void onResponse(Call<RutasResponse> call, Response<RutasResponse> response) {
                if (response.isSuccessful()){
                    RutasResponse rutasResponse = response.body();
                    List<Rutas> rutas = rutasResponse.items;
                    listaRutas =  rutasResponse.items;
//
                    final RutasAdapter.RutasListener listener =  new RutasAdapter.RutasListener() {
                        @Override
                        public void RutasListener(Rutas rutas) {


//
//                            Intent i = new Intent(context, ParrillaActivity.class);
//                            i.putExtra(Common.ARG_IDCLIENTE, idCliente);
//                            i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
//                            i.putExtra(Common.ARG_SECCIOM, seccion);
//
////                            i.putExtra("planes", (Serializable) planes);
//                            context.startActivity(i);

                        }
                    };
                    recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_rutas);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    adapter = new RutasAdapter(rutas, listener);
                    recyclerView.setAdapter(adapter);


                }
            }

            @Override
            public void onFailure(Call<RutasResponse> call, Throwable t) {
                //Log.d("sincronizar Clientes", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        crearRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, RutasCrearActivity.class);
                i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                i.putExtra(Common.ARG_SECCIOM, seccion);
                context.startActivity(i);
            }
        });

    }





}
