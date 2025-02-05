package com.rocnarf.rocnarf;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.view.ContextThemeWrapper;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.services.LocationService;

public class RecorridoActivity extends AppCompatActivity {
    private String seccion, idUsuario;
    private Context context;
    private Button btnInicio, btnFin;
    private AlertDialog.Builder builderIniciar, builderFin;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorrido);

        context = this;

        Intent i = getIntent();
        seccion = i.getStringExtra(Common.ARG_IDCLIENTE);
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
        i.putExtra(Common.ARG_SECCIOM, seccion);

        btnInicio = (Button)findViewById(R.id.bt_iniciar_recorrido_activity);
        btnFin = (Button)findViewById(R.id.bt_fin_recorrido_activity);

        btnInicio.setEnabled(false);
        btnFin.setEnabled(false);

        verificarLocationServices();

        getLocationPermission();

        // builder inicio de recorrido
        builderIniciar = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        builderIniciar.setMessage("Deseas inicar el recorrido");
        builderIniciar.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(context, LocationService.class);
                i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                startService(i);
                Toast.makeText(context, "El recorrido se ha iniciado con exito", Toast.LENGTH_LONG).show();
            }
        });

        builderIniciar.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });



        btnInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builderIniciar.create().show();
            }
        });



        ///////////////////////////////////////////////////////////////////////////
        // builder fin de recorrido
        builderFin = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        builderFin.setMessage("Deseas finalizar recorrido");
        builderFin.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intentService = new Intent(context, LocationService.class);
                stopService(intentService);
                Toast.makeText(context, "El recorrido se ha detenido con exito", Toast.LENGTH_LONG).show();
            }
        });
        builderFin.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        btnFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builderFin.create().show();
            }
        });
    }


    private void verificarLocationServices(){
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            new AlertDialog.Builder(context)
                    .setMessage("El Servicio de Localzacion del Telefono esta desactivado. Deseas Activarlo?")
                    .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(i);
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Aquí puedes realizar otras acciones si es necesario antes de finalizar la actividad
                            finish(); // Finalizar la actividad
                        }
                    })
                    .show();
        }

    }


    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            btnInicio.setEnabled(true);
            btnFin.setEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    btnInicio.setEnabled(true);
                    btnFin.setEnabled(true);
                }
            }
        }
    }

}
