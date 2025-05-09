package com.rocnarf.rocnarf;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Layout;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.dao.DataBaseHelper;
import com.rocnarf.rocnarf.models.Clientes;
import com.rocnarf.rocnarf.models.Parametros;
import com.rocnarf.rocnarf.models.ParametrosResponse;
import com.rocnarf.rocnarf.models.VisitaClientes;
import com.rocnarf.rocnarf.repository.PanelClientesRepository;
import com.rocnarf.rocnarf.viewmodel.PlanificacionCrearViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import android.database.sqlite.SQLiteDatabase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanificacionCrearActivity extends AppCompatActivity
        implements ClienteDetalleFragment.OnFragmentInteractionListener {

    private TextInputEditText mFecha, mHora, mValorCobrar, mValorF2, mValorF3, mValorF4, mValorGEN;
    private Calendar mCalendar;
    private DatePickerDialog mDatePickerDialog;
    private TimePickerDialog mTimePickerDialog;
    private CheckBox mPedido, mCobro, mVisitaPromocional, mSiembraProducto, mCajasVacias, mEntregaPremios, mDevolucion, mPuntoContacto, mVisitaPromocionalDoctores, mReVisita;
    private TextView mTextRevista;
    private LinearLayout mPedidoLayout, opcionesVisitaFarmcaia, opcionesVisitaDoctores, opcionesRevista;
    private TextInputLayout mCobroLayout;
    private Button mAgregarVisita;
    private Date mfechaPrimeraVista;
    private int idLocal, diasAjuste;
    private String idUsuario, codigoCliente, seccion, estadoVisita, revisita;
    private PlanificacionCrearViewModel visitasClientesPlanificarViewModel;
    private VisitaClientes visitaClientes;
    private int anioCalendar, mesCalendar, diaCalendar, horaTimePicker, minutosTimePicker;
    private String nombreCliente, origenPlanificacionVisita, tipoCliente;
    private String direccion;
    private Double latitud, longitud;

    private Context context;

    private SQLiteDatabase sQLiteDatabase;
    private com.rocnarf.rocnarf.dao.DataBaseHelper DataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planificacion_crear);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Habilitar el botón de retroceso
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        Intent intent = getIntent();
        codigoCliente = intent.getStringExtra(Common.ARG_IDCLIENTE);
        idUsuario = intent.getStringExtra(Common.ARG_IDUSUARIO);
        seccion = intent.getStringExtra(Common.ARG_SECCIOM);
        idLocal = intent.getIntExtra("idLocal", 0);
        estadoVisita = intent.getStringExtra(Common.ARG_ESTADOVISTA);
        revisita = intent.getStringExtra(Common.ARG_REVISITA);

        origenPlanificacionVisita = intent.getStringExtra(Common.ARG_ORIGEN_PLANIFICACION_VISITA);
        Log.d("vista desde ","desde"+origenPlanificacionVisita);
        configurarControles();

        // Viewmodel
        visitasClientesPlanificarViewModel = ViewModelProviders.of(this).get(PlanificacionCrearViewModel.class);
        visitasClientesPlanificarViewModel.get().observe(this, new Observer<VisitaClientes>() {
            @Override
            public void onChanged(@Nullable VisitaClientes listaVisitaClientes) {
                visitaClientes = listaVisitaClientes;
            }
        });
        Log.d("vista visitaClientes ","visitaClientes"+visitaClientes);

        CargarDatosVisitaAnterior();
        CargarDias();
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


    private void CargarDatosVisitaAnterior() {
        Log.d("log", "idLocal 1111 --- >" + idLocal);

        if (idLocal != 0) {
            visitasClientesPlanificarViewModel.visitaAnterior.observe(this, new Observer<VisitaClientes>() {
                @Override
                public void onChanged(@Nullable VisitaClientes visitaAnterior) {
                    mCajasVacias.setChecked(visitaAnterior.isVisitaXCajasVacias());
                    mSiembraProducto.setChecked(visitaAnterior.isVisitaXSiembraProducto());
                    mVisitaPromocional.setChecked(visitaAnterior.isVisitaPromocional());
                    Log.d("log", "mfechaPrimeraVista 1111 --- >" + mfechaPrimeraVista);

                    mfechaPrimeraVista = visitaAnterior.getFechaVisita();
                    mPedido.setChecked(visitaAnterior.isVisitaXPedido());
                    if (visitaAnterior.isVisitaXPedido()) {
                        mPedidoLayout.setVisibility(View.VISIBLE);
                        mValorF2.setText(visitaAnterior.getValorPedidoF2().toString());
                        mValorF3.setText(visitaAnterior.getValorPedidoF3().toString());
                        mValorF4.setText(visitaAnterior.getValorPedidoF4().toString());
                        mValorGEN.setText(visitaAnterior.getValorPedidoGEN().toString());
                    }

                    mCobro.setChecked(visitaAnterior.isVisitaXCobro());
                    if (visitaAnterior.isVisitaXCobro()) {
                        mCobroLayout.setVisibility(View.VISIBLE);
                        mValorCobrar.setText(visitaAnterior.getValorXCobrar().toString());
                    }

                    if (visitaAnterior.getEstado() != null && !visitaAnterior.getEstado().equals("NOEFE") && !visitaAnterior.getEstado().equals("PLANI")) {
                        opcionesRevista.setVisibility(View.VISIBLE);
                        mReVisita.setChecked(true); // Siempre marcado si es Re-Visita
                        mReVisita.setEnabled(false);
                        // mReVisita.setClickable(false);
                    } else {
                        opcionesRevista.setVisibility(View.GONE);
                    }

                }
            });
            visitasClientesPlanificarViewModel.getVisitaAnterior(idLocal);
        }
    }


    private void configurarControles() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ClienteDetalleFragment detalleCliente = ClienteDetalleFragment.newInstance(codigoCliente, idUsuario, seccion);
        ft.replace(R.id.fm_cliente_activity_planificacion_crear, detalleCliente);
        ft.commit();

        mFecha = (TextInputEditText) findViewById(R.id.et_fecha_content_planificacion_crear);
        mHora = (TextInputEditText) findViewById(R.id.et_hora_content_planificacion_crear);
        setTimeField();

        mCajasVacias = (CheckBox) findViewById(R.id.cb_cajas_vacias_content_planificacion_crear);
        mSiembraProducto = (CheckBox) findViewById(R.id.cb_siembra_producto_content_planificacion_crear);
        mVisitaPromocional = (CheckBox) findViewById(R.id.cb_visita_promocional_content_planificacion_crear);
        //mVisitaPromocionalDoctores = (CheckBox) findViewById(R.id.cb_visita_promocional_doctores);
        mPuntoContacto = (CheckBox) findViewById(R.id.cb_punto_contacto);
        mEntregaPremios = (CheckBox) findViewById(R.id.cb_entrega_premios_content_planificacion_crear);
        mDevolucion = (CheckBox) findViewById(R.id.cb_devolucion_content_planificacion_crear);
        opcionesRevista = (LinearLayout) findViewById(R.id.ly_revisita);
        mReVisita = (CheckBox) findViewById(R.id.cb_re_visita);
        mTextRevista = (TextView) findViewById(R.id.tv_re_visita);
        opcionesVisitaFarmcaia = (LinearLayout) findViewById(R.id.ly_visita_farmacia_content_planificacion_crear);
        opcionesVisitaDoctores = (LinearLayout) findViewById(R.id.ly_visita_doctores_content_planificacion_crear);
        // Se presenta los controles para ingresar el valor de pedidos (por categoria) si se seleccion que la razon de la visita es pedido
        mPedidoLayout = (LinearLayout) findViewById(R.id.ll_pedido_content_planificacion_crear);
        mValorF2 = (TextInputEditText) findViewById(R.id.et_valorF2_content_planificacion_crear);
        mValorF3 = (TextInputEditText) findViewById(R.id.et_valorF3_content_planificacion_crear);
        mValorF4 = (TextInputEditText) findViewById(R.id.et_valorF4_content_planificacion_crear);
        mValorGEN = (TextInputEditText) findViewById(R.id.et_valorGEN_content_planificacion_crear);
