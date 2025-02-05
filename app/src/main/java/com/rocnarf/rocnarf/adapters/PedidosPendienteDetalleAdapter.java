package com.rocnarf.rocnarf.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.rocnarf.rocnarf.R;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.models.PedidosPendienteDetalle;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class PedidosPendienteDetalleAdapter extends  RecyclerView.Adapter<PedidosPendienteDetalleAdapter.ViewHolder> {
    public   List<PedidosPendienteDetalle> mValues;
    public   List<PedidosPendienteDetalle> mValuesFiltrados;
    SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);
    PedidosPendienteDetalleAdapter.PedidosPendienteDetalleListener listener;
    Context context;
    public PedidosPendienteDetalleAdapter(List<PedidosPendienteDetalle> values, PedidosPendienteDetalleListener listener){

        this.mValues = values;

        this.mValuesFiltrados = values;
        this.listener = listener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_pedidos_pendiente_detalle, viewGroup, false), listener);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.mItem = mValues.get(i);
        NumberFormat formatoNumero = NumberFormat.getNumberInstance();
        BigDecimal bd = mValues.get(i).getValor();
        bd.setScale(2, BigDecimal.ROUND_HALF_UP); // this does change bd
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);

        NumberFormat formatter = new DecimalFormat("###,###,##0.00");
        BigDecimal myNumber1 = mValues.get(i).getValor();

        String formattedValor = formatter.format(mValues.get(i).getValor());
        String formattedPrecio = formatter.format(mValues.get(i).getPrecio());

        holder.mProducto.setText(mValues.get(i).getProducto());
        holder.mCantidad.setText(formatoNumero.format(mValues.get(i).getCantidad()).toString());
        holder.mBoni.setText(formatoNumero.format(mValues.get(i).getBonific()).toString());
        holder.mPrecio.setText("$ "+ formattedPrecio);
        holder.mDescuento.setText(mValues.get(i).getDescuento() + "%");
        holder.mTotal.setText("$ "+ formattedValor);
        holder.mFecha.setText(sdf.format(mValues.get(i).getFecha()));
        if(mValues.get(i).getTipoPrecio() != null){
            holder.mTipoPrecio.setText("P.V.F");
        }else {
            holder.mTipoPrecio.setText("P.V.P");
        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String query = charSequence.toString();

            List<PedidosPendienteDetalle> filtered = new ArrayList<>();
            Log.d("sincronizar Clientes", "sss"+ charSequence);
            if (charSequence.toString().isEmpty()) {
                filtered.addAll(mValuesFiltrados);
            } else {
                for (PedidosPendienteDetalle Busqueda : mValuesFiltrados) {
                    if (Busqueda.getProducto().toLowerCase().contains(charSequence.toString().toLowerCase())
                            || Busqueda.getFactura().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        Log.d("sincronizar Clientes", "sss"+ Busqueda);

                        filtered.add(Busqueda);
                    }
                }
            }

            FilterResults results = new FilterResults();
//            results.count = filtered.size();
            results.values = filtered;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
//            mValuesFiltrados = (List<Politicas>) results.values;
//            mValues.clear();
            mValues = (List<PedidosPendienteDetalle>) results.values;
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mCantidad,mBoni,mPrecio,mDescuento,mTotal,mProducto,mFecha,mTipoPrecio;


        public PedidosPendienteDetalle mItem;

        public ViewHolder(View view, PedidosPendienteDetalleAdapter.PedidosPendienteDetalleListener pedidosPendienteListener) {
            super(view);
            mView = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.PedidosPendienteDetalleListener(mItem);
                }
            });
            mProducto = (TextView) view.findViewById(R.id.tv_pedido_row_pendiente_detalle);
            mCantidad = (TextView) view.findViewById(R.id.tv_cantidad_row_pendiente_detalle);
            mBoni = (TextView) view.findViewById(R.id.tv_bonificacion_row_pendiente_detalle);
            mPrecio = (TextView) view.findViewById(R.id.tv_precio_row_pendiente_detalle);
            mDescuento = (TextView) view.findViewById(R.id.tv_descuento_row_factura_detalle);
            mTotal = (TextView) view.findViewById(R.id.tv_total_row_pendiente_detalle);
            mFecha =(TextView) view.findViewById(R.id.tv_fecha_row_pendiente_detalle);
            mTipoPrecio =(TextView) view.findViewById(R.id.txtPVFTitlePendiente);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mProducto.getText() + "'";
        }
    }

    public interface  PedidosPendienteDetalleListener {
        void  PedidosPendienteDetalleListener(PedidosPendienteDetalle pedidosPendienteDetalle);

    }
}
