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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ClientesAdapter extends RecyclerView.Adapter<ClientesAdapter.ClienteViewHolder>  {
    Context context;
    OnClienteClickListener listener;
    private String filtroTexto = "";
    private String filtroTipoCliente = "";
    private String filtroEstadoVisita = "";
    private boolean filtroRevisita = false;

    private List<Clientes> mValues;
    private List<Clientes> mValuesFiltrados;
    private PopupMenu popup;
    private String idCliente, idUsuario, nombreCliente, mOrigenCliente,categoriaMed;
    public  String tipoCliente;
    private String Seccion, Secciones;
    private List<String> seccionesUsuario;

    public ClientesAdapter(Context context, OnClienteClickListener listener, List<Clientes> values, String idUsuarioTrans, String seccion, String secciones) {

        this.context = context;
        this.listener = listener;
        this.mValues = values;
        this.mValuesFiltrados = values;
        idUsuario = idUsuarioTrans;
        Seccion = seccion;

        if (secciones != null && !secciones.isEmpty()) {
            this.seccionesUsuario = Arrays.asList(secciones.split("\\s*,\\s*"));
        } else {
            this.seccionesUsuario = new ArrayList<>();
        }
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

                    PopupMenu popupMenu = new PopupMenu(context, v);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_pedidos_detalle_cliente, popupMenu.getMenu());
                    String valiZ= mCodigoView.getText().toString().toUpperCase().substring(0, 1);
                    if (valiZ.equals("Z")) {
                        popupMenu.getMenu().findItem(R.id.action_historial_pedidos).setVisible(false);
                        popupMenu.getMenu().findItem(R.id.action_detalle_productos).setVisible(false);
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

            String claseMostrar = null;

// Lista de roles especiales
            List<String> rolesMultiples = Arrays.asList("GRC", "GVP", "GRA", "JVC", "GRS", "JVS");

            if ("MEDICO".equals(cliente.getOrigen()) && "MEDICO".equals(cliente.getTipoObserv())) {

                if (Seccion != null && !Seccion.isEmpty()) {

                    boolean esRolRegional = rolesMultiples.contains(Seccion.toUpperCase());
                    if (esRolRegional && seccionesUsuario != null && !seccionesUsuario.isEmpty()) {
                        for (String seccion : seccionesUsuario) {
                            if (seccion != null && !seccion.isEmpty()) {
                                char primerCaracter = seccion.charAt(0);

                                if (Character.isDigit(primerCaracter)) {
                                    int numero = Character.getNumericValue(primerCaracter);
                                    if (numero >= 1 && numero <= 6 && cliente.getClase() != null && !cliente.getClase().isEmpty()) {
                                        claseMostrar = cliente.getClase();
                                        break;
                                    } else if (numero >= 7 && numero <= 9 && cliente.getClase3() != null && !cliente.getClase3().isEmpty()) {
                                        claseMostrar = cliente.getClase3();
                                        break;
                                    }
                                } else if ((primerCaracter == 'A' || primerCaracter == 'B' || primerCaracter == 'C')
                                        && cliente.getClase4() != null && !cliente.getClase4().isEmpty()) {
                                    claseMostrar = cliente.getClase4();
                                    break;
                                }
                            }
                        }

                    } else {
                        // 🧪 Evaluar una sola sección
                        char primerCaracter = Seccion.charAt(0);

                        if (Character.isDigit(primerCaracter)) {
                            int numero = Character.getNumericValue(primerCaracter);
                            if (numero >= 1 && numero <= 6 && cliente.getClase() != null && !cliente.getClase().isEmpty()) {
                                claseMostrar = cliente.getClase();
                            } else if (numero >= 7 && numero <= 9 && cliente.getClase3() != null && !cliente.getClase3().isEmpty()) {
                                claseMostrar = cliente.getClase3();
                            }
                        } else if ((primerCaracter == 'A' || primerCaracter == 'B' || primerCaracter == 'C')
                                && cliente.getClase4() != null && !cliente.getClase4().isEmpty()) {
                            claseMostrar = cliente.getClase4();
                        }
                    }
                }

                if (claseMostrar != null && !claseMostrar.isEmpty()) {
                    mTipoCliente.setText("Médico " + claseMostrar);
                } else {
                    mTipoCliente.setText(("Médico PC"));
                }

            } else {
                if (cliente.getClaseMedico() != null && !cliente.getClaseMedico().isEmpty()) {
                    mTipoCliente.setText((cliente.getClaseMedico()));
                } else {
                    mTipoCliente.setText("");
                }
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
            boolean esRevisita = false;

            if (cliente.getRevisita() != null && cliente.getRevisita() == 1 && Seccion.equals(cliente.getSeccion2())) {
                esRevisita = true;
            } else if (cliente.getRevisita3() != null && cliente.getRevisita3() == 1 && Seccion.equals(cliente.getSeccion3())) {
                esRevisita = true;
            } else if (cliente.getRevisita4() != null && cliente.getRevisita4() == 1 && Seccion.equals(cliente.getSeccion4())) {
                esRevisita = true;
            }

            mRevisita.setVisibility(esRevisita ? View.VISIBLE : View.GONE);

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
                mEstadoFilas.setBackgroundColor(Color.parseColor("#FF0000")); // Rojo
            } else if (cliente.getEstadoVisita().equals("EFECT")) {
                mEstadoFilas.setBackgroundColor(Color.parseColor("#21d162")); // Verde
            } else if (cliente.getEstadoVisita().equals("PEFECT")) {
                mEstadoFilas.setBackgroundColor(Color.parseColor("#FFA500")); // Naranja
            } else {
                mEstadoFilas.setBackgroundColor(Color.parseColor("#ffff")); // Amarillo
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

    public void filtrarPorTexto(String texto) {
        filtroTexto = texto.toLowerCase().trim();

        filtroRevisita = filtroTexto.equals("x2");

        aplicarFiltros(); // Aplicar los filtros nuevamente
    }
    public void setFiltroTipoCliente(String tipoCliente) {
        this.filtroTipoCliente = tipoCliente;
        aplicarFiltros(); // Aplicar los filtros nuevamente
    }

    public void setFiltroEstadoVisita(String estadoVisita) {
        this.filtroEstadoVisita = estadoVisita;
        aplicarFiltros(); // Aplicar los filtros nuevamente
    }
    private void aplicarFiltros() {
        List<Clientes> filteredList = new ArrayList<>();

        for (Clientes cliente : mValues) {

            String claseMostrar = determinarClaseMostrar(cliente);

            // Caso especial para clientes con clase A y revisita 1
            boolean esClaseAConRevisita = "A".equals(claseMostrar) &&
                    cliente.getRevisita() != null &&
                    cliente.getRevisita() == 1;


            boolean coincideTexto = filtroTexto.isEmpty() || cliente.getNombreCliente().toLowerCase().contains(filtroTexto)
                    || cliente.getIdCliente().toLowerCase().contains(filtroTexto)
                    || (cliente.getRepresentante() != null && cliente.getRepresentante().toLowerCase().contains(filtroTexto))
                    || (cliente.getCiudad() != null && cliente.getCiudad().toLowerCase().contains(filtroTexto))
                    || (cliente.getDireccion() != null && cliente.getDireccion().toLowerCase().contains(filtroTexto))
                    || esClaseAConRevisita;

            boolean coincideTipoCliente = filtroTipoCliente.isEmpty() || filtroTipoCliente.equals("Todos")
                    || (filtroTipoCliente.equals("Clientes Z") && cliente.getIdCliente().toUpperCase().startsWith("Z"))
                    || (filtroTipoCliente.equals("Médicos") && "MEDICO".equals(cliente.getOrigen()))
                    || (filtroTipoCliente.equals("Clientes") && "FARMA".equals(cliente.getOrigen()) && !cliente.getIdCliente().toUpperCase().startsWith("Z"));

            boolean coincideEstadoVisita = filtroEstadoVisita.isEmpty() || filtroEstadoVisita.equals("Todos")
                    || (filtroEstadoVisita.equals("Visitados") &&
                    (cliente.getEstadoVisita() != null &&
                            (cliente.getEstadoVisita().equals("EFECT") || cliente.getEstadoVisita().equals("PEFECT"))))
                    || (filtroEstadoVisita.equals("No Visitados") &&
                    (cliente.getEstadoVisita() == null || cliente.getEstadoVisita().isEmpty() ||
                            cliente.getEstadoVisita().equals("PLANI") || cliente.getEstadoVisita().equals("NOEFE")));


            if (coincideTexto && coincideTipoCliente && coincideEstadoVisita) {
                filteredList.add(cliente);
            }
        }

        mValuesFiltrados = filteredList;
        notifyDataSetChanged();
    }

    private String determinarClaseMostrar(Clientes cliente) {
        String claseMostrar = null;

        if (cliente == null) {
            Log.w("ClaseMostrar", "Cliente es null");
            return null;
        }

        String tipoObserv = cliente.getTipoObserv();
        String origen = cliente.getOrigen();

        if ("MEDICO".equals(tipoObserv) && "MEDICO".equals(origen)) {
            if (seccionesUsuario != null && !seccionesUsuario.isEmpty()) {
                for (String seccion : seccionesUsuario) {
                    if (seccion != null && !seccion.isEmpty()) {
                        char primerCaracter = seccion.charAt(0);

                        if (Character.isDigit(primerCaracter)) {
                            int numero = Character.getNumericValue(primerCaracter);
                            if (numero >= 1 && numero <= 6 && cliente.getClase() != null) {
                                claseMostrar = cliente.getClase(); // F1 y F2
                                break;
                            } else if (numero >= 7 && numero <= 9 && cliente.getClase3() != null) {
                                claseMostrar = cliente.getClase3(); // F3
                                break;
                            }
                        } else if ((primerCaracter == 'A' || primerCaracter == 'B' || primerCaracter == 'C')
                                && cliente.getClase4() != null) {
                            claseMostrar = cliente.getClase4(); // F4
                            break;
                        }
                    }
                }
            } else {
                Log.w("ClaseMostrar", "seccionesUsuario es null o vacía");
            }
        }

        return claseMostrar;
    }



}


