package com.rocnarf.rocnarf.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.rocnarf.rocnarf.ClientesCupoCreditoActivity;
import com.rocnarf.rocnarf.ClientesFacturasActivity;
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
import com.rocnarf.rocnarf.models.PedidosPendiente;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class PedidosPendienteAdapter extends  RecyclerView.Adapter<PedidosPendienteAdapter.ViewHolder> {
    public   List<PedidosPendiente> mValues;
    public   List<PedidosPendiente> mValuesFiltrados;
    private  String idUsuario;
    SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);
    PedidosPendienteAdapter.PedidosPendienteListener listener;
    Context context;
    public PedidosPendienteAdapter(Context context,List<PedidosPendiente> values, PedidosPendienteListener listener, String idUsuarioPass){
        this.mValues = values;
        this.context = context;
        this.mValuesFiltrados = values;
        this.listener = listener;
        idUsuario = idUsuarioPass;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_pedidos_pendiente, viewGroup, false), listener);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.mItem = mValues.get(i);

        holder.mAsesor.setText(mValues.get(i).getVendedor());
        holder.mNombreCliente.setText(mValues.get(i).getNomcli());
        holder.mTipoCliente.setText(mValues.get(i).getTipoObserv());
        //holder.mTipoClienteObs.setText(mValues.get(i).getOrigen());
        BigDecimal bd = mValues.get(i).getValor();
        holder.mCodigoCliente = mValues.get(i).getCodigo();
        holder.mOrigenCliente = mValues.get(i).getOrigen();
        bd.setScale(2, BigDecimal.ROUND_HALF_UP); // this does change bd
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);

        NumberFormat formatter = new DecimalFormat("###,###,##0.00");
        BigDecimal myNumber1 = mValues.get(i).getValor();
        String formattedNumberValor = formatter.format(myNumber1);

        holder.mValor.setText("$ "+ formattedNumberValor + "\n" + sdf.format(mValues.get(i).getFecha())+ "\n" + mValues.get(i).getTransporte());
        holder.mCodigoText.setText(mValues.get(i).getCodigo());
        holder.mCiudad.setText(mValues.get(i).getCiucli());

        if(mValues.get(i).getDespachado() != null){

            holder.mEstadoB.setBackgroundColor(Color.parseColor("#21d162"));
            holder.mEstadoCartera.setBackgroundColor(Color.parseColor("#21d162"));

          // holder.mEstadoB.setVisibility(View.GONE);
///            holder.mEstadoCartera.setVisibility(View.GONE);

            holder.mDespachado.setBackgroundColor(Color.parseColor("#21d162"));

        }
        else {
            holder.mDespachado.setBackgroundColor(Color.parseColor("#ff0000"));
            holder.mEstadoB.setVisibility(View.VISIBLE);
            holder.mEstadoCartera.setVisibility(View.VISIBLE);

            if (mValues.get(i).getAprobado().toUpperCase(Locale.ROOT).equals("A")) {
                holder.mEstadoB.setBackgroundColor(Color.parseColor("#21d162"));
            } else if (mValues.get(i).getAprobado().toUpperCase(Locale.ROOT).equals("N")) {
                holder.mEstadoB.setBackgroundColor(Color.parseColor("#F44336"));

            } else {
                holder.mEstadoB.setBackgroundColor(Color.parseColor("#ffff00"));
            }


            if (mValues.get(i).getOkboni() && !mValues.get(i).getNegaboni()) {
                holder.mEstadoCartera.setBackgroundColor(Color.parseColor("#21d162"));
            } else if (!mValues.get(i).getOkboni() && mValues.get(i).getNegaboni()) {
                holder.mEstadoCartera.setBackgroundColor(Color.parseColor("#F44336"));
            } else if (!mValues.get(i).getOkboni() && !mValues.get(i).getNegaboni()) {
                holder.mEstadoCartera.setBackgroundColor(Color.parseColor("#ffff00"));
            }
        }

        if (mValues.get(i).getOrigen().equals("MEDICO")) {
            holder.mImagenCliente.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_person_black_24dp));
        } else {
            holder.mImagenCliente.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_store_black_24dp));
        }


        holder.mTipoClienteObs.setText(mValues.get(i).getTipo());
        String valiZ= mValues.get(i).getCodigo().substring(0, 1);
        if (valiZ.equals("Z") || mValues.get(i).getOrigen().equals("MEDICO") ) {
            holder.mTipoClienteObs.setText("");
            if(mValues.get(i).getOrigen().equals("MEDICO")) {
                holder.mTipoClienteObs.setText(mValues.get(i).getOrigen());
            }
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

            List<PedidosPendiente> filtered = new ArrayList<>();
            Log.d("sincronizar Clientes", "sss"+ charSequence);
            if (charSequence.toString().isEmpty()) {
                filtered.addAll(mValuesFiltrados);
            } else {
                for (PedidosPendiente Busqueda : mValuesFiltrados) {
                    if (Busqueda.getNomcli().toLowerCase().contains(charSequence.toString().toLowerCase())
                            || Busqueda.getCodigo().toLowerCase().contains(charSequence.toString().toLowerCase())
                            || Busqueda.getCiucli().toLowerCase().contains(charSequence.toString().toLowerCase())
                            || Busqueda.getFactura().toLowerCase().contains(charSequence.toString().toLowerCase())) {
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
            mValues = (List<PedidosPendiente>) results.values;
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mAsesor;
        public final TextView mNombreCliente;
        public final TextView mValor;
        public final TextView mTipoCliente;
        public final TextView mTipoClienteObs,mCodigoText,mCiudad;
        public final ImageView mEstadoCartera,mImagenCliente, mDespachado;
        public final ImageView mEstadoB;
        public String mCodigoCliente,idCliente,mOrigenCliente;
        public PedidosPendiente mItem;

        public ViewHolder(View view, PedidosPendienteAdapter.PedidosPendienteListener pedidosPendienteListener) {
            super(view);

            mAsesor = (TextView) view.findViewById(R.id.tv_Asesor_pen);
            mNombreCliente = (TextView) view.findViewById(R.id.tv_nombre_cliente_pen);
            mValor = (TextView) view.findViewById(R.id.tv_valor_pen);
            mTipoCliente = (TextView) view.findViewById(R.id.tv_codigo_row_tipo_cliente_pedpen);
            mTipoClienteObs = (TextView) view.findViewById(R.id.tv_codigo_row_tipo_cliente_obs_pedpen);
            mEstadoCartera = (ImageView) view.findViewById(R.id.divider_b);
            mEstadoB = (ImageView) view.findViewById(R.id.divider_c);
            mImagenCliente = (ImageView) view.findViewById(R.id.iv_origen_row_pedidos_pendientes);
            mCodigoText = (TextView) view.findViewById(R.id.tv_codigo_cliente_pen);
            mCiudad = (TextView) view.findViewById(R.id.tv_ciudad_cliente_pen);
            mDespachado =  (ImageView) view.findViewById(R.id.divider_d);

            mView = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.PedidosPendienteListener(mItem);
                }
            });

            ImageButton mMenuPedidosView = (ImageButton)view.findViewById(R.id.ib_pedido_pendiente_menu);
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
                        popupMenu.getMenu().findItem(R.id.action_cupos_credito).setVisible(false);
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
                            popupMenu.getMenu().findItem(R.id.action_cupos_credito).setVisible(false);
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
                            } else if (itemId == R.id.action_cupos_credito) {
                                Intent i = new Intent(context, ClientesCupoCreditoActivity.class);
                                i.putExtra(Common.ARG_IDCLIENTE, idCliente);
                                i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                context.startActivity(i);
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

    public interface  PedidosPendienteListener {
        void  PedidosPendienteListener(PedidosPendiente pedidosPendiente);

    }
}
