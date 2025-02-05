package com.rocnarf.rocnarf.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.rocnarf.rocnarf.R;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.models.HistorialComentarios;
import com.rocnarf.rocnarf.models.QuejasConsulta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class QuejaAdapter extends  RecyclerView.Adapter<QuejaAdapter.ViewHolder> {
    private List<QuejasConsulta> mValues;
    public   List<QuejasConsulta> mValuesFiltrados;
    private  String idUsuario;
    SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);
    QuejaAdapter.QuejaListener listener;
    Context context;
    public QuejaAdapter(Context context, List<QuejasConsulta> values, QuejaListener listener){
        this.mValues = values;
        this.context = context;
        this.mValuesFiltrados = values;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_queja, viewGroup, false), listener);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.mItem = mValues.get(i);
        holder.mAsesor.setText(mValues.get(i).getNombreCliente());
        holder.mFecha.setText(sdf.format(mValues.get(i).getFecha()));
        holder.mMotivo.setText(mValues.get(i).getMotivo());


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

            List<QuejasConsulta> filtered = new ArrayList<>();
            if (charSequence.toString().isEmpty()) {
                filtered.addAll(mValuesFiltrados);
            } else {
                for (QuejasConsulta Busqueda : mValuesFiltrados) {
                    if (Busqueda.getMotivo().toLowerCase().contains(charSequence.toString().toLowerCase())) {
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
            mValues = (List<QuejasConsulta>) results.values;
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mAsesor,mFecha,mMotivo;
        public String mCodigoCliente,idCliente,mOrigenCliente;
        public QuejasConsulta mItem;

        public ViewHolder(View view, QuejaAdapter.QuejaListener quejaListener) {
            super(view);
            mView = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.QuejaListener(mItem);
                }
            });
            mAsesor = (TextView) view.findViewById(R.id.tv_Asesor_queja_nombre);
            mFecha = (TextView) view.findViewById(R.id.tv_fecha_queja_valor);
            mMotivo = (TextView) view.findViewById(R.id.tv_motivo_queja_valor);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mAsesor.getText() + "'";
        }
    }

    public interface  QuejaListener {
        void  QuejaListener(QuejasConsulta quejas);

    }
}
