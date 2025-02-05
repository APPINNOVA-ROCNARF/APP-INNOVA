package com.rocnarf.rocnarf;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.PreguntasAdapter;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.models.Planes;
import com.rocnarf.rocnarf.models.PreguntasFrecuentes;
import com.rocnarf.rocnarf.models.PreguntasFrecuentesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreguntasFrecuentesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, nombreCliente, seccion;
    private ProgressBar progressBar;
    private Context context;
    private Planes planes;
    private List<PreguntasFrecuentes> listaPreguntas;
    private SearchView searchView;
    private PreguntasAdapter adapter ;
    ListView ListViewPlanes;
    private boolean expadir;
    private com.rocnarf.rocnarf.dao.DataBaseHelper DataBaseHelper;
    private SQLiteDatabase sQLiteDatabase;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregustas_frecuentes);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
        progressBar = (ProgressBar)findViewById(R.id.pr_list_activity_preguntas);
        progressBar.setVisibility(View.VISIBLE);

        this.expadir = false;
        swipeRefreshLayout =  (SwipeRefreshLayout) findViewById(R.id.swipeRefreshPreguntas);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PlanesService service = ApiClient.getClient().create(PlanesService.class);
                Call<PreguntasFrecuentesResponse> call  = service.GetPreguntas(true);
                call.enqueue(new Callback<PreguntasFrecuentesResponse>() {
                    @Override
                    public void onResponse(Call<PreguntasFrecuentesResponse> call, Response<PreguntasFrecuentesResponse> response) {
                        if (response.isSuccessful()){
                            PreguntasFrecuentesResponse preguntasFrecuentesResponse = response.body();
                            List<PreguntasFrecuentes> preguntasFrecuentes = preguntasFrecuentesResponse.items;
                            listaPreguntas =  preguntasFrecuentesResponse.items;
                            swipeRefreshLayout.setRefreshing(false);

                        }
                    }

                    @Override
                    public void onFailure(Call<PreguntasFrecuentesResponse> call, Throwable t) {
                        //Log.d("sincronizar Clientes", t.getMessage());
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

        });

        recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_preguntas);
        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion =  i.getStringExtra(Common.ARG_SECCIOM);
        //idCliente =  i.getStringExtra(Common.ARG_IDCLIENTE);
        //nombreCliente =  i.getStringExtra(Common.ARG_NOMBRE_CLIENTE);
        context =this;
        PlanesService service = ApiClient.getClient().create(PlanesService.class);
        Call<PreguntasFrecuentesResponse> call  = service.GetPreguntas(true);
        call.enqueue(new Callback<PreguntasFrecuentesResponse>() {
            @Override
            public void onResponse(Call<PreguntasFrecuentesResponse> call, Response<PreguntasFrecuentesResponse> response) {
                if (response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    PreguntasFrecuentesResponse preguntasFrecuentesResponse = response.body();
                    List<PreguntasFrecuentes> preguntasFrecuentes = preguntasFrecuentesResponse.items;
                    listaPreguntas =  preguntasFrecuentesResponse.items;
//
                    final PreguntasAdapter.PreguntasListener listener =  new PreguntasAdapter.PreguntasListener() {
                        @Override
                        public void PreguntasListener(PreguntasFrecuentes preguntasFrecuentes) {


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
                    recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_preguntas);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    adapter = new PreguntasAdapter(preguntasFrecuentes, listener);
                    recyclerView.setAdapter(adapter);


                }
            }

            @Override
            public void onFailure(Call<PreguntasFrecuentesResponse> call, Throwable t) {
                //Log.d("sincronizar Clientes", t.getMessage());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
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


}
