package com.rocnarf.rocnarf;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.HistorialComentariosAdapter;
import com.rocnarf.rocnarf.adapters.RecetasXAdapter;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.models.ComentariosClientes;
import com.rocnarf.rocnarf.models.HistorialComentarios;
import com.rocnarf.rocnarf.models.Recetas;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RecetasXActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, nombreCliente, seccion;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recetas_x);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        final ActionBar actionBar = getSupportActionBar();

        recyclerView = (RecyclerView) findViewById(R.id.list_recetas_x);
        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion = i.getStringExtra(Common.ARG_SECCIOM);
        idCliente = i.getStringExtra(Common.ARG_IDCLIENTE);
        pgsBar = (ProgressBar) findViewById(R.id.pBar_recetas);

        context = this;



        btnRocnarf = findViewById(R.id.opcion_r);
        btnTodos =findViewById(R.id.opcion_t);
        btnMercado=findViewById(R.id.opcion_m);
        mensajeVacio = (TextView)findViewById(R.id.tv_vacio_recetas_x);


        btnRocnarf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcionBtn =1;
                CargaData("1");
            }
        });
        btnTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcionBtn =2;
                CargaData("2");

            }
        });
        btnMercado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opcionBtn =3;
                CargaData("3");

            }
        });
    }


     final void CargaData(String opcion){
         pgsBar.setVisibility(View.VISIBLE);

         PlanesService service = ApiClient.getClient().create(PlanesService.class);


        Call<RecetasXResponse> call = service.GetRecetasX(opcion, idCliente);
        call.enqueue(new Callback<RecetasXResponse>() {
            @Override
            public void onResponse(Call<RecetasXResponse> call, Response<RecetasXResponse> response) {
                if (response.isSuccessful()) {
                    RecetasXResponse recetasXResponse = response.body();
                    List<Recetas> recetas = recetasXResponse.items;
                    listaReceta = recetasXResponse.items;
                        if (recetasXResponse.totalItems > 0) mensajeVacio.setVisibility(View.GONE);
                    final RecetasXAdapter.RecetasXListener listener = new RecetasXAdapter.RecetasXListener() {
                        @Override
                        public void RecetasXListener(Recetas recetas) {

                            if(opcionBtn == 3) {
                                Intent i = new Intent(context, RecetasXMercado.class);
                                i.putExtra(Common.ARG_IDCLIENTE, idCliente);
                                i.putExtra(Common.ARG_IDUSUARIO, idUsuario);

                                i.putExtra(Common.ARG_MERCADO, recetas.getProducto());
                                context.startActivity(i);
                            }

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
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.politicas, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
   //     searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
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
