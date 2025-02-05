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
import com.rocnarf.rocnarf.models.ReporteVentas;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class ReporteVentasAdapter extends  RecyclerView.Adapter<ReporteVentasAdapter.ViewHolder> {
    public   List<ReporteVentas> mValues;
    public   List<ReporteVentas> mValuesFiltrados;
    SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);
    ReporteVentasAdapter.ReporteVentasListener listener;
    Context context;
    public ReporteVentasAdapter(List<ReporteVentas> values, ReporteVentasListener listener){

        this.mValues = values;

        this.mValuesFiltrados = values;
        this.listener = listener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_reporte_ventas, viewGroup, false), listener);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.mItem = mValues.get(i);

        holder.mNombre.setText(mValues.get(i).getNombre());
        holder.mDescripcion.setText(mValues.get(i).getCilco());
        holder.mFuerza.setText(sdf.format(mValues.get(i).getFecha()));
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

            List<ReporteVentas> filtered = new ArrayList<>();
            Log.d("sincronizar Clientes", "sss"+ charSequence);
            if (charSequence.toString().isEmpty()) {
                filtered.addAll(mValuesFiltrados);
            } else {
                for (ReporteVentas Busqueda : mValuesFiltrados) {
                    if (Busqueda.getNombre().toLowerCase().contains(charSequence.toString().toLowerCase())
                            || Busqueda.getCilco().toLowerCase().contains(charSequence.toString().toLowerCase())
                            ) {
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
            mValues = (List<ReporteVentas>) results.values;
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNombre;
        public final TextView mDescripcion;
        public final TextView mFuerza;

        public ReporteVentas mItem;

        public ViewHolder(View view, ReporteVentasAdapter.ReporteVentasListener reporteVentasListener) {
            super(view);
            mView = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.ReporteVentasListener(mItem);
                }
            });
            mNombre = (TextView) view.findViewById(R.id.tv_nombre_proc);
            mDescripcion = (TextView) view.findViewById(R.id.tv_codigo_prod);
            mFuerza = (TextView) view.findViewById(R.id.tv_fuerza_prod);
//            mNombre.setText(mItem.getNombre());
//            mDescripcion.setText(mItem.getDescripcion());
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNombre.getText() + "'";
        }
    }

    public interface  ReporteVentasListener {
        void  ReporteVentasListener(ReporteVentas reporteVentas);

    }
}
