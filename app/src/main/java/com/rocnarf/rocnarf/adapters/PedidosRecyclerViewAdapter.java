package com.rocnarf.rocnarf.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rocnarf.rocnarf.R;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.models.Pedido;

import java.text.SimpleDateFormat;
import java.util.List;


public class PedidosRecyclerViewAdapter extends  RecyclerView.Adapter<PedidosRecyclerViewAdapter.ViewHolder> {
    private final List<Pedido> mValues;
    SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);
    PedidoListaListener listener;

    public PedidosRecyclerViewAdapter(List<Pedido> values, PedidoListaListener listener){
        this.mValues = values;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_pedido_lista, viewGroup, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.mItem = mValues.get(i);
        holder.mCliente.setText(mValues.get(i).getIdCliente()  + " - " + mValues.get(i).getNombreCliente() );
        holder.mPrecioTotal.setText("$ " + String.format("%.2f", mValues.get(i).getPrecioTotal()));
        holder.mDescuento.setText("$ " + String.format("%.2f", mValues.get(i).getDescuento()));
        holder.mPrecioFinal.setText("$ " + String.format("%.2f", mValues.get(i).getPrecioFinal()));
        holder.mFecha.setText(sdf.format(mValues.get(i).getFechaPedido()));
        holder.mObservaciones.setText(mValues.get(i).getObservaciones());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mCliente;
        public final TextView mPrecioTotal;
        public final TextView mDescuento;
        public final TextView mPrecioFinal;
        public final TextView mFecha;
        public final TextView mObservaciones;



        public Pedido mItem;

        public ViewHolder(View view, PedidoListaListener pedidoListaListener) {
            super(view);
            mView = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.PedidoListaListener(mItem);
                }
            });
            mCliente = (TextView) view.findViewById(R.id.tv_cliente_row_pedido_lista);
            mPrecioTotal = (TextView) view.findViewById(R.id.tv_total_row_pedido_lista);
            mDescuento = (TextView) view.findViewById(R.id.tv_descuento_row_pedido_lista);
            mPrecioFinal = (TextView) view.findViewById(R.id.tv_final_row_pedido_lista);

            mFecha = (TextView) view.findViewById(R.id.tv_fecha_row_pedido_lista);
            mObservaciones = (TextView) view.findViewById(R.id.tv_observaciones_row_pedido_lista);


        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCliente.getText() + "'";
        }
    }

    public interface PedidoListaListener {
        void PedidoListaListener(Pedido pedido);

    }
}
