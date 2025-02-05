package com.rocnarf.rocnarf;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.PlanesAdapter;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.models.Planes;
import com.rocnarf.rocnarf.models.PlanesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.rocnarf.rocnarf.api.PlanesService;

public class PlanesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, nombreCliente, seccion;
    private ProgressBar progressBar;
    private Context context;
    private Planes planes;
    private List<Planes> listaPlanes;
    private SearchView searchView;
    private PlanesAdapter adapter ;
    ListView ListViewPlanes;

    private com.rocnarf.rocnarf.dao.DataBaseHelper DataBaseHelper;
    private SQLiteDatabase sQLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planes);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_planes);
        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion =  i.getStringExtra(Common.ARG_SECCIOM);
        //idCliente =  i.getStringExtra(Common.ARG_IDCLIENTE);
        //nombreCliente =  i.getStringExtra(Common.ARG_NOMBRE_CLIENTE);
        context =this;
        PlanesService service = ApiClient.getClient().create(PlanesService.class);
        retrofit2.Call<PlanesResponse> call  = service.Get(true);
        call.enqueue(new Callback<PlanesResponse>() {
            @Override
            public void onResponse(Call<PlanesResponse> call, Response<PlanesResponse> response) {
                if (response.isSuccessful()){
                    PlanesResponse planesResponse = response.body();
                    List<Planes> planes = planesResponse.items;
                    listaPlanes =  planesResponse.items;
//
                    final PlanesAdapter.PlanesListener listener =  new PlanesAdapter.PlanesListener() {
                        @Override
                        public void PlanesListener(Planes planes) {



                            Intent i = new Intent(context, PlanesCreaObsequioActivity.class);
                            i.putExtra(Common.ARG_IDCLIENTE, idCliente);
                            i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                            i.putExtra(Common.ARG_SECCIOM, seccion);
                            i.putExtra(Common.ARG_NOMPBREPLAN, planes.getNombre());
                            i.putExtra(Common.ARG_DESCRIPCIONPLAN, planes.getDescripcion());
                            i.putExtra(Common.ARG_IDNPLAN,  String.valueOf(planes.getIdCodigo()));
//                            i.putExtra("planes", (Serializable) planes);
                            context.startActivity(i);

                        }
                    };
                    recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_planes);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    adapter = new PlanesAdapter(planes, listener);
                    recyclerView.setAdapter(adapter);


                }
            }

            @Override
            public void onFailure(Call<PlanesResponse> call, Throwable t) {
                //Log.d("sincronizar Clientes", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


     //   final ActionBar actionBar = getSupportActionBar();
//        if (nombreCliente != null) {
//            if (!nombreCliente.isEmpty()) {
//                actionBar.setTitle(nombreCliente);
//                actionBar.setSubtitle(idCliente);
//            }
//        }


//        progressBar = (ProgressBar)findViewById(R.id.pr_list_activity_productos);
//        progressBar.setVisibility(View.VISIBLE);
//        recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_planes);
//        recyclerView.setVisibility(View.GONE);
//        recyclerView.setLayoutManager(new LinearLayoutManager(context));



//        final ProductosViewModel productosViewModel = ViewModelProviders.of( this).get(ProductosViewModel.class);
//        productosViewModel.setIdUsuario(idUsuario);

//        adapter = new PlanesAdapter(productos, listaEscalas, idCliente, listenerAddProducto);
//        adapter = new PlanesAdapter( this);
//        recyclerView.setAdapter(adapter);

    //    productosViewModel.getListaProductos();


    }

    private void GetData() {
//        listaPlanes = new ArrayList<>();
//        listaPlanes.add(new Planes(1,"nombre","descripcion",true));
//        return listaPlanes;

        PlanesService service = ApiClient.getClient().create(PlanesService.class);
        retrofit2.Call<PlanesResponse> call  = service.Get(true);
        call.enqueue(new Callback<PlanesResponse>() {
            @Override
            public void onResponse(Call<PlanesResponse> call, Response<PlanesResponse> response) {
                if (response.isSuccessful()){
                    PlanesResponse planesResponse = response.body();
                    List<Planes> planes = planesResponse.items;

//                    clientesDao.addClientes(clientes);
                    Toast.makeText(getApplicationContext(), "Base de datos de clientes actualizada", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PlanesResponse> call, Throwable t) {
                //Log.d("sincronizar Clientes", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.producto, menu);
//
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                adapter.getFilter().filter(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String query) {
//                adapter.getFilter().filter(query);
//                return false;
//            }
//        });
//
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_pedidos) {
//            //Intent i = new Intent(this, PedidoActivity.class);
//            //Pequeño cambio de la forma de pantalla por solicitud del cliente
//            Intent i = new Intent(this, PedidoSimpleActivity.class);
//            i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
//            i.putExtra(Common.ARG_SECCIOM, seccion);
//            i.putExtra(Common.ARG_IDCLIENTE, idCliente);
//            i.putExtra(Common.ARG_NOMBRE_CLIENTE, nombreCliente);
//            i.putExtra(Common.ARG_IDPEDIDO, pedidoCliente.getIdLocalPedido());
//            startActivity(i);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


}
