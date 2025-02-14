package com.rocnarf.rocnarf;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.api.VisitaClientesService;
import com.rocnarf.rocnarf.models.Clientes;
import com.rocnarf.rocnarf.models.Parametros;
import com.rocnarf.rocnarf.models.ParametrosResponse;
import com.rocnarf.rocnarf.models.Promocionado;
import com.rocnarf.rocnarf.models.VisitaClientes;
import com.rocnarf.rocnarf.viewmodel.ResultadoVisitaViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ResultadoVisitaActivity extends AppCompatActivity
        implements ClienteDetalleFragment.OnFragmentInteractionListener{
    private static final String TAG = ResultadoVisitaActivity.class.getSimpleName();
    private ResultadoVisitaViewModel resultadoVisitaViewModel;
    private TextView mVallorF2, mValorF3, mValorF4, mValorGEN, mCobro, mMotivo, mDistancia, mPlanificacion , mAcompanadoText;
    private Button mRegistrar, mEliminar, mReplanificar, mPedido;
    private CheckBox mPedidoEfectivo, mCobroEfectivo, mVisitaPromocional, mSiembraProducto, mCajasVacias, mEntregaPremios, mDevolucion, mVisitaDocEfec, mVisitaSinGestion, mPuntoContacto, mReVisita;
    private LinearLayout opcionesVisitaFarmcaia;
    private EditText mObservacion;
    private EditText mQueja;
    private CheckBox mEfectiva;
    private CheckBox mAcompanado;
    public static final String ACOMPANADO ="";
    private String codigoCliente, idUsuario, seccion, nombreCliente;
    private  int idLocal, diasAjuste;
    private Context context;
    private String origenCliente;
    private Integer revisita;
    private List<Promocionado> listaPro = new ArrayList<>();
    private Date ultimafechavalida;

    private Promocionado promocionadoAdd;

    private VisitaClientes visitaPlanificada;

    private boolean mLocationPermissionGranted;
    private LocationManager milocManager;
    private LocationListener milocListener;
    private Double latitudActual, longitudActual, latitud, longitud ;


    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location  mLastKnownLocation;
    private Location locCliente;
    private LocationCallback locationCallback;
    LinearLayout layoutList;
    Button buttonAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_resultado_visita);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;

        Intent intent = getIntent();
        codigoCliente = intent.getStringExtra(Common.ARG_IDCLIENTE);
        idLocal = intent.getIntExtra(Common.ARG_IDVISITALOCAL, 0);
        idUsuario = intent.getStringExtra(Common.ARG_IDUSUARIO);
        seccion = intent.getStringExtra(Common.ARG_SECCIOM);
        nombreCliente = intent.getStringExtra(Common.ARG_NOMBRE_CLIENTE);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ClienteDetalleFragment detalleCliente = ClienteDetalleFragment.newInstance(codigoCliente, idUsuario);
        ft.replace(R.id.fm_cliente_content_resultado_visita, detalleCliente);
        ft.commit();

        verificarLocationServices();
        opcionesVisitaFarmcaia = (LinearLayout)findViewById(R.id.ll_visita_farmacia_content_resultado_visita);
        mPlanificacion = (TextView) findViewById(R.id.tv_planificacion_content_resultado_visita);
        mAcompanadoText = (TextView) findViewById(R.id.tv_acompanado);
        mVallorF2 = (TextView) findViewById(R.id.tv_valorF2_content_resultado_visita);
        mValorF3 = (TextView) findViewById(R.id.tv_valorF3_content_resultado_visita);
        mValorF4 = (TextView) findViewById(R.id.tv_valorF4_content_resultado_visita);
        mValorGEN = (TextView) findViewById(R.id.tv_valorGEN_content_resultado_visita);
        mCobro = (TextView) findViewById(R.id.tv_valor_cobro_content_resultado_visita);
        mMotivo = (TextView) findViewById(R.id.tv_motivos_content_resultado_visita);
        mDistancia = (TextView) findViewById(R.id.tv_distancia_content_resultado_visita);
        mEfectiva = (CheckBox) findViewById(R.id.cb_efectiva_content_resultado_visita);
        mObservacion = (EditText) findViewById(R.id.et_observacion_content_resultado_visita);
        mAcompanado = (CheckBox) findViewById(R.id.cb_efectiva_content_resultado_acompanado);
        mQueja = (EditText) findViewById(R.id.et_queja_content_resultado_visita);
        mRegistrar = (Button) findViewById(R.id.bt_registrar_content_resultado_visita);
        mEliminar = (Button) findViewById(R.id.bt_eliminar_content_resultado_visita);
        mReplanificar = (Button) findViewById(R.id.bt_replanificar_content_resultado_visita);
        mPedido = (Button) findViewById(R.id.bt_pedido_content_resultado_visita);

        layoutList = findViewById(R.id.layout_list);
        buttonAdd =  findViewById(R.id.button_add);
        mVisitaSinGestion = (CheckBox) findViewById(R.id.cb_visita_sin_gestion);
        mReVisita = (CheckBox) findViewById(R.id.cb_cobro_dialog_re_visita);
        mReVisita.setVisibility(View.GONE);
        mRegistrar.setEnabled(false);
        mRegistrar.setVisibility(View.GONE);
        mEliminar.setVisibility(View.GONE);
        mReplanificar.setVisibility(View.GONE);
        mPedido.setVisibility(View.GONE);

        mPuntoContacto = (CheckBox) findViewById(R.id.cb_cobro_dialog_punto_contacto);

        CargarDias();

        mRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (locCliente == null) {
                        Toast.makeText(context, "No se ha podido establecer la ubicacion del cliente", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (latitudActual == null){
                        Toast.makeText(context, "No se ha podido establecer la ubicacion actual", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (latitudActual == 0){
                        Toast.makeText(context, "No se ha podido establecer la ubicacion actual", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (mObservacion.getText().toString().isEmpty()){
                        Toast.makeText(context, "Campo comentario obligatorio", Toast.LENGTH_LONG).show();
                        return;
                    }
                    checkIfValidAndRead();

                    visitaPlanificada.setLatitud(latitudActual);
                    visitaPlanificada.setLongitud(longitudActual);
                    visitaPlanificada.setFechaVisita(new Date());
                    visitaPlanificada.setFechaModificacion(new Date());

                    if (Objects.equals(revisita, 1)) { // Cliente que requiere revisita
                        if (!mReVisita.isChecked()) {
                            // Primera visita, se marca como EFECTIVA directamente
                            visitaPlanificada.setEstado(VisitaClientes.PEFECTIVA);
                        } else {
                            // Segunda visita (re-visita), simplemente marcar como efectiva si el checkbox está marcado
                            visitaPlanificada.setEstado(VisitaClientes.EFECTIVA);
                        }
                    } else {
                        // Lógica normal para clientes sin re-visita
                        visitaPlanificada.setEstado(mEfectiva.isChecked() ? VisitaClientes.EFECTIVA : VisitaClientes.VISITADO);
                    }


                    visitaPlanificada.setObservacion(mObservacion.getText().toString());
                    visitaPlanificada.setQueja(mQueja.getText().toString());
                    visitaPlanificada.setPendienteSync(false);
                    visitaPlanificada.setVisitaReVisita(mReVisita.isChecked());
                    visitaPlanificada.setVisitaAcompanado(mAcompanado.isChecked());
                    visitaPlanificada.setAcompanate(mAcompanado.isChecked() ? mAcompanadoText.getText().toString() : null);
                    visitaPlanificada.setVisitaPuntoContacto(mPuntoContacto.isChecked());
                    visitaPlanificada.setVisitaReVisita(mReVisita.isChecked());

                    if (origenCliente.equals("FARMA")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.dialog_resultado_visita, null);


                        mCajasVacias = (CheckBox) dialogView.findViewById(R.id.cb_cajas_vacias_dialog_resultado_visita);
                        mSiembraProducto = (CheckBox) dialogView.findViewById(R.id.cb_siembra_producto_dialog_resultado_visita);
                        mVisitaPromocional = (CheckBox) dialogView.findViewById(R.id.cb_visita_promocional_dialog_resultado_visita);
                        mEntregaPremios = (CheckBox) dialogView.findViewById(R.id.cb_entrega_premios_dialog_resultado_visita);
                        mDevolucion = (CheckBox) dialogView.findViewById(R.id.cb_devolucion_dialog_resultado_visita);
                        mPedidoEfectivo = (CheckBox) dialogView.findViewById(R.id.cb_pedido_dialog_resultado_visita);
                        mCobroEfectivo = (CheckBox) dialogView.findViewById(R.id.cb_cobro_dialog_resultado_visita);
//                        mReVisita = (CheckBox) dialogView.findViewById(R.id.cb_cobro_dialog_re_visita);


                        builder.setTitle("Visita Efectiva");
                        builder.setView(dialogView);
                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


//                            Location locActual = new Location("");
//                            locActual.setLatitude(latitudActual);
//                            locActual.setLongitude(longitudActual);

//                            if (locActual.distanceTo(locCliente) > 20) {
//                                Toast.makeText(context, "No estas en la ubicacion del cliente", Toast.LENGTH_LONG).show();
//                                return;
//                            }

                                visitaPlanificada.setEfectivaXCajasVacias(mCajasVacias.isChecked());
                                visitaPlanificada.setEfectivaXSiembraProducto(mSiembraProducto.isChecked());
                                visitaPlanificada.setEfectivaPromocional(origenCliente.equals("FARMA") ? mVisitaPromocional.isChecked() : true);
                                visitaPlanificada.setEfectivaXEntregaPremios(mEntregaPremios.isChecked());
                                visitaPlanificada.setEfectivaXDevolucion(mDevolucion.isChecked());
                                visitaPlanificada.setEfectivaXPedido(mPedidoEfectivo.isChecked());
                                visitaPlanificada.setVisitaSinGestion(mVisitaSinGestion.isChecked());
                                visitaPlanificada.setEfectivaXCobro(mCobroEfectivo.isChecked());
                                resultadoVisitaViewModel.update(visitaPlanificada);
                                Toast.makeText(getApplicationContext(), "Registro de la visita guardado", Toast.LENGTH_LONG).show();
                            }
                        });
                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                        builder.create().show();
                    } else if(origenCliente.equals("MEDICO")) {
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.dialog_resultado_visita_doctores, null);
                        mVisitaDocEfec = (CheckBox) dialogView.findViewById(R.id.cb_visita_promocional);

                        final AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Actividades")
                        .setPositiveButton("Aceptar",null)
                        .setNegativeButton("Cancelar",null)
                        .setView(dialogView)
                        .show();

                        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positiveButton.setOnClickListener(new View.OnClickListener(){
                            public void onClick(View v){
                                // &&  mVisitaSinGestion.isChecked()
//                                if (mVisitaDocEfec.isChecked()) {
//                                    Toast.makeText(context, "Solo se debe seleccionar una opcion", Toast.LENGTH_LONG).show();
//                                    return;
//                                }
                                //&&  !mVisitaSinGestion.isChecked()
                                if (!mVisitaDocEfec.isChecked() ) {
                                    Toast.makeText(context, "Seleccionar una opcion", Toast.LENGTH_LONG).show();
                                    return;
                                }

                                visitaPlanificada.setVisitaDocEfec(mVisitaDocEfec.isChecked());
                                visitaPlanificada.setVisitaSinGestion(mVisitaSinGestion.isChecked());
                                resultadoVisitaViewModel.update(visitaPlanificada);
                                Toast.makeText(getApplicationContext(), "Registro de la visita guardado", Toast.LENGTH_LONG).show();
                                dialog.dismiss();

                            }
                        });

//                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                        LayoutInflater inflater = getLayoutInflater();
//                        View dialogView = inflater.inflate(R.layout.dialog_resultado_visita_doctores, null);
//                        mVisitaDocEfec = (CheckBox) dialogView.findViewById(R.id.cb_visita_promocional);
//                        mVisitaSinGestion = (CheckBox) dialogView.findViewById(R.id.cb_visita_sin_gestion);
//                        builder.setTitle("Actividades");
//                        builder.setView(dialogView);
//                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                if (mVisitaDocEfec.isChecked() &&  mVisitaSinGestion.isChecked()) {
//                                    Toast.makeText(context, "Solo se debe seleccionar una opcion", Toast.LENGTH_LONG).show();
//                                    dialogSingleButtonListener.onButtonClicked(dialog);
//                                    return;
//                                }
////                                visitaPlanificada.setVisitaDocEfec(mVisitaDocEfec.isChecked());
////                                visitaPlanificada.setVisitaSinGestion(mVisitaSinGestion.isChecked());
////                                resultadoVisitaViewModel.update(visitaPlanificada);
////                                Toast.makeText(getApplicationContext(), "Registro de la visita guardado", Toast.LENGTH_LONG).show();
//                            }
//                        });
//                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                // User cancelled the dialog
//                            }
//                        });
//                        builder.create().show();
                    }
                    else
                    {
                        if (mEfectiva.isChecked()) visitaPlanificada.setEfectivaPromocional(true);
                        resultadoVisitaViewModel.update(visitaPlanificada);
                        Toast.makeText(getApplicationContext(), "Registro de la visita guardado", Toast.LENGTH_LONG).show();
                    }

                    if (listaPro.size() != 0) {
                        VisitaClientesService visitaClientesService  = ApiClient.getClient().create(VisitaClientesService.class);
                        visitaClientesService.PostPromocionado(listaPro)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<Promocionado>() {
                                    @Override
                                    public void onCompleted() {
//                                Intent in = new Intent();
//                                setResult(RESULT_OK, in);
//                                finish();
                                        Toast.makeText(getApplicationContext(), " ", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(getApplicationContext(), " ", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onNext(Promocionado promocionado) {

                                    }


                                });
                    }
                } catch (Exception ex) {
                    Log.e("MiAppError", "Ha ocurrido un error: " + ex.getMessage(), ex);
                    Toast.makeText(getApplicationContext(), "Ha ocurrido un error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });

        mEfectiva.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Verifica si el cliente requiere revisita (y revisita no es null)
                ultimafechavalida = resultadoVisitaViewModel.fetchUltimaFechaVisitaValida(visitaPlanificada.getCodigoCliente(),visitaPlanificada.getCodigoAsesor());

                if (revisita != null && revisita == 1 && ultimafechavalida != null) {
                    int diasFaltantes = diasFaltantesParaRevisita();

                    if (diasFaltantes > 0) {
                        // Si aún no han pasado los días, mostrar mensaje y desmarcar checkbox
                        Toast.makeText(context, "Aún faltan " + diasFaltantes + " días para que la revisita sea efectiva.", Toast.LENGTH_LONG).show();
                        mEfectiva.setChecked(false);
                    }
                }
            }
        });



        mPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ProductosActivity.class);
                i.putExtra(Common.ARG_IDCLIENTE, codigoCliente);
                i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                i.putExtra(Common.ARG_SECCIOM, seccion);
                i.putExtra(Common.ARG_NOMBRE_CLIENTE, nombreCliente);
                startActivity(i);
            }
        });

        mEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder adb = new AlertDialog.Builder(context);
                adb.setTitle("Eliminar Visita");
                adb.setMessage("Seguro de querer eliminar la visita planificada?");
                adb.setIcon(android.R.drawable.ic_dialog_alert);
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Calendar cal1 = Calendar.getInstance();
                        Calendar cal2 = Calendar.getInstance();
                        cal1.setTime(visitaPlanificada.getFechaCreacion());
                        boolean sameDay = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
