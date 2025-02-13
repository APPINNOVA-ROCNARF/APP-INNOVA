package com.rocnarf.rocnarf;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.ClienteService;
import com.rocnarf.rocnarf.api.RutasService;
import com.rocnarf.rocnarf.dao.RocnarfDatabase;
import com.rocnarf.rocnarf.dao.UsuariosDao;
import com.rocnarf.rocnarf.models.Constantes;
import com.rocnarf.rocnarf.models.FcmTokenDivice;
import com.rocnarf.rocnarf.models.Rutas;
import com.rocnarf.rocnarf.models.Usuario;
import com.rocnarf.rocnarf.models.VisitaClientes;
import com.rocnarf.rocnarf.services.LocationService;
import com.rocnarf.rocnarf.viewmodel.PlanificacionViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import rx.Completable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PlanificacionFragment.OnListFragmentInteractionListener {

    private PlanificacionViewModel planificacionViewModel;
    private String idAsesor, seccion, nombreUsuario, rolUsuario;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        Intent i = getIntent();
        idAsesor = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion =  i.getStringExtra(Common.ARG_SECCIOM);
        nombreUsuario = i.getStringExtra(Common.ARG_NOMBREUSUARIO);
        rolUsuario = i.getStringExtra(Common.ARG_ROL);
        // Si la actividad es le buscan lamada de una actividad que no contienen los datos de Usuario sen la ase de datos
        if (idAsesor == null){
            UsuariosDao usuariosDao = RocnarfDatabase.getDatabase(getApplicationContext()).UsuariosDao();
            Usuario usuario = usuariosDao.get();
            if (usuario != null){
                idAsesor = usuario.getIdUsuario();
                seccion =  usuario.getSeccion();
                nombreUsuario = usuario.getNombre();
                rolUsuario = usuario.getRol();
            }else {
                // Si pr alguna razon no esta logoneado se los devuelve a la panalla de login
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }

        verificarLocationServices();
        getLocationPermission();
        Log.d("neoti","antes"  );

        //registrarDispositivo();
        Log.d("neoti","despues"  );

        toolbar.setSubtitle(seccion + " - " + nombreUsuario);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                //Cambio se llama a la pantalal de busqueda total
                //Intent i  = new Intent( getApplicationContext(), ClientesCriteriosActivity.class );
                Intent i  = new Intent( getApplicationContext(), ResultadoClientesSemanalActivity.class );
                i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
                i.putExtra(Common.ARG_SECCIOM, seccion);
                i.putExtra(Common.ARG_ROL, rolUsuario);
                startActivity(i);
            }
        });


        planificacionViewModel = ViewModelProviders.of(this).get(PlanificacionViewModel.class);
        planificacionViewModel.SincronizarVisitas(idAsesor).subscribe(new Completable.CompletableSubscriber() {
            @Override
            public void onCompleted() {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                PlanificacionFragment planificacion = PlanificacionFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putString(Common.ARG_IDUSUARIO, idAsesor);
                bundle.putString(Common.ARG_SECCIOM, seccion);
                bundle.putString(Common.ARG_ROL, rolUsuario);
                planificacion.setArguments(bundle);
                ft.replace(R.id.fl_fragment_activity_main, planificacion);
                ft.commit();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSubscribe(Subscription d) {

            }
        });
        planificacionViewModel.SincronizarUbicaciones(idAsesor);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_geolocalizacion) {
            Intent i = new Intent(this, GeoLocalizacionActivity.class);
            i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
            i.putExtra(Common.ARG_SECCIOM, seccion);
            i.putExtra(Common.ARG_ROL, rolUsuario);
            startActivity(i);
        } else if (id == R.id.nav_sync) {
            Intent i = new Intent(this, SyncActivity.class);
            i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
            i.putExtra(Common.ARG_SECCIOM, seccion);
            i.putExtra(Common.ARG_ROL, rolUsuario);
            startActivity(i);

            //AutenticationRepository.Autenticar();
        } else if (id == R.id.nav_pedidos) {
            Intent i = new Intent(this, PedidoListaActivity.class);
            i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
            i.putExtra(Common.ARG_SECCIOM, seccion);
            i.putExtra(Common.ARG_ROL, rolUsuario);
            startActivity(i);
//        } else if (id == R.id.nav_cobros) {
//            Intent i = new Intent(this, ClientesFacturasActivity.class);
//            i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
//            i.putExtra(Common.ARG_SECCIOM, seccion);
//            i.putExtra(Common.ARG_IDCLIENTE, "801107");
//            i.putExtra(Common.ARG_FACTURAS_SELECCION, true);
//            startActivity(i);
        } else if (id == R.id.nav_productos) {
            Intent i = new Intent(this, ProductosActivity.class);
            i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
            i.putExtra(Common.ARG_SECCIOM, seccion);
            i.putExtra(Common.ARG_ROL, rolUsuario);
            startActivity(i);
        } else if (id == R.id.nav_escala) {
            Intent i = new Intent(this, EscalaBonificacionActivity.class);
            i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
            i.putExtra(Common.ARG_SECCIOM, seccion);
            i.putExtra(Common.ARG_ROL, rolUsuario);
            startActivity(i);
        } else if (id == R.id.nav_planes) {
            Intent i = new Intent(this, PlanesActivity.class);
            i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
            i.putExtra(Common.ARG_SECCIOM, seccion);
            i.putExtra(Common.ARG_ROL, rolUsuario);
            startActivity(i);
        } else if (id == R.id.nav_solicitudPremio) {
            Intent i = new Intent(this, ObsequiosActivity.class);
            i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
            i.putExtra(Common.ARG_SECCIOM, seccion);
            i.putExtra(Common.ARG_ROL, rolUsuario);
            startActivity(i);

        } else if (id == R.id.nav_parrilla) {
            Intent i = new Intent(this, ParrillaActivity.class);
            i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
            i.putExtra(Common.ARG_SECCIOM, seccion);
            i.putExtra(Common.ARG_ROL, rolUsuario);
            startActivity(i);
        } else if (id == R.id.nav_guias_productos) {
            Intent i = new Intent(this, GuiasProductosActivity.class);
            i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
            i.putExtra(Common.ARG_SECCIOM, seccion);
            i.putExtra(Common.ARG_ROL, rolUsuario);
            startActivity(i);
        } else if (id == R.id.nav_preguntas) {
                Intent i = new Intent(this, PreguntasFrecuentesActivity.class);
                i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
                i.putExtra(Common.ARG_SECCIOM, seccion);
                i.putExtra(Common.ARG_ROL, rolUsuario);
                startActivity(i);

        } else if (id == R.id.nav_catera) {
            Intent i = new Intent(this, CarteraClienteActivity.class);
            i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
            i.putExtra(Common.ARG_SECCIOM, seccion);
            i.putExtra(Common.ARG_ROL, rolUsuario);
            startActivity(i);

        } else if (id == R.id.nav_cumpleanyos) {
            Intent i = new Intent(this, CumpleanyosActivity.class);
            i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
            i.putExtra(Common.ARG_SECCIOM, seccion);
            i.putExtra(Common.ARG_ROL, rolUsuario);
            startActivity(i);
        } else if (id == R.id.nav_mapas) {
            Intent i = new Intent(this, MapaActivity.class);
            i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
            i.putExtra(Common.ARG_SECCIOM, seccion);
            i.putExtra(Common.ARG_ROL, rolUsuario);
            startActivity(i);
        } else if (id == R.id.nav_rutas) {
            Intent i = new Intent(this, RutasActivity.class);
            i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
            i.putExtra(Common.ARG_SECCIOM, seccion);
            i.putExtra(Common.ARG_ROL, rolUsuario);
            startActivity(i);

        } else if (id == R.id.nav_politicas) {
            Intent i = new Intent(this, PoliticasActivity.class);
            i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
            i.putExtra(Common.ARG_SECCIOM, seccion);
            i.putExtra(Common.ARG_ROL, rolUsuario);
            startActivity(i);
        } else if (id == R.id.nav_pedidos_pendiente) {
            Intent i = new Intent(this, PedidosPendienteActivity.class);
            i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
            i.putExtra(Common.ARG_SECCIOM, seccion);
            i.putExtra(Common.ARG_ROL, rolUsuario);
            startActivity(i);
        }else if (id == R.id.nav_quejas) {
                Intent i = new Intent(this, QuejaActivity.class);
                i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
                i.putExtra(Common.ARG_SECCIOM, seccion);
                i.putExtra(Common.ARG_ROL, rolUsuario);
                startActivity(i);
        }
                else if (id == R.id.nav_reporte_ventas) {
            Intent i = new Intent(this, ReporteVentasActivity.class);
            i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
            i.putExtra(Common.ARG_SECCIOM, seccion);
            i.putExtra(Common.ARG_ROL, rolUsuario);
            startActivity(i);
        }  else if (id == R.id.nav_tiempo_entrega) {
            Intent i = new Intent(this, TiempoEntregaActivity.class);
            i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
            i.putExtra(Common.ARG_SECCIOM, seccion);
            i.putExtra(Common.ARG_ROL, rolUsuario);
            startActivity(i);
        }  else if (id == R.id.nav_estadistica) {
            Intent i = new Intent(this, EstadisticaActivity.class);
            i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
            i.putExtra(Common.ARG_SECCIOM, seccion);
            i.putExtra(Common.ARG_ROL, rolUsuario);
            startActivity(i);
//        }else if (id == R.id.nuevo_viatico) {
//                Intent i = new Intent(this, ViaticoActivity.class);
//                i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
//                i.putExtra(Common.ARG_SECCIOM, seccion);
//                i.putExtra(Common.ARG_ROL, rolUsuario);
//                startActivity(i);
//        }else if (id == R.id.historial_viatico) {
//            Intent i = new Intent(this, HistorialViaticoActivity.class);
//            i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
//            i.putExtra(Common.ARG_SECCIOM, seccion);
//            i.putExtra(Common.ARG_ROL, rolUsuario);
            startActivity(i);
        }else if (id == R.id.nav_viatico_principal) {
            context = this;
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.bottomsheetlayout);
            LinearLayout nuevoRegistro = dialog.findViewById(R.id.layoutVideo);
            //LinearLayout shortsLayout = dialog.findViewById(R.id.layoutShorts);
            LinearLayout historial = dialog.findViewById(R.id.layoutLive);
            ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

            nuevoRegistro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent i = new Intent(context, ViaticoActivity.class);
                    i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
                    i.putExtra(Common.ARG_SECCIOM, seccion);
                    i.putExtra(Common.ARG_ROL, rolUsuario);
                    startActivity(i);

                }
            });

            historial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                    Intent i = new Intent(context, HistorialViaticoActivity.class);
                    i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
                    i.putExtra(Common.ARG_SECCIOM, seccion);
                    i.putExtra(Common.ARG_ROL, rolUsuario);
                    startActivity(i);

                }
            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.BOTTOM);

