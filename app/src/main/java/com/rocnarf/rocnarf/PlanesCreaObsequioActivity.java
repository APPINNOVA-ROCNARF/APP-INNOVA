package com.rocnarf.rocnarf;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.rocnarf.rocnarf.models.Clientes;
import com.rocnarf.rocnarf.repository.ClientesRepository;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.models.LiquidacionObsequio;
import com.rocnarf.rocnarf.viewmodel.ResultadoVisitaViewModel;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.viewmodel.ClientesCupoCreditoViewModel;

import rx.Subscriber;

import java.io.Serializable;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PlanesCreaObsequioActivity extends AppCompatActivity implements Serializable {

    private ClientesRepository clientesRepository;
    private ClientesCupoCreditoViewModel clientesCupoCreditoViewModel;
    private ResultadoVisitaViewModel resultadoVisitaViewModel;
    public static final String argCliente ="";
    private Context context;
    public static final String argIdCliente ="";
    private String idCliente, idUsuario, idFactura, nombrePlan, descripcionPlan,seccion;
    private TextView mNombre, mDescripcion, mNombreCliente, mCodigoCliente,facturas, protestos, chequesAFechas, cupoDisponible,lblCodigoCliente;
    private Button btLiquidacionObs, bt_enviar_Liquidacion;
    public int idPlan,id;
    private EditText mObsequio, mObservacion;
    private List<LiquidacionObsequio> liquidacionObsequio;
    private List<LiquidacionObsequio> mValues;
    private boolean primera;
    private boolean esFarmacia = false;
    private ConstraintLayout mConstraintLayout;
    public  TextView mNombreView;
    public  TextView mRepresentanteView;
    public  TextView mDireccionView;
    public  TextView mCodigoView;
    public  TextView mTipoCliente;
    public  TextView mOrigenMenu;
    //public final TextView mSeccionView;
    public  ImageView mOrigen;
    public  ImageView mPedido;
    public  ImageView mCobro;
    public  ImageView mEstadoVisita;
    public  ImageView mEstadoFilas;
    private PopupMenu popup;
    public  String tipoCliente;
//    public List<LiquidacionObsequio> liquidacionObsequios;
    public LiveData<Clientes> cliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_obsequio);

        Intent i = getIntent();
        idPlan = Integer.parseInt(i.getStringExtra(Common.ARG_IDNPLAN).trim());
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        idCliente =  i.getStringExtra(Common.ARG_IDCLIENTE);
        nombrePlan =  i.getStringExtra(Common.ARG_NOMPBREPLAN);
        descripcionPlan =  i.getStringExtra(Common.ARG_DESCRIPCIONPLAN);
        seccion= i.getStringExtra(Common.ARG_SECCIOM);
//        List<LiquidacionObsequio> liquidacionObsequio = i.getSerializableExtra("LiquidacionObsequio").getClass();
        this.context = getApplicationContext();
//        codigo = (TextView)findViewById(R.id.tv_codigo_activity_cliente_cupo_credito);
//        mNombreCliente = (TextView)findViewById(R.id.tv_nombere_cliente);
//        mCodigoCliente = (TextView)findViewById(R.id.tv_codigo_cliente);
        mObservacion =  (EditText) findViewById(R.id.et_observacion_obs);
        lblCodigoCliente = (TextView) findViewById(R.id.tv_codigo_row_resultado_cliente);
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.xxxx);

        mNombre = (TextView)findViewById(R.id.tv_nombre_plan);
        mDescripcion = (TextView)findViewById(R.id.tv_descripcion_plan);
        btLiquidacionObs = (Button) findViewById(R.id.bt_Liquidacion_premio);
        bt_enviar_Liquidacion = (Button) findViewById(R.id.bt_enviar_Liquidacion);
        mObsequio = (EditText) findViewById(R.id.et_obsequio);
        mNombre.setText(nombrePlan);
        mDescripcion.setText(descripcionPlan);
        bt_enviar_Liquidacion.setVisibility(View.GONE);


        // NUEVO

        mNombreView = (TextView) findViewById(R.id.tv_nombre_cliente_row_resultado_cliente);
        mRepresentanteView = (TextView) findViewById(R.id.tv_representante_row_resultado_cliente);
        mDireccionView = (TextView) findViewById(R.id.tv_direccion_row_resultado_cliente);
        mCodigoView = (TextView) findViewById(R.id.tv_codigo_row_resultado_cliente);
        mTipoCliente = (TextView) findViewById(R.id.tv_codigo_row_tipo_cliente);
        mOrigen = (ImageView) findViewById(R.id.iv_origen_row_resultado_cliente);
        mPedido = (ImageView) findViewById(R.id.iv_pedido_row_resultado_cliente);
        mEstadoFilas = (ImageView) findViewById(R.id.divider3);
        mCobro = (ImageView) findViewById(R.id.iv_cobro_row_resultado_cliente);
        mEstadoVisita = (ImageView) findViewById(R.id.iv_estado_visita_row_resultado_cliente);


        LiquidacionObsequio liquidacionObsequio = (LiquidacionObsequio)i.getSerializableExtra("LiquidacionObsequio");