//                        if (sameDay) {
                            resultadoVisitaViewModel.delete(idLocal);
                            Toast.makeText(getApplicationContext(), "Visita eliminada con exito", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(context, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
//                        }else {
//                            Toast.makeText(getApplicationContext(), "Solo pueden eliminar visitas planificadas hoy", Toast.LENGTH_SHORT).show();
//                        }

                    } });

                adb.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    } });
                adb.show();
            }
        });


        mReplanificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =  new Intent(context, PlanificacionCrearActivity.class);
                i.putExtra(Common.ARG_IDCLIENTE, codigoCliente);
                i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                i.putExtra("idLocal", idLocal);
                startActivity(i);
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View cricketerView = getLayoutInflater().inflate(R.layout.row_agregar_promocionado,null,false);

                EditText editTextProdcuto = (EditText)cricketerView.findViewById(R.id.edit_producto);
                EditText editTextItem = (EditText)cricketerView.findViewById(R.id.edit_item);
//                AppCompatSpinner spinnerTeam = (AppCompatSpinner)cricketerView.findViewById(R.id.spinner_team);
                ImageView imageClose = (ImageView)cricketerView.findViewById(R.id.image_remove);

//                ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,teamList);
//                spinnerTeam.setAdapter(arrayAdapter);

                imageClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeView(cricketerView);
                    }
                });

                layoutList.addView(cricketerView);
            }
        });


