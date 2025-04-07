package com.rocnarf.rocnarf.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rocnarf.rocnarf.CobroFragment.OnListFragmentInteractionListener;
import com.rocnarf.rocnarf.R;
import com.rocnarf.rocnarf.Utils.Common;

import com.rocnarf.rocnarf.models.Cobro;
import com.rocnarf.rocnarf.models.FacturaDetalle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class CobroRecyclerViewAdapter extends RecyclerView.Adapter<CobroRecyclerViewAdapter.ViewHolder> implements Filterable {

    private List<Cobro> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final List<Cobro> mValuesFiltrados;
    private String factura;
    private String textoBusqueda = "";

    public CobroRecyclerViewAdapter(List<Cobro> items, OnListFragmentInteractionListener listener, String Factura) {
        mValues = items;
        mListener = listener;
        mValuesFiltrados = items;
        factura = Factura;
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

        if (factura != null){
            holder.mMonto.setVisibility(View.GONE);
        }

        holder.mPedidosContainer.removeAllViews();


        List<Cobro> pedidos = mValues.get(position).getPedidosRelacionados();
        String numeroChequeActual = mValues.get(position).getNumeroCheque();
        if (pedidos != null && !pedidos.isEmpty()) {
            for (Cobro pedido : pedidos) {
                if (pedido.getNumeroCheque().equals(numeroChequeActual)) {
                    TextView pedidoView = new TextView(holder.mView.getContext());
                    pedidoView.setText("Pedido: " + pedido.getIdFactura() + "       $ " + pedido.getValor());
                    pedidoView.setTextSize(14);
                    pedidoView.setPadding(0, 4, 0, 4);
                    holder.mPedidosContainer.addView(pedidoView);
                }
            }
        } else {
            // Fallback por si no hay lista
            TextView pedidoView = new TextView(holder.mView.getContext());
            pedidoView.setText("Pedido: " + mValues.get(position).getIdFactura() + "       $ " + mValues.get(position).getValor());
            pedidoView.setTextSize(14);
            pedidoView.setPadding(0, 4, 0, 4);
            holder.mPedidosContainer.addView(pedidoView);
        }
        SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);
        holder.mFecha.setText(sdf.format(mValues.get(position).getFecha()));
        holder.mCobrador.setText(mValues.get(position).getCobrador());
        holder.mRecibo.setText(mValues.get(position).getRecibo().isEmpty() ? "" : "Recibo: " + mValues.get(position).getRecibo());
        holder.mMonto.setText("$ " + mValues.get(position).getValor().toString());
        if (mValues.get(position).getBanco().isEmpty()  || mValues.get(position).getBanco().trim().equals("*") ){
            holder.mBanco.setText((Html.fromHtml(
                    "<b>Método: " + mValues.get(position).getNumeroCheque() + "</b>")));
        }else {
            holder.mBanco.setText(Html.fromHtml(
                    "<b>Cheque: " + mValues.get(position).getNumeroCheque() + "</b>" +
                            " - <b>Bco: " + mValues.get(position).getBanco() + "</b>" +
                            "<br>No Cuenta: " + mValues.get(position).getCuenta()
            ));


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


    @Override
    public  Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString();

                List<Cobro> filtered = new ArrayList<>();
                if (query.isEmpty()) {
                    filtered = mValuesFiltrados;
                } else {
                    for (Cobro Busqueda : mValuesFiltrados) {
                        boolean b = (Busqueda.getIdCliente() != null && Busqueda.getIdCliente().toLowerCase().contains(query.toLowerCase())) ||
                                (Busqueda.getCobrador() != null && Busqueda.getCobrador().toLowerCase().contains(query.toLowerCase())) ||
                                (Busqueda.getRecibo() != null && Busqueda.getRecibo().toLowerCase().contains(query.toLowerCase())) ||
                                (Busqueda.getBanco() != null && Busqueda.getBanco().toLowerCase().contains(query.toLowerCase())) ||
                                (Busqueda.getCuenta() != null && Busqueda.getCuenta().toLowerCase().contains(query.toLowerCase())) ||
                                (Busqueda.getNumeroCheque() != null && Busqueda.getNumeroCheque().toLowerCase().contains(query.toLowerCase())) ||
                                (Busqueda.getValor() != null && Busqueda.getValor().toPlainString().contains(query));

                        if (b)
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
                mValues = (List<Cobro>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mFecha;
        public final TextView mCobrador;
        public final TextView mRecibo;
        public final TextView mMonto;
        public final TextView mBanco;
        public Cobro mItem;
        public final LinearLayout mPedidosContainer;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mPedidosContainer = (LinearLayout) view.findViewById(R.id.ll_pedidos_container);
            mFecha = (TextView) view.findViewById(R.id.tv_fecha_row_cobro);
            mCobrador = (TextView) view.findViewById(R.id.tv_cobrador_row_cobro);
            mRecibo = (TextView) view.findViewById(R.id.tv_recibo_row_cobro);
            mMonto = (TextView) view.findViewById(R.id.tv_monto_row_cobro);
            mBanco = (TextView) view.findViewById(R.id.tv_banco_row_cobro);
        }



    }
}