//        Log.d("myTag", "dijo getIdCodigo ----->" + liquidacionObsequio.getId());
        if(liquidacionObsequio == null){
            primera = true;
        }else{
            primera = false;
            btLiquidacionObs.setVisibility(View.GONE);
            bt_enviar_Liquidacion.setVisibility(View.VISIBLE);
            mObsequio.setText(liquidacionObsequio.getObsequio());
            mObservacion.setText(liquidacionObsequio.getObservacion());
            mCodigoCliente.setText(liquidacionObsequio.getIdCliente());
            mNombreCliente.setText(liquidacionObsequio.getNombreCliente());
            id = liquidacionObsequio.getId();
        }

        btLiquidacionObs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PlanesCreaObsequioActivity.this, R.style.myDialog));
//                LayoutInflater inflater = getLayoutInflater();
//                builder.setMessage("Desea solicitar la liquidación de premio?");
//                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
                        btLiquidacionObs.setVisibility(View.GONE);
                        bt_enviar_Liquidacion.setVisibility(View.VISIBLE);

                        Intent i = new Intent(getApplicationContext(), PedidoCobroClienteActivity.class);
                        i.putExtra(Common.ARG_DESTINO_PEDIDO, 6);
                        i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                        i.putExtra(Common.ARG_SECCIOM, seccion);
                        startActivityForResult(i, 1);

//                    }
//                });
//                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User cancelled the dialog
//                        Log.d("myTag", "dijo nooooooooo ----->" );
//                    }
//                });
//                builder.create().show();

            }
        });


        bt_enviar_Liquidacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mObsequio.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Ingrese Obsequio", Toast.LENGTH_LONG).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PlanesCreaObsequioActivity.this, R.style.myDialog));
                LayoutInflater inflater = getLayoutInflater();
                builder.setMessage("¿Desea enviar la solicitud de premio?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                final LiquidacionObsequio liquidacionObsequio = new LiquidacionObsequio();

                if (primera) {
                    liquidacionObsequio.setIdPlan(idPlan);
                    liquidacionObsequio.setNombrePlan(nombrePlan);
                    liquidacionObsequio.setIdCliente(mCodigoCliente.getText().toString());
                    liquidacionObsequio.setNombreCliente(mNombreCliente.getText().toString());
                    liquidacionObsequio.setObsequio(mObsequio.getText().toString());
                    liquidacionObsequio.setIdAsesor(idUsuario);
                    liquidacionObsequio.setEstadoSolicitud("P");
                    liquidacionObsequio.setObservacion(mObservacion.getText().toString());
                    Intent in = new Intent();
                    setResult(RESULT_OK, in);
                    finish();

                    PlanesService service = ApiClient.getClient().create(PlanesService.class);
                    service.Post(liquidacionObsequio)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<LiquidacionObsequio>() {
                                @Override
                                public void onCompleted() {
//                                Intent in = new Intent();
//                                setResult(RESULT_OK, in);
//                                finish();
                                    Toast.makeText(getApplicationContext(), "Solicitud Enviada con Exito", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(getApplicationContext(), "Solicitud NO Fue Enviada Error", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onNext(LiquidacionObsequio liquidacionObsequio) {

                                }


                            });

                }else {

                    Toast.makeText(getApplicationContext(), "Actualizando.......", Toast.LENGTH_LONG).show();

                    liquidacionObsequio.setObsequio(mObsequio.getText().toString());
                    liquidacionObsequio.setObservacion(mObservacion.getText().toString());



                    PlanesService service = ApiClient.getClient().create(PlanesService.class);
                    service.updateObsequio(id, liquidacionObsequio)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<LiquidacionObsequio>() {
                                @Override
                                public void onCompleted() {
//                                Intent in = new Intent();
//                                setResult(RESULT_OK, in);
//                                finish();
                                    Intent in = new Intent();
                                    setResult(RESULT_OK, in);
                                    finish();

                                    Toast.makeText(getApplicationContext(), "Liquidacion Actualizada con exito", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(getApplicationContext(), "Liquidacion No Fue Actualizado", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onNext(LiquidacionObsequio liquidacionObsequio) {

                                }


                            });

                }

                                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        Log.d("myTag", "dijo nooooooooo ----->" );
                    }
                });
                builder.create().show();
            }
        });


    }


    @Override public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            mConstraintLayout.setVisibility(View.VISIBLE);
