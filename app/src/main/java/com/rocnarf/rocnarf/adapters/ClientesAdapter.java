package com.rocnarf.rocnarf.adapters;

import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import android.text.LoginFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;

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
import com.rocnarf.rocnarf.models.Clientes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClientesAdapter extends RecyclerView.Adapter<ClientesAdapter.ClienteViewHolder> implements Filterable {
    Context context;
    OnClienteClickListener listener;

    private List<Clientes> mValues;
    private List<Clientes> mValuesFiltrados;
    private PopupMenu popup;
    private String idCliente, idUsuario, nombreCliente, mOrigenCliente,categoriaMed;
    public  String tipoCliente;
    public ClientesAdapter(Context context, OnClienteClickListener listener, List<Clientes> values, String idUsuarioTrans) {

        this.context = context;
        this.listener = listener;
        this.mValues = values;
        this.mValuesFiltrados = values;
        idUsuario = idUsuarioTrans;

    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.row_resultado_cliente, viewGroup, false);
        return new ClienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder viewHolder, int i) {
        Clientes cliente = mValuesFiltrados.get(i);
        if (cliente != null) {
            viewHolder.bindTo(cliente, this.listener);
        } else {

        }

    }

    @Override
    public int getItemCount() {
        return mValuesFiltrados.size();
    }


    public class ClienteViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNombreView;
        public final TextView mRepresentanteView;
        public final TextView mDireccionView;
        public final TextView mCodigoView;
        public final TextView mTipoCliente;
        public final TextView mTipoClienteObs;
        public final TextView mCodigoCliente;
        public final TextView mOrigenMenu;
        //public final TextView mSeccionView;
        public final ImageView mOrigen;
        public final ImageView mPedido;
        public final ImageView mCobro;
        public final ImageView mRevisita;
        public final ImageView mEstadoVisita;
        public final ImageView mEstadoFilas;
        public Clientes mItem;


        public ClienteViewHolder(View view) {
            super(view);
            mView = view;
            Log.d("xxxxxx", "cliente adapter");
            mNombreView = (TextView) view.findViewById(R.id.tv_nombre_cliente_row_resultado_cliente);
            mRepresentanteView = (TextView) view.findViewById(R.id.tv_representante_row_resultado_cliente);
            mDireccionView = (TextView) view.findViewById(R.id.tv_direccion_row_resultado_cliente);
            mCodigoView = (TextView) view.findViewById(R.id.tv_codigo_row_resultado_cliente);
            mTipoCliente = (TextView) view.findViewById(R.id.tv_codigo_row_tipo_cliente);
            mTipoClienteObs = (TextView) view.findViewById(R.id.tv_codigo_row_tipo_cliente_obs);
            // mSeccionView = (TextView) view.findViewById(R.id.tv_seccion_row_resultado_cliente);
            mOrigen = (ImageView) view.findViewById(R.id.iv_origen_row_resultado_cliente);
            mPedido = (ImageView) view.findViewById(R.id.iv_pedido_row_resultado_cliente);
            mEstadoFilas = (ImageView) view.findViewById(R.id.divider3);
            mCobro = (ImageView) view.findViewById(R.id.iv_cobro_row_resultado_cliente);
            mEstadoVisita = (ImageView) view.findViewById(R.id.iv_estado_visita_row_resultado_cliente);
            mCodigoCliente = (TextView) view.findViewById(R.id.tv_codigo);
            mOrigenMenu = (TextView) view.findViewById(R.id.tv_origen);
            mRevisita = (ImageView) view.findViewById(R.id.iv_cliente_revisita);
            ImageButton mMenuPedidosView = (ImageButton) view.findViewById(R.id.ib_pedido_fragment_cliente_detalle);
//            mMenuPedidosView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    showPopup(view, R.menu.menu_pedidos_detalle_cliente);
//                }
//            });
//            mOrigenCliente = cliente.getOrigen();
//            idCliente = cliente.getIdCliente();
//            nombreCliente = cliente.getNombreCliente();


            mMenuPedidosView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mOrigenCliente = mOrigenMenu.getText().toString();
                    idCliente = mCodigoCliente.getText().toString();
                    String nombreClienteL = mNombreView.getText().toString();
                    if (nombreClienteL.length()>= 30) {
                        nombreClienteL = nombreClienteL.substring(0, 30);
                    }

                    nombreCliente = nombreClienteL;
//                    Log.d("menu", "mOrigenCliente --->" + nombreCliente);

                    PopupMenu popupMenu = new PopupMenu(context, v);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_pedidos_detalle_cliente, popupMenu.getMenu());
                    String valiZ= mCodigoView.getText().toString().toUpperCase().substring(0, 1);
                    if (valiZ.equals("Z")) {
                        popupMenu.getMenu().findItem(R.id.action_historial_pedidos).setVisible(false);
                        popupMenu.getMenu().findItem(R.id.action_detalle_productos).setVisible(false);
                        popupMenu.getMenu().findItem(R.id.action_cupos_credito).setVisible(false);
                        popupMenu.getMenu().findItem(R.id.action_totales_mes).setVisible(false);
                        popupMenu.getMenu().findItem(R.id.action_categoria).setVisible(false);
                        popupMenu.getMenu().findItem(R.id.action_recetas).setVisible(false);
                        popupMenu.getMenu().findItem(R.id.action_ficha_medico).setVisible(false);
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
                            // Log.d("menu", "menu --->" + nombreCliente);
                            // Log.d("menu", "menu --->" + idUsuario);

                            int itemId = menuItem.getItemId();

                            if (itemId == R.id.action_historial_pedidos) {
                                Intent iFacturas = new Intent(context, ClientesFacturasNotaDebitosActivity.class);
                                iFacturas.putExtra(Common.ARG_IDCLIENTE, idCliente);
                                iFacturas.putExtra(Common.ARG_NOMBRE_CLIENTE, nombreCliente);
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
                                Log.d("menu", "menu ---> Ficha Medico");
                                Intent iFichaMedico = new Intent(context, MedicoFichaActivity.class);
                                iFichaMedico.putExtra(Common.ARG_IDCLIENTE, idCliente);
                                iFichaMedico.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                context.startActivity(iFichaMedico);
                                return true;
                            } else if (itemId == R.id.action_historial_comentarios) {
                                Log.d("menu", "menu ---> historia comentarios" + idUsuario);
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
                                Log.d("menu", "menu ---> historia seeeeeeee" + categoriaMed);
                                Intent iCategoria = new Intent(context, MedicosCategoriaActivity.class);
                                iCategoria.putExtra(Common.ARG_IDCLIENTE, idCliente);
                                iCategoria.putExtra(Common.ARG_CATEGORIAMED, categoriaMed);
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


        public void bindTo(final Clientes cliente, final OnClienteClickListener listener) {
            String nombreClienteL = cliente.getNombreCliente();

            if (nombreClienteL.length()>= 30) {
                nombreClienteL = nombreClienteL.substring(0, 30);
            }
            String direccionClienteL = "";
            if (cliente.getDireccion() != null) {

                 direccionClienteL = cliente.getDireccion();

                if (direccionClienteL.length() >= 30) {
                    direccionClienteL = direccionClienteL.substring(0, 30);
                }
            }
            categoriaMed = cliente.getClase();
            String valiZ= cliente.getIdCliente().substring(0, 1);
//            nombreCliente = nombreClienteL;
            mNombreView.setText(nombreClienteL);
//            mNombreView.setText(cliente.getNombreCliente());
            mRepresentanteView.setText(cliente.getRepresentante());
            mDireccionView.setText(cliente.getCiudad() + " - " + direccionClienteL + "...");
            mCodigoView.setText(cliente.getIdCliente());
            mCodigoCliente.setText(cliente.getIdCliente());
            mOrigenMenu.setText(cliente.getOrigen());
            mTipoClienteObs.setText(cliente.getTipo());

            if (valiZ.equals("Z") || cliente.getOrigen().equals("MEDICO") ) {
                mTipoClienteObs.setText("");
                if(cliente.getOrigen().equals("MEDICO")) {
                    mTipoClienteObs.setText(cliente.getIdEspecialidades());
                }
            }
            if (cliente.getTipoObserv() == null){

            }else {
                mTipoCliente.setText(cliente.getClaseMedico());
//                switch (cliente.getTipoObserv()) {
//                    case "CLACT":
//                        tipoCliente = "ACTIVO";
//                        break;
//                    case "CONTA":
//                        tipoCliente = "CONTADO";
//                        break;
//                    case "CLPLE":
//                        tipoCliente = "PRELEGAL";
//                        break;
//                    case "CLINC":
//                        tipoCliente = "INACTIVO";
//                        break;
//                }
            }
//            Log.d("menu", "tipoCliente --->tipoCliente"+ tipoCliente);
            if (cliente.getOrigen().equals("MEDICO")) {
                mOrigen.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.d1));
                mRepresentanteView.setVisibility(View.GONE);
//                mTipoCliente.setText("MEDICO" );
            } else {
              //  mCodigoView.setText(cliente.getTipoObserv() + ": " + cliente.getIdCliente());
//                mTipoCliente.setText(tipoCliente );
                mOrigen.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.h1));
                mRepresentanteView.setVisibility(View.VISIBLE);
            }

            // Validar y mostrar icono de revisita
            if (cliente.getRevisita() != null && cliente.getRevisita() == 1) {
                mRevisita.setVisibility(View.VISIBLE);
            } else {
                mRevisita.setVisibility(View.GONE);
            }

            if (valiZ.equals("Z")){
                mOrigen.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.h2));
             }

            if(cliente.getFechaDesdeAuspicio() != null && cliente.getFechaHastaAuspicio() != null){
                Date fechaHoy = new Date();


                if(fechaHoy.after(cliente.getFechaDesdeAuspicio()) && fechaHoy.before(cliente.getFechaHastaAuspicio()) ){
                    if(cliente.getAuspiciado() == true) mOrigen.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_person_gold_24dp));
                }
            }

