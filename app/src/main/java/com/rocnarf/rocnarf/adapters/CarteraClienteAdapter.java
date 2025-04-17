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
import com.rocnarf.rocnarf.models.CarteraCliente;
import com.rocnarf.rocnarf.models.Clientes;
import com.rocnarf.rocnarf.models.PedidosPendiente;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CarteraClienteAdapter extends  RecyclerView.Adapter<CarteraClienteAdapter.ViewHolder> {
    public   List<CarteraCliente> mValues;
    public   List<CarteraCliente> mValuesFiltrados;
    private String idCliente, idUsuario, nombreCliente, mOrigenCliente,categoriaMed,mCodigoCliente;
    public  String tipoCliente;
    SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);
    CarteraClienteAdapter.CarteraClienteListener listener;
    Context context;
    public CarteraClienteAdapter(Context context, List<CarteraCliente> values, CarteraClienteListener listener, String idUsuarioPass){
        this.mValues = values;
        this.context = context;
        this.mValuesFiltrados = values;
        this.listener = listener;
        idUsuario = idUsuarioPass;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_cartera_cliente, viewGroup, false), listener);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.mItem = mValues.get(i);


        holder.mNombreCliente.setText(mValues.get(i).getNombre());

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
        String mNoVencido = formatter.format(mValues.get(i).getNoVencido());
        String mVencido = formatter.format(mValues.get(i).getVencido());
        String mCarteraTotal = formatter.format(mValues.get(i).getTotalCartera());

        holder.mNoVencido.setText(mNoVencido);
        holder.mVencido.setText(mVencido);
        holder.mCarteraTotal.setText(mCarteraTotal);
