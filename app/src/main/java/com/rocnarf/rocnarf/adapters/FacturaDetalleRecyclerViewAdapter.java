package com.rocnarf.rocnarf.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Filterable;
import com.rocnarf.rocnarf.FacturaDetalleFragment.OnListFragmentInteractionListener;
import com.rocnarf.rocnarf.R;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.models.FacturaDetalle;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class FacturaDetalleRecyclerViewAdapter extends RecyclerView.Adapter<FacturaDetalleRecyclerViewAdapter.ViewHolder>   implements Filterable {

    private List<FacturaDetalle> mValues;
    private final OnListFragmentInteractionListener mListener;
    private List<FacturaDetalle> mValuesFiltrados;
    public FacturaDetalleRecyclerViewAdapter(List<FacturaDetalle> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mValuesFiltrados = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_factura_detalle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        NumberFormat formatoNumero = NumberFormat.getNumberInstance();
        holder.mItem = mValues.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);
        holder.mIdPedido.setText("Pedido: " + mValues.get(position).getIdFactura());
        holder.mFechaPedido.setText(sdf.format(mValues.get(position).getFecha()));
        holder.mProducto.setText(mValues.get(position).getDescripcionProducto());
        holder.mLote.setText(mValues.get(position).getLote());
        holder.mCantidad.setText(formatoNumero.format(mValues.get(position).getCantidad()).toString());
        holder.mBonificacion.setText(formatoNumero.format(mValues.get(position).getBonificacion()).toString());
        holder.mDescuento.setText(mValues.get(position).getDescuento().toString() + "%");
        holder.mPrecio.setText("$ " +  mValues.get(position).getPrecio().toString());
        BigDecimal total = mValues.get(position).getCantidad().multiply(mValues.get(position).getPrecio());
        BigDecimal descuento = total.multiply( mValues.get(position).getDescuento().divide(BigDecimal.valueOf(100)));

        BigDecimal totalMenosDescuento = total.subtract(descuento);

        Number vdescuento = totalMenosDescuento.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        //a = a.setScale(2, BigDecimal.ROUND_HALF_EVEN);


//        holder.mTotal.setText("$ " + totalMenosDescuento.setScale(2).toString());
        holder.mTotal.setText("$ " + vdescuento.toString());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public  Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString();

                List<FacturaDetalle> filtered = new ArrayList<>();
                if (query.isEmpty()) {
                    filtered = mValuesFiltrados;
                } else {
                    for (FacturaDetalle Busqueda : mValuesFiltrados) {
                        if (Busqueda.getDescripcionProducto().toLowerCase().contains(query.toLowerCase()))
                        {
                            filtered.add(Busqueda);
                        }

                    }
                }

                FilterResults results = new FilterResults();
                results.count = filtered.size();
                results.values = filtered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                mValues = (List<FacturaDetalle>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final LinearLayout mPedido;
        public final TextView mIdPedido;
        public final TextView mLote;
        public final TextView mFechaPedido;
        public final TextView mProducto;
        public final TextView mCantidad;
        public final TextView mBonificacion;
        public final TextView mPrecio;
        public final TextView mDescuento;
        public final TextView mTotal;
        public FacturaDetalle mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPedido = (LinearLayout) view.findViewById(R.id.ll_pedido_row_factura_detalle);
            mIdPedido = (TextView) view.findViewById(R.id.tv_pedido_row_factura_detalle);
            mFechaPedido = (TextView) view.findViewById(R.id.tv_fecha_row_factura_detalle);
            mCantidad = (TextView) view.findViewById(R.id.tv_cantidad_row_factura_detalle);
            mProducto = (TextView) view.findViewById(R.id.tv_producto_row_factura_detalle);
            mBonificacion = (TextView) view.findViewById(R.id.tv_bonificacion_row_factura_detalle);
            mPrecio = (TextView) view.findViewById(R.id.tv_precio_row_factura_detalle);
            mDescuento = (TextView) view.findViewById(R.id.tv_descuento_row_factura_detalle);
            mTotal = (TextView) view.findViewById(R.id.tv_total_row_factura_detalle);
            mLote =  (TextView) view.findViewById(R.id.tv_producto_row_factura_detalle_lote);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mProducto.getText() + "'";
        }
    }


}

