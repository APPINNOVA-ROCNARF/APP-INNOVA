package com.rocnarf.rocnarf.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.FusedLocationProviderClient;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.dao.RocnarfDatabase;
import com.rocnarf.rocnarf.dao.UsuariosDao;
import com.rocnarf.rocnarf.models.AsesorLocation;
import com.rocnarf.rocnarf.models.Usuario;
import com.rocnarf.rocnarf.repository.AsesoresRepository;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LocationService extends Service
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private static final String LOGSERVICE = "#######";

    private String idAsesor;
    private List<AsesorLocation> asesorLocationList;
    private AsesoresRepository asesoresRepository;
    private boolean esPrimero = false;
    private Location lastLocation;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Calendar calendarNow;
    Date dateEnd;

    private static final int PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        buildGoogleApiClient();


        dateEnd = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 20);// for 6 hour
        calendar.set(Calendar.MINUTE, 0);// for 0 min
        calendar.set(Calendar.SECOND, 0);// for 0 sec
        dateEnd = calendar.getTime();
        Log.i(LOGSERVICE, "onCreate");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("Recorrido App Innova")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOGSERVICE, "onStartCommand");

        if (intent != null) {
            Bundle extras = intent.getExtras();
            if(extras != null) {
                idAsesor = (String) extras.get(Common.ARG_IDUSUARIO);
            }
        } else {
            UsuariosDao usuariosDao = RocnarfDatabase.getDatabase(getApplicationContext()).UsuariosDao();
            Usuario usuario = usuariosDao.get();
            if (usuario != null){
                idAsesor = usuario.getIdUsuario();
            }
        }
        if (!idAsesor.isEmpty()) {
            asesoresRepository = new AsesoresRepository(this);
            if (!mGoogleApiClient.isConnected()) {
                esPrimero = true;
                mGoogleApiClient.connect();
            }
        }

        return START_STICKY;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(LOGSERVICE, "onConnected" + bundle);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (l != null) {
            Log.i(LOGSERVICE, "lat " + l.getLatitude());
            Log.i(LOGSERVICE, "lng " + l.getLongitude());

        }

        startLocationUpdate();

    }




    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOGSERVICE, "onConnectionSuspended " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOGSERVICE, "onConnectionFailed ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdate();
    }

    private void initLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10*1000);
        mLocationRequest.setFastestInterval(5*1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }


    LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                onLocationChanged(location);

            }
        };

    };

    private void startLocationUpdate() {
        initLocationRequest();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, mLocationCallback,Looper.myLooper());
        //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }



    public void onLocationChanged(Location location) {
//        Log.i(LOGSERVICE, "lat " + location.getLatitude());
//        Log.i(LOGSERVICE, "lng " + location.getLongitude());
        lastLocation = location;
        AsesorLocation asesorLocation = new AsesorLocation();
        asesorLocation.setFechaRegistro(new Date());
        asesorLocation.setIdAsesor(this.idAsesor);
        asesorLocation.setLatitud(location.getLatitude());
        asesorLocation.setLongitud(location.getLongitude());
        asesorLocation.setInicio(esPrimero);
        asesorLocation.setPendienteSync(false);
        asesoresRepository.add(asesorLocation);
        esPrimero = false;
        Date ahora = Calendar.getInstance().getTime();
        if (ahora.after(dateEnd)) stopLocationUpdate();
    }

    private void stopLocationUpdate() {
//        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        if (lastLocation != null){
            AsesorLocation asesorLocation = new AsesorLocation();
            asesorLocation.setFechaRegistro(new Date());
            asesorLocation.setIdAsesor(this.idAsesor);
            asesorLocation.setLatitud(lastLocation.getLatitude());
            asesorLocation.setLongitud(lastLocation.getLongitude());
            asesorLocation.setInicio(true);
            asesorLocation.setPendienteSync(false);
            asesoresRepository.add(asesorLocation);
            esPrimero = true;

        }
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(mLocationCallback);

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }


}
