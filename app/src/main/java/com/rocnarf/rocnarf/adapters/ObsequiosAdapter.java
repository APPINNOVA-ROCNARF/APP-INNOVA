package com.rocnarf.rocnarf.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocnarf.rocnarf.R;
import com.rocnarf.rocnarf.models.LiquidacionObsequio;

import java.util.List;


public class ObsequiosAdapter extends  RecyclerView.Adapter<ObsequiosAdapter.ViewHolder> {
    private final List<LiquidacionObsequio> mValues;
    ObsequiosAdapter.ObsequiosListener listener;
    Context context;
    public ObsequiosAdapter(List<LiquidacionObsequio> values, ObsequiosListener listener){

        this.mValues = values;
        Log.d("myTag", "this.mValues error ----->" + this.mValues  );


        this.listener = listener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_panel_obsequio, viewGroup, false), listener);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.mItem = mValues.get(i);

        holder.mNombreCliente.setText(mValues.get(i).getNombreCliente());
        holder.mNombrePlan.setText(mValues.get(i).getNombrePlan());
        holder.mObsequio.setText(mValues.get(i).getObsequio());
//        Log.d("myTag", "this.mValues error ----->" + mValues.get(i).getEstadoSolicitud()  );
        if (mValues.get(i).getEstadoSolicitud().equals("A")){

            holder.mOk.setVisibility(View.VISIBLE);
        }

        if (mValues.get(i).getEstadoSolicitud().equals("N")){
            holder.mNegado.setVisibility(View.VISIBLE);
        }
        if (mValues.get(i).getEstadoSolicitud().equals("P")){
            holder.mProceso.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNombreCliente,mObsequio,mNombrePlan;
        public final ImageView mOk,mNegado,mProceso;

        public LiquidacionObsequio mItem;

        public ViewHolder(View view, ObsequiosListener obsequiosListener) {
            super(view);
            mView = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.ObsequiosListener(mItem);
                }
            });
            mNombreCliente = (TextView) view.findViewById(R.id.tv_nombre_cliente);
            mNombrePlan = (TextView) view.findViewById(R.id.tv_nombre_plan);
            mObsequio = (TextView) view.findViewById(R.id.tv_nombre_obsequio);
            mOk = (ImageView) view.findViewById(R.id.iv_obsequi_ok);
            mNegado = (ImageView) view.findViewById(R.id.iv_obsequi_negado);
            mProceso = (ImageView) view.findViewById(R.id.iv_obsequi_proceso);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNombreCliente.getText() + "'";
        }
    }

    public interface ObsequiosListener {
        void ObsequiosListener(LiquidacionObsequio liquidacionObsequio);

    }
}
