package com.rocnarf.rocnarf;

import android.Manifest;
import android.app.Activity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.models.Pedido;
import com.rocnarf.rocnarf.viewmodel.CobrosAPedidosViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import rx.Completable;
import rx.Subscription;

public class CobroActivity extends AppCompatActivity {
    private String idUsuario, idCliente, nombreCliente, seccion, tipoPedido;
    private Context context;
    private Pedido pedidoCliente;
    private TextInputLayout mContenedorRecibo2, mContenedorRecibo3, mContenedorRecibo4, mContenedorRecibo5;
    private TextInputLayout mContenedorFactura2, mContenedorFactura3, mContenedorFactura4, mContenedorFactura5;
    private LinearLayout mContenedorCheque2, mContenedorCheque3, mContenedorCheque4, mContenedorCheque5;

    private TextInputEditText mRecibo1, mRecibo2,mRecibo3,mRecibo4,mRecibo5;
    private TextInputEditText mFactura1, mFactura2,mFactura3,mFactura4,mFactura5;
    private TextInputEditText mCheque1, mCheque2,mCheque3,mCheque4,mCheque5;
    private TextInputEditText mValorCheque1, mValorCheque2,mValorCheque3,mValorCheque4,mValorCheque5;
    private TextInputEditText mFechaCheque1, mFechaCheque2,mFechaCheque3,mFechaCheque4,mFechaCheque5;
    private TextInputEditText mEfectivo, mNotaCredito,mObservaciones;

    private Button mGuardar, mCargar, mCargarFormaPago;
    private String[] facturasCobrar;

    private Bitmap bpRecibo, bpFormaPago;
    private byte[] photoRecibo;
    private ImageView mImagenRecibo, mImagenFormaPago;
    private TextView tvImagenRecibo, tvImagenFormaPago;
    private double vch1,vch2,vch3,vch4,vch5, vefe, totalEnvio;

    private Activity estaActividad;

