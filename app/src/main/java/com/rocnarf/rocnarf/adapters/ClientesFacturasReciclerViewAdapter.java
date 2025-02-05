package com.rocnarf.rocnarf.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.rocnarf.rocnarf.FacturaDetalleActivity;
import com.rocnarf.rocnarf.R;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.models.Factura;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ClientesFacturasReciclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Factura> mValues;
    private Context context;
    private String idUsuario;
    private boolean paraSeleccionFactura;
    private final boolean[] mCheckedState;
    private final FacturaSeleccionadaListener mListener;

    public interface FacturaSeleccionadaListener {
        void FacturasSeleccionadas(Factura factura, boolean chequeado);
    }

    public ClientesFacturasReciclerViewAdapter(Context context, String idUsuario,  List<Factura> items, boolean paraSeleccionFactura, FacturaSeleccionadaListener listener) {
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
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_clientes_facturas, viewGroup, false);
            return new FacturasViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder,final int i) {

        FacturasViewHolder holder = (FacturasViewHolder)viewHolder;
        final Factura factura = mValues.get(i);
        holder.mIdFactura.setText(factura.getIdFactura().toString());
        SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);
        if (factura.getFecha() != null) {
            holder.mFecha.setText(sdf.format(factura.getFecha()));
        }else {
            holder.mFecha.setText(" ");
        }

        holder.mVendedor.setText(factura.getIdVendedor().toString());
        holder.mFactura.setText(factura.getNumeroFactura());

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
        int dias = 0;
        if (saldo.compareTo(new BigDecimal(0)) > 0 ) {
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

//        if (diasTranscurridos == "OK"){
//            holder.mSaldo.setTextColor(Color.GRAY);
//        }else {
//            holder.mVencimiento.setTextColor(Color.RED);
//        }
        holder.mVencimiento.setText("Venc: " + String.valueOf(factura.getVencimiento()) + "   Dias: " + diasTranscurridos);
//        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
//        formatter.applyPattern("#,###");
//        formatter.format(saldo);

//        String sx = String.format("%,d", Long.parseLong(saldo.toString()));

//        String pattern = "###,###.###";
//        DecimalFormat decimalFormat = new DecimalFormat(pattern);
//        NumberFormat formatter1 = new DecimalFormat("###,###,##0.00");
//        decimalFormat.applyPattern("#0.##");

//        BigDecimal myNumber = saldo;
//        String formattedNumberSaldo = formatter.format(myNumber);
//
//        String pattern = "###,###.##";
//        DecimalFormat decimalFormat = new DecimalFormat(pattern);

        String format = formatter.format(saldo);


        holder.mSaldo.setText(String.valueOf(format));
       // Log.d("log","getVencimiento --- >"  + factura.getVencimiento());




        if (saldo.signum() > 0 && dias > factura.getVencimiento()){
            holder.mSaldo.setTextColor(Color.RED);
            holder.mVencimiento.setTextColor(Color.RED);
            holder.mSaldodolar.setTextColor(Color.RED);
            holder.mSaldoText.setTextColor(Color.RED);
        }else {
            holder.mSaldo.setTextColor(Color.GRAY);
            holder.mVencimiento.setTextColor(Color.GRAY);
            holder.mSaldodolar.setTextColor(Color.GRAY);
            holder.mSaldoText.setTextColor(Color.GRAY);
        }

        if(saldo.signum() < 0){
            holder.mSaldo.setTextColor(Color.parseColor("#21d162"));
           /// holder.mVencimiento.setTextColor(Color.GREEN);
            holder.mSaldodolar.setTextColor(Color.parseColor("#21d162"));
            holder.mSaldoText.setTextColor(Color.parseColor("#21d162"));
            //holder.mSaldodolar.setTextColor(Color.GRAY);
            //holder.mSaldoText.setTextColor(Color.GRAY);
            //holder.mSaldo.setTextColor(Color.GRAY);
        } else if (saldo.signum() == 0){
            holder.mSaldodolar.setTextColor(Color.GRAY);
            holder.mSaldoText.setTextColor(Color.GRAY);
            holder.mSaldo.setTextColor(Color.GRAY);
        }

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

        holder.mSeleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    boolean chequeado = ((CheckBox) v).isChecked();
                    mCheckedState[i] = chequeado;
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.FacturasSeleccionadas(factura, chequeado);
                }
            }
        });
//        holder.mEstado.setVisibility(View.GONE);
        if (factura.getFecha() != null) {
            holder.mEstadoFactura.setText("Despachado");
          // holder.mEstadoFactura.setTextColor(Color.BLACK);

        } else {
            holder.mEstadoFactura.setText("Por Despachar");
            holder.mEstadoFactura.setTextColor(Color.RED);
        }
//        if (factura.getEstado() != null) {
//            if (factura.getEstado().equals("FDESP")) {
//                holder.mEstado.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_circle_primary_24dp));
//                holder.mEstado.setVisibility(View.VISIBLE);
//            } else if (factura.getEstado().equals("FXDES")) {
//                holder.mEstado.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_timer_black_24dp));
//                holder.mEstado.setVisibility(View.VISIBLE);
//            } else if (factura.getEstado().equals("FPEND")) {
//                holder.mEstado.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_add_circle_red_24dp));
//                holder.mEstado.setVisibility(View.VISIBLE);
//            }
//        }

        holder.mSeleccionar.setVisibility(View.GONE);
        if (paraSeleccionFactura) {
            holder.mSeleccionar.setVisibility(View.VISIBLE);
            holder.mSeleccionar.setChecked(mCheckedState[i]);
        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class FacturasViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public final TextView mIdFactura;
        public final TextView mFecha;
        public final TextView mVendedor;
        public final TextView mFactura;
        public final TextView mVencimiento;
        public final TextView mValor;
        public final TextView mAbono;
        public final TextView mNotaCredito;
        public final TextView mSaldo;
        public final TextView mSaldoText;
        public final TextView mSaldodolar;
        //        public final ImageView mEstado;
        public final CheckBox mSeleccionar;
        public final TextView mEstadoFactura;


        public FacturasViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mIdFactura = (TextView)itemView.findViewById(R.id.tv_idFactura_row_clientes_facturas);
            mFecha = (TextView)itemView.findViewById(R.id.tv_fecha_row_clientes_facturas);
            mVendedor = (TextView)itemView.findViewById(R.id.tv_vendedor_row_clientes_facturas);
            mEstadoFactura = (TextView)itemView.findViewById(R.id.tv_estado_desp);
            mFactura = (TextView)itemView.findViewById(R.id.tv_factura_row_clientes_facturas);
            mVencimiento = (TextView)itemView.findViewById(R.id.tv_vencimiento_row_clientes_facturas);
            mValor = (TextView)itemView.findViewById(R.id.tv_valor_row_clientes_facturas);
            mAbono = (TextView)itemView.findViewById(R.id.tv_abono_row_clientes_facturas);
            mSaldoText = (TextView)itemView.findViewById(R.id.textView36);
            mSaldodolar = (TextView)itemView.findViewById(R.id.textView_dolar);
            mNotaCredito = (TextView)itemView.findViewById(R.id.tv_nota_credito_row_clientes_facturas);
            mSaldo = (TextView)itemView.findViewById(R.id.tv_saldo_row_clientes_facturas);
//            mEstado = (ImageView)itemView.findViewById(R.id.iv_estado_row_clientes_facturas);
            mSeleccionar = (CheckBox)itemView.findViewById(R.id.bt_seleccionar_row_clientes_facturas);
        }
    }
}
