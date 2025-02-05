package com.rocnarf.rocnarf.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rocnarf.rocnarf.R;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.models.Rutas;

import java.text.SimpleDateFormat;
import java.util.List;


public class RutasAdapter extends  RecyclerView.Adapter<RutasAdapter.ViewHolder> {
    private final List<Rutas> mValues;
    SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);
    RutasAdapter.RutasListener listener;
    Context context;
    public RutasAdapter(List<Rutas> values, RutasListener listener){

        this.mValues = values;


        this.listener = listener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_rutas, viewGroup, false), listener);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.mItem = mValues.get(i);

        holder.mNombre.setText(mValues.get(i).getNombre());
        holder.mDescripcion.setText(mValues.get(i).getDescripcion());

//        boolean isExpanded = mValues.get(i).getExpadir();
//        holder.mExpandir.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNombre;
        public final TextView mDescripcion;
        public final ConstraintLayout mExpandir;
        public Rutas mItem;

        public ViewHolder(View view, final RutasAdapter.RutasListener rutasListener) {
            super(view);
            mView = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.RutasListener(mItem);
                }
            });
            mNombre = (TextView) view.findViewById(R.id.tv_preguntas);
            mDescripcion = (TextView) view.findViewById(R.id.tv_respuesta);
            mExpandir = (ConstraintLayout) view.findViewById(R.id.expandir);
//            mNombre.setText(mItem.getNombre());
//            mDescripcion.setText(mItem.getDescripcion());
//            mNombre.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mItem.setExpandir(!mItem.getExpadir());
//                    notifyItemChanged(getAdapterPosition());
//                }
//            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNombre.getText() + "'";
        }
    }

    public interface  RutasListener {
        void RutasListener(Rutas rutas);

    }
}