    private static final int PERMISSIONS_REQUEST_CAMERA = 1;
    private boolean mCameraPermissionGranted = false;
    String currentPhotoPath;
    File photoReciboFile ;
    File photoFormaPagoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cobro);


        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion =  i.getStringExtra(Common.ARG_SECCIOM);
        idCliente =  i.getStringExtra(Common.ARG_IDCLIENTE);
        nombreCliente =  i.getStringExtra(Common.ARG_NOMBRE_CLIENTE);
        facturasCobrar = i.getStringArrayExtra(Common.ARG_FACTURAS_COBRAR);
        tipoPedido = i.getStringExtra(Common.ARG_TIPO_PEDIDO);
        context =this;
        estaActividad = this;

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(nombreCliente);
        actionBar.setSubtitle(idCliente);

        getCameraPermission();

        mContenedorRecibo2 = (TextInputLayout)findViewById(R.id.til_recibo2_activity_cobro);
        mContenedorRecibo3 = (TextInputLayout)findViewById(R.id.til_recibo3_activity_cobro);
        mContenedorRecibo4 = (TextInputLayout)findViewById(R.id.til_recibo4_activity_cobro);
        mContenedorRecibo5 = (TextInputLayout)findViewById(R.id.til_recibo5_activity_cobro);

        mContenedorFactura2 = (TextInputLayout)findViewById(R.id.til_factura2_activity_cobro);
        mContenedorFactura3 = (TextInputLayout)findViewById(R.id.til_factura3_activity_cobro);
        mContenedorFactura4 = (TextInputLayout)findViewById(R.id.til_factura4_activity_cobro);
        mContenedorFactura5 = (TextInputLayout)findViewById(R.id.til_factura5_activity_cobro);

        mContenedorCheque2 = (LinearLayout)findViewById(R.id.ll_cheque2_activity_cobro);
        mContenedorCheque3 = (LinearLayout)findViewById(R.id.ll_cheque3_activity_cobro);
        mContenedorCheque4 = (LinearLayout)findViewById(R.id.ll_cheque4_activity_cobro);
        mContenedorCheque5 = (LinearLayout)findViewById(R.id.ll_cheque5_activity_cobro);

        mRecibo1 = (TextInputEditText) findViewById(R.id.ti_recibo_activity_cobro);
        mRecibo2 = (TextInputEditText) findViewById(R.id.ti_recibo2_activity_cobro);
        mRecibo3 = (TextInputEditText)findViewById(R.id.ti_recibo3_activity_cobro);
        mRecibo4 = (TextInputEditText)findViewById(R.id.ti_recibo4_activity_cobro);
        mRecibo5 = (TextInputEditText)findViewById(R.id.ti_recibo5_activity_cobro);

        mFactura1 = (TextInputEditText) findViewById(R.id.ti_factura_activity_cobro);
        mFactura2 = (TextInputEditText) findViewById(R.id.ti_factura2_activity_cobro);
        mFactura3 = (TextInputEditText)findViewById(R.id.ti_factura3_activity_cobro);
        mFactura4 = (TextInputEditText)findViewById(R.id.ti_factura4_activity_cobro);
        mFactura5 = (TextInputEditText)findViewById(R.id.ti_factura5_activity_cobro);


        mCheque1 = (TextInputEditText)findViewById(R.id.ti_cheques_activity_cobro);
        mCheque2 = (TextInputEditText)findViewById(R.id.ti_cheques2_activity_cobro);
        mCheque3 = (TextInputEditText)findViewById(R.id.ti_cheques3_activity_cobro);
        mCheque4 = (TextInputEditText)findViewById(R.id.ti_cheques4_activity_cobro);
        mCheque5 = (TextInputEditText)findViewById(R.id.ti_cheques5_activity_cobro);

        mValorCheque1 = (TextInputEditText)findViewById(R.id.ti_valor_cheque_activity_cobro);
        mValorCheque2 = (TextInputEditText)findViewById(R.id.ti_valor2_cheque_activity_cobro);
        mValorCheque3 = (TextInputEditText)findViewById(R.id.ti_valor3_cheque_activity_cobro);
        mValorCheque4 = (TextInputEditText)findViewById(R.id.ti_valor4_cheque_activity_cobro);
        mValorCheque5 = (TextInputEditText)findViewById(R.id.ti_valor5_cheque_activity_cobro);

        mFechaCheque1 = (TextInputEditText)findViewById(R.id.ti_fecha_cheque_activity_cobro);
        mFechaCheque2 = (TextInputEditText)findViewById(R.id.ti_fecha2_cheque_activity_cobro);
        mFechaCheque3 = (TextInputEditText)findViewById(R.id.ti_fecha3_cheque_activity_cobro);
        mFechaCheque4 = (TextInputEditText)findViewById(R.id.ti_fecha4_cheque_activity_cobro);
        mFechaCheque5 = (TextInputEditText)findViewById(R.id.ti_fecha5_cheque_activity_cobro);

        mEfectivo = (TextInputEditText)findViewById(R.id.ti_efectivo_activity_cobro);
        mNotaCredito = (TextInputEditText)findViewById(R.id.ti_nota_credito_activity_cobro);
        mObservaciones = (TextInputEditText)findViewById(R.id.ti_observaciones_activity_cobro);

        mFechaCheque1.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMAAAA";
            private Calendar cal = Calendar.getInstance();


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);

                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    mFechaCheque1.setText(current);
                    mFechaCheque1.setSelection(sel < current.length() ? sel : current.length());



                }
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        if (facturasCobrar != null) {
            for (int indice = 0; indice < facturasCobrar.length; indice++) {
                if (indice == 0) mFactura1.setText(facturasCobrar[indice]);
                if (indice == 1) {
                    mFactura2.setText(facturasCobrar[indice]);
                    mContenedorFactura2.setVisibility(View.VISIBLE);
                }
                if (indice == 2) {
                    mFactura3.setText(facturasCobrar[indice]);
                    mContenedorFactura3.setVisibility(View.VISIBLE);
                }
                if (indice == 3) {
                    mFactura4.setText(facturasCobrar[indice]);
                    mContenedorFactura4.setVisibility(View.VISIBLE);
                }
                if (indice == 4) {
                    mFactura5.setText(facturasCobrar[indice]);
                    mContenedorFactura5.setVisibility(View.VISIBLE);
                }
            }
        }

        mImagenRecibo = (ImageView)findViewById(R.id.iv_recibo_activity_cobro);
        mImagenFormaPago = (ImageView)findViewById(R.id.iv_forma_activity_pago);
        tvImagenRecibo = (TextView)findViewById(R.id.tv_recibo_activity_cobro);
        tvImagenFormaPago = (TextView)findViewById(R.id.tv_forma_activity_pago);
        final CobrosAPedidosViewModel cobrosAPedidosViewModel = ViewModelProviders.of( this).get(CobrosAPedidosViewModel.class);
        cobrosAPedidosViewModel.setIdUsuario(idUsuario);

        if (idCliente != null) {
            cobrosAPedidosViewModel.getPedido(idCliente, nombreCliente).subscribe(new Completable.CompletableSubscriber() {
                @Override
                public void onCompleted() {
                    pedidoCliente =  cobrosAPedidosViewModel.pedido.getValue();
                    if (pedidoCliente != null)
                        mObservaciones.setText(pedidoCliente.getObservaciones());

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onSubscribe(Subscription d) {

                }
            });
        }


        cobrosAPedidosViewModel.sincronizado.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s.equals("OK")){
                    Intent i = new Intent(getApplicationContext(), PedidoListaActivity.class);
                    i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    i.putExtra(Common.ARG_SECCIOM, seccion);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);

                    Toast.makeText(getApplicationContext(), "El pedido ha sido enviado", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                }
            }
        });

        mGuardar = (Button)findViewById(R.id.bt_guardar_activity_cobro);
        mGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                totalEnvio=0;vch1=0;vch2=0;vch3=0;vch4=0;vch5=0;vefe=0;

                if (!mValorCheque1.getText().toString().isEmpty()) vch1 = (Double.parseDouble(mValorCheque1.getText().toString()));
                if (!mValorCheque2.getText().toString().isEmpty()) vch2 = (Double.parseDouble(mValorCheque2.getText().toString()));
                if (!mValorCheque3.getText().toString().isEmpty()) vch3 = (Double.parseDouble(mValorCheque3.getText().toString()));
                if (!mValorCheque4.getText().toString().isEmpty()) vch4 = Double.parseDouble(mValorCheque4.getText().toString());
                if (!mValorCheque5.getText().toString().isEmpty()) vch5 = (Double.parseDouble(mValorCheque5.getText().toString()));
                if (!mEfectivo.getText().toString().isEmpty()) vefe = (Double.parseDouble(mEfectivo.getText().toString()));

                totalEnvio = vch1 + vch2 + vch3 + vch4 + vch5 + vefe;

                //Alert dialog

                AlertDialog.Builder builder = new AlertDialog.Builder(CobroActivity.this);
                builder.setMessage("¿Desea enviar cobro de $" + totalEnvio)
                        // action buttons
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                if (pedidoCliente == null) {
                                    pedidoCliente = cobrosAPedidosViewModel.addPedido(idCliente, nombreCliente);
                                }

                                if(mRecibo1.getText().toString().isEmpty())
                                {
                                    Toast.makeText(getApplicationContext(), "Debe llenar datos del recibo."
                                            , Toast.LENGTH_LONG).show();
                                    return;
                                }
                                else
                                {
                                    pedidoCliente.setReciboCobro(mRecibo1.getText().toString());
                                }

                                if ( mContenedorRecibo2.getVisibility() == View.VISIBLE)
                                {
                                    if(mRecibo2.getText().toString().isEmpty())
                                    {
                                        Toast.makeText(getApplicationContext(), "Debe llenar datos del recibo # 2."
                                                , Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    else
                                    {
                                        pedidoCliente.setReciboCobro1(mRecibo2.getText().toString());
                                    }
                                }

                                if ( mContenedorRecibo3.getVisibility() == View.VISIBLE)
                                {
                                    if(mRecibo3.getText().toString().isEmpty())
                                    {
                                        Toast.makeText(getApplicationContext(), "Debe llenar datos del recibo # 3."
                                                , Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    else
                                    {
                                        pedidoCliente.setReciboCobro2(mRecibo3.getText().toString());
                                    }
                                }

                                if ( mContenedorRecibo4.getVisibility() == View.VISIBLE)
                                {
                                    if(mRecibo4.getText().toString().isEmpty())
                                    {
                                        Toast.makeText(getApplicationContext(), "Debe llenar datos del recibo # 4."
                                                , Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    else
                                    {
                                        pedidoCliente.setReciboCobro3(mRecibo4.getText().toString());
                                    }
                                }

                                if ( mContenedorRecibo5.getVisibility() == View.VISIBLE)
                                {
                                    if(mRecibo5.getText().toString().isEmpty())
                                    {
                                        Toast.makeText(getApplicationContext(), "Debe llenar datos del recibo # 5."
                                                , Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    else
                                    {
                                        pedidoCliente.setReciboCobro4(mRecibo5.getText().toString());
                                    }
                                }

                                pedidoCliente.setFactura(mFactura1.getText().toString());
                                pedidoCliente.setFactura1(mFactura2.getText().toString());
                                pedidoCliente.setFactura2(mFactura3.getText().toString());
                                pedidoCliente.setFactura3(mFactura4.getText().toString());
                                pedidoCliente.setFactura4(mFactura5.getText().toString());

                                pedidoCliente.setCheques(mCheque1.getText().toString());
                                pedidoCliente.setCheque1(mCheque2.getText().toString());
                                pedidoCliente.setCheque2(mCheque3.getText().toString());
                                pedidoCliente.setCheque3(mCheque4.getText().toString());
                                pedidoCliente.setCheque4(mCheque5.getText().toString());

                                pedidoCliente.setFechaCheque(mFechaCheque1.getText().toString());
                                pedidoCliente.setFechaCheque1(mFechaCheque2.getText().toString());
                                pedidoCliente.setFechaCheque2(mFechaCheque3.getText().toString());
                                pedidoCliente.setFechaCheque3(mFechaCheque4.getText().toString());
                                pedidoCliente.setFechaCheque4(mFechaCheque5.getText().toString());

                                if (!mValorCheque1.getText().toString().isEmpty()) pedidoCliente.setValorCheque(Double.parseDouble(mValorCheque1.getText().toString()));
                                if (!mValorCheque2.getText().toString().isEmpty()) pedidoCliente.setValorCheque1(Double.parseDouble(mValorCheque2.getText().toString()));
                                if (!mValorCheque3.getText().toString().isEmpty()) pedidoCliente.setValorCheque2(Double.parseDouble(mValorCheque3.getText().toString()));
                                if (!mValorCheque4.getText().toString().isEmpty()) pedidoCliente.setValorCheque3(Double.parseDouble(mValorCheque4.getText().toString()));
                                if (!mValorCheque5.getText().toString().isEmpty()) pedidoCliente.setValorCheque4(Double.parseDouble(mValorCheque5.getText().toString()));

                                if (!mEfectivo.getText().toString().isEmpty())
                                    pedidoCliente.setEfectvo(Double.parseDouble(mEfectivo.getText().toString()));
                                pedidoCliente.setNotaCredito(mNotaCredito.getText().toString());
                                pedidoCliente.setObservaciones(mObservaciones.getText().toString());

                                if (bpRecibo !=null)  pedidoCliente.setImagen1(profileImage(bpRecibo));
                                if (bpFormaPago !=null)  pedidoCliente.setImagen2(profileImage(bpFormaPago));
                                cobrosAPedidosViewModel.updatePedido(pedidoCliente);
                                if (tipoPedido.equals(Common.TIPO_PEDIDO_COBRO)) {
                                    cobrosAPedidosViewModel.sincronizado.observe(CobroActivity.this, new Observer<String>() {
                                        @Override
                                        public void onChanged(@Nullable String s) {
                                            if (s.equals("OK")){
                                                pedidoCliente.setEstado(Common.PED_SINCRONIZADO);
                                                cobrosAPedidosViewModel.updatePedido(pedidoCliente);
                                                Intent i = new Intent(getApplicationContext(), PedidoListaActivity.class);
                                                i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                                i.putExtra(Common.ARG_SECCIOM, seccion);
                                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(i);

                                                Toast.makeText(getApplicationContext(), "El Cobro ha sido enviado", Toast.LENGTH_LONG).show();
                                            }else {
                                                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                                                mGuardar.setEnabled(true);
                                                mCargar.setEnabled(true);
                                                mCargarFormaPago.setEnabled(true);
                                                mObservaciones.setEnabled(true);
                                            }
                                        }
                                    });

                                    cobrosAPedidosViewModel.Sync();

                                }
                                else {
                                    // Si el origen es pedido entonces se regresa  a la pantalla de pedidos para que el usuario la acepte
                                    Intent i = new Intent(getApplicationContext(), PedidoSimpleActivity.class);
                                    i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                    i.putExtra(Common.ARG_SECCIOM, seccion);
                                    i.putExtra(Common.ARG_NOMBRE_CLIENTE, idCliente);
                                    i.putExtra(Common.ARG_IDPEDIDO, pedidoCliente.getIdLocalPedido());
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                }

                                //Se notifica el envio
                                Toast.makeText(CobroActivity.this, "Registro de Cobro listo para ser Enviado....."
                                        , Toast.LENGTH_LONG).show();

                                mGuardar.setEnabled(false);
                                mCargar.setEnabled(false);
                                mCargarFormaPago.setEnabled(false);
                                mObservaciones.setEnabled(false);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // remove the dialog from the screen
                            }
                        })
                        .show();
            }
        });

        /////////////////////////////////////////////////////////////
        // toma de fotos


        mCargar = (Button)findViewById(R.id.bt_cargar_activity_cobro);
        mCargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (mCameraPermissionGranted) {

                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePicture.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    photoReciboFile = null;
                    try {
                        photoReciboFile = createImageFile();
                    } catch (IOException ex) {
                        Toast.makeText(CobroActivity.this, "No se pudo crear archivo para poder tomar la foto"
                                , Toast.LENGTH_LONG).show();
                    }
                    // Continue only if the File was successfully created
                    if (photoReciboFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(CobroActivity.this,
                                "com.rocnarf.rocnarf.fileprovider",
                                photoReciboFile );
                        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePicture, 1);//zero can be replaced with any action code
                    }
                }
            }
            else {
                Toast.makeText(CobroActivity.this, "No tienes permiso para usar la camara en la aplicacion"
                        , Toast.LENGTH_LONG).show();
            }
