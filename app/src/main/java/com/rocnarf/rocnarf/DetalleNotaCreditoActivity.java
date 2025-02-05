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
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.rocnarf.rocnarf.adapters.DetalleNcAdapter;
import com.rocnarf.rocnarf.models.DetalleNotaCredito;
import com.rocnarf.rocnarf.models.Planes;
import com.rocnarf.rocnarf.models.Politicas;

import java.io.Serializable;
import java.util.List;

public class DetalleNotaCreditoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, nombreCliente, seccion;
    private ProgressBar progressBar;
    private Context context;
    private Planes planes;
    private List<Politicas> listaPoliticas;
    private SearchView searchView;
    private DetalleNcAdapter adapter ;
    ListView ListViewPlanes;
    private boolean expadir;
    private com.rocnarf.rocnarf.dao.DataBaseHelper DataBaseHelper;
    private SQLiteDatabase sQLiteDatabase;
    SwipeRefreshLayout swipeRefreshLayout;
    private List<DetalleNotaCredito> detalleNotaCreditos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nc_detalle);
        Intent i = getIntent();

        Serializable detalleNotaCredito =  i.getSerializableExtra("detalleNotaCredito");
        recyclerView = (RecyclerView) findViewById(R.id.item_nc);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new DetalleNcAdapter(this, (List<DetalleNotaCredito>) detalleNotaCredito);
        recyclerView.setAdapter(adapter);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);


    }





}
