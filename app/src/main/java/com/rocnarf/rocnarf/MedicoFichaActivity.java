package com.rocnarf.rocnarf;

import android.app.DatePickerDialog;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.models.EspecialidadMedicos;
import com.rocnarf.rocnarf.models.FichaMedico;
import com.rocnarf.rocnarf.viewmodel.MedicoFichaViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Completable;
import rx.Subscription;

public class MedicoFichaActivity extends AppCompatActivity {
    TextInputEditText mmarca,mconceptoPlan,mfechaDesde,mfechaHasta, mEspecialidad, mEmail, mCedula, mHobbies, mLabora, mSeguros, mAcademicas, mDireccion1, mDireccion2, mCelular, mTelefono, mSecretaria, mRocnarf, mfechaNacimiento;
    Button mGuardar;
    SwitchMaterial simpleSwitch;
    LinearLayout mContenido;
    Spinner mEspecialidadSpi;
    MedicoFichaViewModel medicoFichaViewModel;
    FichaMedico ficha;
    Context context;
    TextView especidalidadesMedicos;
    private String idCliente, idUsuario;
    private Calendar mCalendar,mCalendar1,mCalendar2;
    private DatePickerDialog mDatePickerDialog,mDatePickerDialog1,mDatePickerDialog2;
    private int anioCalendar, mesCalendar, diaCalendar, horaTimePicker, minutosTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medico_ficha);

        context = getApplicationContext();
        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        idCliente =  i.getStringExtra(Common.ARG_IDCLIENTE);
       // mEspecialidadSpi = (Spinner) findViewById(R.id.especialidad);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ArrayList<String> listaNombre = new ArrayList<>();
        listaNombre.add("Ninguno");
        final ArrayAdapter adaptador = new ArrayAdapter(context, android.R.layout.simple_list_item_1,listaNombre);
        PlanesService service = ApiClient.getClient().create(PlanesService.class);
        retrofit2.Call<EspecialidadMedicos> call  = service.GetEspecialidadesMEdicas(idCliente);
        call.enqueue(new Callback<EspecialidadMedicos>() {
            @Override
            public void onResponse(Call<EspecialidadMedicos> call, Response<EspecialidadMedicos> response) {
                if (response.isSuccessful()) {
                    EspecialidadMedicos parametrosResponse = response.body();
                    //Log.d("espec","a ver" + parametrosResponse);
                    if(parametrosResponse != null) {
                        especidalidadesMedicos.setText((parametrosResponse.getF1() != null ? "F1: " + parametrosResponse.getF1() + "\n" : "") +
                                (parametrosResponse.getF2() != null ? "F2: " + parametrosResponse.getF2() + "\n" : "") +
                                (parametrosResponse.getF3() != null ? "F3: " + parametrosResponse.getF3() + "\n" : "") +
                                (parametrosResponse.getF4() != null ? "F4: " + parametrosResponse.getF4() + "\n" : "")
                        );

                        adaptador.setDropDownViewResource(android.R.layout.simple_list_item_1);
//                    mEspecialidadSpi.setAdapter(adaptador);
                    }


                }
            }


            @Override
            public void onFailure(Call<EspecialidadMedicos> call, Throwable t) {

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        mmarca = (TextInputEditText) findViewById(R.id.Marca_doc);
        mconceptoPlan = (TextInputEditText) findViewById(R.id.concepto_plan);
        simpleSwitch = (SwitchMaterial) findViewById(R.id.simpleSwitchAuspiciado);
        especidalidadesMedicos = (TextView) findViewById(R.id.EspcialidadesMedicos);
        mEspecialidad = (TextInputEditText) findViewById(R.id.et_especialidad_activity_medico_ficha);
        mEmail = (TextInputEditText) findViewById(R.id.et_mail_activity_medico_ficha);
        mCedula = (TextInputEditText) findViewById(R.id.et_cedula_activity_medico_ficha);
        mHobbies = (TextInputEditText) findViewById(R.id.et_hobbies_activity_medico_ficha);
        mLabora = (TextInputEditText) findViewById(R.id.et_academicas_activity_medico_ficha);
        mSeguros = (TextInputEditText) findViewById(R.id.et_seguros_activity_medico_ficha);
        mAcademicas = (TextInputEditText) findViewById(R.id.et_academicas_activity_medico_ficha);
        mDireccion1 = (TextInputEditText) findViewById(R.id.et_direccion1_activity_medico_ficha);
        mDireccion2 = (TextInputEditText) findViewById(R.id.et_direccion2_activity_medico_ficha);
        mCelular= (TextInputEditText) findViewById(R.id.et_celular_activity_medico_ficha);
        mTelefono  = (TextInputEditText) findViewById(R.id.et_telefono_activity_medico_ficha);
        mSecretaria = (TextInputEditText) findViewById(R.id.et_secretaria_activity_medico_ficha);
        mRocnarf = (TextInputEditText) findViewById(R.id.et_rocnarf_activity_medico_ficha);;
        //mContenido = (LinearLayout)findViewById(R.id.ll_contenido_activity_medico_ficha);
        mfechaNacimiento = (TextInputEditText) findViewById(R.id.et_fecha_nacimiento);;
        mfechaDesde = (TextInputEditText) findViewById(R.id.fecha_desde_aus);;
        mfechaHasta = (TextInputEditText) findViewById(R.id.fecha_hasta_aus);;
        setDateField();
        medicoFichaViewModel = ViewModelProviders.of(this).get(MedicoFichaViewModel.class);
        medicoFichaViewModel.setIdUsuario(idUsuario);
        medicoFichaViewModel.getFichaMedico(idCliente).observe(this, new Observer<FichaMedico>() {
            @Override
            public void onChanged(@Nullable FichaMedico fichaMedico) {
//                if (fichaMedico.getEspecialidades() != null) {
//                    for (int indice = 0; indice < adaptador.getCount(); indice++) {
//                        if (adaptador.getItem(indice).toString().contains(fichaMedico.getEspecialidades())) {
//                            mEspecialidadSpi.setSelection(indice);
//                        }
//                    }
//                }
                ficha = fichaMedico;
                //mEspecialidad.setText(fichaMedico.getEspecialidades());
                mEmail.setText(fichaMedico.getEmail());
                mCedula.setText(fichaMedico.getCedula());
                mHobbies.setText(fichaMedico.getHobbies());
                mLabora.setText(fichaMedico.getLaboraEn());
                mSeguros.setText(fichaMedico.getSegurosPrivados());
                mAcademicas.setText(fichaMedico.getActividadesAcademicas());
                mDireccion1.setText(fichaMedico.getDireccion());
                mDireccion2.setText(fichaMedico.getDireccion2());
                mCelular.setText(fichaMedico.getCelular());
                mTelefono.setText(fichaMedico.getTelefono1());
                mSecretaria.setText(fichaMedico.getNombreSecretaria());
                mRocnarf.setText(fichaMedico.getActividadRocnarf());
                mmarca.setText(fichaMedico.getMarca() != null ? fichaMedico.getMarca() : "");
                mconceptoPlan.setText(fichaMedico.getConceptoPlan() != null ? fichaMedico.getConceptoPlan() : "");

                if(fichaMedico.getAuspiciado() == null || fichaMedico.getAuspiciado() == false){
                    simpleSwitch.setChecked(false);
                }else{

                    if(fichaMedico.getFechaDesdeAuspicio() != null && fichaMedico.getFechaHastaAuspicio() != null){
                        Date fechaHoy = new Date();
                        if(fechaHoy.after(fichaMedico.getFechaDesdeAuspicio()) && fechaHoy.before(fichaMedico.getFechaHastaAuspicio()) ){
                            simpleSwitch.setChecked(true);

                        }else{
                            simpleSwitch.setChecked(false);
                        }
                    }

                }

                SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT, new Locale("es", "ES"));
                if (fichaMedico.getFechaNacimiento() == null)
                {

                }else {
                    mfechaNacimiento.setText(sdf.format(fichaMedico.getFechaNacimiento()));
                }

                if (fichaMedico.getFechaDesdeAuspicio() != null) mfechaDesde.setText(sdf.format(fichaMedico.getFechaDesdeAuspicio()));
                if (fichaMedico.getFechaHastaAuspicio() != null) mfechaHasta.setText(sdf.format(fichaMedico.getFechaHastaAuspicio()));
            }
        });


        mGuardar = (Button)findViewById(R.id.bt_guardar_activity_medico_ficha);
        mGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // ficha.setEspecialidades(mEspecialidadSpi.getSelectedItem().toString());
                ficha.setEmail(mEmail.getText().toString());
                ficha.setCedula(mCedula.getText().toString());
                ficha.setHobbies(mHobbies.getText().toString());
                ficha.setLaboraEn(mLabora.getText().toString());
                ficha.setSegurosPrivados(mSeguros.getText().toString());
                ficha.setActividadesAcademicas(mAcademicas.getText().toString());
                ficha.setDireccion(mDireccion1.getText().toString());
                ficha.setDireccion2(mDireccion2.getText().toString());
                ficha.setCelular(mCelular.getText().toString());
                ficha.setTelefono1(mTelefono.getText().toString());
                ficha.setNombreSecretaria(mSecretaria.getText().toString());
                ficha.setActividadRocnarf(mRocnarf.getText().toString());
                if(mfechaNacimiento.getText().equals("") || mfechaDesde == null){
                    ficha.setFechaNacimiento(null);
                }
                Log.d("xxxxx","xxxxx" + mfechaDesde.getText());
                Log.d("xxxxx","xxxxx" + mfechaHasta.getText());
                Log.d("xxxxx","xxxxx" + mCalendar2.getTime());
                Log.d("xxxxx","xxxxx" + mCalendar1.getTime());

                if(mfechaDesde.getText().equals("") || mfechaDesde == null){
                    ficha.setFechaDesdeAuspicio(null);
                }else {
                    ficha.setFechaDesdeAuspicio(mCalendar1.getTime());
                }
                if(mfechaHasta.getText().equals("") || mfechaDesde == null){
                    ficha.setFechaHastaAuspicio(null);
                }else {
                    ficha.setFechaHastaAuspicio(mCalendar2.getTime());

                }
                //ficha.setFechaNacimiento(mCalendar.getTime());
                ficha.setAuspiciado(simpleSwitch.isChecked() ? true : false );
                ficha.setMarca(mmarca.getText().toString());
                ficha.setConceptoPlan(mconceptoPlan.getText().toString());

                Completable.CompletableSubscriber resultado = new Completable.CompletableSubscriber() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(getApplication(), "Ficha Actualizada con exito", Toast.LENGTH_LONG).show();
                        final Snackbar snackbar = Snackbar
                                .make(mContenido, "Ficha Actualizada con exito", Snackbar.LENGTH_LONG);
                        snackbar.setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                snackbar.dismiss();
                            }
                        });
                        snackbar.show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplication(), "Ha ocurrido un error al guarda la ficha. " + e.getMessage(), Toast.LENGTH_LONG).show();
                        final Snackbar snackbar = Snackbar
                                .make(mContenido, "Ha ocurrido un error al guardar la ficha. " + e.getMessage(), Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        snackbar.dismiss();
                                    }
                                });
                        snackbar.show();

                    }

                    @Override
                    public void onSubscribe(Subscription d) {

                    }
                };

                medicoFichaViewModel.update(ficha, resultado);


            }
        });

    }
    private void setDateField() {
        mCalendar = Calendar.getInstance();
        mCalendar1 = Calendar.getInstance();
        mCalendar2 = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateNacimiento = createDateSetListener(mfechaNacimiento);
//        DatePickerDialog.OnDateSetListener dateDesde = createDateSetListener(mfechaDesde);
//        DatePickerDialog.OnDateSetListener dateHasta = createDateSetListener(mfechaHasta);
        DatePickerDialog.OnDateSetListener dateDesde = createDateSetListener(mfechaDesde, mCalendar1);
        DatePickerDialog.OnDateSetListener dateHasta = createDateSetListener(mfechaHasta, mCalendar2);

        mDatePickerDialog = new DatePickerDialog(this, dateNacimiento, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog1 = new DatePickerDialog(this, dateDesde, mCalendar1.get(Calendar.YEAR), mCalendar1.get(Calendar.MONTH), mCalendar1.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog2 = new DatePickerDialog(this, dateHasta, mCalendar2.get(Calendar.YEAR), mCalendar2.get(Calendar.MONTH), mCalendar2.get(Calendar.DAY_OF_MONTH));


        mfechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog.show();
            }
        });

        mfechaDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog1.show();
            }
        });

        mfechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog2.show();
            }
        });
    }

    private DatePickerDialog.OnDateSetListener createDateSetListener(final TextView textView, final Calendar calendar) {
        return new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                anioCalendar = year;
                mesCalendar = month;
                diaCalendar = dayOfMonth;
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT, new Locale("es", "ES"));
                textView.setText(sdf.format(calendar.getTime()));
            }
        };
    }

    private DatePickerDialog.OnDateSetListener createDateSetListener(final TextView textView) {
        return new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                anioCalendar = year;
                mesCalendar = month;
                diaCalendar = dayOfMonth;
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT, new Locale("es", "ES"));
                textView.setText(sdf.format(mCalendar.getTime()));
            }
        };
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Manejar los clics de los elementos del menú de la ActionBar
        switch (item.getItemId()) {
            case android.R.id.home:
                // Acción cuando se hace clic en el botón de retroceso
                onBackPressed(); // Esto cierra la actividad actual y vuelve a la actividad anterior
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
