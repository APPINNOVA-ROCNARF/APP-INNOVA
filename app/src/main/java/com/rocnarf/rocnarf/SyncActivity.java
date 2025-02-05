package com.rocnarf.rocnarf;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.viewmodel.SyncViewModel;

public class SyncActivity extends AppCompatActivity {
    private SyncViewModel syncViewModel;
    private Button mClientes, mVisitas, mProducto,mDataButtom;
    private String idAsesor, seccion, rolUsuario;
    private ProgressBar pgsBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        idAsesor = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion =  i.getStringExtra(Common.ARG_SECCIOM);
        rolUsuario = i.getStringExtra(Common.ARG_ROL);

        pgsBar = (ProgressBar) findViewById(R.id.pBar);

//        Log.d("RocnarfDatabase", "populating with data... rol" + rolUsuario);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        syncViewModel = ViewModelProviders.of(this).get(SyncViewModel.class);
        syncViewModel.estadoCliente.observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        if (s!= null) {
                            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                        }
                    }
                });

        syncViewModel.estadoProductos.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s!= null) {
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                }
            }
        });

//        mClientes = (Button)findViewById(R.id.bt_clientes_activity_sync);
//        mClientes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                syncViewModel.SincronizarClientes(idAsesor, seccion, rolUsuario);
//            }
//        });
//        mVisitas = (Button)findViewById(R.id.bt_visitas_activity_sync);
//        mVisitas.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                syncViewModel.SincronizarVisitas(idAsesor );
//            }
//        });
//        mProducto = (Button)findViewById(R.id.bt_producto_activity_sync);
//        mProducto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                syncViewModel.SincronizarProductos(idAsesor);
//                syncViewModel.SincronizarEscalasBonificacion(idAsesor);
//            }
//        });


        mDataButtom = (Button)findViewById(R.id.bt_sync_data);
        mDataButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                if(!isInternet()){
                    Toast.makeText(getApplicationContext(),"Actualmente usted no posee conexión a internet para sincronizar los datos, por favor intente luego.",Toast.LENGTH_LONG).show();
                    return;
                }

                new android.os.Handler(Looper.getMainLooper()).postDelayed(
                        new Runnable() {
                            public void run() {
                                Log.d("RocnarfDatabase", "1" + rolUsuario);

                                pgsBar.setVisibility(View.VISIBLE);
                                syncViewModel.SincronizarClientes(idAsesor, seccion, rolUsuario);

                            }
                        },
                        00000);

                new android.os.Handler(Looper.getMainLooper()).postDelayed(
                        new Runnable() {
                            public void run() {
                                syncViewModel.SincronizarVisitas(idAsesor );

                            }
                        },
                        7000);

                new android.os.Handler(Looper.getMainLooper()).postDelayed(
                        new Runnable() {
                            public void run() {
                                syncViewModel.SincronizarProductos(idAsesor);
                                syncViewModel.SincronizarEscalasBonificacion(idAsesor);
                                pgsBar.setVisibility(View.GONE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            }
                        },
                        12000);

            }
        });

    }

    boolean isInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo!=null){
            if (networkInfo.isConnected()){
                return true;
            }else  {
                return false;
            }
        }else
            return false;
    }

    public void yourmethod(){
        Log.d("RocnarfDatabase", "populating with data... rol" );
        try {

                    //TimeUnit.SECONDS.sleep(7);
                    Thread.sleep(10000);
                    //pgsBar.setVisibility(view.GONE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

    }

}
