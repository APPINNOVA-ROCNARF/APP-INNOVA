package com.rocnarf.rocnarf;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import com.rocnarf.rocnarf.adapters.ReporteVentasAdapter;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.models.Planes;
import com.rocnarf.rocnarf.models.ReporteVentas;
import com.rocnarf.rocnarf.models.ReporteVentasResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReporteVentasActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, nombreCliente, seccion;
    private ProgressBar progressBar;
    private Context context;
    private Planes planes;
    private List<ReporteVentas> listaGuiaProductos;
    private SearchView searchView;
    private ReporteVentasAdapter adapter ;
    ListView ListViewPlanes;

    private com.rocnarf.rocnarf.dao.DataBaseHelper DataBaseHelper;
    private SQLiteDatabase sQLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_ventas);

        recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_reporte_ventas);
        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion =  i.getStringExtra(Common.ARG_SECCIOM);


        context =this;
        PlanesService service = ApiClient.getClient().create(PlanesService.class);
        Call<ReporteVentasResponse> call  = service.GetReporteVentas(true);
        call.enqueue(new Callback<ReporteVentasResponse>() {
            @Override
            public void onResponse(Call<ReporteVentasResponse> call, Response<ReporteVentasResponse> response) {
                if (response.isSuccessful()){
                    ReporteVentasResponse reporteVentasResponse = response.body();
                    List<ReporteVentas> reporteVentas = reporteVentasResponse.items;
                    listaGuiaProductos =  reporteVentasResponse.items;
//
                    final ReporteVentasAdapter.ReporteVentasListener listener =  new ReporteVentasAdapter.ReporteVentasListener() {
                        @Override
                        public void ReporteVentasListener(ReporteVentas reporteVentas) {



                            Intent i = new Intent(context, PdfReporteVentasActivity.class);
                            i.putExtra(Common.ARG_IDCLIENTE, idCliente);
                            i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                            i.putExtra(Common.ARG_SECCIOM, seccion);
                            i.putExtra(Common.ARG_IDNPLAN,  String.valueOf(reporteVentas.getIdCodigo()));
                            i.putExtra(Common.ARG_NOMPBREPLAN, reporteVentas.getNombre());
//                            i.putExtra("planes", (Serializable) planes);
                            context.startActivity(i);

                        }
                    };
                    recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_reporte_ventas);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    adapter = new ReporteVentasAdapter(reporteVentas, listener);
                    recyclerView.setAdapter(adapter);


                }
            }

            @Override
            public void onFailure(Call<ReporteVentasResponse> call, Throwable t) {
                //Log.d("sincronizar Clientes", t.getMessage());
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