//
        holder.mDiasPlazo.setText(String.valueOf(mValues.get(i).getDiasPlazo()));
        holder.mDiasVen.setText(String.valueOf(mValues.get(i).getDiazVencido()));

        String mSaldoFavor = formatter.format(mValues.get(i).getSaldoCliente());
        holder.mSaldoFavor.setText(mSaldoFavor);

        holder.mOrigenMenu.setText(mValues.get(i).getOrigen());
        holder.mTipoClienteObs.setText(mValues.get(i).getTipo());

        if (valiZ.equals("Z") || mValues.get(i).getOrigen().equals("MEDICO") ) {
            holder.mTipoClienteObs.setText("");
            if(mValues.get(i).getOrigen().equals("MEDICO")) {
                holder.mTipoClienteObs.setText(mValues.get(i).getIdEspecialidades());
            }
        }
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
        if (mValues.get(i).getAuspiciado() != null){
            if(mValues.get(i).getAuspiciado() == true) holder.mOrigen.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_person_gold_24dp));
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

            List<CarteraCliente> filtered = new ArrayList<>();
            Log.d("sincronizar Clientes", "sss"+ charSequence);
            if (charSequence.toString().isEmpty()) {
                filtered.addAll(mValuesFiltrados);
            } else {
                for (CarteraCliente Busqueda : mValuesFiltrados) {
                    if (Busqueda.getNombre().toLowerCase().contains(charSequence.toString().toLowerCase())
                            || Busqueda.getCiudad().toLowerCase().contains(charSequence.toString().toLowerCase())
                            || Busqueda.getDireccion().toLowerCase().contains(charSequence.toString().toLowerCase())
                            || Busqueda.getTipo().toLowerCase().contains(charSequence.toString().toLowerCase())) {
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
            mValues = (List<CarteraCliente>) results.values;
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNombreCliente;
        public String mCodigoCliente,idCliente,mOrigenCliente;
        public final TextView mNombreView;

        public final TextView mDireccionView,mNoVencido,mVencido,mCarteraTotal,mDiasPlazo,mDiasVen,mSaldoFavor;
        public final TextView mCodigoView;
        public final TextView mTipoCliente;
        public final TextView mTipoClienteObs;
        public final TextView mOrigenMenu;;
        public final ImageView mOrigen;
        public final ImageView mEstadoVisita;
        public final ImageView mEstadoFilas;
        public CarteraCliente mItem;

        public ViewHolder(View view, CarteraClienteAdapter.CarteraClienteListener carteraClienteListener) {
            super(view);


            mNombreCliente = (TextView) view.findViewById(R.id.tv_nombre_cliente_row_resultado_cliente_cartera);
            mNombreView = (TextView) view.findViewById(R.id.tv_nombre_cliente_row_resultado_cliente_cartera);

            mDireccionView = (TextView) view.findViewById(R.id.tv_direccion_row_resultado_cliente_cartera);
            mCodigoView = (TextView) view.findViewById(R.id.tv_codigo_row_resultado_cliente_cartera);
            mTipoCliente = (TextView) view.findViewById(R.id.tv_codigo_row_tipo_cliente_cartera);
            mTipoClienteObs = (TextView) view.findViewById(R.id.tv_codigo_row_tipo_cliente_obs_cartera);
            // mSeccionView = (TextView) view.findViewById(R.id.tv_seccion_row_resultado_cliente);
            mOrigen = (ImageView) view.findViewById(R.id.iv_origen_row_resultado_cliente_cartera);
            mEstadoFilas = (ImageView) view.findViewById(R.id.divider3_cartera);
            mEstadoVisita = (ImageView) view.findViewById(R.id.iv_estado_visita_row_resultado_cliente_cartera);
            mOrigenMenu = (TextView) view.findViewById(R.id.tv_origen_cartera);
            mNoVencido  = (TextView) view.findViewById(R.id.no_vencido_ND_cartera);
            mVencido = (TextView) view.findViewById(R.id.valor_saldo_ND_cartera);
            mCarteraTotal = (TextView) view.findViewById(R.id.valor_total_ND_cartera);
            mDiasPlazo = (TextView) view.findViewById(R.id.valor_vencido_ND_cartera);
            mDiasVen = (TextView) view.findViewById(R.id.f_mas_dia_ND_cartera);
            mSaldoFavor = (TextView) view.findViewById(R.id.tv_cliente_nd_cartera);
            mView = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.CarteraClienteListener(mItem);
                }
            });

            ImageButton mMenuPedidosView = (ImageButton)view.findViewById(R.id.ib_pedido_fragment_cliente_detalle_cartera_cartera);
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
                            int id = menuItem.getItemId();

                            if (id == R.id.action_historial_pedidos) {
                                Intent iFacturas = new Intent(context, ClientesFacturasNotaDebitosActivity.class);
                                iFacturas.putExtra(Common.ARG_IDCLIENTE, idCliente);
                                iFacturas.putExtra(Common.ARG_NOMBRE_CLIENTE, mNombreCliente.getText().toString());
                                iFacturas.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                context.startActivity(iFacturas);
                                return true;

                            } else if (id == R.id.action_detalle_productos) {
                                Intent iFacturaDetalle = new Intent(context, FacturaDetalleActivity.class);
                                iFacturaDetalle.putExtra(Common.ARG_IDCLIENTE, idCliente);
                                iFacturaDetalle.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                context.startActivity(iFacturaDetalle);
                                return true;

                            } else if (id == R.id.action_totales_mes) {
                                Intent iTotalesXMes = new Intent(context, VentasMensualesClientesActivity.class);
                                iTotalesXMes.putExtra(Common.ARG_IDCLIENTE, idCliente);
                                iTotalesXMes.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                context.startActivity(iTotalesXMes);
                                return true;

                            } else if (id == R.id.action_ficha_medico) {
                                Intent iFichaMedico = new Intent(context, MedicoFichaActivity.class);
                                iFichaMedico.putExtra(Common.ARG_IDCLIENTE, idCliente);
                                iFichaMedico.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                context.startActivity(iFichaMedico);
                                return true;

                            } else if (id == R.id.action_historial_comentarios) {
                                Intent iComentarios = new Intent(context, HistorialComentariosActivity.class);
                                iComentarios.putExtra(Common.ARG_IDCLIENTE, idCliente);
                                iComentarios.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                context.startActivity(iComentarios);
                                return true;

                            } else if (id == R.id.action_historial_visitas) {
                                Intent iVisitas = new Intent(context, HistorialVisitasActivity.class);
                                iVisitas.putExtra(Common.ARG_IDCLIENTE, idCliente);
                                iVisitas.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                context.startActivity(iVisitas);
                                return true;

                            } else if (id == R.id.action_recetas) {
                                Intent iRecetas = new Intent(context, RecetasXActivity.class);
                                iRecetas.putExtra(Common.ARG_IDCLIENTE, idCliente);
                                iRecetas.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                context.startActivity(iRecetas);
                                return true;

                            } else if (id == R.id.action_categoria) {
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

    public interface  CarteraClienteListener {
        void  CarteraClienteListener(CarteraCliente carteraCliente);

    }
}