//            if (cliente.getAuspiciado() != null){
//                if(cliente.getAuspiciado() == true) mOrigen.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_person_gold_24dp));
//            }

            if(cliente.getCumpleAnyos() != null) {
                if (cliente.getCumpleAnyos()) {
                    mOrigen.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_torta_cumplanyo));
                }
            }
            mRepresentanteView.setVisibility(View.GONE);

            if (TextUtils.isEmpty(cliente.getEstadoVisita())) {
                mEstadoVisita.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_add_circle_red_24dp));
//                mEstadoVisita.setVisibility(View.VISIBLE);
                mEstadoVisita.setVisibility(View.GONE);
                mEstadoFilas.setBackgroundColor(Color.parseColor("#FF0000"));
            } else if (cliente.getEstadoVisita().equals("EFECT")) {
                mEstadoVisita.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_circle_primary_24dp));
                //mEstadoVisita.setVisibility(View.VISIBLE);
                mEstadoVisita.setVisibility(View.GONE);
                mEstadoFilas.setBackgroundColor(Color.parseColor("#21d162"));
            } else {
      //          mEstadoVisita.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_timer_black_24dp));
         //       mEstadoVisita.setVisibility(View.VISIBLE);
                //mEstadoFilas.setVisibility(View.GONE); ///00FFFFFF
                mEstadoFilas.setBackgroundColor(Color.parseColor("#ffff00"));
            }



            mPedido.setVisibility(View.GONE);
            if (cliente.getPedido() != null) mPedido.setVisibility(View.VISIBLE);
            mCobro.setVisibility(View.GONE);
            if (cliente.getCobro() != null) mCobro.setVisibility(View.VISIBLE);

            if (cliente.getEstadoSeleccion() != null ){
                mNombreView.setTextColor(Color.parseColor("#a3a3a0"));
                mCodigoView.setTextColor(Color.parseColor("#a3a3a0"));
                mDireccionView.setTextColor(Color.parseColor("#a3a3a0"));
                mRepresentanteView.setTextColor(Color.parseColor("#a3a3a0"));
                mTipoCliente.setTextColor(Color.parseColor("#a3a3a0"));
//                if (cliente.getTipoObserv() != null && cliente.getTipoObserv().equals("CLINC")){
//                    mNombreView.setTextColor(Color.parseColor("#9932CC"));
//                }

            } else {

                mNombreView.setTextColor(Color.parseColor("#0f0f0e"));
             //   mCodigoView.setTextColor(Color.parseColor("#0f0f0e"));
              //  mDireccionView.setTextColor(Color.parseColor("#0f0f0e"));
                //mRepresentanteView.setTextColor(Color.parseColor("#0f0f0e"));
             //   mTipoCliente.setTextColor(Color.parseColor("#0f0f0e"));
                mTipoCliente.setTextColor(Color.GRAY);
                mCodigoView.setTextColor(Color.GRAY);
                mDireccionView.setTextColor(Color.GRAY);
                mRepresentanteView.setTextColor(Color.GRAY);

//                if (cliente.getTipoObserv() != null && cliente.getTipoObserv().equals("CLINC")){
//                    mNombreView.setTextColor(Color.parseColor("#9932CC"));
//                }
            }