//            Log.d("myTag", "dijo argIdCliente ----->" + data.getParcelableExtra("argClienteModel") );
            Clientes cliente = data.getParcelableExtra("argClienteModel");

//            cliente =  this.clientesRepository.getById("801659");
//            Log.d("myTag", "dijo nooooooooo ----->" +cliente );
//            List<Clientes> clientes = clientesRepository.getById(idCliente);

//            lblCodigoCliente.setText(data.getStringExtra("argIdCliente"));

//            mCodigoCliente.setText(data.getStringExtra("argIdCliente"));
//            mNombreCliente.setText(data.getStringExtra("argCliente"));
//            mNombreView.setText(data.getStringExtra("argCliente"));
//            ImageButton mMenuPedidosView = (ImageButton) findViewById(R.id.ib_pedido_fragment_cliente_detalle_plan);

//            mMenuPedidosView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    showPopup(view, R.menu.menu_pedidos_detalle_cliente);
////                    PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
////                    popupMenu.getMenuInflater().inflate(R.menu.menu_pedidos_detalle_cliente, popupMenu.getMenu());
////                    popupMenu.show();
//
////                    PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
////                    popupMenu.getMenuInflater().inflate(R.menu.menu_pedidos_detalle_cliente, popupMenu.getMenu());
////                    popupMenu.show();
////
////                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
////                        @Override
////                        public boolean onMenuItemClick(MenuItem menuItem) {
////
////                            if (menuItem.getTitle().equals("1. item title")) {
////                                //do something
////                            } else if (menuItem.getTitle().equals("2.item Title")) {
////                                // do something
////                            }
////                            return false;
////                        }
////                    });
//                }
//            });

            mNombreView.setText(cliente.getNombreCliente());
            mRepresentanteView.setText(cliente.getRepresentante());
            mDireccionView.setText(cliente.getCiudad() + " - " + cliente.getDireccion() + "...");
            mCodigoView.setText(cliente.getIdCliente());
//            mCodigoCliente.setText(cliente.getIdCliente());
//            mOrigenMenu.setText(cliente.getOrigen());

            String TipoObserv = data.getStringExtra("argTipoObser");

            if (TipoObserv == null){

            }else {
                switch (TipoObserv) {
                    case "CLACT":
                        tipoCliente = "ACTIVO";
                        break;
                    case "CONTA":
                        tipoCliente = "CONTADO";
                        break;
                    case "CLPLE":
                        tipoCliente = "PRELEGAL";
                        break;
                    case "CLINC":
                        tipoCliente = "INACTIVO";
                        break;
                }
            }

            String Origen = data.getStringExtra("argOrigen");

            if ( Origen.equals("MEDICO") ) {
                mOrigen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_person_black_24dp));
                mRepresentanteView.setVisibility(View.GONE);
                mTipoCliente.setText("MEDICO" );
            } else {
                //  mCodigoView.setText(cliente.getTipoObserv() + ": " + cliente.getIdCliente());
                mTipoCliente.setText(tipoCliente );
                mOrigen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_store_black_24dp));
                mRepresentanteView.setVisibility(View.VISIBLE);
            }
            String valiZ= cliente.getIdCliente().substring(0, 1);
            if (valiZ.equals("Z")){
                mTipoCliente.setText("CLI Z " );
            }
            if(cliente.getCumpleAnyos() != null) {
                if (cliente.getCumpleAnyos()) {
                    mOrigen.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_torta_cumplanyo));
                }
            }
            mRepresentanteView.setVisibility(View.GONE);

            if (Origen == null ||Origen.equals("FARMA")){
                if (popup != null ) {
                    popup.getMenu().findItem(R.id.action_ficha_medico).setVisible(false);
                    popup.getMenu().findItem(R.id.action_recetas).setVisible(false);
                    popup.getMenu().findItem(R.id.action_categoria).setVisible(false);
                }
                else
                    esFarmacia = true;
            }

            String estadoVisita = data.getStringExtra("argEstadoVisita");
            if (TextUtils.isEmpty(estadoVisita)) {
                mEstadoVisita.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_add_circle_red_24dp));
//                mEstadoVisita.setVisibility(View.VISIBLE);
                mEstadoVisita.setVisibility(View.GONE);
                mEstadoFilas.setBackgroundColor(Color.parseColor("#FF0000"));
            } else if (estadoVisita.equals("EFECT")) {
                mEstadoVisita.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check_circle_primary_24dp));
                //mEstadoVisita.setVisibility(View.VISIBLE);
                mEstadoVisita.setVisibility(View.GONE);
                mEstadoFilas.setBackgroundColor(Color.parseColor("#21d162"));
            } else {
                mEstadoVisita.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_timer_black_24dp));
                mEstadoVisita.setVisibility(View.VISIBLE);
                //mEstadoFilas.setVisibility(View.GONE); ///00FFFFFF
                mEstadoFilas.setBackgroundColor(Color.parseColor("#ffff00"));
            }

            mPedido.setVisibility(View.GONE);
            if (cliente.getPedido() != null) mPedido.setVisibility(View.VISIBLE);
            mCobro.setVisibility(View.GONE);
            if (cliente.getCobro() != null) mCobro.setVisibility(View.VISIBLE);

