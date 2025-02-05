package com.rocnarf.rocnarf;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.ClientesFacturasReciclerViewAdapter;
import com.rocnarf.rocnarf.models.Factura;
import com.rocnarf.rocnarf.viewmodel.ClientesFacturasViewModel;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClientesFacturasActivity extends AppCompatActivity {
    private ClientesFacturasViewModel clientesFacturasViewModel;
    private Context context;
    private ClientesFacturasReciclerViewAdapter clientesFacturasReciclerViewAdapter;
    private RecyclerView recyclerView;
    private String idCliente, idUsuario, nombreCliente, tipoPedido, seccion;
    private boolean paraSeleccionFactura, paraSeleccionFacturaQuejas = false;
    private ProgressBar progressBar;
    private ArrayList<String> facturasSeleccionadas = new ArrayList<String>();
    private Button btCobrar,btQueja;
    private TextView pendientesPago, totalSeleccionadosTextView, valorSaldoAcu, valorVencAcu, FacMasDias, valorNoVencido;
    private int totalSeleccionados = 0;
    private LinearLayout cobroLayout,quejaLayout;
    private int FacturaMasDia = 0;
    private String IcClienteFac = "0";
    private String IdFactura = "0";
    public BigDecimal valorNovencidoAcu =  new BigDecimal(0.00);;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes_facturas);

        context = this;

        Intent i = getIntent();
        idCliente = i.getStringExtra(Common.ARG_IDCLIENTE);
        nombreCliente = i.getStringExtra(Common.ARG_NOMBRE_CLIENTE);
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion = i.getStringExtra(Common.ARG_SECCIOM);
        paraSeleccionFactura = i.getBooleanExtra(Common.ARG_FACTURAS_SELECCION, false);
        paraSeleccionFacturaQuejas = i.getBooleanExtra(Common.ARG_FACTURAS_SELECCION_QUEJAS, false);
        tipoPedido = i.getStringExtra(Common.ARG_TIPO_PEDIDO);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(nombreCliente);
        actionBar.setSubtitle(idCliente);

        cobroLayout = (LinearLayout) findViewById(R.id.ll_cobro_activity_cliente_factura);
        quejaLayout = (LinearLayout) findViewById(R.id.ll_quejas_factura);

        pendientesPago = (TextView) findViewById(R.id.tv_porpagar_activity_cliente_facturas);
        totalSeleccionadosTextView = (TextView) findViewById(R.id.tv_seleccionados_activity_cliente_facturas);
        valorSaldoAcu = (TextView) findViewById(R.id.valor_saldo);
        valorVencAcu = (TextView) findViewById(R.id.valor_vencido);
        FacMasDias = (TextView) findViewById(R.id.f_mas_dia);
        valorNoVencido = (TextView) findViewById(R.id.no_vencido);

        final ClientesFacturasReciclerViewAdapter.FacturaSeleccionadaListener facturaSeleccionadaListener = new ClientesFacturasReciclerViewAdapter.FacturaSeleccionadaListener() {
            @Override
            public void FacturasSeleccionadas(Factura factura, boolean chequeado) {
                if (chequeado) {
                    totalSeleccionados += 1;
                    facturasSeleccionadas.add(factura.getIdFactura());
                } else {
                    totalSeleccionados -= 1;
                    facturasSeleccionadas.remove((Object) factura.getIdFactura());
                }
                totalSeleccionadosTextView.setText(totalSeleccionados + " Seleccionadas");
            }
        };


        progressBar = (ProgressBar) findViewById(R.id.pr_list_activity_cliente_facturas);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView = (RecyclerView) findViewById(R.id.rv_facturas_activity_cliente_facturas);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        clientesFacturasViewModel = ViewModelProviders.of(this).get(ClientesFacturasViewModel.class);
        clientesFacturasViewModel.setIdUsuario(idUsuario);

        clientesFacturasViewModel.getFacturas(idCliente, "").observe(this, new Observer<List<Factura>>() {
            @Override
            public void onChanged(@Nullable List<Factura> facturas) {
                progressBar.setVisibility(View.GONE);

                BigDecimal valorSaldo = BigDecimal.ZERO;
                BigDecimal acuSaldo = BigDecimal.ZERO;
                BigDecimal acuVenc = BigDecimal.ZERO;

                //  BigDecimal valorSaldo = factura.getValor().subtract(factura.getAbonos());
                for (int i = 0; i < facturas.size(); i++) {
                    valorSaldo = facturas.get(i).getValor().subtract(facturas.get(i).getAbonos());
                    valorSaldo = valorSaldo.subtract(facturas.get(i).getNotaCredito());
                    if (valorSaldo.compareTo(new BigDecimal(0)) < 0) {

                        acuSaldo = acuSaldo.add(valorSaldo);
                    }
                    String diasTranscurridos = "";
                    int dias = 0;
                    int mas = 0;
                    if (valorSaldo.compareTo(new BigDecimal(0)) > 0) {
                        Calendar fechaPedido = Calendar.getInstance();
                        fechaPedido.setTime(facturas.get(i).getFecha() != null ? facturas.get(i).getFecha() : Calendar.getInstance().getTime());
                        Calendar hoy = Calendar.getInstance();
                        long diff = hoy.getTimeInMillis() - fechaPedido.getTimeInMillis(); //result
                        long secondsInMilli = 1000;
                        long minutesInMilli = secondsInMilli * 60;
                        long hoursInMilli = minutesInMilli * 60;
                        long daysInMilli = hoursInMilli * 24;
                        long elapsedDays = diff / daysInMilli;
                        diasTranscurridos = String.valueOf(elapsedDays);
                        dias = (int) elapsedDays;
                        Log.d("dias", "dias" + dias);

                        if (dias > mas) {
                            mas = dias;
                            //FacturaMasDia = valorSaldo.toString();
                            FacturaMasDia = dias;
                            IcClienteFac= facturas.get(i).getIdCliente();
                            IdFactura = facturas.get(i).getIdFactura();
                            valorNovencidoAcu = valorNovencidoAcu.add(valorSaldo);

                        }


                    }

                    if (valorSaldo.signum() > 0 && dias > facturas.get(i).getVencimiento()) {

                        acuVenc = acuVenc.add(valorSaldo);

                    }
                }
                NumberFormat formatter = new DecimalFormat("###,###,##0.00");
                String format = formatter.format(acuSaldo);

                if (acuSaldo.compareTo(BigDecimal.ZERO) < 0) {
                    format = formatter.format(acuSaldo.abs());
                }
                valorSaldoAcu.setTextColor(Color.parseColor("#21d162"));


                FacMasDias.setText( "" + FacturaMasDia);

                if (valorNovencidoAcu.compareTo(BigDecimal.ZERO) > 0) {
                valorNoVencido.setText("$ " + valorNovencidoAcu);
                }

                FacMasDias.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      Intent iFacturaDetalle = new Intent(context, FacturaDetalleActivity.class);
                                                      iFacturaDetalle.putExtra(Common.ARG_IDCLIENTE, IcClienteFac);
                                                      iFacturaDetalle.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                                      iFacturaDetalle.putExtra(Common.ARG_IDFACTURA, IdFactura);
                                                      iFacturaDetalle.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                      context.startActivity(iFacturaDetalle );
                                                  }
                                              }
                );
                valorSaldoAcu.setText("$ " + format);
                if(acuVenc.compareTo(BigDecimal.ZERO) > 0) {
                    valorVencAcu.setText("$ " + acuVenc);
                    valorVencAcu.setTextColor(Color.RED);

                }

                if (facturas != null) {
                    if (facturas.size() == 0)
                        Toast.makeText(context, "Cliente no tiene pedidos realizados", Toast.LENGTH_LONG).show();
                    clientesFacturasReciclerViewAdapter = new ClientesFacturasReciclerViewAdapter(getApplicationContext(), idUsuario, facturas, paraSeleccionFactura, facturaSeleccionadaListener);
                    recyclerView.setAdapter(clientesFacturasReciclerViewAdapter);
                    List<Factura> listaPendientesPago = new ArrayList<>();
                    for (Factura f : facturas) {
                        BigDecimal saldo = f.getValor().subtract(f.getAbonos());
                        saldo = saldo.subtract(f.getNotaCredito());
                        if (saldo.compareTo(new BigDecimal(0)) > 0)
                            listaPendientesPago.add(f);
                    }

                    pendientesPago.setText("Pendientes de Pago " + listaPendientesPago.size());
                    if (paraSeleccionFactura && !paraSeleccionFacturaQuejas){
                        cobroLayout.setVisibility(View.VISIBLE);
                    }else if(paraSeleccionFactura && paraSeleccionFacturaQuejas){
                        quejaLayout.setVisibility(View.VISIBLE);

                    }
                }
            }
        });
        //clientesFacturasViewModel.getFacturas("803414", "");
        btCobrar = (Button) findViewById(R.id.bt_cobrar_activity_cliente_facturas);
        btCobrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (totalSeleccionados > 0) {
                    Intent i = new Intent(getApplicationContext(), CobroActivity.class);
                    i.putExtra(Common.ARG_IDCLIENTE, idCliente);
                    i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    i.putExtra(Common.ARG_SECCIOM, seccion);
                    i.putExtra(Common.ARG_NOMBRE_CLIENTE, nombreCliente);
                    String[] mStringArray = new String[facturasSeleccionadas.size()];
                    mStringArray = facturasSeleccionadas.toArray(mStringArray);
                    i.putExtra(Common.ARG_TIPO_PEDIDO, tipoPedido);
                    i.putExtra(Common.ARG_FACTURAS_COBRAR, mStringArray); //  facturasSeleccionadas.toArray());
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Debe seleccionar al menos una factura para ir a Cobros."
                            , Toast.LENGTH_LONG).show();
                }

            }
        });

        btQueja = (Button) findViewById(R.id.bt_agregar_factura_queja);
        btQueja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalSeleccionados > 0) {
                    String idFacturas = "";

                    for (int s = 0; s < facturasSeleccionadas.size(); s++) {
                        idFacturas = idFacturas + facturasSeleccionadas.get(s);

                        // Agregar coma solo si no es el último elemento
                        if (s < facturasSeleccionadas.size() - 1) {
                            idFacturas = idFacturas + ",";
                        }                    }

                    Intent intent = new Intent();
                    String[] mStringArray = new String[facturasSeleccionadas.size()];
                    intent.putExtra(Common.ARG_TIPO_PEDIDO, tipoPedido);
                    intent.putExtra(Common.ARG_FACTURAS_COBRAR, idFacturas); //  facturasSeleccionadas.toArray());
                    // Establecer el resultado y finalizar la actividad
                    setResult(RESULT_OK, intent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Debe seleccionar al menos una factura."
                            , Toast.LENGTH_LONG).show();
                }
            }
        });


    }


}
