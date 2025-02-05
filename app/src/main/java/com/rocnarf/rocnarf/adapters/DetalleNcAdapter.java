package com.rocnarf.rocnarf.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rocnarf.rocnarf.R;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.models.DetalleNotaCredito;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;


public class DetalleNcAdapter extends  RecyclerView.Adapter<DetalleNcAdapter.ViewHolder> {
    private final List<DetalleNotaCredito> mValues;
    SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);
    DetalleNcAdapter.PoliticasListener listener;
    Context context;
    public DetalleNcAdapter(Context context, List<DetalleNotaCredito> values){

        this.mValues = values;


        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item_nc, viewGroup, false), listener);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.mItem = mValues.get(i);
        if(mValues != null && mValues.size()> 0){

        holder.mProducto.setText(mValues.get(i).getProducto());
        int totalCantidad= mValues.get(i).getCantidad().intValue();
        holder.mCantidad.setText(String.valueOf(totalCantidad));
        int totalBoni = mValues.get(i).getBonificacion().intValue();
        holder.mBonificacion.setText(String.valueOf(totalBoni));
        Number totalPre = mValues.get(i).getPrecio().setScale(2, BigDecimal.ROUND_HALF_EVEN);
        holder.mPrecio.setText("$" + totalPre.toString());
        Number total = (totalCantidad * totalPre.floatValue());
        NumberFormat formatter = new DecimalFormat("###,###,##0.00");
        String formattedNumberValor = formatter.format(total);
        holder.mTotal.setText("$" + formattedNumberValor);
        }
        else   {
            return;
        }

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mProducto;
        public final TextView mCantidad;
        public final TextView mBonificacion;
        public final TextView mPrecio;
        public final TextView mTotal;
        public DetalleNotaCredito mItem;

        public ViewHolder(View view, final DetalleNcAdapter.PoliticasListener politicasListener) {
            super(view);
            mView = view;
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    listener.PoliticasListener(mItem);
//                }
//            });
            mProducto = (TextView) view.findViewById(R.id.tv_producto_row_nc_detalle);
            mCantidad = (TextView) view.findViewById(R.id.tv_cantidad_row_nc_detalle);
            mBonificacion = (TextView) view.findViewById(R.id.tv_bonificacion_row_nc_detalle);
            mPrecio = (TextView) view.findViewById(R.id.tv_precio_row_nc_detalle);
            mTotal = (TextView) view.findViewById(R.id.tv_total_row_nc_detalle);
//            mNombre.setText(mItem.getNombre());
//            mDescripcion.setText(mItem.getDescripcion());
//            mNombre.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mItem.setExpandir(!mItem.getExpadir());
//                    notifyItemChanged(getAdapterPosition());
//                }
//            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mProducto.getText() + "'";
        }
    }

    public interface  PoliticasListener {
        void PoliticasListener(DetalleNotaCredito detalleNotaCredito);

    }
}
