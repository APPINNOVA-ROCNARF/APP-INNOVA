package com.rocnarf.rocnarf;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.models.Clientes;
import com.rocnarf.rocnarf.models.ClientesCupoCredito;
import com.rocnarf.rocnarf.models.Factura;
import com.rocnarf.rocnarf.models.Pedido;
import com.rocnarf.rocnarf.models.PedidoDetalle;
import com.rocnarf.rocnarf.viewmodel.ClienteDetalleViewModel;
import com.rocnarf.rocnarf.viewmodel.ClientesCupoCreditoViewModel;
import com.rocnarf.rocnarf.viewmodel.PedidoViewModel;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class PedidoSimpleActivity extends AppCompatActivity implements PedidoProductoFragment.OnListFragmentInteractionListener {

    private ViewPager mViewPager;
    private PedidoViewModel pedidoViewModel;
    private String idUsuario, seccion, idCliente, nombreCliente;
    private String mensajeAntesEnviarPedido = "Desea Enviar Pedido?";
    private ClientesCupoCredito cupoCredito;
    private int idLocalPedido;
    private List<Factura> facturasCliente;
    private Button mCobros;
    private Pedido pedidoExistemte;
    private PedidoDetalle pedidoDetalleLoop;
    private TextView mPedido, mFecha, mTotal, mDescuento, mFinal, mObservaciones, tvObservaciones;
    private TextView mTotalF3, mDescuentoF3, mFinalF3;
    private TextView mTotalF2, mDescuentoF2, mFinalF2;
    private TextView mTotalF4, mDescuentoF4, mFinalF4;
    private TextView mTotalGEN, mDescuentoGEN, mFinalGEN;
    //private ImageButton mAgregarDescuento;
    FloatingActionButton mAgregarDescuento;
    private ClienteDetalleViewModel clienteDetalleViewModel;
    private List<PedidoDetalle> pedidoDetalleExistente;
    private RecyclerView recyclerView;
    private LinearLayout mLayout;
    private ProgressBar progressBar;
    private MenuItem menuAgregarProducto;
    private MenuItem menuEliminarPedido;
    Spinner mTipoPrecio;
    Context context;
    private MenuItem menuEnviarPedido;
    private com.rocnarf.rocnarf.dao.DataBaseHelper DataBaseHelper;
    private SQLiteDatabase sQLiteDatabase;
    private String IdLocalPedido, IdCliente, IdPedidoLocal, IdProducto,
            Nombre, IdPedido, Cantidad, Bono, PrecioTotal, Descuento, FechaPedido, PrecioFinal, ProductoEliminar, IdProductoEliminar;

    private ListView LstProductos;
    public String estadoCliente;
    private double PrecioTotalAcu = 0, PrecioFinalAcu = 0, DescuentoAcu = 0;
    private ClientesCupoCreditoViewModel clientesCupoCreditoViewModel;
private Boolean usarPrecioEspecial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_simple);

        Intent intent = getIntent();
        idUsuario = intent.getStringExtra(Common.ARG_IDUSUARIO);
        seccion = intent.getStringExtra(Common.ARG_SECCIOM);
        idCliente = intent.getStringExtra(Common.ARG_IDCLIENTE);
        idLocalPedido = intent.getIntExtra(Common.ARG_IDPEDIDO, 0);
        nombreCliente = intent.getStringExtra(Common.ARG_NOMBRE_CLIENTE);
        usarPrecioEspecial = getIntent().getBooleanExtra(Common.ARG_USAR_PRECIO_ESPECIAL, false);
        Log.d("local", "local" + idLocalPedido);
        final ActionBar actionBar = getSupportActionBar();
        if (nombreCliente != null) {
            actionBar.setTitle(nombreCliente);
            actionBar.setSubtitle(idCliente);
        }


        CargarControles();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment detalleCliente = PedidoProductoFragment.newInstance(idUsuario, seccion, idCliente, idLocalPedido);
        ft.replace(R.id.fm_producto_activity_pedido_simple, detalleCliente);
        ft.commit();
        pedidoViewModel = ViewModelProviders.of(this).get(PedidoViewModel.class);


        clientesCupoCreditoViewModel = ViewModelProviders.of(this).get(ClientesCupoCreditoViewModel.class);
        clientesCupoCreditoViewModel.setIdUsuario(idUsuario);
        clientesCupoCreditoViewModel.getCupoCredito(idCliente).observe(this, new Observer<ClientesCupoCredito>() {
            @Override
            public void onChanged(@Nullable ClientesCupoCredito clientesCupoCredito) {
                cupoCredito = clientesCupoCredito;

            }
        });

        clienteDetalleViewModel = ViewModelProviders.of(this).get(ClienteDetalleViewModel.class);
        clienteDetalleViewModel.getByid(idCliente).observe(this, new Observer<Clientes>() {
            @Override
            public void onChanged(@Nullable final Clientes clientes) {
                estadoCliente = clientes.getTipoObserv();
            }
        });
        //pedidoViewModel = ViewModelProviders.of(this).get(PedidoViewModel.class);

        if(idUsuario.equals("SBC")){
            tvObservaciones.setText("Orden de Compra");
        }

        mObservaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PedidoSimpleActivity.this);
                LayoutInflater inflater = PedidoSimpleActivity.this.getLayoutInflater();

                if (idUsuario.equals("SBC")) {
                    builder.setTitle("Orden de Compra");
                } else {
                    builder.setTitle("Observaciones");
                }

                // Set up the input
                final EditText input = new EditText(PedidoSimpleActivity.this);

                input.setFilters(new InputFilter[] { new InputFilter.LengthFilter(75) });

                input.setText(mObservaciones.getText().toString());
                builder.setView(input);

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String observaciones = input.getText().toString();
                        mObservaciones.setText(observaciones);
                        pedidoExistemte.setObservaciones(observaciones);
                        pedidoViewModel.updatePedido(pedidoExistemte);

                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                builder.create().show();
            }
        });


        mAgregarDescuento = (FloatingActionButton) findViewById(R.id.ib_descuento_fragment_pedido);
        mAgregarDescuento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean validacionBono = false;
                for (int i = 0; i < pedidoDetalleExistente.size(); i++) {
                    if (pedidoDetalleExistente.get(i).getBono() > 0) {
                        validacionBono = true;
                    }
                }
                if (validacionBono) {
                    // Toast.makeText(PedidoSimpleActivity.this, "No se puede ingresar descuento, productos con bonificación", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(PedidoSimpleActivity.this);
                    builder.setMessage("No se puede ingresar descuento, productos con bonificación")
                            .setTitle("Advertencia");
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    builder.create().show();

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PedidoSimpleActivity.this);
                    LayoutInflater inflater = PedidoSimpleActivity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog_pedido_descuento, null);
                    final TextView porcentajeDescuento = dialogView.findViewById(R.id.et_descuento_dialog_pedido_descuento);
                    builder.setTitle("Descuento");
                    builder.setView(dialogView);
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //pedidoViewModel.deletePedidoDetalle(pedidoDetalleExistente);
                            if (porcentajeDescuento.getText() != "") {
                                if (Double.parseDouble(porcentajeDescuento.getText().toString()) < 0 || Double.parseDouble(porcentajeDescuento.getText().toString()) > 100) {
                                    Toast.makeText(PedidoSimpleActivity.this, "Porcentaje no valido ", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                pedidoExistemte.setPorcentajeDescuento(Double.parseDouble(porcentajeDescuento.getText().toString()));
                                pedidoExistemte.setDescuento((Double.parseDouble(porcentajeDescuento.getText().toString()) / 100) * pedidoExistemte.getPrecioTotal());
                                pedidoExistemte.setPrecioFinal(pedidoExistemte.getPrecioTotal() - pedidoExistemte.getDescuento());
                                pedidoViewModel.updatePedido(pedidoExistemte);
                                CalcularTotalesXLinea();
                                Toast.makeText(PedidoSimpleActivity.this, " Descuento asignado", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
                    builder.create().show();
                }
            }
        });


        mTipoPrecio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                PedidoProductoFragment fragment = (PedidoProductoFragment) getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1);
                if (selectedItem.equals("P.V.F")) {
                    fragment.setTipo("P.V.F");
                    CalcularTotalesXLineaPvf();
                } else if (selectedItem.equals("P.V.P")) {
                    fragment.setTipo("P.V.P");
                    CalcularTotalesXLineaPvp();
                } else if (selectedItem.equals("ESP")){
                    fragment.setTipo("ESP");
                    CalcularTotalesXLineaESP();
                }

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mCobros = (Button) findViewById(R.id.bt_cobro_pedido);
        mCobros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), ClientesFacturasActivity.class);
                i.putExtra(Common.ARG_IDCLIENTE, idCliente);
                i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                i.putExtra(Common.ARG_SECCIOM, seccion);
                i.putExtra(Common.ARG_TIPO_PEDIDO, Common.TIPO_PEDIDO_PEDIDO);
                i.putExtra(Common.ARG_FACTURAS_SELECCION, true);
                startActivity(i);

            }
        });


        pedidoViewModel = ViewModelProviders.of(this).get(PedidoViewModel.class);
        pedidoViewModel.init(idLocalPedido);
        pedidoViewModel.setIdUsuario(this.idUsuario);

        CargarPedidoExistente();

        pedidoViewModel.detallesPedido.observe(this, new Observer<List<PedidoDetalle>>() {
            @Override
            public void onChanged(@Nullable List<PedidoDetalle> pedidoDetalles) {
                pedidoDetalleExistente = pedidoDetalles;
                mTotal.setText(String.format("%.2f", pedidoExistemte.getPrecioTotal()));
                mDescuento.setText(String.format("%.2f", pedidoExistemte.getDescuento()));
                mFinal.setText(String.format("%.2f", pedidoExistemte.getPrecioFinal()));

                CalcularTotalesXLinea();

            }
        });


    }

    private void CargarPedidoExistente() {

        pedidoViewModel.getPedidoByIdLocal(idLocalPedido)
                .observe(this, new Observer<Pedido>() {
                    @Override
                    public void onChanged(@Nullable Pedido pedido) {
                        pedidoExistemte = pedido;
                        SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);
                        mPedido.setText("Pedido " + String.valueOf(pedido.getIdPedido()));
                        mFecha.setText(sdf.format(pedido.getFechaPedido()));
                        mTotal.setText(String.format("%.2f", pedido.getPrecioTotal()));
                        mDescuento.setText(String.format("%.2f", pedido.getDescuento()));
                        mFinal.setText(String.format("%.2f", pedido.getPrecioFinal()));
                        mObservaciones.setText(pedido.getObservaciones());

                       // mTipoPrecio.setSelection(1);

                        if (pedido.getTipoPrecio() != null) {
                            for (int indice = 0; indice < mTipoPrecio.getAdapter().getCount(); indice++) {
                                if (mTipoPrecio.getAdapter().getItem(indice).toString().equals(pedido.getTipoPrecio())) {
                                    mTipoPrecio.setSelection(indice);
                                }
                            }
                        } else {
                            pedido.setTipoPrecio("P.V.F");
                        }


//                        if (pedido.isConBonos()){
//                            mAgregarDescuento.setVisibility(View.GONE);
//                        }else {
//                            mAgregarDescuento.setVisibility(View.VISIBLE);
//                        }
                        if (pedido.getReciboCobro() != null)
                            if (!pedido.getReciboCobro().isEmpty()) {
                                mCobros.setText("CAMBIAR COBRO");
                            }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_pedido, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pedido, menu);
        menuAgregarProducto = menu.getItem(0);
        menuEliminarPedido = menu.getItem(1);
        menuEnviarPedido = menu.getItem(2);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_eliminar_pedido) {
            eliminarPedido();
            return true;
        } else if (id == R.id.action_agregar_producto) {
            agregarProducto();
            return true;
        } else if (id == R.id.action_enviar_pedido) {
            syncronizarPedido();
        }

        return super.onOptionsItemSelected(item);
    }


    private void eliminarPedido() {

        pedidoViewModel = ViewModelProviders.of(this).get(PedidoViewModel.class);
        pedidoViewModel.init(idLocalPedido);
        pedidoViewModel.getPedidoByIdLocal(idLocalPedido).observe(this, new Observer<Pedido>() {
            @Override
            public void onChanged(@Nullable Pedido pedido) {
                pedidoExistemte = pedido;
                pedidoViewModel.deletePedido();
                Toast.makeText(getApplicationContext(), "Pedido Eliminado", Toast.LENGTH_LONG).show();

                Intent i = new Intent(getApplicationContext(), PedidoListaActivity.class);
                i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                i.putExtra(Common.ARG_SECCIOM, seccion);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });


    }

    private void agregarProducto() {

        pedidoViewModel = ViewModelProviders.of(this).get(PedidoViewModel.class);
        pedidoViewModel.init(idLocalPedido);
        pedidoViewModel.getPedidoByIdLocal(idLocalPedido).observe(this, new Observer<Pedido>() {
            @Override
            public void onChanged(@Nullable Pedido pedido) {
                pedidoExistemte = pedido;

                Intent i = new Intent(PedidoSimpleActivity.this, ProductosActivity.class);
                i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                i.putExtra(Common.ARG_SECCIOM, seccion);
                i.putExtra(Common.ARG_IDCLIENTE, pedidoViewModel.pedido.getValue().getIdCliente());
                i.putExtra(Common.ARG_NOMBRE_CLIENTE, pedidoViewModel.pedido.getValue().getNombreCliente());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                if (Objects.equals(pedidoExistemte.getTipoPrecio(), "ESP")){
                    i.putExtra(Common.ARG_USAR_PRECIO_ESPECIAL, usarPrecioEspecial);
                }
                startActivity(i);

            }
        });

    }

    private void syncronizarPedido() {
        pedidoViewModel = ViewModelProviders.of(this).get(PedidoViewModel.class);
        pedidoViewModel.init(idLocalPedido);
        pedidoViewModel.getPedidoByIdLocal(idLocalPedido).observe(this, new Observer<Pedido>() {
            @Override
            public void onChanged(@Nullable Pedido pedido) {
                pedidoExistemte = pedido;

            }
        });




        if (pedidoViewModel.detallesPedido.getValue().size() == 0) {
            AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
            alert.setTitle("ERROR");
            alert.setMessage("El pedido no contiene productos");
            alert.setPositiveButton("OK", null);
            alert.show();
            return;
            //Toast.makeText(getApplicationContext(), "El pedido no contiene productos", Toast.LENGTH_LONG).show();
        }

        if (facturasCliente != null) {
            for (Factura factura : facturasCliente) {
                BigDecimal saldo = factura.getValor().subtract(factura.getAbonos());
                saldo = saldo.subtract(factura.getNotaCredito());
                if (saldo.compareTo(new BigDecimal(0)) > 0) {
                    mensajeAntesEnviarPedido = "Cliente con Cartera Vencida. Desea Enviar Pedido?";
                }
            }
        }

        if (estadoCliente.equals("INACTIVO")){
            AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
            alert.setTitle("ERROR");
            alert.setMessage("Cliente Inactivo, debe actualizar datos y entregar al Dpto. de Crédito y Cobranzas.");
            alert.setPositiveButton("OK", null);
            alert.show();
            return;
        }

//        if (pedidoViewModel.detallesPedido.getValue().size() >20 ){
////            AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
////            alert.setTitle("ERROR");
////            alert.setMessage("El pedido no debe contener mas de 20 productos");
////            alert.setPositiveButton("OK",null);
////            alert.show();
////            return;
////            //Toast.makeText(getApplicationContext(), "El pedido no debe contener mas de 20 productos", Toast.LENGTH_LONG).show();
////        }
        BigDecimal disponible = cupoCredito.getCupoCredito().subtract(cupoCredito.getFacturas());
        double CupoDisponible = disponible.doubleValue(); // The double you want
        mensajeAntesEnviarPedido = "¿Desea Enviar Pedido?";
        //Verificar si tiene cupo credito
        if (cupoCredito != null) {
            if (pedidoViewModel.pedido.getValue().getPrecioTotal().compareTo(CupoDisponible) > 0) {
                mensajeAntesEnviarPedido = "Pedido Excede Cupo de Crédito. ¿Desea Enviar Pedido?" ;
            }
        }


        pedidoViewModel.sincronizado.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s.equals("OK")) {
                    //pedidoViewModel.deletePedido();
                    Intent i = new Intent(getApplicationContext(), PedidoListaActivity.class);
                    i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    i.putExtra(Common.ARG_SECCIOM, seccion);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);

                    Toast.makeText(getApplicationContext(), "El pedido ha sido enviado", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    mLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    menuAgregarProducto.setEnabled(true);
                    menuEliminarPedido.setEnabled(true);
                    menuEnviarPedido.setEnabled(true);
                }
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setMessage(mensajeAntesEnviarPedido);

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                menuAgregarProducto.setEnabled(false);
                menuEliminarPedido.setEnabled(false);
                menuEnviarPedido.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                //mLayout.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Enviando pedido", Toast.LENGTH_LONG).show();
                pedidoViewModel.Sync();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.create().show();

    }

    private void CalcularTotalesXLinea() {
        double totalF3 = 0, descuentoF3 = 0, finalF3 = 0;
        for (PedidoDetalle detalle : pedidoDetalleExistente) {
            if (detalle.getTipo().equals("F3")) {
                totalF3 += detalle.getPrecioTotal();
            }
        }
        descuentoF3 = totalF3 * (pedidoExistemte.getPorcentajeDescuento() / 100);
        finalF3 = totalF3 - descuentoF3;
        mTotalF3.setText(String.format("%.2f", totalF3));
        mDescuentoF3.setText(String.format("%.2f", descuentoF3));
        mFinalF3.setText(String.format("%.2f", finalF3));

        double totalF2 = 0, descuentoF2 = 0, finalF2 = 0;
        for (PedidoDetalle detalle : pedidoDetalleExistente) {
            if (detalle.getTipo().equals("F2")) {
                totalF2 += detalle.getPrecioTotal();
            }
        }
        descuentoF2 = totalF2 * (pedidoExistemte.getPorcentajeDescuento() / 100);
        finalF2 = totalF2 - descuentoF2;
        mTotalF2.setText(String.format("%.2f", totalF2));
        mDescuentoF2.setText(String.format("%.2f", descuentoF2));
        mFinalF2.setText(String.format("%.2f", finalF2));

        double totalF4 = 0, descuentoF4 = 0, finalF4 = 0;
        for (PedidoDetalle detalle : pedidoDetalleExistente) {
            if (detalle.getTipo().equals("F4")) {
                totalF4 += detalle.getPrecioTotal();
            }
        }
        descuentoF4 = totalF4 * (pedidoExistemte.getPorcentajeDescuento() / 100);
        finalF4 = totalF4 - descuentoF4;
        mTotalF4.setText(String.format("%.2f", totalF4));
        mDescuentoF4.setText(String.format("%.2f", descuentoF4));
        mFinalF4.setText(String.format("%.2f", finalF4));

        double totalGEN = 0, descuentoGEN = 0, finalGEN = 0;
        for (PedidoDetalle detalle : pedidoDetalleExistente) {
            if (detalle.getTipo().equals("GE")) {
                totalGEN += detalle.getPrecioTotal();
            }
        }
        descuentoGEN = totalGEN * (pedidoExistemte.getPorcentajeDescuento() / 100);
        finalGEN = totalGEN - descuentoGEN;
        mTotalGEN.setText(String.format("%.2f", totalGEN));
//        mTotalGEN.setText(String.format("%.2f", totalGEN));
        mDescuentoGEN.setText(String.format("%.2f", descuentoGEN));
        mFinalGEN.setText(String.format("%.2f", finalGEN));
    }


    public void CalcularTotalesXLineaPvp() {
        double totalF3 = 0, descuentoF3 = 0, finalF3 = 0;
        for (PedidoDetalle detalle : pedidoDetalleExistente) {
            if (detalle.getTipo().equals("F3")) {
                detalle.setPrecioTotal(detalle.getCantidad() * detalle.getPvp());
                totalF3 += detalle.getPrecioTotal();
            }
        }
        descuentoF3 = totalF3 * (pedidoExistemte.getPorcentajeDescuento() / 100);
        finalF3 = totalF3 - descuentoF3;
        mTotalF3.setText(String.format("%.2f", totalF3));
        mDescuentoF3.setText(String.format("%.2f", descuentoF3));
        mFinalF3.setText(String.format("%.2f", finalF3));

        double totalF2 = 0, descuentoF2 = 0, finalF2 = 0;
        for (PedidoDetalle detalle : pedidoDetalleExistente) {
            if (detalle.getTipo().equals("F2")) {
                detalle.setPrecioTotal(detalle.getCantidad() * detalle.getPvp());
                totalF2 += detalle.getPrecioTotal();
            }
        }
        descuentoF2 = totalF2 * (pedidoExistemte.getPorcentajeDescuento() / 100);
        finalF2 = totalF2 - descuentoF2;
        mTotalF2.setText(String.format("%.2f", totalF2));
        mDescuentoF2.setText(String.format("%.2f", descuentoF2));
        mFinalF2.setText(String.format("%.2f", finalF2));

        double totalF4 = 0, descuentoF4 = 0, finalF4 = 0;
        for (PedidoDetalle detalle : pedidoDetalleExistente) {
            if (detalle.getTipo().equals("F4")) {
                detalle.setPrecioTotal(detalle.getCantidad() * detalle.getPvp());
                totalF4 += detalle.getPrecioTotal();
            }
        }
        descuentoF4 = totalF4 * (pedidoExistemte.getPorcentajeDescuento() / 100);
        finalF4 = totalF4 - descuentoF4;
        mTotalF4.setText(String.format("%.2f", totalF4));
        mDescuentoF4.setText(String.format("%.2f", descuentoF4));
        mFinalF4.setText(String.format("%.2f", finalF4));

        double totalGEN = 0, descuentoGEN = 0, finalGEN = 0;
        for (PedidoDetalle detalle : pedidoDetalleExistente) {
            if (detalle.getTipo().equals("GE")) {
                if (detalle.getPvp() != null) {
                    detalle.setPrecioTotal(detalle.getCantidad() * detalle.getPvp());
                } else {
                    detalle.setPrecioTotal(0.0);
                }
                totalGEN += detalle.getPrecioTotal();
            }
        }
        descuentoGEN = totalGEN * (pedidoExistemte.getPorcentajeDescuento() / 100);
        finalGEN = totalGEN - descuentoGEN;
        mTotalGEN.setText(String.format("%.2f", totalGEN));
//        mTotalGEN.setText(String.format("%.2f", totalGEN));
        mDescuentoGEN.setText(String.format("%.2f", descuentoGEN));
        mFinalGEN.setText(String.format("%.2f", finalGEN));

        pedidoExistemte.setPrecioTotal(totalGEN + totalF4 + totalF2 + totalF3);
        pedidoExistemte.setDescuento(descuentoGEN + descuentoF4 + descuentoF2 + descuentoF3);
        pedidoExistemte.setPrecioFinal(finalGEN + finalF4 + finalF2 + finalF3);

        mTotal.setText(String.format("%.2f", pedidoExistemte.getPrecioTotal()));
        mDescuento.setText(String.format("%.2f", pedidoExistemte.getDescuento()));
        mFinal.setText(String.format("%.2f", pedidoExistemte.getPrecioFinal()));

        pedidoExistemte.setTipoPrecio("P.V.P");
        pedidoViewModel.updatePedido(pedidoExistemte);
    }

    public void CalcularTotalesXLineaPvf() {
        double totalF3 = 0, descuentoF3 = 0, finalF3 = 0;
        for (PedidoDetalle detalle : pedidoDetalleExistente) {
            if (detalle.getTipo().equals("F3")) {
                detalle.setPrecioTotal(detalle.getCantidad() * detalle.getPvf());
                totalF3 += detalle.getPrecioTotal();
            }
        }
        descuentoF3 = totalF3 * (pedidoExistemte.getPorcentajeDescuento() / 100);
        finalF3 = totalF3 - descuentoF3;
        mTotalF3.setText(String.format("%.2f", totalF3));
        mDescuentoF3.setText(String.format("%.2f", descuentoF3));
        mFinalF3.setText(String.format("%.2f", finalF3));

        double totalF2 = 0, descuentoF2 = 0, finalF2 = 0;
        for (PedidoDetalle detalle : pedidoDetalleExistente) {
            if (detalle.getTipo().equals("F2")) {
                detalle.setPrecioTotal(detalle.getCantidad() * detalle.getPvf());
                totalF2 += detalle.getPrecioTotal();
            }
        }
        descuentoF2 = totalF2 * (pedidoExistemte.getPorcentajeDescuento() / 100);
        finalF2 = totalF2 - descuentoF2;
        mTotalF2.setText(String.format("%.2f", totalF2));
        mDescuentoF2.setText(String.format("%.2f", descuentoF2));
        mFinalF2.setText(String.format("%.2f", finalF2));

        double totalF4 = 0, descuentoF4 = 0, finalF4 = 0;
        for (PedidoDetalle detalle : pedidoDetalleExistente) {
            if (detalle.getTipo().equals("F4")) {
                detalle.setPrecioTotal(detalle.getCantidad() * detalle.getPvf());
                totalF4 += detalle.getPrecioTotal();
            }
        }
        descuentoF4 = totalF4 * (pedidoExistemte.getPorcentajeDescuento() / 100);
        finalF4 = totalF4 - descuentoF4;
        mTotalF4.setText(String.format("%.2f", totalF4));
        mDescuentoF4.setText(String.format("%.2f", descuentoF4));
        mFinalF4.setText(String.format("%.2f", finalF4));

        double totalGEN = 0, descuentoGEN = 0, finalGEN = 0;
        for (PedidoDetalle detalle : pedidoDetalleExistente) {
            if (detalle.getTipo().equals("GE")) {
                if (detalle.getPvp() != null) {
                    detalle.setPrecioTotal(detalle.getCantidad() * detalle.getPvf());
                } else {
                    detalle.setPrecioTotal(0.0);
                }
                totalGEN += detalle.getPrecioTotal();
            }
        }
        descuentoGEN = totalGEN * (pedidoExistemte.getPorcentajeDescuento() / 100);
        finalGEN = totalGEN - descuentoGEN;
        mTotalGEN.setText(String.format("%.2f", totalGEN));
//        mTotalGEN.setText(String.format("%.2f", totalGEN));
        mDescuentoGEN.setText(String.format("%.2f", descuentoGEN));
        mFinalGEN.setText(String.format("%.2f", finalGEN));

        pedidoExistemte.setPrecioTotal(totalGEN + totalF4 + totalF2 + totalF3);
        pedidoExistemte.setDescuento(descuentoGEN + descuentoF4 + descuentoF2 + descuentoF3);
        pedidoExistemte.setPrecioFinal(finalGEN + finalF4 + finalF2 + finalF3);

        mTotal.setText(String.format("%.2f", pedidoExistemte.getPrecioTotal()));
        mDescuento.setText(String.format("%.2f", pedidoExistemte.getDescuento()));
        mFinal.setText(String.format("%.2f", pedidoExistemte.getPrecioFinal()));

        pedidoExistemte.setTipoPrecio("P.V.F");
        pedidoViewModel.updatePedido(pedidoExistemte);
    }

    public void CalcularTotalesXLineaESP() {
        double totalF3 = 0, descuentoF3 = 0, finalF3 = 0;
        for (PedidoDetalle detalle : pedidoDetalleExistente) {
            if (detalle.getTipo().equals("F3")) {
                detalle.setPrecioTotal(detalle.getCantidad() * detalle.getEsp());
                totalF3 += detalle.getPrecioTotal();
            }
        }
        descuentoF3 = totalF3 * (pedidoExistemte.getPorcentajeDescuento() / 100);
        finalF3 = totalF3 - descuentoF3;
        mTotalF3.setText(String.format("%.2f", totalF3));
        mDescuentoF3.setText(String.format("%.2f", descuentoF3));
        mFinalF3.setText(String.format("%.2f", finalF3));

        double totalF2 = 0, descuentoF2 = 0, finalF2 = 0;
        for (PedidoDetalle detalle : pedidoDetalleExistente) {
            if (detalle.getTipo().equals("F2")) {
                detalle.setPrecioTotal(detalle.getCantidad() * detalle.getEsp());
                totalF2 += detalle.getPrecioTotal();
            }
        }
        descuentoF2 = totalF2 * (pedidoExistemte.getPorcentajeDescuento() / 100);
        finalF2 = totalF2 - descuentoF2;
        mTotalF2.setText(String.format("%.2f", totalF2));
        mDescuentoF2.setText(String.format("%.2f", descuentoF2));
        mFinalF2.setText(String.format("%.2f", finalF2));

        double totalF4 = 0, descuentoF4 = 0, finalF4 = 0;
        for (PedidoDetalle detalle : pedidoDetalleExistente) {
            if (detalle.getTipo().equals("F4")) {
                detalle.setPrecioTotal(detalle.getCantidad() * detalle.getEsp());
                totalF4 += detalle.getPrecioTotal();
            }
        }
        descuentoF4 = totalF4 * (pedidoExistemte.getPorcentajeDescuento() / 100);
        finalF4 = totalF4 - descuentoF4;
        mTotalF4.setText(String.format("%.2f", totalF4));
        mDescuentoF4.setText(String.format("%.2f", descuentoF4));
        mFinalF4.setText(String.format("%.2f", finalF4));

        double totalGEN = 0, descuentoGEN = 0, finalGEN = 0;
        for (PedidoDetalle detalle : pedidoDetalleExistente) {
            if (detalle.getTipo().equals("GE")) {
                if (detalle.getPvp() != null) {
                    detalle.setPrecioTotal(detalle.getCantidad() * detalle.getEsp());
                } else {
                    detalle.setPrecioTotal(0.0);
                }
                totalGEN += detalle.getPrecioTotal();
            }
        }
        descuentoGEN = totalGEN * (pedidoExistemte.getPorcentajeDescuento() / 100);
        finalGEN = totalGEN - descuentoGEN;
        mTotalGEN.setText(String.format("%.2f", totalGEN));
//        mTotalGEN.setText(String.format("%.2f", totalGEN));
        mDescuentoGEN.setText(String.format("%.2f", descuentoGEN));
        mFinalGEN.setText(String.format("%.2f", finalGEN));

        pedidoExistemte.setPrecioTotal(totalGEN + totalF4 + totalF2 + totalF3);
        pedidoExistemte.setDescuento(descuentoGEN + descuentoF4 + descuentoF2 + descuentoF3);
        pedidoExistemte.setPrecioFinal(finalGEN + finalF4 + finalF2 + finalF3);

        mTotal.setText(String.format("%.2f", pedidoExistemte.getPrecioTotal()));
        mDescuento.setText(String.format("%.2f", pedidoExistemte.getDescuento()));
        mFinal.setText(String.format("%.2f", pedidoExistemte.getPrecioFinal()));

        pedidoExistemte.setTipoPrecio("ESP");
        pedidoViewModel.updatePedido(pedidoExistemte);
    }


    public void CargarControles() {
        progressBar = (ProgressBar) findViewById(R.id.pr_list_activity_productos);


        mLayout = (LinearLayout) findViewById(R.id.ll_layout_activity_pedido_simple);
        mObservaciones = (TextView) findViewById(R.id.tv_observaciones_pedido_simple_activity);
        tvObservaciones = (TextView) findViewById(R.id.tv_observaciones);
        mPedido = (TextView) findViewById(R.id.tv_id_fragment_pedido);
        mTotal = (TextView) findViewById(R.id.tv_total_fragment_pedido);
        mFinal = (TextView) findViewById(R.id.tv_final_fragment_pedido);
        mDescuento = (TextView) findViewById(R.id.tv_descuento_fragment_pedido);
        mFecha = (TextView) findViewById(R.id.tv_fecha_fragment_pedido);


        mTotalF3 = (TextView) findViewById(R.id.tv_total_f3_fragment_pedido);
        mDescuentoF3 = (TextView) findViewById(R.id.tv_descuento_f3_fragment_pedido);
        mFinalF3 = (TextView) findViewById(R.id.tv_final_f3_fragment_pedido);

        mTotalF2 = (TextView) findViewById(R.id.tv_total_f2_fragment_pedido);
        mDescuentoF2 = (TextView) findViewById(R.id.tv_descuento_f2_fragment_pedido);
        mFinalF2 = (TextView) findViewById(R.id.tv_final_f2_fragment_pedido);


        mTotalF4 = (TextView) findViewById(R.id.tv_total_f4_fragment_pedido);
        mDescuentoF4 = (TextView) findViewById(R.id.tv_descuento_f4_fragment_pedido);
        mFinalF4 = (TextView) findViewById(R.id.tv_final_f4_fragment_pedido);


        mTotalGEN = (TextView) findViewById(R.id.tv_total_gen_fragment_pedido);
        mDescuentoGEN = (TextView) findViewById(R.id.tv_descuento_gen_fragment_pedido);
        mFinalGEN = (TextView) findViewById(R.id.tv_final_gen_fragment_pedido);

        mTipoPrecio = (Spinner) findViewById(R.id.tipo_precio);


        final ArrayList<String> listaNombre = new ArrayList<>();

        String tipoPrecio = pedidoExistemte != null ? pedidoExistemte.getTipoPrecio() : null;

        if (usarPrecioEspecial || "ESP".equals(tipoPrecio)) {
            listaNombre.add("ESP");
        } else {
            listaNombre.add("P.V.F");
            listaNombre.add("P.V.P");
        }

        final ArrayAdapter adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaNombre);

        //   adaptador.setDropDownViewResource(android.R.layout.simple_list_item_1);
        mTipoPrecio.setAdapter(adaptador);
        if (listaNombre.size() == 1) {
            mTipoPrecio.setEnabled(false);
        }
        PrecioTotalAcu = 0;
        PrecioFinalAcu = 0;
        DescuentoAcu = 0;

    }

    @Override
    public void onListFragmentInteraction(PedidoDetalle item) {


    }
}

