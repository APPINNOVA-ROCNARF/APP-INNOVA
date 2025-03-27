package com.rocnarf.rocnarf.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.rocnarf.rocnarf.ClientesCupoCreditoActivity;
import com.rocnarf.rocnarf.ClientesFacturasNotaDebitosActivity;
import com.rocnarf.rocnarf.FacturaDetalleActivity;
import com.rocnarf.rocnarf.HistorialComentariosActivity;
import com.rocnarf.rocnarf.HistorialVisitasActivity;
import com.rocnarf.rocnarf.MedicoFichaActivity;
import com.rocnarf.rocnarf.MedicosCategoriaActivity;
import com.rocnarf.rocnarf.R;
import com.rocnarf.rocnarf.RecetasXActivity;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.VentasMensualesClientesActivity;
import com.rocnarf.rocnarf.models.MedicosCumpleanyos;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class MedicosCumpleanyosAdapter extends  RecyclerView.Adapter<MedicosCumpleanyosAdapter.ViewHolder> {
    public   List<MedicosCumpleanyos> mValues;
    public   List<MedicosCumpleanyos> mValuesFiltrados;
    private String idCliente, idUsuario, nombreCliente, mOrigenCliente,categoriaMed,mCodigoCliente;
    public  String tipoCliente;
    SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);
    MedicosCumpleanyosAdapter.MedicosCumpleanyosListener listener;
    Context context;
    public MedicosCumpleanyosAdapter(Context context, List<MedicosCumpleanyos> values, MedicosCumpleanyosListener listener, String idUsuarioPass){
        this.mValues = values;
        this.context = context;
        this.mValuesFiltrados = values;
        this.listener = listener;
        idUsuario = idUsuarioPass;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_cumpleanyos, viewGroup, false), listener);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.mItem = mValues.get(i);


        holder.mNombreCliente.setText(mValues.get(i).getNombre());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");

        if(mValues.get(i).getFechaNacimiento() != null) {
            holder.mAnyo.setText(sdf.format(mValues.get(i).getFechaNacimiento()) +" - " + mValues.get(i).getAnyo() + " AÑOS");
        }else  {
            holder.mAnyo.setText("");
        }

        String nombreClienteL = mValues.get(i).getNombre();

        if (nombreClienteL.length()>= 30) {
            nombreClienteL = nombreClienteL.substring(0, 30);
        }
        String direccionClienteL = "";
        if (mValues.get(i).getDireccion() != null) {

            direccionClienteL = mValues.get(i).getDireccion();

            if (direccionClienteL.length() >= 30) {
                direccionClienteL = direccionClienteL.substring(0, 30);
            }
        }

        categoriaMed = mValues.get(i).getClase();
        String valiZ= mValues.get(i).getIdCliente().substring(0, 1);
        holder.mNombreCliente.setText(nombreClienteL);
        holder.mDireccionView.setText(mValues.get(i).getCiudad() + " - " + direccionClienteL + "...");
        holder.mCodigoView.setText(mValues.get(i).getIdCliente());
        holder.mCodigoCliente = mValues.get(i).getIdCliente();
        holder.mOrigenCliente = mValues.get(i).getOrigen();


        NumberFormat formatter = new DecimalFormat("###,###,##0.00");



        holder.mOrigenMenu.setText(mValues.get(i).getOrigen());
        holder.mTipoClienteObs.setText(mValues.get(i).getTipo());

        if (valiZ.equals("Z") || mValues.get(i).getOrigen().equals("MEDICO") ) {
            holder.mTipoClienteObs.setText("");
            if(mValues.get(i).getOrigen().equals("MEDICO")) {
                holder.mTipoClienteObs.setText(mValues.get(i).getIdEspecialidades());
            }
        }
        holder.mTipoCliente.setText("");
        if (mValues.get(i).getTipoObserv() == null){

        }else {
            holder.mTipoCliente.setText(mValues.get(i).getClaseMedico());

        }

        if (TextUtils.isEmpty(mValues.get(i).getEstadoVisita())) {
            holder.mEstadoFilas.setBackgroundColor(Color.parseColor("#FF0000"));
        } else if (mValues.get(i).getEstadoVisita().equals("EFECT")) {
            holder.mEstadoFilas.setBackgroundColor(Color.parseColor("#21d162"));
        } else {
            holder.mEstadoFilas.setBackgroundColor(Color.parseColor("#ffff00"));
        }

        if (valiZ.equals("Z")){
            holder.mOrigen.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_client_z2));
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

            List<MedicosCumpleanyos> filtered = new ArrayList<>();
            if (charSequence.toString().isEmpty()) {
                filtered.addAll(mValuesFiltrados);
            } else {
                for (MedicosCumpleanyos Busqueda : mValuesFiltrados) {
                    if (Busqueda.getNombre().toLowerCase().contains(charSequence.toString().toLowerCase())
                            || Busqueda.getCiudad().toLowerCase().contains(charSequence.toString().toLowerCase())
                            || Busqueda.getDireccion().toLowerCase().contains(charSequence.toString().toLowerCase())
                            || Busqueda.getTipo().toLowerCase().contains(charSequence.toString().toLowerCase())) {

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

            mValues = (List<MedicosCumpleanyos>) results.values;
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNombreCliente;
        public String mCodigoCliente,idCliente,mOrigenCliente;
        public final TextView mNombreView;

        public final TextView mDireccionView, mAnyo;
        public final TextView mCodigoView;
        public final TextView mTipoCliente;
        public final TextView mTipoClienteObs;
        public final TextView mOrigenMenu;;
        public final ImageView mOrigen;
        public final ImageView mEstadoVisita;
        public final ImageView mEstadoFilas;

        public MedicosCumpleanyos mItem;

        public ViewHolder(View view, MedicosCumpleanyosAdapter.MedicosCumpleanyosListener medicosCumpleanyosListener) {
            super(view);


            mNombreCliente = (TextView) view.findViewById(R.id.tv_nombre_cliente_row_resultado_cumpleanyos);
            mNombreView = (TextView) view.findViewById(R.id.tv_nombre_cliente_row_resultado_cumpleanyos);

            mDireccionView = (TextView) view.findViewById(R.id.tv_direccion_row_resultado_cumpleanyos);
            mCodigoView = (TextView) view.findViewById(R.id.tv_codigo_row_resultado_cumpleanyos);
            mTipoCliente = (TextView) view.findViewById(R.id.tv_codigo_row_tipo_cumpleanyos);
            mTipoClienteObs = (TextView) view.findViewById(R.id.tv_codigo_row_tipo_cliente_obs_cumpleanyos);
            // mSeccionView = (TextView) view.findViewById(R.id.tv_seccion_row_resultado_cliente);
            mOrigen = (ImageView) view.findViewById(R.id.iv_origen_row_resultado_cumpleanyos);
            mEstadoVisita = (ImageView) view.findViewById(R.id.iv_estado_visita_row_resultado_cumpleanyos);
            mOrigenMenu = (TextView) view.findViewById(R.id.tv_origen_cumpleanyos);
            mAnyo = (TextView) view.findViewById(R.id.tv_direccion_row_fechaNac_cumpleanyos);
            mEstadoFilas = (ImageView) view.findViewById(R.id.divider3_cumple);

            mView = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.MedicosCumpleanyosListener(mItem);
                }
            });

            ImageButton mMenuPedidosView = (ImageButton)view.findViewById(R.id.ib_pedido_fragment_cliente_detalle_cumpleanyos);
            mMenuPedidosView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    idCliente = mCodigoCliente.toString();
                    String nombreClienteL = mNombreCliente.getText().toString();
                    if (nombreClienteL.length()>= 30) {
                        nombreClienteL = nombreClienteL.substring(0, 30);
                    }


                    PopupMenu popupMenu = new PopupMenu(context, v);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_pedidos_detalle_cliente, popupMenu.getMenu());
                    String valiZ= mCodigoCliente.toUpperCase().substring(0, 1);
                    if (valiZ.equals("Z")) {
                        popupMenu.getMenu().findItem(R.id.action_historial_pedidos).setVisible(false);
                        popupMenu.getMenu().findItem(R.id.action_detalle_productos).setVisible(false);
                        popupMenu.getMenu().findItem(R.id.action_totales_mes).setVisible(false);
                        popupMenu.getMenu().findItem(R.id.action_categoria).setVisible(false);
                        popupMenu.getMenu().findItem(R.id.action_recetas).setVisible(false);
                    }else {
                        if (mOrigenCliente.equals("FARMA"))
                            popupMenu.getMenu().findItem(R.id.action_ficha_medico).setVisible(false);
                        if (mOrigenCliente.equals("FARMA"))
                              popupMenu.getMenu().findItem(R.id.action_recetas).setVisible(false);
                        if (mOrigenCliente.equals("FARMA"))
                            popupMenu.getMenu().findItem(R.id.action_categoria).setVisible(false);
                        if (!mOrigenCliente.equals("FARMA"))
                            popupMenu.getMenu().findItem(R.id.action_historial_pedidos).setVisible(false);
                        if (!mOrigenCliente.equals("FARMA"))
                            popupMenu.getMenu().findItem(R.id.action_detalle_productos).setVisible(false);
                        if (!mOrigenCliente.equals("FARMA"))
                            popupMenu.getMenu().findItem(R.id.action_totales_mes).setVisible(false);
                    }
                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            int itemId = menuItem.getItemId();

                            if (itemId == R.id.action_historial_pedidos) {
                                Intent iFacturas = new Intent(context, ClientesFacturasNotaDebitosActivity.class);
                                iFacturas.putExtra(Common.ARG_IDCLIENTE, idCliente);
                                iFacturas.putExtra(Common.ARG_NOMBRE_CLIENTE, mNombreCliente.getText().toString());
                                iFacturas.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                context.startActivity(iFacturas);
                                return true;
                            } else if (itemId == R.id.action_detalle_productos) {
                                Intent iFacturaDetalle = new Intent(context, FacturaDetalleActivity.class);
                                iFacturaDetalle.putExtra(Common.ARG_IDCLIENTE, idCliente);
                                iFacturaDetalle.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                context.startActivity(iFacturaDetalle);
                                return true;
                            } else if (itemId == R.id.action_totales_mes) {
                                Intent iTotalesXMes = new Intent(context, VentasMensualesClientesActivity.class);
                                iTotalesXMes.putExtra(Common.ARG_IDCLIENTE, idCliente);
                                iTotalesXMes.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                context.startActivity(iTotalesXMes);
                                return true;
                            } else if (itemId == R.id.action_ficha_medico) {
                                Intent iFichaMedico = new Intent(context, MedicoFichaActivity.class);
                                iFichaMedico.putExtra(Common.ARG_IDCLIENTE, idCliente);
                                iFichaMedico.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                context.startActivity(iFichaMedico);
                                return true;
                            } else if (itemId == R.id.action_historial_comentarios) {
                                Intent iComentarios = new Intent(context, HistorialComentariosActivity.class);
                                iComentarios.putExtra(Common.ARG_IDCLIENTE, idCliente);
                                iComentarios.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                context.startActivity(iComentarios);
                                return true;
                            } else if (itemId == R.id.action_historial_visitas) {
                                Intent iVisitas = new Intent(context, HistorialVisitasActivity.class);
                                iVisitas.putExtra(Common.ARG_IDCLIENTE, idCliente);
                                iVisitas.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                context.startActivity(iVisitas);
                                return true;
                            } else if (itemId == R.id.action_recetas) {
                                Intent iRecetas = new Intent(context, RecetasXActivity.class);
                                iRecetas.putExtra(Common.ARG_IDCLIENTE, idCliente);
                                iRecetas.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                context.startActivity(iRecetas);
                                return true;
                            } else if (itemId == R.id.action_categoria) {
                                Intent iCategoria = new Intent(context, MedicosCategoriaActivity.class);
                                iCategoria.putExtra(Common.ARG_IDCLIENTE, idCliente);
                                iCategoria.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                context.startActivity(iCategoria);
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
            return super.toString() + " '" + mNombreCliente.getText() + "'";
        }
    }

    public interface  MedicosCumpleanyosListener {
        void  MedicosCumpleanyosListener(MedicosCumpleanyos medicosCumpleanyos);

    }
}