//        mAcompanado.onCheckIsTextEditor(); {
//            Toast.makeText(getApplicationContext(), "En check entra", Toast.LENGTH_SHORT).show();
//        }

//        public void mAcompanado(View); {
//            Toast.makeText(getApplicationContext(), "En check entra", Toast.LENGTH_SHORT).show();
//        }

        resultadoVisitaViewModel = ViewModelProviders.of(this).get(ResultadoVisitaViewModel.class);
        resultadoVisitaViewModel.visitaClientes.observe(this, new Observer<VisitaClientes>() {
            @Override
            public void onChanged(@Nullable VisitaClientes visitaClientes) {
                if (visitaClientes != null) {
                    visitaPlanificada = visitaClientes;
//                    mVallorF2.setText(visitaClientes.getValorPedidoF2().toString());
                    mVallorF2.setText(String.format("%.2f",visitaClientes.getValorPedidoF2()));
                    mValorF3.setText(String.format("%.2f",visitaClientes.getValorPedidoF3()));
                    mValorF4.setText(String.format("%.2f",visitaClientes.getValorPedidoF4()));
                    mValorGEN.setText(String.format("%.2f",visitaClientes.getValorPedidoGEN()));
                    mCobro.setText(String.format("%.2f",visitaClientes.getValorXCobrar()));

                    String motivoVisita = "";
                    motivoVisita += visitaClientes.isVisitaXPedido() ? "- Pedido \n" : "";
                    motivoVisita += visitaClientes.isVisitaPromocional() ? "- Visita Promocional \n" : "";
                    motivoVisita += visitaClientes.isVisitaXCobro() ? "- Cobro \n" : "";
                    motivoVisita += visitaClientes.isVisitaXSiembraProducto() ? "- Siembra de Producto \n" : "";
                    motivoVisita += visitaClientes.isVisitaXCajasVacias() ? "- Cajas Vacias \n" : "";
                    motivoVisita += visitaClientes.isVisitaXEntregaPremios() ? "- Entrega de Premios \n" : "";
                    motivoVisita += visitaClientes.isVisitaXDevolucion() ? "- Devolucion \n" : "";
                    motivoVisita += visitaClientes.isVisitaPuntoContacto() ? "- Punto de Contacto \n" : "";
                    motivoVisita += visitaClientes.isVisitaReVisita() ? "- Re - Visita \n" : "";
                    mMotivo.setText(motivoVisita);
                    mEfectiva.setChecked(visitaClientes.getEstado().equals(VisitaClientes.EFECTIVA));
                    mObservacion.setText(visitaClientes.getObservacion());
                    mQueja.setText(visitaClientes.getQueja());


                    if (visitaClientes.isVisitaReVisita()) {
                        mReVisita.setVisibility(View.VISIBLE);
                        mReVisita.setChecked(visitaClientes.isVisitaReVisita());
                        mReVisita.setClickable(false);
                    }

                    if (visitaClientes.getEstado().equals(VisitaClientes.PLANIFICADO)) {
                        mRegistrar.setVisibility(View.VISIBLE);
                        mEliminar.setVisibility(View.VISIBLE);
                        mReplanificar.setVisibility(View.GONE);
                        mPedido.setVisibility(View.GONE);

                        // Si esta planificado se consulta la ubicacion actual para qu epermita hacer planificacion efectiva
                        getLocationPermission();
                        getDeviceLocation();
                    }else if (visitaClientes.getEstado().equals(VisitaClientes.EFECTIVA)){
                        mRegistrar.setVisibility(View.GONE);
                        mEliminar.setVisibility(View.GONE);
                        mReplanificar.setVisibility(View.VISIBLE);
                        //if (origenCliente.equals("FARMA")) mPedido.setVisibility(View.VISIBLE);
                        mPedido.setVisibility(View.VISIBLE);
                    }
                }
            }


        });
        resultadoVisitaViewModel.getByid(idLocal);