//        } else if (id == R.id.nav_track) {
//
//            Intent i = new Intent(this, RecorridoActivity.class);
//            i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
//            i.putExtra(Common.ARG_SECCIOM, seccion);
//            startActivity(i);


//        } else if (id == R.id.nav_impulsadoras) {
//            Intent i = new Intent(this, VisitasImpulsadorasActivity.class);
//            i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
//            i.putExtra(Common.ARG_SECCIOM, seccion);
//            startActivity(i);
        } else if (id == R.id.nav_version) {

            Intent i = new Intent(this, VersionActivity.class);
            startActivity(i);


        }else if (id == R.id.nav_cliente) {
            //Intent i = new Intent(this, ClientesCriteriosSemanalActivity.class);
            Intent i = new Intent(this, ResultadoClientesSemanalActivity.class);
            i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
            i.putExtra(Common.ARG_SECCIOM, seccion);
            i.putExtra(Common.ARG_ROL, rolUsuario);
            startActivity(i);

        }else if (id == R.id.nav_salir) {

            // Se detiene el seguimiento del asesor
            Intent intentService = new Intent(this, LocationService.class);
            stopService(intentService);

            UsuariosDao usuariosDao = RocnarfDatabase.getDatabase(getApplicationContext()).UsuariosDao();
            usuariosDao.deleteAll();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onListFragmentInteraction(VisitaClientes visitaClientes) {
        Intent i = new Intent(getApplicationContext(), ResultadoVisitaActivity.class);
        i.putExtra(Common.ARG_IDCLIENTE, visitaClientes.getCodigoCliente());
        i.putExtra(Common.ARG_IDVISITALOCAL, visitaClientes.getIdLocal());
        i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
        i.putExtra(Common.ARG_SECCIOM, seccion);
        i.putExtra(Common.ARG_ROL, rolUsuario);
        i.putExtra(Common.ARG_NOMBRE_CLIENTE, visitaClientes.getNombreCliente());
        startActivity(i);
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
            IniciarRecorrido();

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

                    IniciarRecorrido();
                }else{
                    Log.d("ssss","sin permiso");
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.myDialog));
                    LayoutInflater inflater = getLayoutInflater();
                    builder.setCancelable(false);
                    builder.setTitle("Advertencia AppInnova");
                    builder.setMessage("Active GPS");

                    builder.setPositiveButton("De acuerdo", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.create().show();

                }
            }
        }
    }

    private void IniciarRecorrido() {
        Date dateEnd = new Date();
        Calendar calendar = Calendar.getInstance();
        Date ahora = Calendar.getInstance().getTime();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.SUNDAY) return;

        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 20);// for 6 hour
        calendar.set(Calendar.MINUTE, 0);// for 0 min
        calendar.set(Calendar.SECOND, 0);// for 0 sec
        dateEnd = calendar.getTime();

        if (ahora.before(dateEnd)) {
            Intent i = new Intent(context, LocationService.class);
            i.putExtra(Common.ARG_IDUSUARIO, idAsesor);
            startService(i);
            //Toast.makeText(context, "En recorrido ", Toast.LENGTH_SHORT).show();
        }
    }

    public void registrarDispositivo(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(ContentValues.TAG,
                                    "Fetching FCM registration token failed xxxxxxxx",
                                    task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        String tokenGuardado = getSharedPreferences(Constantes.SP_FILE,0)
                                .getString(Constantes.SP_KEY_DEVICEID,null);
                        Log.d("neoti","token" +token );
                        Log.d("neoti","tokenGuardado" +tokenGuardado );

                        if(token != null){

                            if (tokenGuardado == null || !token.equals(tokenGuardado)){
                                MainActivity.this.getSharedPreferences(Constantes.SP_FILE,0).edit()
                                        .putString(Constantes.SP_KEY_DEVICEID, token).commit();
                                final FcmTokenDivice fcmtoken = new FcmTokenDivice();
                                fcmtoken.setToken(token);
                                fcmtoken.setIdUsuario(idAsesor);
                                ClienteService service = ApiClient.getClient().create(ClienteService.class);
                                service.addToken(fcmtoken)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Subscriber<FcmTokenDivice>() {
                                            @Override
                                            public void onCompleted() {
                                          //      Toast.makeText(getApplicationContext(), "Token Guardada ", Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                        //        Toast.makeText(getApplicationContext(), "Token No Fue Generada", Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onNext(FcmTokenDivice fcmTokenDivice) {

                                            }


                                        });
                            }
                        }

//                        Toast.makeText(MainActivity.this, token,
//                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