//            this.context = context;
            ImageButton mMenuPedidosView = (ImageButton) findViewById(R.id.ib_pedido_fragment_cliente_detalle_plan);
            //////
            mMenuPedidosView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                showPopup(view, R.menu.menu_pedidos_detalle_cliente);

                    PopupMenu popupMenu = new PopupMenu(context, view);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_pedidos_detalle_cliente, popupMenu.getMenu());
                    popupMenu.show();
//                    if (esFarmacia)  popupMenu.getMenu().findItem(R.id.action_ficha_medico).setVisible(false);
//                    if (!esFarmacia)  popupMenu.getMenu().findItem(R.id.action_historial_pedidos).setVisible(false);
//                    if (!esFarmacia)  popupMenu.getMenu().findItem(R.id.action_detalle_productos).setVisible(false);
//                    if (!esFarmacia)  popupMenu.getMenu().findItem(R.id.action_cupos_credito).setVisible(false);
//                    if (!esFarmacia)  popupMenu.getMenu().findItem(R.id.action_totales_mes).setVisible(false);
//                    popupMenu.show();
//                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                        @Override
//                        public boolean onMenuItemClick(MenuItem menuItem) {
//                            switch (menuItem.getItemId()) {
//                                case R.id.action_historial_pedidos:
//                                    Intent iFacturas = new Intent(context, ClientesFacturasActivity.class);
//                                    iFacturas.putExtra(Common.ARG_IDCLIENTE, idCliente);
//                                    iFacturas.putExtra(Common.ARG_NOMBRE_CLIENTE, mNombreView.getText().toString());
//                                    iFacturas.putExtra(Common.ARG_IDUSUARIO, idUsuario);
//                                    startActivity(iFacturas);
//                                    return true;
//                                case R.id.action_detalle_productos:
//                                    Intent iFacturaDetalle = new Intent(context, FacturaDetalleActivity.class);
//                                    iFacturaDetalle.putExtra(Common.ARG_IDCLIENTE, idCliente);
//                                    iFacturaDetalle.putExtra(Common.ARG_IDUSUARIO, idUsuario);
//                                    startActivity(iFacturaDetalle);
//                                    return true;
//                                case R.id.action_cupos_credito:
//                                    Intent i = new Intent(context, ClientesCupoCreditoActivity.class);
//                                    i.putExtra(Common.ARG_IDCLIENTE, idCliente);
//                                    i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
//                                    startActivity(i);
//                                    return true;
//                                case R.id.action_totales_mes:
//                                    Intent iTotalesXMes = new Intent(context, VentasMensualesClientesActivity.class);
//                                    iTotalesXMes.putExtra(Common.ARG_IDCLIENTE, idCliente);
//                                    iTotalesXMes.putExtra(Common.ARG_IDUSUARIO, idUsuario);
//                                    startActivity(iTotalesXMes);
//                                    return true;
//                                case R.id.action_ficha_medico:
//                                    Intent iFichaMedico = new Intent(context, MedicoFichaActivity.class);
//                                    iFichaMedico.putExtra(Common.ARG_IDCLIENTE, idCliente);
//                                    iFichaMedico.putExtra(Common.ARG_IDUSUARIO, idUsuario);
//                                    startActivity(iFichaMedico);
//                                    return true;
//                                case R.id.action_historial_comentarios:
//                                    Log.d("menu", "menu ---> historia comentarios");
//                                    Intent iComentarios = new Intent(context, HistorialComentariosActivity.class);
//                                    iComentarios.putExtra(Common.ARG_IDCLIENTE, idCliente);
//                                    iComentarios.putExtra(Common.ARG_IDUSUARIO, idUsuario);
//                                    context.startActivity(iComentarios);
//                                    return true;
//                                case R.id.action_historial_visitas:
//                                    Intent iVisitas = new Intent(context, HistorialVisitasActivity.class);
//                                    iVisitas.putExtra(Common.ARG_IDCLIENTE, idCliente);
//                                    iVisitas.putExtra(Common.ARG_IDUSUARIO, idUsuario);
//                                    context.startActivity(iVisitas);
//                                    return true;
//                                default:
//                                    return false;
//
//                            }
//                        }
//                    });
                }
            });


        } else if (resultCode == RESULT_CANCELED) {

        }
    }

}
