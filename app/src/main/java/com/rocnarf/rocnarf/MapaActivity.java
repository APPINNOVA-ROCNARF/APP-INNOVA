package com.rocnarf.rocnarf;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
///import com.crashlytics.android.Crashlytics;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.models.Clientes;
import com.rocnarf.rocnarf.models.MarkerTag;
import com.rocnarf.rocnarf.repository.ClientesRepository;

import java.util.List;

public class MapaActivity  extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Location mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int DEFAULT_ZOOM = 15;
    private String idUsuario, seccion, idCliente, rol;
    private Context context;
    private static final String TAG = GeoLocalizacionMapsActivity.class.getSimpleName();
    private LatLng mDefaultLocation = new LatLng(-2.159725, -79.889553);
    private String title = "Rocnarf";
    private ClientesRepository clientesRepository;
    public List<Clientes> clientes;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        idUsuario = intent.getStringExtra(Common.ARG_IDUSUARIO);
        seccion =  intent.getStringExtra(Common.ARG_SECCIOM);
        idCliente = intent.getStringExtra(Common.ARG_IDCLIENTE);
        rol=  intent.getStringExtra(Common.ARG_ROL);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        clientesRepository = new ClientesRepository(this, idUsuario);
        clientes = clientesRepository.getClientes(seccion, idUsuario, null, null);

//        Log.d("map","cliente m" + clientes);

    }


    public void onBackPressed(){
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
        i.putExtra(Common.ARG_SECCIOM, seccion);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(mDefaultLocation).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mDefaultLocation));

        getLocationPermission();
        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();
        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        Drawable circleDrawable = getResources().getDrawable(R.drawable.ic_store_green_24dp);

        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);


        Drawable circleDrawable1 = getResources().getDrawable(R.drawable.ic_person_black_24dp);
        BitmapDescriptor markerIcon1 = getMarkerIconFromDrawable(circleDrawable1);

        for (int i=0; i<clientes.size(); i++){

            if (TextUtils.isEmpty(clientes.get(i).getEstadoVisita())){
                circleDrawable = getResources().getDrawable(R.drawable.ic_store_red_24dp);
                markerIcon = getMarkerIconFromDrawable(circleDrawable);
            } else if (clientes.get(i).getEstadoVisita().equals("EFECT")) {
                circleDrawable = getResources().getDrawable(R.drawable.ic_store_green_24dp);
                markerIcon = getMarkerIconFromDrawable(circleDrawable);
            } else {
                circleDrawable = getResources().getDrawable(R.drawable.ic_store_yellow_24dp);
                markerIcon = getMarkerIconFromDrawable(circleDrawable);

            }

            if (TextUtils.isEmpty(clientes.get(i).getEstadoVisita())){
                circleDrawable = getResources().getDrawable(R.drawable.ic_person_red_24dp);
                markerIcon1 = getMarkerIconFromDrawable(circleDrawable);
            } else if (clientes.get(i).getEstadoVisita().equals("EFECT")) {
                circleDrawable = getResources().getDrawable(R.drawable.ic_person_green_24dp);
                markerIcon1 = getMarkerIconFromDrawable(circleDrawable);
            } else {
                circleDrawable = getResources().getDrawable(R.drawable.ic_person_yellow_24dp);
                markerIcon1 = getMarkerIconFromDrawable(circleDrawable);

            }
            if (clientes.get(i).getLatitud() != null && clientes.get(i).getLongitud() != null) {
                Marker marker= mMap.addMarker(new MarkerOptions().position(new LatLng(clientes.get(i).getLatitud(), clientes.get(i).getLongitud()))
                        .snippet("Direccion: " + clientes.get(i).getDireccion())
                        .title(clientes.get(i).getNombreCliente()).icon(clientes.get(i).getOrigen() !=null && clientes.get(i).getOrigen().equals("FARMA")  ? markerIcon : markerIcon1));
                //marker.setTag(clientes.get(i).getIdCliente());

                MarkerTag yourMarkerTag = new MarkerTag();
                yourMarkerTag.setCodigo(clientes.get(i).getIdCliente());
                yourMarkerTag.setEstado(clientes.get(i).getEstadoVisita());
                marker.setTag(yourMarkerTag);

            }


//            mMap.setOnMarkerClickListener( new GoogleMap.OnMarkerClickListener() {
//                @Override
//                public boolean onMarkerClick(Marker clickedMarker) {
//
//                    if (clickedMarker.equals(marker)) {
//                       // Popup.showMarkerOptions(MapFragment.this.getActivity(), marker, radiusCircle);
//                    }
//                    return false;
//                }
//            });

        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            public void onInfoWindowClick(Marker marker)
            {
                MarkerTag yourMarkerTag = ((MarkerTag)marker.getTag());
                Intent in = new Intent(getApplicationContext(), PlanificacionCrearActivity.class);
                in.putExtra(Common.ARG_IDCLIENTE, yourMarkerTag.getCodigo());
                in.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                in.putExtra(Common.ARG_SECCIOM, seccion);
                in.putExtra(Common.ARG_ESTADOVISTA,  yourMarkerTag.getEstado());
                in.putExtra(Common.ARG_ORIGEN_PLANIFICACION_VISITA, Common.VISITA_DESDE_MAPA);
                startActivity(in);

            }
        });
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
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
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            if (mLastKnownLocation != null ) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }else {
                                Toast.makeText(getApplicationContext(), "No se puede acceder a la ubicacion. Habilite la localizacion en el telefono.", Toast.LENGTH_LONG).show();
                                Log.d(TAG, "No se puede acceder a la ubicacion. habilite la localizacion en el telefono.");
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                        //    Crashlytics.logException(task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            //Log.e("Exception: %s", e.getMessage());
           /// Crashlytics.logException(e);
        }
    }

}
