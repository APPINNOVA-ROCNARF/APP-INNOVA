package com.rocnarf.rocnarf;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.ProductosReciclerViewAdapter;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.models.Clientes;
import com.rocnarf.rocnarf.models.EscalaBonificacion;
import com.rocnarf.rocnarf.models.LiquidacionObsequio;
import com.rocnarf.rocnarf.models.MotivoQuejas;
import com.rocnarf.rocnarf.models.Parametros;
import com.rocnarf.rocnarf.models.ParametrosResponse;
import com.rocnarf.rocnarf.models.PedidoDetalle;
import com.rocnarf.rocnarf.models.Producto;
import com.rocnarf.rocnarf.models.Quejas;
import com.rocnarf.rocnarf.viewmodel.PedidoViewModel;
import com.rocnarf.rocnarf.viewmodel.ProductosViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Completable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DetalleQuejaActivity  extends AppCompatActivity
        implements ClienteDetalleFragment.OnFragmentInteractionListener
{
    private String idUsuario, idCliente, nombreCliente, seccion;
    private Context context;
    private String  facturasQuejas, tipoCliente;
    private String direccion;
    private Double latitud, longitud;
    private Spinner mMotivo, mOpcionCliente, mOpcionFactura;
    private Button btnEnviarQueja,btnFactura;
    private TextView mObservacion,mListaFact;
    final ArrayList<String> listaNombre = new ArrayList<>();
    final ArrayList<MotivoQuejas> listaMotivos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queja_crear);
        CargarParametros();

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);

        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion = i.getStringExtra(Common.ARG_SECCIOM);
        idCliente = i.getStringExtra(Common.ARG_IDCLIENTE);
        nombreCliente = i.getStringExtra(Common.ARG_NOMBRE_CLIENTE);
        context = this;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Log.d("cliente","xcccc" + idCliente);
        ClienteDetalleFragment detalleCliente = ClienteDetalleFragment.newInstance(idCliente, idUsuario);
        btnEnviarQueja = (Button) findViewById(R.id.bt_enviar_queja);
        mOpcionCliente = (Spinner) findViewById(R.id.tv_opcion_queja_cliente);
        mObservacion = (TextView) findViewById(R.id.et_observacion_queja);
        mListaFact = (TextView) findViewById(R.id.tv_facturas_showm);
        mOpcionFactura = (Spinner) findViewById(R.id.tv_opcion_queja_factura);
        mMotivo = (Spinner) findViewById(R.id.tv_motivo_queja_crear);
        btnFactura = (Button) findViewById(R.id.bt_elegir_factura);

        final ArrayList<String> listaNombre = new ArrayList<>();
        listaNombre.add("Motivo 1");
        listaNombre.add("Motivo 2");
        listaNombre.add("Motivo 3");
        final ArrayAdapter adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaNombre);
        mMotivo.setAdapter(adaptador);

        final ArrayList<String> opcionClie = new ArrayList<>();
        opcionClie.add("NO");
        opcionClie.add("SI");
        final ArrayAdapter adaptador1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, opcionClie);
        mOpcionCliente.setAdapter(adaptador1);

        final ArrayList<String> opcionFac = new ArrayList<>();
        opcionFac.add("NO");
        opcionFac.add("SI");
        final ArrayAdapter adaptador2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, opcionFac);
        mOpcionFactura.setAdapter(adaptador2);
        ft.replace(R.id.fm_quejas_detalle_cliente, detalleCliente);
        ft.commit();
        final ActionBar actionBar = getSupportActionBar();
        if (nombreCliente != null) {
            if (!nombreCliente.isEmpty()) {
                actionBar.setTitle(nombreCliente);
                actionBar.setSubtitle(idCliente);
            }
        }

        // Agregar el Listener al Spinner
        mOpcionFactura.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Obtener el valor seleccionado del Spinner
                String valorSeleccionado = opcionFac.get(position);
                if(valorSeleccionado.equals("SI")){
                    btnFactura.setVisibility(View.VISIBLE);
                    mListaFact.setVisibility(View.VISIBLE);
                }else{
                    btnFactura.setVisibility(View.GONE);
                    mListaFact.setVisibility(View.GONE);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Este método se llama cuando no hay ningún elemento seleccionado
            }
        });

        btnFactura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), ClientesFacturasActivity.class);
                in.putExtra(Common.ARG_IDCLIENTE, idCliente);
                in.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                in.putExtra(Common.ARG_SECCIOM, seccion);
                in.putExtra(Common.ARG_NOMBRE_CLIENTE, nombreCliente);
                in.putExtra(Common.ARG_TIPO_PEDIDO, Common.TIPO_PEDIDO_COBRO);
                in.putExtra(Common.ARG_FACTURAS_SELECCION, true);
                in.putExtra(Common.ARG_FACTURAS_SELECCION_QUEJAS, true);
                startActivityForResult(in,1);
            }
        });

        btnEnviarQueja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mObservacion.getText().toString().isEmpty()){
                    Toast.makeText(context, "Campo observacion obligatorio", Toast.LENGTH_LONG).show();
                    return;
                }
                int posicionSeleccionada = mMotivo.getSelectedItemPosition();
                if (posicionSeleccionada == Spinner.INVALID_POSITION) {
                    Toast.makeText(context, "Campo motivo obligatorio", Toast.LENGTH_LONG).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myDialog));
                builder.setMessage("¿Desea registrar la queja?");

                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        final Quejas quejas = new Quejas();
                        quejas.setIdCliente(idCliente);

                        MotivoQuejas motivoSeleccionado = (MotivoQuejas) mMotivo.getSelectedItem();

                        Object itemSeleccionado = mMotivo.getSelectedItem();
                        quejas.setMotivo(String.valueOf(motivoSeleccionado.getId()));
                        quejas.setObservacion(mObservacion.getText().toString());
                        Object itemSeleccionadoCliente = mOpcionCliente.getSelectedItem();
                        Object itemSeleccionadoFactura = mOpcionFactura.getSelectedItem();
                        quejas.setOpcionCliente(itemSeleccionadoCliente == "SI" ? true : false);
                        quejas.setOpcionFactura(itemSeleccionadoFactura == "SI" ? true : false);
                        quejas.setIdUsuario(idUsuario);
                        quejas.setFacturas(facturasQuejas);
                        quejas.setEstado(true);
                        Date currentDate = Calendar.getInstance().getTime();
                        quejas.setFecha(currentDate);
                        PlanesService service = ApiClient.getClient().create(PlanesService.class);
                        service.AddQuejas(quejas)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Subscriber<Quejas>() {
                                    @Override
                                    public void onCompleted() {
                                        Intent i = new Intent(context, MainActivity.class);
                                        i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                        i.putExtra(Common.ARG_SECCIOM, seccion);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                        Toast.makeText(getApplicationContext(), "Queja Enviada con Exito", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(getApplicationContext(), "Queja no Fue Enviada Error", Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onNext(Quejas quejas1) {

                                    }


                                });


                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                builder.create().show();


            }
        });

    }

    public void CargarParametros() {
        PlanesService service = ApiClient.getClient().create(PlanesService.class);
        retrofit2.Call<ParametrosResponse> call  = service.GetJefes("MOTIVOS");
        call.enqueue(new Callback<ParametrosResponse>() {
            @Override
            public void onResponse(Call<ParametrosResponse> call, Response<ParametrosResponse> response) {
                if (response.isSuccessful()) {
                    ParametrosResponse parametrosResponse = response.body();
                    List<Parametros> parametros = parametrosResponse.items;

                    for(int indice = 0;indice<parametros.size();indice++){
                        listaMotivos.add(new MotivoQuejas(parametros.get(indice).getIdParametro(), parametros.get(indice).getValor()));
                    }

                    // Utiliza YourActivityName.this en lugar de this
                    final ArrayAdapter<MotivoQuejas> adaptador = new ArrayAdapter<>(DetalleQuejaActivity.this, android.R.layout.simple_list_item_1, listaMotivos);
                    mMotivo.setAdapter(adaptador);

                }
            }


            @Override
            public void onFailure(Call<ParametrosResponse> call, Throwable t) {

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public void onFragmentInteraction(Clientes cliente) {
        nombreCliente = cliente.getNombreCliente();
        tipoCliente = cliente.getClaseMedico();
        direccion = cliente.getDireccion();
        latitud = cliente.getLatitud();
        longitud = cliente.getLongitud();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            String tipoPedido = data.getStringExtra(Common.ARG_TIPO_PEDIDO);
            facturasQuejas = data.getStringExtra(Common.ARG_FACTURAS_COBRAR);
            mListaFact.setText(facturasQuejas);



        }
    }

}
