package com.rocnarf.rocnarf.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rocnarf.rocnarf.FacturaDetalleActivity;
import com.rocnarf.rocnarf.R;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.models.Factura;
import com.rocnarf.rocnarf.models.FacturasNotaDebitos;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ClientesFacturasNotaDebitosReciclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<FacturasNotaDebitos> mValues;
    private Context context;
    private String idUsuario;
    private boolean paraSeleccionFactura;
    private final boolean[] mCheckedState;
    private final FacturaSeleccionadaListener mListener;

    public interface FacturaSeleccionadaListener {
        void FacturasSeleccionadas(Factura factura, boolean chequeado);
    }

    public ClientesFacturasNotaDebitosReciclerViewAdapter(Context context, String idUsuario, List<FacturasNotaDebitos> items, boolean paraSeleccionFactura, FacturaSeleccionadaListener listener) {
        this.mValues = items;
        this.context = context;
        this.idUsuario = idUsuario;
        this.paraSeleccionFactura = paraSeleccionFactura;
        this.mListener = listener;
        mCheckedState = new boolean[items.size()];
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_clientes_facturas_nota_debitos, viewGroup, false);
            return new FacturasViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder,final int i) {

        FacturasViewHolder holder = (FacturasViewHolder)viewHolder;
        final FacturasNotaDebitos factura = mValues.get(i);
        int dias = 0;
        int diasPagado = 0;
        if(factura.getTipoDoc().equals("FA")){
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent iFacturaDetalle = new Intent(context, FacturaDetalleActivity.class);
                    iFacturaDetalle.putExtra(Common.ARG_IDCLIENTE, factura.getIdCliente());
                    iFacturaDetalle.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    iFacturaDetalle.putExtra(Common.ARG_IDFACTURA, factura.getIdFactura());
                    iFacturaDetalle.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(iFacturaDetalle );
                }
            });

            holder.mLineaNotaDebito.setVisibility(View.GONE);
            holder.mLineaFactura.setVisibility(View.VISIBLE);
        }else{
            holder.mLineaFactura.setVisibility(View.GONE);
            holder.mLineaNotaDebito.setVisibility(View.VISIBLE);



            Calendar fechaPedido = Calendar.getInstance();
                fechaPedido.setTime(factura.getFecha() != null ? factura.getFecha() : Calendar.getInstance().getTime());
                Calendar hoy = Calendar.getInstance();
                long diff = hoy.getTimeInMillis() - fechaPedido.getTimeInMillis(); //result
                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;
                long daysInMilli = hoursInMilli * 24;

                long elapsedDays = diff / daysInMilli;
                //diasTranscurridosNB = String.valueOf(elapsedDays);
                dias = (int) elapsedDays;
                holder.mDiasFacND.setText(String.valueOf(dias));
                holder.mDiasVenND.setText(String.valueOf(dias));

                if(dias > 0 ){
                    holder.mDiasFacND.setTextColor(Color.RED);
                    holder.mDiasVenND.setTextColor(Color.RED);
                }else{
                    holder.mDiasFacND.setTextColor(Color.GRAY);
                    holder.mDiasVenND.setTextColor(Color.GRAY);

                }



        }
        holder.mNumeroFac.setText(factura.getIdFactura().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy", new Locale("es", "ES"));

        if (factura.getFecha() != null) {
            holder.mEstadoFactura.setText("Despachado");
            holder.mFechaFac.setText(sdf.format(factura.getFecha()));
            holder.mFechaND.setText(sdf.format(factura.getFecha()));
        }else {
            holder.mEstadoFactura.setText("Por Despachar");
            holder.mFechaFac.setText(" ");
           // holder.mFechaND.setText(sdf.format(factura.getFecha()));
            holder.mFechaND.setText(" ");
        }

        holder.mVendedor.setText(factura.getIdVendedor()+ " - " + factura.getCobrador());
        holder.mNumeroFac.setText(factura.getNumeroFactura());
        holder.mPedidoFac.setText(factura.getIdFactura());
        holder.mPlazoFac.setText(String.valueOf(factura.getVencimiento()));

        holder.mCheque.setText(factura.getNumcheq());
        holder.mBanco.setText(factura.getBanco());

        NumberFormat formatter = new DecimalFormat("###,###,##0.00");
        BigDecimal myNumber1 = factura.getValor();
        String formattedNumberValor = formatter.format(myNumber1);

        holder.mValor.setText(formattedNumberValor);
        BigDecimal myNumberAb = factura.getAbonos();
        String formattedNumberAbono = formatter.format(myNumberAb);

        holder.mAbono.setText(String.valueOf(formattedNumberAbono));


        BigDecimal myNumberNc = factura.getNotaCredito();
        String formattedNumberNc = formatter.format(myNumberNc);
        holder.mNotaCredito.setText(String.valueOf(formattedNumberNc));
        BigDecimal saldo = factura.getValor().subtract(factura.getAbonos());
        saldo = saldo.subtract(factura.getNotaCredito());

        String diasTranscurridos = "OK";
        if(saldo.compareTo(BigDecimal.ZERO) == 0){
            Log.d("xxxx","pagadooo" + saldo);

            Date fechaDeCobro = factura.getFechaCobro() != null ? factura.getFechaCobro() : factura.getFechaNotaCredito();
            Calendar fechaPago = Calendar.getInstance();

            fechaPago.setTime(factura.getFecha() != null ? factura.getFecha() : Calendar.getInstance().getTime());
            Calendar hoy = Calendar.getInstance();
            hoy.setTime(fechaDeCobro);
            long diff = hoy.getTimeInMillis() - fechaPago.getTimeInMillis(); //result
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = diff / daysInMilli;
            diasTranscurridos = String.valueOf(elapsedDays);
            diasPagado = (int) elapsedDays;
            Log.d("xxxx","dias" + diasPagado);
            dias = diasPagado;
        }


        if (saldo.compareTo(new BigDecimal(0)) > 0  && factura.getTipoDoc().equals("FA")) {
//        if (saldo.signum() > 0 ) {
            Calendar fechaPedido = Calendar.getInstance();
            fechaPedido.setTime(factura.getFecha() != null ? factura.getFecha() : Calendar.getInstance().getTime());
            Calendar hoy = Calendar.getInstance();
            long diff = hoy.getTimeInMillis() - fechaPedido.getTimeInMillis(); //result
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = diff / daysInMilli;
            diasTranscurridos = String.valueOf(elapsedDays);
            dias = (int) elapsedDays;
        }

        holder.mDiasFac.setText(String.valueOf(dias));
        Log.d("xxxx","antes" + saldo);



        int diasVen = 0;
        if(factura.getVencimiento()>0 && dias>0){
            diasVen = dias - factura.getVencimiento() ;
        }
        String format = formatter.format(saldo);
        holder.mSaldo.setText(String.valueOf(format));





        if(diasVen < 0){
            holder.mDiasVenFac.setText(String.valueOf(0));
            holder.mDiasVenFac.setTextColor(Color.BLACK);

            //holder.mSaldo.setTextColor(Color.parseColor("#21d162"));
            if(saldo.compareTo(BigDecimal.ZERO) == 0) holder.mSaldo.setTextColor(Color.BLACK);
        }else if (diasVen == 0 ){
            holder.mDiasVenFac.setText(String.valueOf(diasVen));
            holder.mDiasVenFac.setTextColor(Color.BLACK);
            holder.mSaldo.setTextColor(Color.BLACK);

        }else{
            holder.mDiasVenFac.setText(String.valueOf(diasVen));
            holder.mDiasVenFac.setTextColor(Color.RED);
            holder.mSaldo.setTextColor(Color.BLACK);
        }

        BigDecimal valorFinalDB = factura.getValor().subtract(factura.getAbonos());
        String formatvalorFinalDB = formatter.format(valorFinalDB);
        holder.mValorND.setText(formatvalorFinalDB);
        holder.mCheque.setText(factura.getNumcheq());
        holder.mBanco.setText(factura.getBanco());

        if(factura.getTipo().equals("PRO")){
            holder.mEstadoND.setText("Protesto");
        }else if(factura.getTipo().equals("CAN")){
            holder.mEstadoND.setText("Canje");
        }

        if(factura.getTipoDoc().equals("FA")){
            Log.d("entra","entra;" + diasVen);
            if(diasVen<0)diasVen=0;
            if(saldo.compareTo(new BigDecimal(0)) > 0 && diasVen == 0) {
                holder.mEstado.setText("No Vencido");
                holder.mEstado.setTextColor(Color.BLACK);

            }else if(saldo.compareTo(new BigDecimal(0)) > 0 && diasVen > 0){
                holder.mEstado.setText("Vencido");
                holder.mEstado.setTextColor(Color.RED);

            }else if(saldo.equals(0)){
                holder.mEstado.setText("Cancelado");
                holder.mEstado.setTextColor(Color.BLACK);

            }
        }

        if (factura.getFechaCobro() != null) {
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yy", new Locale("es", "ES"));
            String fechaFormateada = sdf2.format(factura.getFechaCobro());
            holder.mFechaPago.setText(fechaFormateada);

            // Comparar con la fecha actual
            Date hoy = new Date();
            if (factura.getFechaCobro().after(hoy)) {
                holder.mFechaPago.setTextColor(Color.RED);
            } else {
                holder.mFechaPago.setTextColor(Color.BLACK); // o el color normal por defecto
            }
        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class FacturasViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public final TextView mFechaFac,mFechaND;
        public final TextView mVendedor;
        public final TextView mValor;
        public final TextView mAbono;
        public final TextView mNotaCredito;
        public final TextView mSaldo;
        public final TextView mNumeroFac, mPedidoFac,mValorND,mCheque,mBanco,mEstadoFactura,mPlazoFac,mDiasFac,mDiasVenFac,mEstadoND,mDiasVenND,mDiasFacND,mEstado,mFechaPago;
        public final LinearLayout mLineaNotaDebito,mLineaFactura;


        public FacturasViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mNumeroFac = (TextView)itemView.findViewById(R.id.tv_numero_nd);
            mFechaFac = (TextView)itemView.findViewById(R.id.tv_fecha_row_cobro_nd);
            mVendedor = (TextView)itemView.findViewById(R.id.tv_rep_nd);
            mPedidoFac = (TextView)itemView.findViewById(R.id.tv_pedido_nd);
            mValor = (TextView)itemView.findViewById(R.id.tv_valor_nd);
            mAbono = (TextView)itemView.findViewById(R.id.tv_abono_nd);
            mNotaCredito = (TextView)itemView.findViewById(R.id.tv_nc_nd);
            mSaldo = (TextView)itemView.findViewById(R.id.tv_saldo_nd);
            mFechaND = (TextView)itemView.findViewById(R.id.tv_fecha_row_cobro);
            mValorND = (TextView)itemView.findViewById(R.id.tv_valor_nd_debito);
            mCheque = (TextView)itemView.findViewById(R.id.tv_cheque_nd);
            mBanco = (TextView)itemView.findViewById(R.id.tv_banco_nd);
            mEstadoFactura = (TextView)itemView.findViewById(R.id.tv_fecha_row_cobro2);
            mPlazoFac = (TextView)itemView.findViewById(R.id.tv_dia_plazo_nd);
            mDiasFac = (TextView)itemView.findViewById(R.id.tv_dia_fact_nd);
            mDiasVenFac = (TextView)itemView.findViewById(R.id.tv_dia_vcdo_nd);
            mEstadoND = (TextView)itemView.findViewById(R.id.tv_estado_nd);
            mDiasFacND = (TextView)itemView.findViewById(R.id.tv_dia_fact_nd_debito);
            mDiasVenND = (TextView)itemView.findViewById(R.id.tv_dia_vcdo_nd_debito);
            mLineaNotaDebito = (LinearLayout)itemView.findViewById(R.id.linearLayout4);
            mLineaFactura = (LinearLayout)itemView.findViewById(R.id.linearLayout3);
            mEstado = (TextView)itemView.findViewById(R.id.tv_tipo_factura);
            mFechaPago = (TextView)itemView.findViewById(R.id.fecha_pago);




        }
    }
}
