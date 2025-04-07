package com.rocnarf.rocnarf.adapters;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rocnarf.rocnarf.DetalleNotaCreditoActivity;
import com.rocnarf.rocnarf.R;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.models.DetalleNotaCredito;
import com.rocnarf.rocnarf.models.NotaCredito;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotaCreditoRecyclerViewAdapter extends RecyclerView.Adapter<NotaCreditoRecyclerViewAdapter.ViewHolder> {

    private final List<NotaCredito> mValues;
    public  List<DetalleNotaCredito> detalleNotaCredito;
    private final NotaCreditoRecyclerViewAdapter.OnListFragmentInteractionListener mListener;
    private Context context;

    public interface OnListFragmentInteractionListener{
        void onListFragmentInteraction(NotaCredito notaCredito);
    }

    public NotaCreditoRecyclerViewAdapter(List<NotaCredito> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
//        this.context = context;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_nc_fragment, parent, false);
        return new ViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mConcepto.setText("" + mValues.get(position).getConcepto());
        SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);
        holder.mFecha.setText(sdf.format(mValues.get(position).getFecha()));
        holder.mNumeroNc.setText("Número N/C: " + mValues.get(position).getNumero());
        holder.mRecibo.setText(" ");
        Number total = mValues.get(position).getValor().setScale(2, BigDecimal.ROUND_HALF_EVEN);
        holder.mMonto.setText("$ "+total.toString());

            holder.mBanco.setVisibility(View.GONE);
            holder.mBanco.setText(mValues.get(position).getNumNota());

//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.onListFragmentInteraction(holder.mItem);
//            }
//        });


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String numeroApi =holder.mNumeroNc.getText().toString();
                numeroApi = numeroApi.replace("Numero: ","");
                PlanesService service = ApiClient.getClient().create(PlanesService.class);
                retrofit2.Call<List<DetalleNotaCredito>> call  = service.getNcDetalle(numeroApi);
                call.enqueue(new Callback<List<DetalleNotaCredito>>() {
                    @Override
                    public void onResponse(Call<List<DetalleNotaCredito>> call, Response<List<DetalleNotaCredito>> response) {
                        if (response.isSuccessful()){
                            detalleNotaCredito = response.body();
                            Intent i = new Intent(context, DetalleNotaCreditoActivity.class);
                            i.putExtra("detalleNotaCredito", (Serializable) detalleNotaCredito);
//                            i.putExtra("planes", (Serializable) planes);
                            context.startActivity(i);

                        }
                    }

                    @Override
                    public void onFailure(Call<List<DetalleNotaCredito>> call, Throwable t) {
                        Log.d("sincronizar Clientes","xxxxx");
//                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

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
        public final TextView mConcepto;
        public final TextView mFecha;
        public final TextView mNumeroNc;
        public final TextView mRecibo;
        public final TextView mMonto;
        public final TextView mBanco;
        public NotaCredito mItem;

        public ViewHolder(View view, OnListFragmentInteractionListener NcListener) {
            super(view);
            mView = view;
            mConcepto = (TextView) view.findViewById(R.id.tv_concepto);
            mFecha = (TextView) view.findViewById(R.id.tv_fecha_row_cobro);
            mNumeroNc = (TextView) view.findViewById(R.id.tv_numeronc);
            mRecibo = (TextView) view.findViewById(R.id.tv_recibo_row_cobro);
            mMonto = (TextView) view.findViewById(R.id.tv_monto_row_nc);
            mBanco = (TextView) view.findViewById(R.id.tv_banco_nc);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onListFragmentInteraction(mItem);
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '"  + "'";
        }

    }

    public interface NcListener {
        void NcListener(NotaCredito notaCredito);

    }
}
