package com.rocnarf.rocnarf;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.HistorialViaticoAdapter;
import com.rocnarf.rocnarf.adapters.QuejaAdapter;
import com.rocnarf.rocnarf.adapters.ViaticoAdapter;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.models.Estadistica;
import com.rocnarf.rocnarf.models.HistorialComentarios;
import com.rocnarf.rocnarf.models.HistorialViatico;
import com.rocnarf.rocnarf.models.LiquidacionObsequio;
import com.rocnarf.rocnarf.models.PedidosPendiente;
import com.rocnarf.rocnarf.models.Quejas;
import com.rocnarf.rocnarf.models.Viatico;
import com.rocnarf.rocnarf.models.ViaticoAdd;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegistroViaticoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, nombreCliente, seccion;
    private List<PedidosPendiente> listaPedidosPendiente;
    private Context context;
    private QuejaAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar pgsBar;
    private List<HistorialComentarios> listaComentario;
    private String ciclo, concepto;
    private TextView mCiclo,mConcepto;
    private Bitmap imageBitmap;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_SELECT_IMAGE = 2;
    private boolean mCameraPermissionGranted = false;

    private ImageView ivSubeImagen;
    private ImageView ivTomaFoto, FotoSeleccionda;
    private Button btnGuardarVia;
    private LinearLayout layout;
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
    private static final int REQUEST_TAKE_PHOTO = 1;
    private TextInputEditText etRucViatico,etRazonSocialViatico,etFacturaViatico,etSubtotalViatico,etSubtotalIvaViatico,etPlacaViatico,etTotalViatico,mFechaFactura;
    private static final int PERMISSIONS_REQUEST_CAMERA = 1;
    private Uri photoURI;
    private String esHistorial = "N";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_viatico);
        Intent i = getIntent();

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

        getCameraPermission();

        aLodingDialog = new ALodingDialog(this);


        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion = i.getStringExtra(Common.ARG_SECCIOM);
        ciclo = i.getStringExtra(Common.ARG_NOMBRE_CICLO);
        concepto = i.getStringExtra(Common.ARG_NOMBRE_CONCEPTO);
        idciclo =  Integer.parseInt(i.getStringExtra(Common.ARG_ID_CICLO));
        idconcepto = Integer.parseInt(i.getStringExtra(Common.ARG_ID_CONCEPTO));
        esHistorial =  i.getStringExtra(Common.ARG_HISTORIAL);
        layout = (LinearLayout) findViewById(R.id.layout_placa);

        if(idconcepto == 128){
            layout.setVisibility(View.VISIBLE);
        }else layout.setVisibility(View.GONE);

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

        mFechaFactura = (TextInputEditText) findViewById(R.id.et_fecha_factura_viatico);
        setDateField();

        etRazonSocialViatico = findViewById(R.id.et_razonsocial_viatico);
        etFacturaViatico = findViewById(R.id.et_factura_viatico);
        etSubtotalViatico = findViewById(R.id.et_subtotal_viatico);
        etSubtotalIvaViatico = findViewById(R.id.et_subtotal_iva_viatico);
        etTotalViatico = findViewById(R.id.et_total_viatico);
        etPlacaViatico = findViewById(R.id.et_placa_viatico);
        FotoSeleccionda = findViewById(R.id.imageV_foto_viatico);
        etSubtotalViatico.addTextChangedListener(textWatcher);
        etSubtotalIvaViatico.addTextChangedListener(textWatcher);

        if(viaticoEdit != null){
            llenarDatos(viaticoEdit);
            primera= false;
        }

        if (estadoCiclo == 1) { // Si el ciclo ha sido enviado
            if (estadoViatico == 1 || estadoViatico == 2) { // En revisión o aprobado
                disableFields(etRucViatico, etRazonSocialViatico, etFacturaViatico,etTotalViatico,
                        etSubtotalViatico, etSubtotalIvaViatico, etPlacaViatico, mFechaFactura, btnGuardarVia);
            }
            // Rechazado: Los campos permanecen habilitados
        } else if (estadoCiclo == 0) { // Si el ciclo no ha sido enviado
            // Permitir edición
            enableFields(etRucViatico, etRazonSocialViatico, etFacturaViatico, etTotalViatico,
                    etSubtotalViatico, etSubtotalIvaViatico, etPlacaViatico, mFechaFactura, btnGuardarVia);
        }

        btnGuardarVia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etRucViatico.getText().toString().isEmpty()) {
                    etRucViatico.setError("Por favor ingrese el RUC");
                    return;
                }

                if(etRucViatico.getText().toString().length() < 13){
                    etRucViatico.setError("Por favor ingrese 13 digitos");
                    return;
                }

                if(idconcepto == 128){
                    if (etPlacaViatico.getText().toString().isEmpty()) {
                        etPlacaViatico.setError("Por favor ingrese el RUC");
                        return;
                    }
                }

                if (mFechaFactura.getText().length() == 0) {
                    mFechaFactura.setError("Falta ingresar la fecha");
                    return;
                }

                if(imageBitmap == null && primera){
                    Toast.makeText(getApplicationContext(), "Favor ingresar imagen o foto", Toast.LENGTH_LONG).show();
                    return;
                }

                aLodingDialog.show();

                if(primera) {

                ViaticoAdd viatico = new ViaticoAdd();
                viatico.setRuc(etRucViatico.getText().toString());
                viatico.setIdCatalogo(idconcepto);
                viatico.setEstadoViatico(1);
                viatico.setEstatus(true);
                viatico.setIdUsuario(idUsuario);
                viatico.setFecha(new Date());
                mCalendar.set(anioCalendar, mesCalendar, diaCalendar, horaTimePicker, minutosTimePicker);
                viatico.setFechaFactura(mCalendar.getTime());
                viatico.setIdCiclo(idciclo);
                viatico.setComentario("Registro en revisión");
                viatico.setRazonSocial(etRazonSocialViatico.getText().toString());
                viatico.setNumeroFactura(etFacturaViatico.getText().toString());
                viatico.setSubTotal(Double.parseDouble(etSubtotalViatico.getText().toString()));
                if(etSubtotalIvaViatico.getText() == null || etSubtotalIvaViatico.getText().toString().isEmpty()) etSubtotalIvaViatico.setText("0");
                viatico.setSubTotalIva(Double.parseDouble(etSubtotalIvaViatico.getText().toString()));
                viatico.setTotal(Double.parseDouble(etTotalViatico.getText().toString()));
                viatico.setPlaca(etPlacaViatico.getText().toString());
                if (imageBitmap !=null){  viatico.setImagen(profileImage(imageBitmap));};
                    PlanesService service = ApiClient.getClient().create(PlanesService.class);
                             service.AddViatico(viatico)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<ViaticoAdd>() {
                                @Override
                                public void onCompleted() {
                                    Intent intent = new Intent();
                                    setResult(RESULT_OK, intent);
                                    Toast.makeText(getApplicationContext(), "Datos Guardados", Toast.LENGTH_LONG).show();
                                    aLodingDialog.cancel();
                                    finish();


                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(getApplicationContext(), "Error, intente nuevamente", Toast.LENGTH_LONG).show();
                                    aLodingDialog.cancel();
                                }

                                @Override
                                public void onNext(ViaticoAdd viatico1) {

                                }


                            });
                }else{

                    PlanesService service = ApiClient.getClient().create(PlanesService.class);
                    ViaticoAdd viaticoEditar = new ViaticoAdd();
                    viaticoEditar.setRuc(etRucViatico.getText().toString());
                    viaticoEditar.setIdCatalogo(idconcepto);
                    viaticoEditar.setEstadoViatico(1);
                    viaticoEditar.setEstatus(true);
                    viaticoEditar.setIdUsuario(idUsuario);
                    viaticoEditar.setFecha(new Date());
                    mCalendar.set(anioCalendar, mesCalendar, diaCalendar, horaTimePicker, minutosTimePicker);
                    viaticoEditar.setFechaFactura(mCalendar.getTime());
                    viaticoEditar.setIdCiclo(idciclo);
                    viaticoEditar.setRazonSocial(etRazonSocialViatico.getText().toString());
                    viaticoEditar.setNumeroFactura(etFacturaViatico.getText().toString());
                    viaticoEditar.setSubTotal(Double.parseDouble(etSubtotalViatico.getText().toString()));
                    if(etSubtotalIvaViatico.getText() == null || etSubtotalIvaViatico.getText().toString().isEmpty()) etSubtotalIvaViatico.setText("0");
                    viaticoEditar.setSubTotalIva(Double.parseDouble(etSubtotalIvaViatico.getText().toString()));
                    viaticoEditar.setTotal(Double.parseDouble(etTotalViatico.getText().toString()));
                    viaticoEditar.setPlaca(etPlacaViatico.getText().toString());
                    if (imageBitmap !=null){  viaticoEditar.setImagen(profileImage(imageBitmap));};
                    service.updateViatico(idViatico, viaticoEditar)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<ViaticoAdd>() {
                                @Override
                                public void onCompleted() {
                                    Intent intent = new Intent();
                                    setResult(RESULT_OK, intent);
                                    Toast.makeText(getApplicationContext(), "Datos Actualizados", Toast.LENGTH_LONG).show();
                                    aLodingDialog.cancel();
                                    finish();

                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(getApplicationContext(), "Error, intente nuevamente", Toast.LENGTH_LONG).show();
                                    aLodingDialog.cancel();
                                }

                                @Override
                                public void onNext(ViaticoAdd viatico1) {

                                }


                            });
                }

            }
        });

        ivSubeImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarImagen();
            }
        });

        ivTomaFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tomarFoto();
            }
        });
        //    CargaData();
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
                SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT, Locale.US);
                mFechaFactura.setText(sdf.format(mCalendar.getTime()));
            }
        };


        mDatePickerDialog = new DatePickerDialog(this, date, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));

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

    private void calcularTotal() {
        // Obtener los valores de los TextInputEditText

        String subtotalViaticoStr = etSubtotalViatico.getText().toString();
        String subtotalIvaViaticoStr = etSubtotalIvaViatico.getText().toString();

        // Convertir los valores a números
        double subtotalViatico = subtotalViaticoStr.isEmpty() ? 0 : Double.parseDouble(subtotalViaticoStr);
        double subtotalIvaViatico = subtotalIvaViaticoStr.isEmpty() ? 0 : Double.parseDouble(subtotalIvaViaticoStr);

        // Calcular el total
        double totalViatico = subtotalViatico + subtotalIvaViatico;

        // Actualizar el TextInputEditText de total
        etTotalViatico.setText(String.valueOf(totalViatico));
    }

    private void seleccionarImagen() {
        photoReciboFile = null;
        try {
            photoReciboFile = createImageFile();
        } catch (IOException ex) {
            Toast.makeText(RegistroViaticoActivity.this, "No se pudo crear archivo para poder tomar la foto"
                    , Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_SELECT_IMAGE);
    }

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
           //     Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(takePicture, 0);//zero can be replaced with any action code
        if (mCameraPermissionGranted) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


            // Asegúrate de que hay una actividad de cámara disponible
                // Crea el archivo donde se almacenará la foto
                try {
                    photoFile = createImageFile();
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(this,
                                "com.rocnarf.rocnarf.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
                } catch (IOException ex) {
                    // Error al crear el archivo de la imagen
                    Toast.makeText(this, "Error al crear archivo para la imagen", Toast.LENGTH_LONG).show();
                }

        } else {
            Toast.makeText(this, "No tienes permiso para usar la cámara", Toast.LENGTH_LONG).show();
        }


    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CAMERA_PERMISSION);
        } else {
            mCameraPermissionGranted = true;
            tomarFoto();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                // Decodifica la imagen desde el archivo y muestra en el ImageView
                imageBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                FotoSeleccionda.setImageBitmap(imageBitmap);
                // Aquí puedes ajustar la imagen si es necesario
                imageBitmap = decodeBitmap(imageBitmap, 400);
            } else if (requestCode == REQUEST_SELECT_IMAGE) {
                Uri selectedImageUri = data.getData();
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    FotoSeleccionda.setImageBitmap(imageBitmap);
                    imageBitmap = decodeBitmap(imageBitmap, 400);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected Bitmap decodeBitmap(Bitmap selectedImage, int REQUIRED_SIZE) {
        try {
            // Convertir el objeto Bitmap a un array de bytes
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, o);

            // The new size we want to scale to
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, o2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    protected Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE) {

        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

            // The new size we want to scale to
            // final int REQUIRED_SIZE =  size;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public void llenarDatos(Viatico viatico){
        aLodingDialog.show();
        etRucViatico.setText(viatico.getRuc());
        etRazonSocialViatico.setText(viatico.getRazonSocial());
        etFacturaViatico.setText(viatico.getNumeroFactura());
        etSubtotalViatico.setText(String.valueOf(viatico.getSubTotal()));
        etSubtotalIvaViatico.setText(String.valueOf(viatico.getSubTotalIva()));
        etTotalViatico.setText(String.valueOf(viatico.getTotal()));
        etPlacaViatico.setText(viatico.getPlaca());
        idViatico = viatico.getIdViatico();
        SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);
        mFechaFactura.setText(sdf.format(viatico.getFechaFactura()));
        String imageUrl = "http://200.105.252.218/rocnarf/api/viatico/imagen/" + idViatico;
        new DownloadImageTask().execute(imageUrl);

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

    private void getCameraPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            mCameraPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mCameraPermissionGranted = true;
                tomarFoto();  // Llamar a la función para tomar la foto si el permiso es concedido
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private byte[] profileImage(Bitmap b){

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();

    }

    final void CargaDataImagen() {
        PlanesService service = ApiClient.getClient().create(PlanesService.class);
        Call<List<HistorialViatico>> call = service.GetHistorialViatico(idUsuario);
        call.enqueue(new Callback<List<HistorialViatico>>() {
            @Override
            public void onResponse(Call<List<HistorialViatico>> call, Response<List<HistorialViatico>> response) {
                if (response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<List<HistorialViatico>> call, Throwable t) {
                //Log.d("sincronizar Clientes", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(strings[0])
                    .build();

            try {
                okhttp3.Response response = client.newCall(request).execute();
                InputStream inputStream = response.body().byteStream();
                return BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                // Muestra la imagen en el ImageView
                FotoSeleccionda.setImageBitmap(bitmap);
                aLodingDialog.cancel();
            } else {
                Toast.makeText(RegistroViaticoActivity.this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                aLodingDialog.cancel();

            }
        }
    }

    private void disableFields(View... fields) {
        for (View field : fields) {
            field.setEnabled(false);
        }
    }

    private void enableFields(View... fields) {
        for (View field : fields) {
            field.setEnabled(true);
        }
    }
}
