package com.rocnarf.rocnarf.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rocnarf.rocnarf.R;
import com.rocnarf.rocnarf.models.VentaMensualXCliente;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class VentasMensualesClientesReciclerViewAdapter extends RecyclerView.Adapter<VentasMensualesClientesReciclerViewAdapter.ViewHolder>  {
    private List<VentaMensualXCliente> mValues;
    private Context context;
    private String idUsuario;

    public VentasMensualesClientesReciclerViewAdapter(Context context, String idUsuario,  List<VentaMensualXCliente> items){
        this.mValues = items;
        this.context = context;
        this.idUsuario =  idUsuario;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_ventas_mensuales_clientes, viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        LocalDate today = LocalDate.now();
        holder.mItem = mValues.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM-yyyy" , new Locale("es","ES"));
        holder.mMes.setText(sdf.format(mValues.get(position).getFecha()).toUpperCase());
        holder.mVenta.setText(mValues.get(position).getMonto().toString());
        holder.mCobro.setText(mValues.get(position).getCobrado().toString());
        holder.mNotaCredito.setText(mValues.get(position).getNotaCredito().toString());
        holder.mSaldo.setText(mValues.get(position).getSaldo().toString());
        holder.mNeta.setText(mValues.get(position).getVentaNeta().toString());


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mMes;
        public final TextView mVenta;
        public final TextView mNeta;
        public final TextView mNotaCredito;
        public final TextView mCobro;
        public final TextView mSaldo;
        public VentaMensualXCliente mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            mMes = (TextView) view.findViewById(R.id.tv_mes_row_ventas_mensuales_clientes);
            mVenta = (TextView) view.findViewById(R.id.tv_venta_row_ventas_mensuales_clientes);
            mNotaCredito = (TextView) view.findViewById(R.id.tv_nota_credito_row_ventas_mensuales_clientes);
            mNeta = (TextView) view.findViewById(R.id.tv_neta_row_ventas_mensuales_clientes);
            mCobro = (TextView) view.findViewById(R.id.tv_cobro_row_ventas_mensuales_clientes);
            mSaldo = (TextView) view.findViewById(R.id.tv_saldo_row_ventas_mensuales_clientes);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mMes.getText() + "'";
        }
    }



}
