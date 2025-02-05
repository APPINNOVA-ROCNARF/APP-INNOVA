package com.rocnarf.rocnarf.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rocnarf.rocnarf.ClientesCupoCreditoActivity;
import com.rocnarf.rocnarf.ClientesFacturasNotaDebitosActivity;
import com.rocnarf.rocnarf.FacturaDetalleActivity;
import com.rocnarf.rocnarf.HistorialComentariosActivity;
import com.rocnarf.rocnarf.HistorialVisitasActivity;
import com.rocnarf.rocnarf.MainActivity;
import com.rocnarf.rocnarf.MedicoFichaActivity;
import com.rocnarf.rocnarf.MedicosCategoriaActivity;
import com.rocnarf.rocnarf.R;
import com.rocnarf.rocnarf.RecetasXActivity;
import com.rocnarf.rocnarf.RegistroViaticoActivity;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.VentasMensualesClientesActivity;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.models.GuiaProductos;
import com.rocnarf.rocnarf.models.MotivoQuejas;
import com.rocnarf.rocnarf.models.Quejas;
import com.rocnarf.rocnarf.models.Viatico;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ViaticoAdapter extends  RecyclerView.Adapter<ViaticoAdapter.ViewHolder> {
    public   List<Viatico> mValues;
    public   List<Viatico> mValuesFiltrados;
    private AdapterCallback adapterCallback;

    SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);
    ViaticoAdapter.ViaticoListener listener;
    Context context;
    Boolean historial = false;
    public ViaticoAdapter(Context context, List<Viatico> values, ViaticoListener listener, Boolean historial){
        this.context = context;
        this.mValues = values;

        this.mValuesFiltrados = values;
        this.listener = listener;
        this.historial = historial;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_viatico, viewGroup, false), listener);

    }
    public interface AdapterCallback {
        void onDataDeleted();
    }
    public void setAdapterCallback(AdapterCallback callback) {
        this.adapterCallback = callback;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.mItem = mValues.get(i);

        holder.mNombre.setText(mValues.get(i).getRazonSocial());
        holder.mRuc.setText(mValues.get(i).getRuc());
        holder.mConceptoFac.setText("Factura #" + mValues.get(i).getNumeroFactura() + " - " + mValues.get(i).getNombreCatalogo());
        holder.mFecha.setText(sdf.format(mValues.get(i).getFecha()));
        holder.mTotal.setText("$ " + String.format("%.2f", mValues.get(i).getTotal()));
        holder.idViatico = mValues.get(i).getIdViatico();
        holder.estadoViatico = mValues.get(i).getEstadoViatico(); // Actualiza el estado
        holder.comentarioUsuario = mValues.get(i).getComentario();

        if(mValues.get(i).getIdCatalogo() == 128) {
            holder.icoViatico.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.baseline_emoji_transportation_24));
        } else if (mValues.get(i).getIdCatalogo() == 129) {
            holder.icoViatico.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.baseline_food_bank_24));
        } else if (mValues.get(i).getIdCatalogo() == 130) {
            holder.icoViatico.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.baseline_bed_24));
        }

        if (mValues.get(i).getEstadoViatico() == 1) {
            holder.mEstadoViatico.setBackgroundColor(Color.parseColor("#ffff00"));
        } else if (mValues.get(i).getEstadoViatico() == 2) {
            holder.mEstadoViatico.setBackgroundColor(Color.parseColor("#21d162"));
        }else {
            holder.mEstadoViatico.setBackgroundColor(Color.parseColor("#FF0000"));
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

            List<Viatico> filtered = new ArrayList<>();
            if (charSequence.toString().isEmpty()) {
                filtered.addAll(mValuesFiltrados);
            } else {
                for (Viatico Busqueda : mValuesFiltrados) {
                    if (Busqueda.getRazonSocial().toLowerCase().contains(charSequence.toString().toLowerCase())
                            || Busqueda.getNombreCatalogo().toLowerCase().contains(charSequence.toString().toLowerCase())
                            || Busqueda.getNombreCiclo().toLowerCase().contains(charSequence.toString().toLowerCase())
                            || Busqueda.getNumeroFactura().toLowerCase().contains(charSequence.toString().toLowerCase())
                            || Busqueda.getRuc().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filtered.add(Busqueda);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filtered;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mValues = (List<Viatico>) results.values;
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNombre,mFecha,mTotal,mRuc,mConceptoFac;
        public Viatico mItem;
        public final ImageView mEstadoViatico, icoViatico;
        private int idViatico;
        private int estadoViatico;  // Añade una variable para el estado del Viatico
        private String comentarioUsuario;

        public ViewHolder(View view, ViaticoAdapter.ViaticoListener guiaProductosListener) {
            super(view);
            mView = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.ViaticoListener(mItem);
                }
            });
            mNombre = (TextView) view.findViewById(R.id.tv_proveedor_razon_social);
            mFecha = (TextView) view.findViewById(R.id.tv_fecha_viatico);
            mTotal = (TextView) view.findViewById(R.id.tv_total_viatico);
            mRuc = (TextView) view.findViewById(R.id.tv_ruc_viatico);
            mConceptoFac = (TextView) view.findViewById(R.id.tv_motivo_fac_viatico);
            mEstadoViatico = (ImageView) view.findViewById(R.id.divider3_viatico);
            icoViatico = (ImageView) view.findViewById(R.id.iv_origen_row_resultado_cumpleanyos);

            ImageButton mMenuViatico = (ImageButton)view.findViewById(R.id.ib_menu_viatico);
            mMenuViatico.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(context, v);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_viatico, popupMenu.getMenu());

                    if (historial) {
                        popupMenu.getMenu().findItem(R.id.action_viatico_eliminar).setVisible(false);
                    }


                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            int itemId = menuItem.getItemId();

                            if (itemId == R.id.action_viatico_eliminar) {
                                // Verifica el estado del Viatico antes de permitir eliminar
                                if (estadoViatico == 2 || estadoViatico == 0) {
                                    Toast.makeText(context, "No se puede eliminar un registro", Toast.LENGTH_LONG).show();
                                    return true;
                                }

                                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myDialog));
                                builder.setMessage("¿Desea eliminar el registro?");

                                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        PlanesService service = ApiClient.getClient().create(PlanesService.class);
                                        Call<Void> call = service.Delete(idViatico);
                                        call.enqueue(new Callback<Void>() {
                                            @Override
                                            public void onResponse(Call<Void> call, Response<Void> response) {
                                                if (response.isSuccessful()) {
                                                    Toast.makeText(context, "Registro Eliminado", Toast.LENGTH_LONG).show();
                                                    adapterCallback.onDataDeleted();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Void> call, Throwable t) {
                                                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                });

                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Acción al presionar "No"
                                    }
                                });

                                builder.create().show();
                                return true;
                            } else if (itemId == R.id.action_viatico_comentario) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.myDialog));
                                builder1.setMessage(comentarioUsuario);

                                // Configurar el botón "Cerrar"
                                builder1.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        // Simplemente cierra el diálogo
                                        dialog.dismiss();
                                    }
                                });

                                // Crear y mostrar el diálogo
                                builder1.create().show();
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });


            }
            });

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNombre.getText() + "'";
        }
    }

    private void notifyDataDeleted() {
        if (adapterCallback != null) {
            adapterCallback.onDataDeleted();
        }
    }

    public interface  ViaticoListener {
        void  ViaticoListener(Viatico viatico);

    }
}
