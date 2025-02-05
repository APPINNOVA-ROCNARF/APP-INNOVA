package com.rocnarf.rocnarf;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;

import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.models.Clientes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class VisitaImpulsadoraCrearActivity extends AppCompatActivity
        implements ClienteDetalleFragment.OnFragmentInteractionListener {
    Context context;
    private TextInputEditText mFecha, mHoraInicio, mHoraFin, mValorF2, mValorF3, mValorGEN;
    private Spinner mAsesor;
    private String idUsuario, codigoCliente, sccion ;
    private Calendar mCalendar;
    private DatePickerDialog mDatePickerDialog;
    private TimePickerDialog mTimePickerDialog, TimePickerDialogHoraFin;
    private Button mAgregarVisita;

    private int anioCalendar, mesCalendar, diaCalendar, horaTimePicker, minutosTimePicker;
    private String nombreCliente;
    private String direccion;
    private Double latitud, longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visita_impulsadora_crear);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        Intent intent = getIntent();
        codigoCliente = intent.getStringExtra(Common.ARG_IDCLIENTE);
        idUsuario = intent.getStringExtra(Common.ARG_IDUSUARIO);
        //idLocal= intent.getIntExtra("idLocal", 0);
        configurarControles();
    }

    private void configurarControles() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ClienteDetalleFragment detalleCliente = ClienteDetalleFragment.newInstance(codigoCliente, idUsuario);
        ft.replace(R.id.fm_cliente_activity_planificacion_crear, detalleCliente);
        ft.commit();

        mFecha = (TextInputEditText) findViewById(R.id.et_fecha_content_visita_impulsadora_crear);
        setDateField();
        mHoraInicio  = (TextInputEditText) findViewById(R.id.et_hora_inicio_content_visita_impulsadora_crear);
        setTimeFieldHoraInicio();
        mHoraFin= (TextInputEditText) findViewById(R.id.et_hora_fin_content_visita_impulsadora_crear);
        setTimeFieldHoraFin();

        List<String> list = new ArrayList<String>();
        list.add("IPACARAI");
        list.add("IVETTE PEREZ Z");
        list.add("I. SUAREZ M.");

        mAsesor = (Spinner)findViewById(R.id.sp_content_visita_impulsadora_crear);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAsesor.setAdapter(dataAdapter);


        mAgregarVisita = (Button)findViewById(R.id.btn_agregar_content_visita_impulsadora_crear);
        mAgregarVisita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
//                    if (Validar()) {
//                        CrearVisita();
                        Toast.makeText(context, "La visita se ha agregado con extito", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(context, VisitasImpulsadorasActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
  //                  }
                }catch (Exception ex){
                    Toast.makeText(context, "A ocuriido un error al intentar agregar la visita " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    private void setDateField(){
        mCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                anioCalendar = year; mesCalendar = month; diaCalendar = dayOfMonth;
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT, Locale.US);
                mFecha.setText(sdf.format(mCalendar.getTime()));
            }};
        mDatePickerDialog = new DatePickerDialog(this, date, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));

        mFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog.show();
            }
        });
    }


    private void setTimeFieldHoraInicio(){
        mCalendar = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener hora = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                mHoraInicio.setText(String.valueOf(i) + ":" + (i1 > 9 ? String.valueOf(i1): "0"+ String.valueOf(i1)));
                horaTimePicker = i;
                minutosTimePicker = i1;
            }
        } ;
        mTimePickerDialog= new TimePickerDialog(this, hora, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE),  DateFormat.is24HourFormat(this));
        mHoraInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimePickerDialog.show();
            }
        });
    }

    private void setTimeFieldHoraFin(){
        mCalendar = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener hora = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                mHoraFin.setText(String.valueOf(i) + ":" + (i1 > 9 ? String.valueOf(i1): "0"+ String.valueOf(i1)));
                horaTimePicker = i;
                minutosTimePicker = i1;
            }
        } ;
        TimePickerDialogHoraFin= new TimePickerDialog(this, hora, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE),  DateFormat.is24HourFormat(this));
        mHoraFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialogHoraFin.show();
            }
        });
    }

    @Override
    public void onFragmentInteraction(Clientes cliente) {
        nombreCliente = cliente.getNombreCliente();
        direccion = cliente.getDireccion();
        latitud = cliente.getLatitud();
        longitud = cliente.getLatitud();
    }

}
