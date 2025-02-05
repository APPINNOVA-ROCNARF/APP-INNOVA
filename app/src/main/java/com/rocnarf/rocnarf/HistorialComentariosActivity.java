package com.rocnarf.rocnarf;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.HistorialComentariosAdapter;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.models.ComentariosClientes;
import com.rocnarf.rocnarf.models.HistorialComentarios;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HistorialComentariosActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, nombreCliente, seccion;
    private ProgressBar progressBar;
    private Context context;
    private HistorialComentarios historialComentarios;
    private List<HistorialComentarios> listaComentario;
    private SearchView searchView;
    private Button btnAddComment;
    private HistorialComentariosAdapter adapter;
    ListView ListViewPlanes;

    private com.rocnarf.rocnarf.dao.DataBaseHelper DataBaseHelper;
    private SQLiteDatabase sQLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_comentarios);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_comentarios);
        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);

        Log.d("menu", "idUsuario ---> load"+idUsuario);

        seccion = i.getStringExtra(Common.ARG_SECCIOM);
        idCliente = i.getStringExtra(Common.ARG_IDCLIENTE);
        //nombreCliente =  i.getStringExtra(Common.ARG_NOMBRE_CLIENTE);
        context = this;

        btnAddComment = (Button) findViewById(R.id.bt_agregar_comentario);

        this.CargaData();

        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HistorialComentariosActivity.this);
                LayoutInflater inflater = HistorialComentariosActivity.this.getLayoutInflater();

                builder.setTitle("Comentario");
                final EditText input = new EditText(HistorialComentariosActivity.this);

                builder.setView(input);
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final String comentarios = input.getText().toString();
                        AlertDialog.Builder builder = new AlertDialog.Builder(HistorialComentariosActivity.this);

                        builder.setMessage("¿Desearía guardar su comentario?")
                                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {

                                        final ComentariosClientes comentariosClientes = new ComentariosClientes();
                                        Log.d("ccc","cc" +idUsuario);

                                        comentariosClientes.setSeccion(seccion);
                                        Log.d("ccc","cc" +comentariosClientes.getSeccion());
                                        comentariosClientes.setIdCliente(idCliente);
                                        comentariosClientes.setComentarios(comentarios);
                                        comentariosClientes.setFecha(new Date());
                                        comentariosClientes.setStatus(true);
                                        comentariosClientes.setUsuario(idUsuario);

                                        PlanesService service = ApiClient.getClient().create(PlanesService.class);
                                        service.PostAddComentarios(comentariosClientes)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new Subscriber<ComentariosClientes>() {
                                                    @Override
                                                    public void onCompleted() {
                                                        CargaData();

                                                        Toast.makeText(getApplicationContext(), "Comentario Guardado con Exito", Toast.LENGTH_LONG).show();
                                                    }

                                                    @Override
                                                    public void onError(Throwable e) {
                                                        Toast.makeText(getApplicationContext(), "Comentario NO Fue Guardado Error", Toast.LENGTH_LONG).show();
                                                    }

                                                    @Override
                                                    public void onNext(ComentariosClientes comentariosClientes) {

                                                    }


                                                });
                                    }
                                })
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // remove the dialog from the screen
                                    }
                                })
                                .show();

                    }
                });


                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                builder.create().show();
            }
        });


    }


     final void CargaData(){
        PlanesService service = ApiClient.getClient().create(PlanesService.class);
         Log.d("menu", "idUsuario ---> idUsuario"+idUsuario);

         Call<HistorialComentariosResponse> call = service.GetComentariosClientes(idUsuario, idCliente);
        call.enqueue(new Callback<HistorialComentariosResponse>() {
            @Override
            public void onResponse(Call<HistorialComentariosResponse> call, Response<HistorialComentariosResponse> response) {
                if (response.isSuccessful()) {
                    HistorialComentariosResponse historialComentariosResponse = response.body();
                    List<HistorialComentarios> historialComentarios = historialComentariosResponse.items;
                    listaComentario = historialComentariosResponse.items;

                    final HistorialComentariosAdapter.HistoralComentariosListener listener = new HistorialComentariosAdapter.HistoralComentariosListener() {
                        @Override
                        public void HistoralComentariosListener(HistorialComentarios historialComentarios) {


                            Intent i = new Intent(context, HistorialProductosPromocionadoActivity.class);
                            i.putExtra(Common.ARG_IDVISITA, String.valueOf(historialComentarios.getId()));
                            i.putExtra(Common.ARG_IDCLIENTE, idCliente);
                            i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                            startActivity(i);

                        }
                    };
                    recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_comentarios);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    adapter = new HistorialComentariosAdapter(listaComentario, listener);
                    recyclerView.setAdapter(adapter);


                }
            }

            @Override
            public void onFailure(Call<HistorialComentariosResponse> call, Throwable t) {
                //Log.d("sincronizar Clientes", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}
