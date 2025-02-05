package com.rocnarf.rocnarf.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rocnarf.rocnarf.R;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.models.HistorialComentarios;

import java.text.SimpleDateFormat;
import java.util.List;


public class HistorialComentariosAdapter extends  RecyclerView.Adapter<HistorialComentariosAdapter.ViewHolder> {
    private final List<HistorialComentarios> mValues;
    SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);
    HistorialComentariosAdapter.HistoralComentariosListener listener;
    Context context;
    public HistorialComentariosAdapter(List<HistorialComentarios> values, HistoralComentariosListener listener){

        this.mValues = values;


        this.listener = listener;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_historial_comentario, viewGroup, false), listener);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.mItem = mValues.get(i);
        holder.mIdAsesor.setText( mValues.get(i).getSeccion() + " - " + mValues.get(i).getUsuario());
        holder.mComentario.setText( mValues.get(i).getComentarios());
//        holder.mSeccion.setText( mValues.get(i).getSeccion());
        holder.mFechaVisita.setText( sdf.format(mValues.get(i).getFecha()));

//        PlanesService service = ApiClient.getClient().create(PlanesService.class);
//        Call<Promocionado> call  = service.GetPromocionado( mValues.get(i).getIdVisitaCliente());
//        call.enqueue(new Callback<Promocionado>() {
//            @Override
//            public void onResponse(Call<Promocionado> call, Response<Promocionado> response) {
//                if (response.isSuccessful()){
//                    Promocionado historialComentariosResponse = response.body();
////                    List<Promocionado> historialComentarios = historialComentariosResponse;
//                    Log.d("sincronizar Clientes","tag" + historialComentariosResponse);
//                }
//            }
//            @Override
//            public void onFailure(Call<Promocionado> call, Throwable t) {
//                //Log.d("sincronizar Clientes", t.getMessage());
//                Toast.makeText(context.getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mComentario, mFechaVisita, mIdAsesor;

        public HistorialComentarios mItem;

        public ViewHolder(View view, HistoralComentariosListener historalComentariosListener) {
            super(view);
            mView = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.HistoralComentariosListener(mItem);
                }
            });
            mComentario = (TextView) view.findViewById(R.id.tv_comentario);
//            mSeccion = (TextView) view.findViewById(R.id.tv_seccion);
            mFechaVisita = (TextView) view.findViewById(R.id.tv_fechaVisita);
            mIdAsesor = (TextView) view.findViewById(R.id.tv_idAsesor);

        }

    }

    public interface HistoralComentariosListener {
        void HistoralComentariosListener(HistorialComentarios historialComentarios);

    }
}
