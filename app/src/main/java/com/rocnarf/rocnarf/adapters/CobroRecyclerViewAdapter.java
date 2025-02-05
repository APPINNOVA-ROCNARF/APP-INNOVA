package com.rocnarf.rocnarf.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rocnarf.rocnarf.CobroFragment.OnListFragmentInteractionListener;
import com.rocnarf.rocnarf.R;
import com.rocnarf.rocnarf.Utils.Common;

import com.rocnarf.rocnarf.models.Cobro;

import java.text.SimpleDateFormat;
import java.util.List;


public class CobroRecyclerViewAdapter extends RecyclerView.Adapter<CobroRecyclerViewAdapter.ViewHolder> {

    private final List<Cobro> mValues;
    private final OnListFragmentInteractionListener mListener;

    public CobroRecyclerViewAdapter(List<Cobro> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_cobro_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdFactura.setText("Pedido:" + mValues.get(position).getIdFactura());
        SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);
        holder.mFecha.setText(sdf.format(mValues.get(position).getFecha()));
        holder.mCobrador.setText(mValues.get(position).getCobrador());
        holder.mRecibo.setText(mValues.get(position).getRecibo().isEmpty() ? "" : "Recibo: " + mValues.get(position).getRecibo());
        holder.mMonto.setText(mValues.get(position).getValor().toString());
        if (mValues.get(position).getBanco().isEmpty()  || mValues.get(position).getBanco().trim().equals("*") ){
            holder.mBanco.setVisibility(View.GONE);
            holder.mBanco.setText("");
        }else {
            holder.mBanco.setVisibility(View.VISIBLE);
            holder.mBanco.setText("Bco: " + mValues.get(position).getBanco() + " - No Cta: " + mValues.get(position).getCuenta() + " - Cheque: " + mValues.get(position).getNumeroCheque()  );
        }


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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdFactura;
        public final TextView mFecha;
        public final TextView mCobrador;
        public final TextView mRecibo;
        public final TextView mMonto;
        public final TextView mBanco;
        public Cobro mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdFactura = (TextView) view.findViewById(R.id.tv_pedido_row_cobro);
            mFecha = (TextView) view.findViewById(R.id.tv_fecha_row_cobro);
            mCobrador = (TextView) view.findViewById(R.id.tv_cobrador_row_cobro);
            mRecibo = (TextView) view.findViewById(R.id.tv_recibo_row_cobro);
            mMonto = (TextView) view.findViewById(R.id.tv_monto_row_cobro);
            mBanco = (TextView) view.findViewById(R.id.tv_banco_row_cobro);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mIdFactura.getText() + "'";
        }
    }
}
