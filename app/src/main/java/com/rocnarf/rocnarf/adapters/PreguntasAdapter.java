package com.rocnarf.rocnarf.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocnarf.rocnarf.R;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.models.PreguntasFrecuentes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class PreguntasAdapter extends  RecyclerView.Adapter<PreguntasAdapter.ViewHolder> {
    public List<PreguntasFrecuentes> mValues;
    public   List<PreguntasFrecuentes> mValuesFiltrados;
    SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);
    PreguntasAdapter.PreguntasListener listener;
    Context context;
    public PreguntasAdapter(List<PreguntasFrecuentes> values, PreguntasListener listener){

        this.mValues = values;
    this.mValuesFiltrados = values;
        this.listener = listener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_preguntas, viewGroup, false), listener);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.mItem = mValues.get(i);

        holder.mNombre.setText(mValues.get(i).getPregunta());
        holder.mDescripcion.setText(mValues.get(i).getRespuesta());

        boolean isExpanded = mValues.get(i).getExpadir();
        holder.mExpandir.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.mFlecha.setRotation(isExpanded ? 180f : 0f);
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

            List<PreguntasFrecuentes> filtered = new ArrayList<>();
            if (charSequence.toString().isEmpty()) {
                filtered.addAll(mValuesFiltrados);
            } else {
                for (PreguntasFrecuentes Busqueda : mValuesFiltrados) {
                    if (Busqueda.getPregunta().toLowerCase().contains(charSequence.toString().toLowerCase())
                            || Busqueda.getRespuesta().toLowerCase().contains(charSequence.toString().toLowerCase())) {


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
            mValues = (List<PreguntasFrecuentes>) results.values;
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNombre;
        public final TextView mDescripcion;
        public final ConstraintLayout mExpandir;
        public final ImageView mFlecha;
        public PreguntasFrecuentes mItem;

        public ViewHolder(View view, final PreguntasAdapter.PreguntasListener preguntasListener) {
            super(view);
            mView = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.PreguntasListener(mItem);
                }
            });
            mFlecha = (ImageView) view.findViewById(R.id.iv_down_pregunta);
            mNombre = (TextView) view.findViewById(R.id.tv_preguntas);
            mDescripcion = (TextView) view.findViewById(R.id.tv_respuesta);
            mExpandir = (ConstraintLayout) view.findViewById(R.id.expandir);
//            mNombre.setText(mItem.getNombre());
//            mDescripcion.setText(mItem.getDescripcion());
            mNombre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItem.setExpandir(!mItem.getExpadir());
                    notifyItemChanged(getAdapterPosition());
                }
            });
            mFlecha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItem.setExpandir(!mItem.getExpadir());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNombre.getText() + "'";
        }
    }

    public interface  PreguntasListener {
        void PreguntasListener(PreguntasFrecuentes preguntasFrecuentes);

    }
}