//        mLocationPermissionGranted = true;
//        milocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        //milocListener = new MiLocationListener();
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},1);
//            return;
//        }
//        if (mLocationPermissionGranted){
//            milocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, milocListener);
//            mRegistrar.setEnabled(false);
//        }



    }



    public void CargarDias() {
        final ArrayList<String> listaNombre = new ArrayList<>();
        PlanesService service = ApiClient.getClient().create(PlanesService.class);
        retrofit2.Call<ParametrosResponse> call  = service.GetJefes("VARIABLE");
        call.enqueue(new Callback<ParametrosResponse>() {
            @Override
            public void onResponse(Call<ParametrosResponse> call, Response<ParametrosResponse> response) {
                if (response.isSuccessful()) {
                    ParametrosResponse parametrosResponse = response.body();
                    List<Parametros> parametros = parametrosResponse.items;
                    diasAjuste = Integer.parseInt(parametros.get(0).getValor());


                }
            }


            @Override
            public void onFailure(Call<ParametrosResponse> call, Throwable t) {

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private int diasFaltantesParaRevisita() {
        ultimafechavalida = resultadoVisitaViewModel.fetchUltimaFechaVisitaValida(visitaPlanificada.getCodigoCliente(),visitaPlanificada.getCodigoAsesor());
        if (ultimafechavalida != null) {
            Calendar cal = Calendar.getInstance();
            Calendar calToday = Calendar.getInstance();

            // Obtener la fecha de la primera visita desde ViewModel
            cal.setTime(ultimafechavalida);
            cal.add(Calendar.DAY_OF_YEAR, diasAjuste); // Sumar días de ajuste

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date dateBefore = null, dateAfter = null;

            try {
                dateBefore = sdf.parse(sdf.format(cal.getTime()));
                dateAfter = sdf.parse(sdf.format(calToday.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long timeDiff = Math.abs(dateAfter.getTime() - dateBefore.getTime());
            long daysDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);

            if (calToday.compareTo(cal) >= 0) {
                return 0; // Ya han pasado los días requeridos
            } else {
                return (int) daysDiff; // Días restantes para que la revisita sea efectiva
            }
        }
        return 0;
    }

    private void removeView(View view){

        layoutList.removeView(view);

    }

    private boolean checkIfValidAndRead() {
        listaPro.clear();
        boolean result = true;


        for(int i=0;i<layoutList.getChildCount();i++){

            View cricketerView = layoutList.getChildAt(i);

            EditText editTextProdcuto = (EditText)cricketerView.findViewById(R.id.edit_producto);
            EditText editTextItem = (EditText)cricketerView.findViewById(R.id.edit_item);

            final Promocionado promocionado = new Promocionado();

            if(!editTextProdcuto.getText().toString().equals("")){
                promocionado.setProducto(editTextProdcuto.getText().toString());
            }else {
                result = false;
                break;
            }

            if(!editTextItem.getText().toString().equals("")){
                promocionado.setItem(editTextItem.getText().toString());
            }else {
                result = false;
                break;
            }
            promocionado.setIdVisitaCliente(visitaPlanificada.getIdVisitaCliente());
            promocionado.setIdPromocionado(0);
            listaPro.add(promocionado);


        }

        if(listaPro.size()==0){
            result = false;
//            Toast.makeText(this, "Agregar Producto!", Toast.LENGTH_SHORT).show();
        }else if(!result){
            Toast.makeText(context, "Ingrese los Datos de la Promocion Correctamente!", Toast.LENGTH_LONG).show();
        }


        return result;
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
                    mLocationPermissionGranted = true;
                }
            }
        }

    }




    @Override
    public void onFragmentInteraction(Clientes cliente) {
        latitud = cliente.getLatitud();
        longitud = cliente.getLongitud();
        origenCliente = cliente.getOrigen();
        revisita = cliente.getRevisita();

        if (cliente.getLatitud() != null) {
            if (cliente.getLatitud() != 0) {
                locCliente = new Location("");
                locCliente.setLatitude(cliente.getLatitud());
                locCliente.setLongitude(cliente.getLongitud());
            }
        }

        if (cliente.getOrigen().equals("FARMA")) {
            mPlanificacion.setVisibility(View.VISIBLE);
            opcionesVisitaFarmcaia.setVisibility(View.VISIBLE);
        } else {
            mPedido.setVisibility(View.GONE);
        }
        if (latitud != null ) {
            if (latitud != 0) {
                mRegistrar.setEnabled(true);
            }
        }
    }


//    private void getDeviceLocation() {
//        /*
//         * Get the best and most recent location of the device, which may be null in rare
//         * cases when a location is not available.
//         */
//        try {
//            if (mLocationPermissionGranted) {
//                Task locationResult = mFusedLocationProviderClient.getLastLocation();
//                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
//                    @Override
//                    public void onComplete(@NonNull Task task) {
//                        if (task.isSuccessful()) {
//                            // Set the map's camera position to the current location of the device.
//                            mLastKnownLocation = (Location) task.getResult();
//                            if (mLastKnownLocation != null ) {
//                                latitudActual = mLastKnownLocation.getLatitude();
//                                longitudActual=   mLastKnownLocation.getLongitude();
//                            }else {
//                                Toast.makeText(context, "No se puede acceder a la ubicacion. Habilite la localizacion en el telefono.", Toast.LENGTH_LONG).show();
//                                Log.d(TAG, "No se puede acceder a la ubicacion. habilite la localizacion en el telefono.");
//
//                            }
//                        } else {
//                            //Log.d(TAG, "Current location is null. Using defaults.");
//                            Toast.makeText(context, "No se puede acceder a la ubicacion. Habilite la localizacion en el telefono.", Toast.LENGTH_LONG).show();
//                            Log.e(TAG, "Exception: %s", task.getException());
//                            Crashlytics.logException(task.getException());
//                        }
//                    }
//                });
//            }
//        } catch(SecurityException e)  {
//            Crashlytics.logException(e);
//            //Log.e("Exception: %s", e.getMessage());
//        }
//    }


    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                LocationRequest mLocationRequest = new LocationRequest();
                GoogleApiClient mGoogleApiClient;
                mLocationRequest.setInterval(10*1000);
                mLocationRequest.setFastestInterval(5*1000);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest,
                         new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                if (locationResult == null) {
                                    mDistancia.setText("Sin Ubicacion");
                                    mRegistrar.setEnabled(false);
                                    latitudActual = 0.0;
                                    longitudActual=   0.0;

                                    Toast.makeText(context, "No se puede acceder a la ubicacion. Habilite la localizacion en el telefono.", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                for (Location location : locationResult.getLocations()) {
                                    latitudActual = location.getLatitude();
                                    longitudActual=   location.getLongitude();
                                    if (locCliente !=null)
                                    {
                                        mDistancia.setText("A " + String.format("%.2f",location.distanceTo(locCliente))  + " mts de distancia");
                                        if (location.distanceTo(locCliente) > 50) {
                                            mRegistrar.setEnabled(false);
                                            return;
                                        }
                                        else {
                                            mRegistrar.setEnabled(true);
                                            return;
                                        }
                                    }
                                }
                            };
                        }
                        , Looper.myLooper());

            }
        } catch(SecurityException e)  {
            //Crashlytics.logException(e);
            //Log.e("Exception: %s", e.getMessage());
        }
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
                    })                    .show();
        }

    }

    public void onCheckboxClickedEfc(View view){
        if (mEfectiva.isChecked()){
            if(mVisitaSinGestion.isChecked()){
                mVisitaSinGestion.setChecked(false);
            }

        }

    }
    public void onCheckboxClicked(View view) {
//        String s = (mAcompanado.isChecked() ? "Acompaña" : "No Acompaña");
        if (mAcompanado.isChecked()){
            //Toast.makeText(this, s, Toast.LENGTH_LONG).show();
//            Intent i =  new Intent(context, UsuarioActivity.class);
//            i.putExtra(Common.ARG_IDCLIENTE, codigoCliente);
//            i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
//            i.putExtra("idLocal", idLocal);
//            startActivity(i);
            Intent i = new Intent(context, UsuarioActivity.class);
            startActivityForResult(i, 1);
        }else {
            mAcompanadoText.setText("");
            mAcompanadoText.setVisibility(View.GONE);
        //    Toast.makeText(this, s, Toast.LENGTH_LONG).show(); mEfectiva
        }

//        if (mEfectiva.isChecked() && (origenCliente.equals("MEDICO")) ){
//            String s = (mAcompanado.isChecked() ? "efectiva" : "No efectiva");
//            Toast.makeText(this, s, Toast.LENGTH_LONG).show();
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            LayoutInflater inflater = getLayoutInflater();
//            View dialogView = inflater.inflate(R.layout.dialog_resultado_visita_doctores, null);
//            mVisitaDocEfec = (CheckBox) dialogView.findViewById(R.id.cb_visita_promocional);
//            mVisitaSinGestion = (CheckBox) dialogView.findViewById(R.id.cb_visita_sin_gestion);
//
//            builder.setView(dialogView);
//            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) { Toast.makeText(getApplicationContext(), "Registro de la visita guardado", Toast.LENGTH_LONG).show();
//                    Log.d("myTag", "regresoo.... ok " +mVisitaDocEfec.isChecked());
//                    Log.d("myTag", "regresoo.... ok " +mVisitaSinGestion.isChecked());
//
//                }
//            });
//            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    // User cancelled the dialog
//                }
//            });
//            builder.create().show();
//        }else {
//
//        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            //Log.d("myTag", "regresoo.... ok " + data.getStringExtra(ACOMPANADO));
            //mAcompanado.setText("Visita acompañada     " + ACOMPANADO);

            mAcompanadoText.setText(data.getStringExtra(ACOMPANADO));
            mAcompanadoText.setVisibility(View.VISIBLE);

        } else if (resultCode == RESULT_CANCELED) {
            mAcompanadoText.setText("");
            mAcompanadoText.setVisibility(View.GONE);
            mAcompanado.setChecked(false);
            //Log.d("myTag", "cancelado...." );

        }
    }
//    public class MiLocationListener implements LocationListener {
//        @Override
//        public void onLocationChanged(Location loc) {
//            longitudActual= loc.getLongitude();
//            latitudActual= loc.getLatitude();
//        }
//        @Override
//        public void onProviderDisabled(String provider) {
//            Toast.makeText(getApplicationContext(), "Gps Desactivado. Por favor enciéndalo",Toast.LENGTH_SHORT).show();
//        }
//        @Override
//        public void onProviderEnabled(String provider) {
//            Toast.makeText(getApplicationContext(), "Gps Activo",Toast.LENGTH_SHORT).show();
//        }
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//        }
//    }

//
//    /** calculates the distance between two locations in MILES */
//    public double distance(double lat1, double lng1, double lat2, double lng2) {
//
//        double earthRadius = 6371; // 3958.75 in miles, change to 6371 for kilometer output
//
//        double dLat = Math.toRadians(lat2-lat1);
//        double dLng = Math.toRadians(lng2-lng1);
//
//        double sindLat = Math.sin(dLat / 2);
//        double sindLng = Math.sin(dLng / 2);
//
//        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
//                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
//
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
//
//        double dist = earthRadius * c;
//
//        return dist; // output distance, in MILES
//    }
}
