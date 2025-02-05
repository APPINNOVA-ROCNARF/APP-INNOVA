package com.rocnarf.rocnarf.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rocnarf.rocnarf.R;
import com.rocnarf.rocnarf.models.EscalaBonificacion;

import java.util.List;

public class EscalaBonificacionRecyclerViewAdapter extends RecyclerView.Adapter<EscalaBonificacionRecyclerViewAdapter.ViewHolder>  {

    private final List<EscalaBonificacion> mValues;



    public EscalaBonificacionRecyclerViewAdapter(List<EscalaBonificacion> items) {
        mValues = items;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_escala_bonificacion, viewGroup, false);
        return new EscalaBonificacionRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.mItem = mValues.get(i);
        // if not first item, check if item above has the same header
//        Log.d("tag", "--->"+ mValues.get(i).getNombre());
        if (i > 0 && mValues.get(i - 1).getIdEscala() == mValues.get(i).getIdEscala()) {
            holder.mIdEscala.setVisibility(View.GONE);
        } else {
            holder.mIdEscala.setVisibility(View.VISIBLE);
        }

        holder.mIdEscala.setText("Tabla " + String.valueOf(mValues.get(i).getIdEscala()));
        holder.mNombre.setText(String.valueOf(mValues.get(i).getNombre()));
        holder.mCantidad.setText(String.valueOf(mValues.get(i).getCantidad()));
        holder.mBono.setText(String.valueOf(mValues.get(i).getBonificacion()));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdEscala;
        public final TextView mCantidad;
        public final TextView mBono;
        public final TextView mNombre;

        public EscalaBonificacion mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdEscala= (TextView) view.findViewById(R.id.tv_escala_row_escala_bonificacion);
            mCantidad = (TextView) view.findViewById(R.id.tv_cantidad_row_escala_bonificacion);
            mBono = (TextView) view.findViewById(R.id.tv_bono_row_escala_bonificacion);
            mNombre = (TextView) view.findViewById(R.id.tv_nombre_row_escala_bonificacion);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }


}
