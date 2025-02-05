package com.rocnarf.rocnarf.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocnarf.rocnarf.R;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.models.GuiaProductos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class GuiaProductosAdapter extends  RecyclerView.Adapter<GuiaProductosAdapter.ViewHolder> {
    public   List<GuiaProductos> mValues;
    public   List<GuiaProductos> mValuesFiltrados;
    SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);
    GuiaProductosAdapter.GuiaProductosListener listener;
    Context context;
    public GuiaProductosAdapter(Context context,List<GuiaProductos> values,  GuiaProductosListener listener){
        this.context = context;
        this.mValues = values;

        this.mValuesFiltrados = values;
        this.listener = listener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_guia_productos, viewGroup, false), listener);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.mItem = mValues.get(i);

        holder.mNombre.setText(mValues.get(i).getMarca());
        holder.mDescripcion.setText(mValues.get(i).getNombre());
        holder.mFuerza.setText(mValues.get(i).getFuerza());

        if (mValues.get(i).getUrlVideo() != null) {
            String link = mValues.get(i).getUrlVideo();
            holder.mVideo.setVisibility(View.VISIBLE);

            holder.mVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));

                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        // Si está instalada, iniciar la actividad de YouTube
                        context.startActivity(intent);
                    } else {
                        // Si no está instalada, puedes manejar esto según tus necesidades
                        // Por ejemplo, abrir el enlace en un navegador web o mostrar un mensaje al usuario
                        // ...

                        // En este ejemplo, abrir el enlace en un navegador web
                        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                        context.startActivity(webIntent);
                    }
                }

            });

        } else {
            holder.mVideo.setVisibility(View.GONE);
        }

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

            List<GuiaProductos> filtered = new ArrayList<>();
            Log.d("sincronizar Clientes", "sss"+ charSequence);
            if (charSequence.toString().isEmpty()) {
                filtered.addAll(mValuesFiltrados);
            } else {
                for (GuiaProductos Busqueda : mValuesFiltrados) {
                    if (Busqueda.getNombre().toLowerCase().contains(charSequence.toString().toLowerCase())
                            || Busqueda.getFuerza().toLowerCase().contains(charSequence.toString().toLowerCase())
                            || Busqueda.getMarca().toLowerCase().contains(charSequence.toString().toLowerCase())) {
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
            mValues = (List<GuiaProductos>) results.values;
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNombre;
        public final TextView mDescripcion;
        public final TextView mFuerza;
        public final ImageView mVideo;
        public GuiaProductos mItem;

        public ViewHolder(View view, GuiaProductosAdapter.GuiaProductosListener guiaProductosListener) {
            super(view);
            mView = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.GuiaProductosListener(mItem);
                }
            });
            mNombre = (TextView) view.findViewById(R.id.tv_nombre_proc);
            mDescripcion = (TextView) view.findViewById(R.id.tv_codigo_prod);
            mFuerza = (TextView) view.findViewById(R.id.tv_fuerza_prod);
            mVideo = (ImageView)  view.findViewById(R.id.im_video_guia);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNombre.getText() + "'";
        }
    }

    public interface  GuiaProductosListener {
        void  GuiaProductosListener(GuiaProductos guiaProductos);

    }
}
