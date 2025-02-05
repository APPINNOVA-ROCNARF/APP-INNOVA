package com.rocnarf.rocnarf.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rocnarf.rocnarf.R;
import com.rocnarf.rocnarf.models.ImageImpulsacion;

import java.util.ArrayList;

public class ImageImpulsacionRecyclerViewAdapter extends RecyclerView.Adapter<ImageImpulsacionRecyclerViewAdapter.ViewHolder> {
    private ArrayList<ImageImpulsacion> imageUrls;
    private Context context;

    public ImageImpulsacionRecyclerViewAdapter(Context context, ArrayList<ImageImpulsacion> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_image_impulsacion, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Glide.with(context).load(imageUrls.get(i).getImageUrl()).into(viewHolder.img);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.imageView);
        }
    }
}
