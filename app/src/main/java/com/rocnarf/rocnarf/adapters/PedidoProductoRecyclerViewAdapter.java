package com.rocnarf.rocnarf.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rocnarf.rocnarf.R;
import com.rocnarf.rocnarf.models.PedidoDetalle;

import java.text.NumberFormat;
import java.util.List;


public class PedidoProductoRecyclerViewAdapter extends RecyclerView.Adapter<PedidoProductoRecyclerViewAdapter.ViewHolder> {

    private final List<PedidoDetalle> mValues;
    private final PedidoProductoEliminarListener mListener;
    private final PedidoProductoEditarListener mListenerEdit;
    public String tipo ="PVP";
    public interface PedidoProductoEliminarListener{
        void PedidoProductoEliminar(PedidoDetalle pedidoDetalle);
    }

    public interface PedidoProductoEditarListener{
        void PedidoProductoEditar(PedidoDetalle pedidoDetalle);
    }

    public PedidoProductoRecyclerViewAdapter(List<PedidoDetalle> items, PedidoProductoEliminarListener listener,  PedidoProductoEditarListener listenerEdit) {
        mValues = items;
        mListener = listener;
        mListenerEdit = listenerEdit;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_pedido_producto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        NumberFormat formatoNumero = NumberFormat.getNumberInstance();
        holder.mItem = mValues.get(position);
        holder.mProductoView.setText(mValues.get(position).getNombre());
        holder.mCantidad.setText(String.valueOf(formatoNumero.format(mValues.get(position).getCantidad())));
        holder.mBono.setText(String.valueOf(mValues.get(position).getBono()));
        if (tipo == null) {
            Log.d("DEBUG", "El valor de tipo es null");
        } else {
            Log.d("DEBUG", "tipo: " + tipo);
        }
        if(tipo.equalsIgnoreCase("P.V.F")){

            mValues.get(position).setPrecio( mValues.get(position).getPvf());
            mValues.get(position).setPrecioTotal(mValues.get(position).getCantidad() *  mValues.get(position).getPrecio());
            holder.mPrecio.setText("$ " + String.format("%.2f",mValues.get(position).getPrecio()));

        }else if(tipo.equalsIgnoreCase("ESP")){
            mValues.get(position).setPrecio( mValues.get(position).getEsp());
            mValues.get(position).setPrecioTotal(mValues.get(position).getCantidad() *  mValues.get(position).getPrecio());
            holder.mPrecio.setText("$ " + String.format("%.2f",mValues.get(position).getPrecio()));

        }else if (tipo.equalsIgnoreCase("P.V.P")){
            mValues.get(position).setPrecio( mValues.get(position).getPvp());
            mValues.get(position).setPrecioTotal(mValues.get(position).getCantidad() *  mValues.get(position).getPrecio());
            holder.mPrecio.setText("$ " + String.format("%.2f",mValues.get(position).getPrecio()));
        }

        holder.mTotal.setText(String.format("%.2f", mValues.get(position).getPrecioTotal()));

        holder.txtPVFTitle.setText(tipo);

        holder.mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.PedidoProductoEliminar(holder.mItem);
                }
            }
        });

        holder.mImageButtonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListenerEdit.PedidoProductoEditar(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mProductoView;
        public final ImageButton mImageButton;
        public final ImageButton mImageButtonEdit;
        public final TextView mCantidad;
        public final TextView mBono;
        public final TextView mPrecio;
        public final TextView mTotal;
        public TextView mViewPrecio;
        public TextView txtPVFTitle;
        public PedidoDetalle mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mProductoView = (TextView) view.findViewById(R.id.tv_producto_row_pedido_producto);
            mImageButton = (ImageButton) view.findViewById(R.id.ib_eliminar_row_pedido_producto);
            mCantidad = (TextView) view.findViewById(R.id.tv_cantidad_row_pedido_producto);
            mBono = (TextView) view.findViewById(R.id.tv_bonificacion_row_pedido_producto);
            mPrecio = (TextView) view.findViewById(R.id.tv_precio_row_pedido_producto);
            mTotal = (TextView) view.findViewById(R.id.tv_total_row_pedido_producto);
            mViewPrecio = (TextView) view.findViewById(R.id.txtPVFTitle);
            txtPVFTitle = (TextView) view.findViewById(R.id.txtTitle);
            mImageButtonEdit= (ImageButton) view.findViewById(R.id.ib_editar_cantidad);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mProductoView.getText() + "'";
        }
    }



}
