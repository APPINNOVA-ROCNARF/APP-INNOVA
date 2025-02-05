package com.rocnarf.rocnarf.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.rocnarf.rocnarf.R;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.models.HistorialViatico;
import com.rocnarf.rocnarf.models.Viatico;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HistorialViaticoAdapter extends  RecyclerView.Adapter<HistorialViaticoAdapter.ViewHolder> {
    public   List<HistorialViatico> mValues;
    public   List<HistorialViatico> mValuesFiltrados;
    SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);
    HistorialViaticoAdapter.HistorialViaticoListener listener;
    Context context;
    public HistorialViaticoAdapter(Context context, List<HistorialViatico> values, HistorialViaticoListener listener){
        this.context = context;
        this.mValues = values;

        this.mValuesFiltrados = values;
        this.listener = listener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_viatico_historial, viewGroup, false), listener);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.mItem = mValues.get(i);

        holder.mNombre.setText(mValues.get(i).getDescripcion());
        holder.mFecha.setText(sdf.format(mValues.get(i).getFechaPeriodo()));
        holder.mEstado.setText("Revision");
        holder.mEstado.setBackgroundResource(R.drawable.rounded_red_background);


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

            List<HistorialViatico> filtered = new ArrayList<>();
            if (charSequence.toString().isEmpty()) {
                filtered.addAll(mValuesFiltrados);
            } else {
                for (HistorialViatico Busqueda : mValuesFiltrados) {
                    if (Busqueda.getDescripcion().toLowerCase().contains(charSequence.toString().toLowerCase())) {
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
            mValues = (List<HistorialViatico>) results.values;
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNombre,mFecha,mEstado;
        public HistorialViatico mItem;


        public ViewHolder(View view, HistorialViaticoAdapter.HistorialViaticoListener guiaProductosListener) {
            super(view);
            mView = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.HistorialViaticoListener(mItem);
                }
            });
            mNombre = (TextView) view.findViewById(R.id.tv_ciclo_his_via);
            mFecha = (TextView) view.findViewById(R.id.tv_fecha_his_via);
            mEstado = (TextView) view.findViewById(R.id.tv_estado_viatico);



        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNombre.getText() + "'";
        }
    }

    public interface  HistorialViaticoListener {
        void  HistorialViaticoListener(HistorialViatico historialViatico);

    }
}
