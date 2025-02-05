package com.rocnarf.rocnarf.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocnarf.rocnarf.R;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.models.VisitasImpulsadoras;

import java.text.SimpleDateFormat;
import java.util.List;

public class VisitasImpulsadorasReciclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<VisitasImpulsadoras> mValues;
    SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);
    VisitasImpulsadorasListaListener listener;

    public VisitasImpulsadorasReciclerViewAdapter(List<VisitasImpulsadoras> values, VisitasImpulsadorasListaListener listener){
        this.mValues = values;
        this.listener = listener;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_visitas_impulsadoras, viewGroup, false);
        return new VisitasImpulsadorasReciclerViewAdapter.VisitasImpulsadorasViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final VisitasImpulsadoras planificacionItem = (VisitasImpulsadoras) mValues.get(i);
        VisitasImpulsadorasViewHolder contenidoHolder = (VisitasImpulsadorasViewHolder)viewHolder;
        contenidoHolder.mItem = planificacionItem;
        contenidoHolder.mNombreView.setText(planificacionItem.getNombreCliente());
        contenidoHolder.mAsesorView.setText(planificacionItem.getCodigoAsesor());
        contenidoHolder.mFechaPlanificada.setText("El " + sdf.format(planificacionItem.getFechaInicioVisitaPlanificada()));
        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
        contenidoHolder.mDesde.setText("desde: " + localDateFormat.format(planificacionItem.getFechaInicioVisitaPlanificada()));
        contenidoHolder.mHasta.setText("hasta: " + localDateFormat.format(planificacionItem.getFechaFinVisitaPlanificada()));

        contenidoHolder.mDireccionView.setText(planificacionItem.getDireccion() );
        contenidoHolder.mEstadoView.setVisibility( planificacionItem.getEstado().equals("PEND")  ? View.GONE : View.VISIBLE);
        contenidoHolder.mSyncView.setVisibility(planificacionItem.getIdVisitaImpulsadora() != 0 && !planificacionItem.isPendienteSync() ? View.GONE : View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class VisitasImpulsadorasViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mFechaPlanificada;
        public final TextView mNombreView;
        public final TextView mDireccionView;
        public final TextView mAsesorView;

        public final ImageView mEstadoView;
        public final ImageView mSyncView;
        public final TextView mDesde;
        public final TextView mHasta;

        public VisitasImpulsadoras mItem;

        public VisitasImpulsadorasViewHolder(View view, final VisitasImpulsadorasListaListener listener) {
            super(view);
            mView = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.VisitasImpulsadorasListaListener(mItem);
                }
            });

            mNombreView = (TextView) view.findViewById(R.id.tv_cliente_row_visitas_impulsadoras);
            mDireccionView = (TextView) view.findViewById(R.id.tv_direccion_row_visitas_impulsadoras);
            mAsesorView= (TextView) view.findViewById(R.id.tv_asesor_row_visitas_impulsadoras);
            mFechaPlanificada = (TextView) view.findViewById(R.id.tv_fecha_row_visitas_impulsadoras);
            mDesde = (TextView) view.findViewById(R.id.tv_desde_row_visitas_impulsadoras);
            mHasta = (TextView) view.findViewById(R.id.tv_hasta_row_visitas_impulsadoras);

            mEstadoView = (ImageView) view.findViewById(R.id.iv_estado_row_visitas_impulsadoras);
            mSyncView = (ImageView) view.findViewById(R.id.iv_sync_row_visitas_impulsadoras);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNombreView.getText() + "'";
        }
    }


    public interface VisitasImpulsadorasListaListener {
        void VisitasImpulsadorasListaListener(VisitasImpulsadoras visitasImpulsadoras);

    }
}
