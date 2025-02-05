package com.rocnarf.rocnarf;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.ObsequiosAdapter;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.models.LiquidacionObsequio;
import com.rocnarf.rocnarf.models.LiquidacionObsequioResponse;
import com.rocnarf.rocnarf.models.Planes;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ObsequiosActivity extends AppCompatActivity implements Serializable  {
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, nombreCliente, seccion;
    private ProgressBar progressBar;
    private Context context;
    private Planes planes;
    private List<LiquidacionObsequio> listaObsequio;
    private SearchView searchView;
    private ObsequiosAdapter adapter ;
    ListView ListViewPlanes;

    private com.rocnarf.rocnarf.dao.DataBaseHelper DataBaseHelper;
    private SQLiteDatabase sQLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obsequios);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_obsequios);
        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion =  i.getStringExtra(Common.ARG_SECCIOM);
        //idCliente =  i.getStringExtra(Common.ARG_IDCLIENTE);
        //nombreCliente =  i.getStringExtra(Common.ARG_NOMBRE_CLIENTE);
        context =this;
        PlanesService service = ApiClient.getClient().create(PlanesService.class);
        Call<LiquidacionObsequioResponse> call  = service.GetObsequios(idUsuario);
        //Call<HistorialComentariosResponse> call  = service.GetComentarios(idCliente,"EFECT");
        call.enqueue(new Callback<LiquidacionObsequioResponse>() {
            @Override
            public void onResponse(Call<LiquidacionObsequioResponse> call, Response<LiquidacionObsequioResponse> response) {
                if (response.isSuccessful()){
                    LiquidacionObsequioResponse liquidacionObsequioResponse = response.body();
                    List<LiquidacionObsequio> liquidacionObsequio = liquidacionObsequioResponse.items;
                    listaObsequio =  liquidacionObsequioResponse.items;

                    final ObsequiosAdapter.ObsequiosListener listener =  new ObsequiosAdapter.ObsequiosListener() {
                        @Override
                        public void ObsequiosListener(LiquidacionObsequio liquidacionObsequio) {

                            if (liquidacionObsequio.getEstadoSolicitud().equals("P")){
                            Intent i = new Intent(context, PlanesCreaObsequioActivity.class);
                            i.putExtra(Common.ARG_IDCLIENTE, idCliente);
                            i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                            i.putExtra(Common.ARG_SECCIOM, seccion);
                            i.putExtra(Common.ARG_NOMPBREPLAN, liquidacionObsequio.getNombrePlan());
                            i.putExtra(Common.ARG_DESCRIPCIONPLAN, liquidacionObsequio.getNombrePlan());
                            i.putExtra(Common.ARG_IDNPLAN,  String.valueOf(liquidacionObsequio.getIdPlan()));

                            i.putExtra("LiquidacionObsequio", liquidacionObsequio);
//                            i.putExtra("planes", (Serializable) planes);
//                            context.startActivity(i);
//                                scontext.startActivityForResult(i);Ä
                                startActivityForResult(i, 1);
                            }else {
                                Toast.makeText(getApplicationContext(), "Solicitud de Premio ya Fue Generada", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                    };
//
                    recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_obsequios);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    adapter = new ObsequiosAdapter(liquidacionObsequio, listener);
                    recyclerView.setAdapter(adapter);


                }
            }

            @Override
            public void onFailure(Call<LiquidacionObsequioResponse> call, Throwable t) {
                //Log.d("sincronizar Clientes", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }
    @Override public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){

            finish();
            startActivity(getIntent());

        } else if (resultCode == RESULT_CANCELED) {

            finish();
            startActivity(getIntent());
        }
    }

}
