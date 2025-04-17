package com.rocnarf.rocnarf;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.helpers.DateHelper;
import com.rocnarf.rocnarf.helpers.ImageDownloader;
import com.rocnarf.rocnarf.models.Vehiculo;
import com.rocnarf.rocnarf.models.Viatico;
import com.rocnarf.rocnarf.validators.RucValidator;
import com.rocnarf.rocnarf.viewmodel.VehiculoViewModel;
import com.rocnarf.rocnarf.viewmodel.ViaticoViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class RegistroViaticoActivity extends AppCompatActivity {
    private String idUsuario;
    private Context context;
    private View viewVehiculo;
    private String ciclo, concepto;
    private TextView mCiclo,mConcepto, tvVehiculo, tvPlaca;
    private Spinner spPlacaViatico;
    private String placaSeleccionada;
    private List<String> listPlacas;
    private Bitmap imageBitmap;
    private static final int REQUEST_SELECT_IMAGE = 2;
    private ImageView ivSubeImagen;
    private ImageView ivTomaFoto, FotoSeleccionda;
    private Button btnGuardarVia;
    int idciclo,idconcepto,idViatico;
    boolean primera = true;
    private Calendar mCalendar;

    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private int anioCalendar, mesCalendar, diaCalendar, horaTimePicker, minutosTimePicker;
    private DatePickerDialog mDatePickerDialog;

    File photoFile;
    String currentPhotoPath;
    File photoReciboFile ;
    private ALodingDialog aLodingDialog;
    private TextInputEditText etRucViatico,etRazonSocialViatico,etFacturaViatico,etSubtotalViatico,etSubtotalIvaViatico,etTotalViatico,mFechaFactura;
    private ViaticoViewModel viaticoViewModel;
    private VehiculoViewModel vehiculoViewModel;

    private String esHistorial = "N";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_viatico);

        Intent i = getIntent();

        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);

        //Inicializar View Models
        viaticoViewModel = new ViewModelProvider(this).get(ViaticoViewModel.class);
        vehiculoViewModel = new ViewModelProvider(this).get(VehiculoViewModel.class);

        observeViaticoViewModel();
        observeVehiculoViewModel();

        // Cargar las placas al iniciar la actividad
        vehiculoViewModel.cargarVehiculos(idUsuario);

        int estadoCiclo = i.getIntExtra(Common.ARG_ESTADO_CICLO, 0);
        int estadoViatico = i.getIntExtra(Common.ARG_ESTADO_VIATICO, 0);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            for (String key : extras.keySet()) {
                Object value = extras.get(key);
                Log.d("RegistroViaticoActivity", String.format("Key: %s, Value: %s", key, value));
            }
        } else {
            Log.d("RegistroViaticoActivity", "No extras received in the intent");
        }

        Viatico viaticoEdit = (Viatico)i.getSerializableExtra("Viatico");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ciclo = i.getStringExtra(Common.ARG_NOMBRE_CICLO);
        concepto = i.getStringExtra(Common.ARG_NOMBRE_CONCEPTO);
        idciclo =  Integer.parseInt(i.getStringExtra(Common.ARG_ID_CICLO));
        idconcepto = Integer.parseInt(i.getStringExtra(Common.ARG_ID_CONCEPTO));
        esHistorial =  i.getStringExtra(Common.ARG_HISTORIAL);

        context = this;

        mCiclo = (TextView) findViewById(R.id.ciclo_show);
        mConcepto = (TextView) findViewById(R.id.concepto_show);

        mCiclo.setText(ciclo);
        mConcepto.setText(concepto);
        ivSubeImagen = findViewById(R.id.iv_sube_imagen);
        ivTomaFoto = findViewById(R.id.iv_toma_foto);
        btnGuardarVia = (Button) findViewById(R.id.bt_guardar_viatico);


        if (esHistorial != null) {
            if (esHistorial.equals("S")) {
                btnGuardarVia.setVisibility(View.GONE);
                ivTomaFoto.setVisibility(View.GONE);
                ivSubeImagen.setVisibility(View.GONE);
            }
        }

        etRucViatico = findViewById(R.id.et_ruc_viatico);
        TextInputLayout tilRucViatico = findViewById(R.id.til_ruc_viatico);

        tilRucViatico.setEndIconMode(TextInputLayout.END_ICON_NONE);

        etRucViatico.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validarYMostrarIcono(tilRucViatico);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        //CAMPO FECHA
        mFechaFactura = findViewById(R.id.et_fecha_factura_viatico);
        TextInputLayout tilFechaViatico = findViewById(R.id.til_fecha_viatico);
        tilFechaViatico.setEndIconMode(TextInputLayout.END_ICON_NONE);
        setDateField(tilFechaViatico);

        etRazonSocialViatico = findViewById(R.id.et_razonsocial_viatico);
        etFacturaViatico = findViewById(R.id.et_factura_viatico);
        etSubtotalViatico = findViewById(R.id.et_subtotal_viatico);
        etSubtotalIvaViatico = findViewById(R.id.et_subtotal_iva_viatico);
        etTotalViatico = findViewById(R.id.et_total_viatico);
        etTotalViatico.setEnabled(false);

        spPlacaViatico = findViewById(R.id.spPlacaViatico);

        listPlacas = new ArrayList<>();

        spPlacaViatico.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                placaSeleccionada = listPlacas.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                placaSeleccionada = null;
            }
        });


        FotoSeleccionda = findViewById(R.id.imageV_foto_viatico);
        viewVehiculo = findViewById(R.id.view_vehiculo);
        tvVehiculo = findViewById(R.id.tv_vehiculo);
        tvPlaca = findViewById(R.id.tv_placa);
        etSubtotalViatico.addTextChangedListener(textWatcher);
        etSubtotalIvaViatico.addTextChangedListener(textWatcher);

        if(idconcepto == 128){
            spPlacaViatico.setVisibility(View.VISIBLE);
            viewVehiculo.setVisibility(View.VISIBLE);
            tvVehiculo.setVisibility(View.VISIBLE);
            tvPlaca.setVisibility(View.VISIBLE);
        }else{
            spPlacaViatico.setVisibility(View.GONE);
            viewVehiculo.setVisibility(View.GONE);
            tvVehiculo.setVisibility(View.GONE);
            tvPlaca.setVisibility(View.GONE);
        }

        if(viaticoEdit != null){
            llenarDatos(viaticoEdit);
            primera= false;
        }

        if (estadoCiclo == 1) { // Si el ciclo ha sido enviado
            if (estadoViatico == 1 || estadoViatico == 2) { // En revisión o aprobado
                disableFields(etRucViatico, etRazonSocialViatico, etFacturaViatico,
                        etSubtotalViatico, etSubtotalIvaViatico, spPlacaViatico, mFechaFactura, btnGuardarVia);
            }
            // Rechazado: Los campos permanecen habilitados
        } else if (estadoCiclo == 0) { // Si el ciclo no ha sido enviado
            // Permitir edición
            enableFields(etRucViatico, etRazonSocialViatico, etFacturaViatico,
                    etSubtotalViatico, etSubtotalIvaViatico, spPlacaViatico, mFechaFactura, btnGuardarVia);
        }

        btnGuardarVia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ruc = etRucViatico.getText().toString().trim();

                if (ruc.isEmpty()) {
                    etRucViatico.setError("Por favor ingrese el RUC");
                    return;
                }

                if (!RucValidator.validarRUC(ruc)) {
                    etRucViatico.setError("RUC inválido. Verifique los datos.");
                    return;
                }

                String fechaFactura = mFechaFactura.getText().toString();
                if (fechaFactura.isEmpty()) {
                    mFechaFactura.setError("Falta ingresar la fecha");
                    return;
                }

                // Validar la fecha antes de guardar
                if (!esFechaValida(fechaFactura)) {
                    mFechaFactura.setText(""); // Borrar la fecha inválida
                    mFechaFactura.setError("Fecha inválida. No puede ser en fin de semana.");
                    Toast.makeText(getApplicationContext(), "La fecha ingresada NO puede ser en fin de semana.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (imageBitmap == null) {
                    FotoSeleccionda.setDrawingCacheEnabled(true);
                    imageBitmap = Bitmap.createBitmap(FotoSeleccionda.getDrawingCache());
                    FotoSeleccionda.setDrawingCacheEnabled(false);
                }

                if (imageBitmap == null) {
                    Toast.makeText(getApplicationContext(), "Por favor ingrese imagen de la factura", Toast.LENGTH_LONG).show();
                    return;
                }

                if(idconcepto == 128){
                    if (placaSeleccionada == null || placaSeleccionada.isEmpty()) {
                        Toast.makeText(RegistroViaticoActivity.this, "Por favor, seleccione una placa", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Viatico viatico = construirViatico();

                if (primera) {
                    viaticoViewModel.guardarViatico(viatico);
                } else {
                    viaticoViewModel.actualizarViatico(idViatico, viatico);
                }
            }
        });

        ivSubeImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarImagen();
            }
        });

        ivTomaFoto.setOnClickListener(v -> solicitarPermisoCamara());

    }

    private void validarYMostrarIcono(TextInputLayout tilRucViatico) {
        String ruc = etRucViatico.getText().toString().trim();

        if (ruc.length() == 13 && RucValidator.validarRUC(ruc)) {
            tilRucViatico.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
            tilRucViatico.setEndIconDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check_circle_green_24dp));
        } else {
            etRucViatico.setError("RUC inválido. Verifique los datos.");
            tilRucViatico.setEndIconMode(TextInputLayout.END_ICON_NONE);
        }
    }

    private Viatico construirViatico() {
        Viatico viatico = new Viatico();
        viatico.setRuc(etRucViatico.getText().toString());
        viatico.setIdCatalogo(idconcepto);
        viatico.setEstadoViatico(1);
        viatico.setEstatus(1);
        viatico.setIdUsuario(idUsuario);
        viatico.setFecha(new Date());
        mCalendar.set(anioCalendar, mesCalendar, diaCalendar, horaTimePicker, minutosTimePicker);
        viatico.setFechaFactura(mCalendar.getTime());
        viatico.setIdCiclo(idciclo);
        viatico.setComentario("Registro en revisión");
        viatico.setRazonSocial(etRazonSocialViatico.getText().toString());
        viatico.setNumeroFactura(etFacturaViatico.getText().toString());
        viatico.setSubTotal(Double.parseDouble(etSubtotalViatico.getText().toString()));
        viatico.setSubTotalIva(etSubtotalIvaViatico.getText().toString().isEmpty() ? 0.0 : Double.parseDouble(etSubtotalIvaViatico.getText().toString()));
        viatico.setTotal(Double.parseDouble(etTotalViatico.getText().toString()));
        viatico.setPlaca(spPlacaViatico.getSelectedItem().toString());

        if (imageBitmap != null) {
            viatico.setImagen(profileImage(imageBitmap));
        }

        return viatico;
    }

    private void observeViaticoViewModel() {
        viaticoViewModel.getViaticoGuardado().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean guardado) {
                if (guardado != null && guardado) {
                    Toast.makeText(RegistroViaticoActivity.this, "Viático guardado con éxito", Toast.LENGTH_SHORT).show();
                    finish();  // Cerrar la actividad
                }
            }
        });

        viaticoViewModel.getErrorMensaje().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String mensaje) {
                if (mensaje != null) {
                    Toast.makeText(RegistroViaticoActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void observeVehiculoViewModel() {
        vehiculoViewModel.getVehiculos().observe(this, new Observer<List<Vehiculo>>() {
            @Override
            public void onChanged(List<Vehiculo> vehiculos) {
                if (vehiculos != null) {
                    listPlacas.clear();
                    for (Vehiculo vehiculo : vehiculos) {
                        listPlacas.add(vehiculo.getPlaca());
                    }
                    configurarSpinner();
                }
            }
        });

        vehiculoViewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String mensaje) {
                if (mensaje != null) {
                    Log.d("VehiculoViewModel", "El usuario no tiene placas registradas: " + mensaje);
                }
            }
        });

    }
    private void configurarSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listPlacas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPlacaViatico.setAdapter(adapter);

        // Si solo hay "No tiene placas registradas", deshabilitar la selección
        if (listPlacas.size() == 1 && listPlacas.get(0).equals("No tiene placas registradas")) {
            spPlacaViatico.setEnabled(false);
        } else {
            spPlacaViatico.setEnabled(true);
        }
    }


    private void setDateField(TextInputLayout tilFechaViatico) {
        mCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Configurar la fecha en el calendario
                anioCalendar = year;
                mesCalendar = month;
                diaCalendar = dayOfMonth;
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // Formatear la fecha seleccionada
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US); // Asegurar formato correcto
                String fechaSeleccionada = sdf.format(mCalendar.getTime());

                Log.d("DEBUG_FECHA", "Fecha seleccionada antes de validación: " + fechaSeleccionada);

                // Validar la fecha
                if (!esFechaValida(fechaSeleccionada)) {
                    tilFechaViatico.setErrorEnabled(true);
                    tilFechaViatico.setError("Fecha inválida. No puede ser en fin de semana.");
                    tilFechaViatico.setEndIconMode(TextInputLayout.END_ICON_NONE);
                } else {
                    Log.d("DEBUG_FECHA", "Fecha aceptada correctamente: " + fechaSeleccionada);
                    mFechaFactura.setError(null);
                    tilFechaViatico.setError(null);
                    tilFechaViatico.setErrorEnabled(false);
                    tilFechaViatico.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    tilFechaViatico.setEndIconDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_circle_green_24dp));
                }

                // Establecer la fecha en el campo de texto
                mFechaFactura.setText(fechaSeleccionada);
            }
        };

        mDatePickerDialog = new DatePickerDialog(this, date,
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH)
        );

        mFechaFactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerDialog.show();
            }
        });
    }


    // TextWatcher para escuchar cambios en los TextInputEditText
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // No se necesita hacer nada aquí
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Llamar al método para calcular el total cuando cambia el texto
            calcularTotal();
        }

        @Override
        public void afterTextChanged(Editable s) {
            // No se necesita hacer nada aquí
        }
    };

    private boolean esFechaValida(String fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US); // Formato explícito
        try {
            // Intentar parsear la fecha
            Date date = sdf.parse(fecha);
            if (date == null) {
                Log.e("DEBUG_FECHA", "Error: La fecha parseada es nula");
                return false;
            }

            // Configurar el calendario con la fecha parseada
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

            Log.d("DEBUG_FECHA", "Fecha convertida correctamente: " + fecha);
            Log.d("DEBUG_FECHA", "Día de la semana: " + dayOfWeek);

            // Si el concepto es 129 o 130, la fecha NO debe ser fin de semana
            if ((idconcepto == 129 || idconcepto == 130) && (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)) {
                Log.w("DEBUG_FECHA", "Fecha inválida: es fin de semana y el concepto es " + idconcepto);
                return false; // Fecha no válida
            }
        } catch (ParseException e) {
            Log.e("DEBUG_FECHA", "Error al parsear la fecha: " + fecha, e);
            return false; // Si ocurre un error en el parseo, la fecha es inválida
        }
        return true; // Fecha válida
    }



    private void calcularTotal() {
        double subtotalViatico = ParseDouble(etSubtotalViatico.getText().toString());
        double subtotalIvaViatico = ParseDouble(etSubtotalIvaViatico.getText().toString());

        double totalViatico = subtotalViatico + subtotalIvaViatico;
        etTotalViatico.setText(String.valueOf(totalViatico));
    }


    private double ParseDouble(String value) {
        try {
            return value.isEmpty() ? 0.0 : Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }


    private void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        seleccionarImagenLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> seleccionarImagenLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        try {
                            // ✅ Convertir la URI en un Bitmap correctamente
                            imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                            // ✅ Mostrar la imagen en el ImageView
                            FotoSeleccionda.setImageBitmap(imageBitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });


    private File createImageFile() throws IOException {
    // Create an image file name
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String imageFileName = "JPEG_" + timeStamp + "_";
    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    File image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",         /* suffix */
            storageDir      /* directory */
    );

    // Save a file: path for use with ACTION_VIEW intents
    currentPhotoPath = image.getAbsolutePath();
    return image;
}


    private void tomarFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // ✅ Verificar si hay una aplicación de cámara disponible
        if (takePictureIntent.resolveActivity(getPackageManager()) == null) {
            Toast.makeText(this, "No hay aplicación de cámara disponible", Toast.LENGTH_LONG).show();
            return;
        }

        // ✅ Crear el archivo donde se almacenará la imagen
        try {
            photoFile = createImageFile();
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.rocnarf.rocnarf.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                tomarFotoLauncher.launch(takePictureIntent);
            }
        } catch (IOException ex) {
            Toast.makeText(this, "Error al crear archivo para la imagen", Toast.LENGTH_LONG).show();
        }
    }


    private void solicitarPermisoCamara() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            tomarFoto();  // ✅ Ya tiene permiso, toma la foto inmediatamente
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            // ✅ Mostrar una explicación si el usuario ha denegado el permiso antes
            new AlertDialog.Builder(this)
                    .setTitle("Permiso de Cámara Necesario")
                    .setMessage("Esta aplicación necesita acceso a la cámara para tomar fotos de viáticos.")
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.CAMERA},
                                REQUEST_CAMERA_PERMISSION);
                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        } else {
            // ✅ Solicitar permiso directamente
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    private final ActivityResultLauncher<Intent> tomarFotoLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // ✅ Imagen tomada con éxito, cargarla en el ImageView
                    imageBitmap = decodeBitmap(photoFile.getAbsolutePath(), 400);
                    FotoSeleccionda.setImageBitmap(imageBitmap);
                } else {
                    Toast.makeText(this, "Foto no tomada", Toast.LENGTH_SHORT).show();
                }
            });


    private Bitmap decodeBitmap(String imagePath, int requiredSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        int widthTmp = options.outWidth, heightTmp = options.outHeight;
        int scale = 1;
        while (widthTmp / 2 >= requiredSize && heightTmp / 2 >= requiredSize) {
            widthTmp /= 2;
            heightTmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options finalOptions = new BitmapFactory.Options();
        finalOptions.inSampleSize = scale;
        return BitmapFactory.decodeFile(imagePath, finalOptions);
    }


    public void llenarDatos(Viatico viatico){
        etRucViatico.setText(viatico.getRuc());
        etRazonSocialViatico.setText(viatico.getRazonSocial());
        etFacturaViatico.setText(viatico.getNumeroFactura());
        etSubtotalViatico.setText(String.valueOf(viatico.getSubTotal()));
        etSubtotalIvaViatico.setText(String.valueOf(viatico.getSubTotalIva()));
        etTotalViatico.setText(String.valueOf(viatico.getTotal()));
        idViatico = viatico.getIdViatico();
        mFechaFactura.setText(DateHelper.formatDate(viatico.getFechaFactura()));
        String imageUrl = "http://200.105.252.218/rocnarf/api/viatico/imagen/" + idViatico;
        cargarImagenDesdeUrl(imageUrl);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            setSpinnerSelection(spPlacaViatico, viatico.getPlaca());
        }, 500);
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        if (spinner.getAdapter() == null) {
            return; // Si el adapter aún no está asignado, no hacer nada
        }

        for (int i = 0; i < spinner.getAdapter().getCount(); i++) {
            String item = spinner.getAdapter().getItem(i).toString().trim();
            if (item.equalsIgnoreCase(value.trim())) { // Comparación insensible a mayúsculas/minúsculas y espacios
                spinner.setSelection(i);
                break;
            }
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permiso concedido, tomar foto
                tomarFoto();
            } else {
                //Permiso denegado, informar al usuario
                Toast.makeText(this, "Permiso de cámara denegado. No puedes tomar fotos.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private byte[] profileImage(Bitmap b){

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();

    }

    private void cargarImagenDesdeUrl(String imageUrl) {
        ImageDownloader downloader = new ImageDownloader();
        downloader.descargarImagen(imageUrl, FotoSeleccionda);
    }

    private void disableFields(View... fields) {
        for (View field : fields) {
            field.setEnabled(false);
            field.setClickable(false);
            field.setFocusable(false);
            if (field instanceof Spinner) {
                field.setOnTouchListener((v, event) -> true);
            }
        }
    }



    private void enableFields(View... fields) {
        for (View field : fields) {
            field.setEnabled(true);
        }
    }
}