//                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
            }
        });

        mCargarFormaPago = (Button)findViewById(R.id.bt_forma_activity_pago);
        mCargarFormaPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCameraPermissionGranted) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePicture.resolveActivity(getPackageManager()) != null) {
                        // Create the File where the photo should go
                        photoFormaPagoFile  = null;
                        try {
                            photoFormaPagoFile = createImageFile();
                        } catch (IOException ex) {
                            Toast.makeText(CobroActivity.this, "No se pudo crear archivo para poder tomar la foto"
                                    , Toast.LENGTH_LONG).show();
                        }
                        // Continue only if the File was successfully created
                        if (photoFormaPagoFile != null) {
                            Uri photoURI = FileProvider.getUriForFile(CobroActivity.this,
                                    "com.rocnarf.rocnarf.fileprovider",
                                    photoFormaPagoFile);
                            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            startActivityForResult(takePicture, 2);//zero can be replaced with any action code
                        }
                    }
                }
                else {
                    Toast.makeText(CobroActivity.this, "No tienes permiso para usar la camara en la aplicacion"
                            , Toast.LENGTH_LONG).show();
                }
//
//                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(takePicture , 2);//one can be replaced with any action code
            }
        });

    }



    private void getCameraPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            mCameraPermissionGranted  = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mCameraPermissionGranted  = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mCameraPermissionGranted  = true;
                }
            }
        }
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


    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 1:
                if(resultCode == RESULT_OK){
                    bpRecibo = decodeUri(Uri.fromFile(photoReciboFile), 400);
                    mCargar.setBackgroundColor(Color.GREEN);

                    //Bundle extras = imageReturnedIntent.getExtras();
                    //Bitmap imageBitmap = (Bitmap) extras.get("data");
                    //mImagenRecibo.setImageBitmap(imageBitmap);
                    //mImagenRecibo.setImageBitmap(bpRecibo);
                    tvImagenRecibo.setText("OK");
                    Toast.makeText(this, "Imagen cargada", Toast.LENGTH_LONG).show();
                }
                break;
            case 2:
                if(resultCode == RESULT_OK){
                    bpFormaPago = decodeUri(Uri.fromFile(photoFormaPagoFile), 400);
                    mCargarFormaPago.setBackgroundColor(Color.GREEN);
                    tvImagenFormaPago.setText("OK");
                    Toast.makeText(this, "Imagen cargada", Toast.LENGTH_LONG).show();
                }
                break;


        }
    }



    //COnvert and resize our image to 400dp for faster uploading our images to DB
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


    //Convert bitmap to bytes
    //@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private byte[] profileImage(Bitmap b){

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();

    }

    public void onAgregarRecibo(View view) {
        if (mContenedorRecibo2.getVisibility() == View.GONE) {
            mContenedorRecibo2.setVisibility(View.VISIBLE);
            return;
        }
        if (mContenedorRecibo3.getVisibility() == View.GONE) {
            mContenedorRecibo3.setVisibility(View.VISIBLE);
            return;
        }
        if (mContenedorRecibo4.getVisibility() == View.GONE) {
            mContenedorRecibo4.setVisibility(View.VISIBLE);
            return;
        }
        if (mContenedorRecibo5.getVisibility() == View.GONE) {
            mContenedorRecibo5.setVisibility(View.VISIBLE);
            return;
        }
    }


    public void onAgregarFactura(View view) {
        if (mContenedorFactura2.getVisibility() == View.GONE) {
            mContenedorFactura2.setVisibility(View.VISIBLE);
            return;
        }
        if (mContenedorFactura3.getVisibility() == View.GONE) {
            mContenedorFactura3.setVisibility(View.VISIBLE);
            return;
        }
        if (mContenedorFactura4.getVisibility() == View.GONE) {
            mContenedorFactura4.setVisibility(View.VISIBLE);
            return;
        }
        if (mContenedorFactura5.getVisibility() == View.GONE) {
            mContenedorFactura5.setVisibility(View.VISIBLE);
            return;
        }

    }


    public void onAgregarCheque(View view) {
        if (mContenedorCheque2.getVisibility() == View.GONE) {
            mContenedorCheque2.setVisibility(View.VISIBLE);
            return;
        }
        if (mContenedorCheque3.getVisibility() == View.GONE) {
            mContenedorCheque3.setVisibility(View.VISIBLE);
            return;
        }
        if (mContenedorCheque4.getVisibility() == View.GONE) {
            mContenedorCheque4.setVisibility(View.VISIBLE);
            return;
        }
        if (mContenedorCheque5.getVisibility() == View.GONE) {
            mContenedorCheque5.setVisibility(View.VISIBLE);
            return;
        }

    }


}
