package com.rocnarf.rocnarf;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.RecetasXAdapter;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.models.Recetas;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecetasXMercado extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, nombreCliente, seccion,Mercado;
    private ProgressBar progressBar;
    private Context context;
    private Recetas recetas;
    private List<Recetas> listaReceta;
    private SearchView searchView;
    private Button btnRocnarf, btnTodos, btnMercado;
    private RecetasXAdapter adapter;
    ListView ListViewPlanes;
    private ProgressBar pgsBar;
    private TextView mensajeVacio;
    private com.rocnarf.rocnarf.dao.DataBaseHelper DataBaseHelper;
    private SQLiteDatabase sQLiteDatabase;
    int opcionBtn = 0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recetas_x_mercado);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        final ActionBar actionBar = getSupportActionBar();

        recyclerView = (RecyclerView) findViewById(R.id.list_recetas_x);
        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion = i.getStringExtra(Common.ARG_SECCIOM);
        idCliente = i.getStringExtra(Common.ARG_IDCLIENTE);
        Mercado = i.getStringExtra(Common.ARG_MERCADO);

        pgsBar = (ProgressBar) findViewById(R.id.pBar_recetas);
        this.setTitle(Mercado);
        context = this;
        CargaData("3");
    }

    final void CargaData(String opcion){
        pgsBar.setVisibility(View.VISIBLE);

        PlanesService service = ApiClient.getClient().create(PlanesService.class);


        Call<RecetasXResponse> call = service.GetRecetasXMercado(idCliente, Mercado);
        call.enqueue(new Callback<RecetasXResponse>() {
            @Override
            public void onResponse(Call<RecetasXResponse> call, Response<RecetasXResponse> response) {
                if (response.isSuccessful()) {
                    RecetasXResponse recetasXResponse = response.body();
                    List<Recetas> recetas = recetasXResponse.items;
                    listaReceta = recetasXResponse.items;
                    final RecetasXAdapter.RecetasXListener listener = new RecetasXAdapter.RecetasXListener() {
                        @Override
                        public void RecetasXListener(Recetas recetas) {

                        }
                    };
                    recyclerView = (RecyclerView) findViewById(R.id.list_recetas_x);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    adapter = new RecetasXAdapter(listaReceta, listener);
                    recyclerView.setAdapter(adapter);
                    pgsBar.setVisibility(View.GONE);


                }
            }

            @Override
            public void onFailure(Call<RecetasXResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
