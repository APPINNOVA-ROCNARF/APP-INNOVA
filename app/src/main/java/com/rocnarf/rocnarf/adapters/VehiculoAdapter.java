package com.rocnarf.rocnarf.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.rocnarf.rocnarf.R;
import com.rocnarf.rocnarf.models.Vehiculo;

import java.util.ArrayList;
import java.util.List;

public class VehiculoAdapter extends RecyclerView.Adapter<VehiculoAdapter.ViewHolder> {

    private List<Vehiculo> vehiculos = new ArrayList<>();

    public void setVehiculos(List<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehiculo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vehiculo vehiculo = vehiculos.get(position);
        holder.tvPlaca.setText("Placa: " + vehiculo.getPlaca());
    }

    @Override
    public int getItemCount() {
        return vehiculos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPlaca;

        public ViewHolder(View itemView) {
            super(itemView);
            tvPlaca = itemView.findViewById(R.id.tvPlaca);
        }
    }
}