//        mValorF2.setText("0");
//        mValorF3.setText("0");
//        mValorF4.setText("0");
//        mValorGEN.setText("0");
        if (estadoVisita != null && !estadoVisita.equals("NOEFE") && !estadoVisita.equals("PLANI")) {
            opcionesRevista.setVisibility(View.VISIBLE);
            mReVisita.setChecked(true); // Siempre marcado si es Re-Visita
            mReVisita.setEnabled(false);
            if(mReVisita.isChecked()){

            }
           // mReVisita.setClickable(false);
        } else {
            opcionesRevista.setVisibility(View.GONE);
        }

        setDateField();

        mPedido = (CheckBox) findViewById(R.id.cb_pedido_content_planificacion_crear);
        mPedido.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    mPedidoLayout.setVisibility(View.VISIBLE);
                else {
                    mPedidoLayout.setVisibility(View.GONE);
//                    mValorF2.setText("0");
//                    mValorF3.setText("0");
//                    mValorF4.setText("0");
//                    mValorGEN.setText("0");
                }

            }
        });

        // Se presenta el control para ingresar el valor a cobrar si se seleccion que la razon de la visita es cobrar
        mCobroLayout = (TextInputLayout) findViewById(R.id.ti_cobro_content_planificacion_crear);
        mValorCobrar = (TextInputEditText) findViewById(R.id.et_cobro_content_planificacion_crear);
        ///  mValorCobrar.setText("0");
        mCobro = (CheckBox) findViewById(R.id.cb_cobro_content_planificacion_crear);
        mCobro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    mCobroLayout.setVisibility(View.VISIBLE);
                else {
                    mCobroLayout.setVisibility(View.GONE);
                    ///mValorCobrar.setText("0");
                }
            }
        });

        mAgregarVisita = (Button) findViewById(R.id.btn_agregar_content_planificacion_crear);
        mAgregarVisita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Validar()) {
                        Date fechaUltimaVisita = visitasClientesPlanificarViewModel.fetchUltimaFechaVisitaValida(codigoCliente,idUsuario);
                        if (fechaUltimaVisita != null) {
                            Calendar calUltimaVisita = Calendar.getInstance();
                            calUltimaVisita.setTime(fechaUltimaVisita);
                            calUltimaVisita.add(Calendar.DAY_OF_YEAR, diasAjuste);
                            Date fechaMinimaEfectiva = calUltimaVisita.getTime();

                            if (mCalendar.getTime().before(fechaMinimaEfectiva)) {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                Calendar calendarioMinimo = Calendar.getInstance();
                                calendarioMinimo.setTime(fechaMinimaEfectiva);
                                calendarioMinimo.add(Calendar.DAY_OF_MONTH, 1); // 🔹 Se suma un día

                                // Formatear la nueva fecha para mostrarla en el AlertDialog
                                String fechaMinimaStrAjustada = sdf.format(calendarioMinimo.getTime());

                                new AlertDialog.Builder(context)
                                        .setTitle("Advertencia")
                                        .setMessage("La fecha ingresada no cumple con el tiempo mínimo requerido desde la última visita para ser considerada efectiva. La próxima visita efectiva puede realizarse a partir del: " + fechaMinimaStrAjustada + "\n¿Desea continuar con la creación de la visita de todas formas?")
                                        .setPositiveButton("Sí, crear visita", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                CrearVisita();
                                                Toast.makeText(context, "Visita creada, pero no será contabilizada como efectiva.", Toast.LENGTH_LONG).show();
                                                finalizarActividad();
                                            }
                                        })
                                        .setNegativeButton("No, cancelar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Toast.makeText(context, "Creación de la visita cancelada.", Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .show();
                                return;
                            }
                        }
                        CrearVisita();
                        Toast.makeText(context, "La visita se ha agregado con éxito.", Toast.LENGTH_LONG).show();
                        finalizarActividad();
                    }
                } catch (Exception ex) {
                    Log.e("Error al crear vista", Log.getStackTraceString(ex));
                    Toast.makeText(context, "Error al intentar agregar la visita:\n" + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void finalizarActividad() {
        if (Common.VISITA_DESDE_PANEL.equals(origenPlanificacionVisita)) {
            PanelClientesRepository panelClientesRepository = new PanelClientesRepository(context);
            panelClientesRepository.deletePanelCliente(codigoCliente);
            Intent i = new Intent(context, PanelClientesActivity.class);
            i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
            i.putExtra(Common.ARG_SECCIOM, seccion);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else if (Common.VISITA_DESDE_MAPA.equals(origenPlanificacionVisita)) {
            Intent i = new Intent(context, MapaActivity.class);
            i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
            i.putExtra(Common.ARG_SECCIOM, seccion);
            startActivity(i);
        } else {
            Intent i = new Intent(context, MainActivity.class);
            startActivity(i);
        }
        finish();
    }

    private void setDateField() {
        mCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                anioCalendar = year;
                mesCalendar = month;
                diaCalendar = dayOfMonth;
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // Formatear la fecha seleccionada
                SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT, Locale.US);
                String fechaSeleccionada = sdf.format(mCalendar.getTime());
                mFecha.setText(fechaSeleccionada);

                // Restablecer mensaje de advertencia antes de validar nuevamente
                mTextRevista.setText(""); // Se limpia el mensaje de advertencia

                // Validar tiempo mínimo para revisita
                Date fechaUltimaVisita = visitasClientesPlanificarViewModel.fetchUltimaFechaVisitaValida(codigoCliente, idUsuario);
                    if (fechaUltimaVisita != null) {
                        Calendar calUltimaVisita = Calendar.getInstance();
                        calUltimaVisita.setTime(fechaUltimaVisita);
                        calUltimaVisita.add(Calendar.DAY_OF_YEAR, 1);
                        calUltimaVisita.add(Calendar.DAY_OF_YEAR, diasAjuste);
                        Date fechaMinimaEfectiva = calUltimaVisita.getTime();

                        // Si la fecha ingresada es antes de la fecha mínima efectiva, mostrar advertencia
                        if (mCalendar.getTime().before(fechaMinimaEfectiva)) {
                            mReVisita.setChecked(false); // Desmarcar el checkbox si la fecha es inválida
                            mTextRevista.setText("Esta visita no va a ser tomada como efectiva");
                        } else {
                            mReVisita.setChecked(true); // Permitir marcarlo si la fecha es válida
                        }
                    }

            }
        };

        mFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Se actualizan los valores del DatePicker para reflejar cualquier cambio previo en la fecha
                mDatePickerDialog = new DatePickerDialog(
                        v.getContext(), date,
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)
                );
                mDatePickerDialog.show();
            }
        });
    }


    private void setTimeField() {
        mCalendar = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener hora = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                mHora.setText(String.valueOf(i) + ":" + (i1 > 9 ? String.valueOf(i1) : "0" + String.valueOf(i1)));
                horaTimePicker = i;
                minutosTimePicker = i1;
            }
        };


        mTimePickerDialog = new TimePickerDialog(this, hora, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(this));

        mHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimePickerDialog.show();
            }
        });
    }


    private boolean Validar() {
        if (mFecha.getText().length() == 0) {
            mFecha.setError("Falta ingresar la fecha");
            return false;
        }
        if (mHora.getText().length() == 0) {
            mHora.setError("Falta ingresar la hora");
            return false;
        }

        if (!mVisitaPromocional.isChecked() && !mPedido.isChecked() && !mCobro.isChecked() && !mSiembraProducto.isChecked() && !mCajasVacias.isChecked() && !mPuntoContacto.isChecked()) {
            Toast.makeText(context, "Debes seleccionar al menos un motivo ", Toast.LENGTH_LONG).show();
            return false;
        }

        if (mPedido.isChecked() && (TextUtils.isEmpty(mValorF2.getText()) || TextUtils.isEmpty(mValorF3.getText()) || TextUtils.isEmpty(mValorF4.getText()) || TextUtils.isEmpty(mValorGEN.getText()))) {
            Toast.makeText(context, "Debes llenar informacion de pedido ", Toast.LENGTH_LONG).show();
            return false;
        }

        if (mCobro.isChecked() && TextUtils.isEmpty(mValorCobrar.getText())) {
            Toast.makeText(context, "Debes llenar informacion de cobro ", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void CrearVisita() {
        VisitaClientes visitaClientes = new VisitaClientes();
        visitaClientes.setIdVisitaCliente(0);
        visitaClientes.setCodigoAsesor(idUsuario);
        visitaClientes.setSeccion(seccion);

        visitaClientes.setCodigoCliente(codigoCliente);
        visitaClientes.setNombreCliente(nombreCliente);   // VPO Se debe regualrizar
        visitaClientes.setDireccion(direccion);
        visitaClientes.setLatitud(latitud);
        visitaClientes.setLongitud(longitud);
        visitaClientes.setTipoCliente(tipoCliente);
        mCalendar.set(anioCalendar, mesCalendar, diaCalendar, horaTimePicker, minutosTimePicker);
        visitaClientes.setFechaVisitaPlanificada(mCalendar.getTime()); // mHora

        visitaClientes.setVisitaXPedido(mPedido.isChecked());
        visitaClientes.setValorPedidoF2(Double.parseDouble(TextUtils.isEmpty(mValorF2.getText().toString()) ? "0" : mValorF2.getText().toString()));
        visitaClientes.setValorPedidoF3(Double.parseDouble(TextUtils.isEmpty(mValorF3.getText().toString()) ? "0" : mValorF3.getText().toString()));
        visitaClientes.setValorPedidoF4(Double.parseDouble(TextUtils.isEmpty(mValorF4.getText().toString()) ? "0" : mValorF4.getText().toString()));
        visitaClientes.setValorPedidoGEN(Double.parseDouble(TextUtils.isEmpty(mValorGEN.getText().toString()) ? "0" : mValorGEN.getText().toString()));

        visitaClientes.setVisitaXCobro(mCobro.isChecked());
        visitaClientes.setValorXCobrar(Double.parseDouble(TextUtils.isEmpty(mValorCobrar.getText().toString()) ? "0" : mValorCobrar.getText().toString()));

        visitaClientes.setVisitaXCajasVacias(mCajasVacias.isChecked());

        ///if (cliente.getOrigen().equals("FARMA")) {
        visitaClientes.setVisitaPromocional(mVisitaPromocional.isChecked());
        // }


        //visitaClientes.setVisitaPromocional(mVisitaPromocionalDoctores.isChecked());
        visitaClientes.setVisitaPuntoContacto(mPuntoContacto.isChecked());
        visitaClientes.setVisitaXSiembraProducto(mSiembraProducto.isChecked());
        visitaClientes.setVisitaXEntregaPremios(mEntregaPremios.isChecked());
        visitaClientes.setVisitaXDevolucion(mDevolucion.isChecked());
        visitaClientes.setVisitaReVisita(mReVisita.isChecked());

        visitaClientes.setPendienteSync(false);

        visitaClientes.setFechaCreacion(new Date());
        visitaClientes.setFechaModificacion(new Date());
        visitaClientes.setEstado(VisitaClientes.PLANIFICADO);

        visitasClientesPlanificarViewModel.add(visitaClientes);

    }


    @Override
    public void onFragmentInteraction(Clientes cliente) {
        nombreCliente = cliente.getNombreCliente();
        tipoCliente = cliente.getClaseMedico();
        direccion = cliente.getDireccion();
        latitud = cliente.getLatitud();
        longitud = cliente.getLongitud();

        if (cliente.getOrigen().equals("FARMA")) {
            opcionesVisitaFarmcaia.setVisibility(View.VISIBLE);
        } else if (cliente.getOrigen().equals("MEDICO")) {
            opcionesVisitaDoctores.setVisibility(View.VISIBLE);
            mVisitaPromocional = (CheckBox) findViewById(R.id.cb_visita_promocional_doctores);
            mPuntoContacto = (CheckBox) findViewById(R.id.cb_punto_contacto_cliente);
        } else {
            mVisitaPromocional.setChecked(true);
        }


        String valiZ= cliente.getIdCliente().toUpperCase().substring(0, 1);

        if (valiZ.equals("Z")){
            mSiembraProducto.setVisibility(View.GONE);
            mDevolucion.setVisibility(View.GONE);
            mPedido.setVisibility(View.GONE);
            mCobro.setVisibility(View.GONE);
        }

    }

    public void DelPanelClientePlanificado() {

        DataBaseHelper = new DataBaseHelper(this);
        sQLiteDatabase = DataBaseHelper.getReadableDatabase();

        //Si existe un registro en el panel lo elimino para ser agregado nuevamente
        sQLiteDatabase.delete("panelclientes", "idCliente ='" + codigoCliente + "'", null);
    }

}
