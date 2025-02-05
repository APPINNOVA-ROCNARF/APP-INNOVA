package com.rocnarf.rocnarf;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.RutasService;
import com.rocnarf.rocnarf.models.LiquidacionObsequio;
import com.rocnarf.rocnarf.models.Rutas;
import com.rocnarf.rocnarf.viewmodel.ClientesCupoCreditoViewModel;
import com.rocnarf.rocnarf.viewmodel.ResultadoVisitaViewModel;

import java.io.Serializable;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RutasCrearActivity extends AppCompatActivity implements Serializable {


    private ClientesCupoCreditoViewModel clientesCupoCreditoViewModel;
    private ResultadoVisitaViewModel resultadoVisitaViewModel;
    public static final String argCliente ="";
    public static final String argIdCliente ="";
    private String idCliente, idUsuario, idFactura, nombrePlan, descripcionPlan,seccion;
    private TextView mNombre, mDescripcion, mNombreCliente, mCodigoCliente,facturas, protestos, chequesAFechas, cupoDisponible;
    private Button bt_enviar_ruta, bt_enviar_Liquidacion;
    public int idPlan,id;
    private EditText mObsequio, mObservacion;
    private List<LiquidacionObsequio> liquidacionObsequio;
    private List<Rutas> mValues;
    private boolean primera;
//    public List<LiquidacionObsequio> liquidacionObsequios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_rutas);

        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion= i.getStringExtra(Common.ARG_SECCIOM);


        mNombre = (EditText)findViewById(R.id.et_nombre_ruta);
        mDescripcion =  (EditText) findViewById(R.id.et_descripcion_ruta);
        bt_enviar_ruta = (Button) findViewById(R.id.tv_enviar_ruta);

//        LiquidacionObsequio liquidacionObsequio = (LiquidacionObsequio)i.getSerializableExtra("LiquidacionObsequio");
////        Log.d("myTag", "dijo getIdCodigo ----->" + liquidacionObsequio.getId());
//        if(liquidacionObsequio == null){
            primera = true;
//        }else{
//            primera = false;
//            btLiquidacionObs.setVisibility(View.GONE);
//            bt_enviar_Liquidacion.setVisibility(View.VISIBLE);
//            mObsequio.setText(liquidacionObsequio.getObsequio());
//            mObservacion.setText(liquidacionObsequio.getObservacion());
//            mCodigoCliente.setText(liquidacionObsequio.getIdCliente());
//            mNombreCliente.setText(liquidacionObsequio.getNombreCliente());
//            id = liquidacionObsequio.getId();
//        }


        bt_enviar_ruta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNombre.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Ingrese Nombre", Toast.LENGTH_LONG).show();
                    return;
                }


                final Rutas rutas = new Rutas();

                if (primera) {
                    rutas.setNombre(mNombre.getText().toString());
                    rutas.setDescripcion(mDescripcion.getText().toString());
//                    Intent in = new Intent();
//                    setResult(RESULT_OK, in);
//                    finish();

                    RutasService service = ApiClient.getClient().create(RutasService.class);
                    service.CrearRuta(rutas)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<Rutas>() {
                                @Override
                                public void onCompleted() {
                                Intent in = new Intent();
                                setResult(RESULT_OK, in);
                                finish();
                                    Toast.makeText(getApplicationContext(), "Ruta Guardada ", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(getApplicationContext(), "Ruta No Fue Generada", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onNext(Rutas rutas) {

                                }


                            });

                }
            }
        });


    }


//    @Override public void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK){
//            mCodigoCliente.setText(data.getStringExtra("argIdCliente"));
//            mNombreCliente.setText(data.getStringExtra("argCliente"));
//        } else if (resultCode == RESULT_CANCELED) {
//
//        }
//    }

}
