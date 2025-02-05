package com.rocnarf.rocnarf.adapters;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rocnarf.rocnarf.R;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.models.HistorialComentarios;
import com.rocnarf.rocnarf.models.Recetas;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class RecetasXAdapter extends  RecyclerView.Adapter<RecetasXAdapter.ViewHolder> {
    private  List<Recetas> mValues;
    SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);
    RecetasXAdapter.RecetasXListener listener;
    public   List<Recetas> mValuesFiltrados;

    Context context;
    public RecetasXAdapter(List<Recetas> values, RecetasXListener listener){

        this.mValues = values;

        this.mValuesFiltrados = values;
        this.listener = listener;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_recetas_x, viewGroup, false), listener);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.mItem = mValues.get(i);

        //String text = "<font color=#000000>"+ mValues.get(i).getProducto() + "\n" +"</font> <font color=#808080>"+mValues.get(i).getLaboratorio()+"</font>";
        //holder.marca.setText(Html.fromHtml(text));

        holder.marca.setText( mValues.get(i).getProducto());
        holder.sem1.setText( String.valueOf(mValues.get(i).getSem1()));
        holder.sem2.setText( String.valueOf(mValues.get(i).getSem2()));
        holder.ytd1.setText( String.valueOf(mValues.get(i).getYtd1()));
        holder.ytd2.setText( String.valueOf(mValues.get(i).getYtd2()));
        holder.marcaSub.setText( String.valueOf(mValues.get(i).getLaboratorio()));


    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView marca, sem1,sem2,ytd1,ytd2,marcaSub;

        public Recetas mItem;

        public ViewHolder(View view, RecetasXListener recetasXListener) {
            super(view);
            mView = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.RecetasXListener(mItem);
                }
            });
            marca = (TextView) view.findViewById(R.id.recetas_marca);
            sem1 = (TextView) view.findViewById(R.id.recetas_sem1);
            sem2 = (TextView) view.findViewById(R.id.recetas_sem2);
            ytd1 = (TextView) view.findViewById(R.id.recetas_ytd1);
            ytd2 = (TextView) view.findViewById(R.id.recetas_ytd2);
            marcaSub = (TextView) view.findViewById(R.id.recetas_marca_sub);

        }

    }

    public interface RecetasXListener {
        void RecetasXListener(Recetas recetas);

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

            List<Recetas> filtered = new ArrayList<>();
            if (charSequence.toString().isEmpty()) {
                filtered.addAll(mValuesFiltrados);
            } else {
                for (Recetas Busqueda : mValuesFiltrados) {
                    if (Busqueda.getLaboratorio().toLowerCase().contains(charSequence.toString().toLowerCase())
                            || Busqueda.getCodigo().toLowerCase().contains(charSequence.toString().toLowerCase())
                            || Busqueda.getProducto().toLowerCase().contains(charSequence.toString().toLowerCase())) {
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
            mValues = (List<Recetas>) results.values;
            notifyDataSetChanged();
        }
    };

}