//            String seccion = "Seccion: ";
//            seccion += cliente.getSeccion() ==  null ? "":  cliente.getSeccion();
//            seccion += cliente.getSeccion2() ==  null ? "": " " + cliente.getSeccion2();
//            seccion += cliente.getSeccion3() ==  null ? "": " " + cliente.getSeccion3();
//            seccion += cliente.getSeccion4() ==  null ? "": " " + cliente.getSeccion4();
//            mSeccionView.setText(seccion);

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (cliente.getEstadoSeleccion() != null ){
                        mNombreView.setTextColor(Color.parseColor("#0f0f0e"));
                        ///mCodigoView.setTextColor(Color.parseColor("#0f0f0e"));
                       // mDireccionView.setTextColor(Color.parseColor("#0f0f0e"));
                       // mRepresentanteView.setTextColor(Color.parseColor("#0f0f0e"));
                       // mTipoCliente.setTextColor(Color.parseColor("#0f0f0e"));


                        mTipoCliente.setTextColor(Color.GRAY);
                        mCodigoView.setTextColor(Color.GRAY);
                        mDireccionView.setTextColor(Color.GRAY);
                        mRepresentanteView.setTextColor(Color.GRAY);

                        if (cliente.getTipoObserv() != null && cliente.getTipoObserv().equals("CLINC")){
                            mNombreView.setTextColor(Color.parseColor("#9932CC"));
                        }
                        cliente.setEstadoSeleccion(null);
                        listener.onClienteClick(cliente);
                        ///Toast.makeText(context.getApplicationContext(), "Cliente ya fue seleccionado", Toast.LENGTH_LONG).show();
                        return;
                    } else{
                    cliente.setEstadoSeleccion("1");
                    mNombreView.setTextColor(Color.parseColor("#a3a3a0"));
                    mCodigoView.setTextColor(Color.parseColor("#a3a3a0"));
                    mDireccionView.setTextColor(Color.parseColor("#a3a3a0"));
                    mRepresentanteView.setTextColor(Color.parseColor("#a3a3a0"));
                        mTipoCliente.setTextColor(Color.parseColor("#a3a3a0"));
                    listener.onClienteClick(cliente);
                    }

                }
            });

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mRepresentanteView.getText() + "'";
        }

    }


    public interface OnClienteClickListener {

        void onClienteClick(Clientes cliente);
    }


    @Override
    public Filter getFilter() {
        //mValues = Values;

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString();
                //Log.d("log","--->"  + query);

                String[] splited = query.split("\\s+");


                List<Clientes> filtered = new ArrayList<>();

                if (query.isEmpty()) {
                    filtered = mValues;
                } else {
              //      for (String part : splited){
                        //   Log.d("log","--->"  + part);
                        for (Clientes Busqueda : mValues) {
//                            String[] nombre = Busqueda.getNombreCliente().toLowerCase().split("\\s+");
//                            Log.d("log", "nombre a --->" + nombre.length);
//
//                            //     Log.d("log","nombre--->"  + nombre.toString());
//                            for (int i = 0; i < nombre.length; i++){
//                                if( nombre[i].toLowerCase().contains(query.toLowerCase()));
//                                {
//
//                                    Log.d("log", "Busqueda a --->" + Busqueda);
//                                    filtered.add(Busqueda);
//                                }
//                            }

                            if (


                                    Busqueda.getNombreCliente().toLowerCase().contains(query.toLowerCase())
                                    || Busqueda.getIdCliente().toLowerCase().contains(query.toLowerCase())
                                    || (Busqueda.getRepresentante() != null && Busqueda.getRepresentante().toLowerCase().contains(query.toLowerCase()))
                                    || (Busqueda.getCiudad() != null && Busqueda.getCiudad().toLowerCase().contains(query.toLowerCase()))
                                    || (Busqueda.getEspecialidades() != null && Busqueda.getEspecialidades().toLowerCase().contains(query.toLowerCase()))
                                    || (Busqueda.getIdEspecialidades() != null && Busqueda.getIdEspecialidades().toLowerCase().contains(query.toLowerCase()))
                                    || (Busqueda.getTipoObserv() != null && Busqueda.getTipoObserv().toLowerCase().contains(query.toLowerCase()))
                                    || (Busqueda.getClaseMedico() != null && Busqueda.getClaseMedico().toLowerCase().contains(query.toLowerCase()))
                                    || (Busqueda.getDireccion() != null && Busqueda.getDireccion().toLowerCase().contains(query.toLowerCase())))
                            {
                                filtered.add(Busqueda);
                            }
            //            }

//                    for (String part : splited){
//                        Log.d("log", "part a --->" + part);
//                        Log.d("log", " filtered.size() a --->" +  filtered.size());
//                        for (int i = 0; i < filtered.size(); i++) {
//
//                            if (splited.length > 1 && filtered.get(i).getNombreCliente().contains(part.toLowerCase())) {
//                                filtered.add(filtered.get(i));
//                            }
//                        }
//                    };

                        //}
                    }
                }

                FilterResults results = new FilterResults();
                results.count = filtered.size();
                results.values = filtered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                mValuesFiltrados = (List<Clientes>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void showPopup(View v, int menures) {
        PopupMenu.OnMenuItemClickListener listener = new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if (itemId == R.id.action_historial_pedidos) {
                    Intent iFacturas = new Intent(context, ClientesFacturasActivity.class);
                    iFacturas.putExtra(Common.ARG_IDCLIENTE, "cliente.getOrigen()");
                    iFacturas.putExtra(Common.ARG_NOMBRE_CLIENTE, "nombreCliente");
                    iFacturas.putExtra(Common.ARG_IDUSUARIO, "idUsuario");
                    context.startActivity(iFacturas);
                    return true;
                } else if (itemId == R.id.action_detalle_productos) {
                    Intent iFacturaDetalle = new Intent(context, FacturaDetalleActivity.class);
                    // iFacturaDetalle.putExtra(Common.ARG_IDCLIENTE, idCliente);
                    // iFacturaDetalle.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    // startActivity(iFacturaDetalle);
                    return true;
                } else if (itemId == R.id.action_cupos_credito) {
                    Intent i = new Intent(context, ClientesCupoCreditoActivity.class);
                    // i.putExtra(Common.ARG_IDCLIENTE, idCliente);
                    // i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    // startActivity(i);
                    return true;
                } else if (itemId == R.id.action_totales_mes) {
                    Intent iTotalesXMes = new Intent(context, VentasMensualesClientesActivity.class);
                    // iTotalesXMes.putExtra(Common.ARG_IDCLIENTE, idCliente);
                    // iTotalesXMes.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    // startActivity(iTotalesXMes);
                    return true;
                } else if (itemId == R.id.action_ficha_medico) {
                    // Intent iFichaMedico = new Intent(context, MedicoFichaActivity.class);
                    // iFichaMedico.putExtra(Common.ARG_IDCLIENTE, idCliente);
                    // iFichaMedico.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    // startActivity(iFichaMedico);
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
        };

        popup = new PopupMenu(context.getApplicationContext(), v);
        popup.setOnMenuItemClickListener(listener);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(menures, popup.getMenu());

        // Configurar visibilidad de los elementos del menú
        popup.getMenu().findItem(R.id.action_ficha_medico).setVisible(true);
        popup.getMenu().findItem(R.id.action_recetas).setVisible(true);
        popup.getMenu().findItem(R.id.action_categoria).setVisible(true);
        popup.getMenu().findItem(R.id.action_historial_pedidos).setVisible(true);
        popup.getMenu().findItem(R.id.action_detalle_productos).setVisible(true);
        popup.getMenu().findItem(R.id.action_cupos_credito).setVisible(true);
        popup.getMenu().findItem(R.id.action_totales_mes).setVisible(true);

        popup.show();
    }


}
