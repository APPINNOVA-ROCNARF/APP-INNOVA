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
import com.rocnarf.rocnarf.models.Planes;

import java.text.SimpleDateFormat;
import java.util.List;


public class PlanesAdapter extends  RecyclerView.Adapter<PlanesAdapter.ViewHolder> {
    private final List<Planes> mValues;
    SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);
    PlanesAdapter.PlanesListener listener;
    Context context;
    public PlanesAdapter(List<Planes> values, PlanesListener listener){

        this.mValues = values;


        this.listener = listener;

    }


//    public View getView(int i ,View view, ViewGroup viewGroup){
//
//        TextView mNombre;
//        TextView mDescripcion;
//        Planes mItem = this.mValues.get(i);
//        view = LayoutInflater.from(context).inflate(R.layout.row_panel_plan, null);
//
//        mNombre = (TextView) view.findViewById(R.id.tv_nombre_plan);
//        mDescripcion = (TextView) view.findViewById(R.id.tv_descripcion_plan);
//
//        mNombre.setText(mItem.getNombre());
//        mDescripcion.setText(mItem.getDescripcion());
//
//        Log.d("myTag", "lo agarra ----->" );
//        return null;
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


//        View view = LayoutInflater.from(viewGroup.getContext())
//                .inflate(R.layout.row_panel_plan, viewGroup, false);
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_panel_plan, viewGroup, false), listener);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.mItem = mValues.get(i);

        holder.mNombre.setText(mValues.get(i).getNombre());
        holder.mDescripcion.setText(mValues.get(i).getDescripcion());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNombre;
        public final TextView mDescripcion;

        public Planes mItem;

        public ViewHolder(View view, PlanesListener planesListener) {
            super(view);
            mView = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.PlanesListener(mItem);
                }
            });
            mNombre = (TextView) view.findViewById(R.id.tv_nombre_plan);
            mDescripcion = (TextView) view.findViewById(R.id.tv_descripcion_plan);
//            mNombre.setText(mItem.getNombre());
//            mDescripcion.setText(mItem.getDescripcion());
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNombre.getText() + "'";
        }
    }

    public interface PlanesListener {
        void PlanesListener(Planes planes);

    }
}
