package com.rocnarf.rocnarf;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.ClientesFacturasNotaDebitosReciclerViewAdapter;
import com.rocnarf.rocnarf.models.Factura;
import com.rocnarf.rocnarf.models.FacturasNotaDebitos;
import com.rocnarf.rocnarf.models.FacturasNotaDebitosEstadistica;
import com.rocnarf.rocnarf.viewmodel.ClientesFacturasViewModel;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClientesFacturasNotaDebitosActivity extends AppCompatActivity {
    private ClientesFacturasViewModel clientesFacturasViewModel;
    private Context context;
    private ClientesFacturasNotaDebitosReciclerViewAdapter clientesFacturasReciclerViewAdapter;
    private RecyclerView recyclerView;
    private String idCliente, idUsuario, nombreCliente, tipoPedido, seccion;
    private boolean paraSeleccionFactura;
    private ProgressBar progressBar;
    private ArrayList<String> facturasSeleccionadas = new ArrayList<String>();
    private Button btCobrar;
    private TextView pendientesPago, totalSeleccionadosTextView, valorSaldoAcu,
            valorVencAcu, FacMasDias, valorNoVencido,totalNBFac,SaldoFavorCliente, cupoCredito, promDiasFact, chequeFecha,cupoDisponible;
    private int totalSeleccionados = 0;
    private LinearLayout cobroLayout;
    private int FacturaMasDia = 0;
    private String IcClienteFac = "0";
    private  int diasVen = 0;
    private int diasVenFinal = 0;
    private String IdFactura = "0";
    public BigDecimal valorNovencidoAcu =  new BigDecimal(0.00);;
    public BigDecimal saldoAFavor =  new BigDecimal(0.00);;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes_facturas_nota_debitos);

        context = this;

        Intent i = getIntent();
        idCliente = i.getStringExtra(Common.ARG_IDCLIENTE);
        nombreCliente = i.getStringExtra(Common.ARG_NOMBRE_CLIENTE);
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion = i.getStringExtra(Common.ARG_SECCIOM);
        paraSeleccionFactura = i.getBooleanExtra(Common.ARG_FACTURAS_SELECCION, false);
        tipoPedido = i.getStringExtra(Common.ARG_TIPO_PEDIDO);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(nombreCliente);
        actionBar.setSubtitle(idCliente);


        valorSaldoAcu = (TextView) findViewById(R.id.valor_saldo_ND);
        valorVencAcu = (TextView) findViewById(R.id.valor_vencido_ND);
        FacMasDias = (TextView) findViewById(R.id.f_mas_dia_ND);
        totalNBFac = (TextView) findViewById(R.id.valor_total_ND);
        SaldoFavorCliente = (TextView) findViewById(R.id.tv_cliente_nd);
        valorNoVencido = (TextView) findViewById(R.id.no_vencido_ND);
        cupoCredito = (TextView) findViewById(R.id.cupo_credito_valor);
        promDiasFact = (TextView) findViewById(R.id.prom_dias_factura_valor);
        chequeFecha = (TextView) findViewById(R.id.cheque_fecha_valor);
        cupoDisponible = (TextView) findViewById(R.id.cupo_disponible_valor);

        final ClientesFacturasNotaDebitosReciclerViewAdapter.FacturaSeleccionadaListener facturaSeleccionadaListener = new ClientesFacturasNotaDebitosReciclerViewAdapter.FacturaSeleccionadaListener() {
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


        progressBar = (ProgressBar) findViewById(R.id.pr_list_activity_cliente_facturas_ND);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView = (RecyclerView) findViewById(R.id.rv_facturas_activity_cliente_facturas_ND);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        clientesFacturasViewModel = ViewModelProviders.of(this).get(ClientesFacturasViewModel.class);
        clientesFacturasViewModel.setIdUsuario(idUsuario);

        clientesFacturasViewModel.getFacturasNotaDebitosEstadistica(idCliente,"").observe(this, new Observer<FacturasNotaDebitosEstadistica>() {
            @Override
            public void onChanged(FacturasNotaDebitosEstadistica facturasNotaDebitosEstadistica) {
                DecimalFormat df = new DecimalFormat("#,##0.00");
                cupoCredito.setText(df.format(facturasNotaDebitosEstadistica.getCupoTotal()));
                valorNoVencido.setText(df.format(facturasNotaDebitosEstadistica.getValorNoVencido()));
                valorVencAcu.setText(df.format(facturasNotaDebitosEstadistica.getValorVencido()));
                promDiasFact.setText(String.valueOf(facturasNotaDebitosEstadistica.getPromedioDias()));
                chequeFecha.setText(df.format(facturasNotaDebitosEstadistica.getTotalChequesFecha()));
                totalNBFac.setText(df.format(facturasNotaDebitosEstadistica.getCarteraTotal()));
                cupoDisponible.setText(df.format(facturasNotaDebitosEstadistica.getCupoDisponible()));

            }
        });

        clientesFacturasViewModel.getFacturasNotaDebitos(idCliente, "").observe(this, new Observer<List<FacturasNotaDebitos>>() {
            @Override
            public void onChanged(@Nullable List<FacturasNotaDebitos> facturas) {
                progressBar.setVisibility(View.GONE);

                BigDecimal valorSaldo = BigDecimal.ZERO;
                BigDecimal acuSaldo = BigDecimal.ZERO;
                BigDecimal acuVenc = BigDecimal.ZERO;

                //  BigDecimal valorSaldo = factura.getValor().subtract(factura.getAbonos());
                int plazoDiasMax = 0;
                int DiasVencMax = 0;
                BigDecimal total = new BigDecimal(0);
                BigDecimal CarteraVencida = new BigDecimal(0);
                Log.d("valor v","nota debito----->");

                for (int i = 0; i < facturas.size(); i++) {

                    valorSaldo = facturas.get(i).getValor().subtract(facturas.get(i).getAbonos());
                    valorSaldo = valorSaldo.subtract(facturas.get(i).getNotaCredito());
                    if (valorSaldo.compareTo(new BigDecimal(0)) < 0) {

                        acuSaldo = acuSaldo.add(valorSaldo);
                    }
                    String diasTranscurridos = "";
                    int dias = 0;
                    int mas = 0;
                    Log.d("valor v","nota debito" + facturas.get(i).getTipoDoc());

                    if (valorSaldo.compareTo(new BigDecimal(0)) > 0 || facturas.get(i).getTipoDoc().equals("ND")) {
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

                        if (dias > mas) {
                            mas = dias;
                            //FacturaMasDia = valorSaldo.toString();
                            FacturaMasDia = dias;
                            IcClienteFac= facturas.get(i).getIdCliente();
                            IdFactura = facturas.get(i).getIdFactura();


                        }
                        Log.d("valor v","fffff" + valorNovencidoAcu);

                        //valorNovencidoAcu = valorNovencidoAcu.add(valorSaldo);
                    }


                    if (valorSaldo.compareTo(new BigDecimal(0)) <0){
                        saldoAFavor = saldoAFavor.add(valorSaldo);
                    }


                    int diasVen = 0;
                    if(facturas.get(i).getVencimiento()>0 && dias>0){
                        diasVen = facturas.get(i).getVencimiento() - dias;
                    }

                    if(plazoDiasMax < facturas.get(i).getVencimiento()) plazoDiasMax = facturas.get(i).getVencimiento();

                    if(DiasVencMax < diasVen) DiasVencMax = diasVen;

                    if (valorSaldo.signum() > 0 && dias > facturas.get(i).getVencimiento() && facturas.get(i).getTipoDoc().equals("FA")) {

                        acuVenc = acuVenc.add(valorSaldo);

                    }else if(valorSaldo.signum() > 0 && dias < facturas.get(i).getVencimiento() && facturas.get(i).getTipoDoc().equals("FA")){
                        valorNovencidoAcu = valorNovencidoAcu.add(valorSaldo);
                    }

                    if(facturas.get(i).getTipoDoc().equals("ND")){
                        CarteraVencida = CarteraVencida.add(facturas.get(i).getValor().subtract(facturas.get(i).getAbonos()));

                        if(dias>diasVen) diasVen = dias;
                    }


                    if(facturas.get(i).getVencimiento()>0 && dias>0){
                        diasVen = dias - facturas.get(i).getVencimiento() ;
                    }


                    if(diasVen >= diasVenFinal) {
                        diasVenFinal = diasVen;
                    }

                }


                NumberFormat formatter = new DecimalFormat("###,###,##0.00");
                String format = formatter.format(acuSaldo);

                if (acuSaldo.compareTo(BigDecimal.ZERO) > 0) {

                    total = total.add(acuSaldo);
                    format = formatter.format(acuSaldo.abs());
                }

                Log.d("ssasas","sas"+ valorSaldo);
                Log.d("ssasas","sas"+ acuVenc);
                CarteraVencida = CarteraVencida.add(acuVenc);
                String formatCartera = formatter.format(CarteraVencida);

                String formatAFavor= formatter.format(saldoAFavor.abs());

                SaldoFavorCliente.setText("$ " + formatAFavor);
                if(CarteraVencida.compareTo(BigDecimal.ZERO )> 0){
                    valorSaldoAcu.setTextColor(Color.RED);
                    valorSaldoAcu.setText(formatCartera);
                }else{
                    valorSaldoAcu.setTextColor(Color.BLACK);
                }



                if (DiasVencMax > 0){
                    FacMasDias.setTextColor(Color.RED);
                }

                if(diasVenFinal> 0){
                    FacMasDias.setTextColor(Color.RED);

                }else{
                    FacMasDias.setTextColor(Color.BLACK);
                }
                FacMasDias.setText(String.valueOf(diasVenFinal));

                if (valorNovencidoAcu.compareTo(BigDecimal.ZERO) > 0) {

                    String formattV = formatter.format(valorNovencidoAcu);

                    //valorNoVencido.setText(formattV);
                    total = total.add(valorNovencidoAcu);
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
//                valorSaldoAcu.setText("$ " + format);
//                if(acuVenc.compareTo(BigDecimal.ZERO) > 0) {
//                    valorVencAcu.setText("$ " + acuVenc);
//                    valorVencAcu.setTextColor(Color.RED);
//
//                }

                //valorVencAcu.setText(String.valueOf(plazoDiasMax));


                String formatt = formatter.format(valorNovencidoAcu.add(CarteraVencida));
                if(saldoAFavor.abs().compareTo(BigDecimal.ZERO) > 0 ){
                    SaldoFavorCliente.setTextColor(Color.parseColor("#21d162"));
                }else{
                    SaldoFavorCliente.setTextColor(Color.BLACK);
                }
                //totalNBFac.setText(formatt);

                if (facturas != null) {
                    if (facturas.size() == 0)
                        Toast.makeText(context, "Cliente no tiene pedidos realizados", Toast.LENGTH_LONG).show();
                    clientesFacturasReciclerViewAdapter = new ClientesFacturasNotaDebitosReciclerViewAdapter(getApplicationContext(), idUsuario, facturas, paraSeleccionFactura, facturaSeleccionadaListener);
                    recyclerView.setAdapter(clientesFacturasReciclerViewAdapter);
                    List<FacturasNotaDebitos> listaPendientesPago = new ArrayList<>();
                    for (FacturasNotaDebitos f : facturas) {
                        BigDecimal saldo = f.getValor().subtract(f.getAbonos());
                        saldo = saldo.subtract(f.getNotaCredito());
                        if (saldo.compareTo(new BigDecimal(0)) > 0)
                            listaPendientesPago.add(f);
                    }

                }
            }
        });



    }


}
