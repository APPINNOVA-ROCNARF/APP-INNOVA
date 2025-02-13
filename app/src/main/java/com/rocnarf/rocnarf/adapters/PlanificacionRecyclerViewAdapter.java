package com.rocnarf.rocnarf.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Locale;

import com.rocnarf.rocnarf.ClientesCupoCreditoActivity;
import com.rocnarf.rocnarf.ClientesFacturasActivity;
import com.rocnarf.rocnarf.ClientesFacturasNotaDebitosActivity;
import com.rocnarf.rocnarf.FacturaDetalleActivity;
import com.rocnarf.rocnarf.HistorialComentariosActivity;
import com.rocnarf.rocnarf.HistorialVisitasActivity;
import com.rocnarf.rocnarf.MedicoFichaActivity;
import com.rocnarf.rocnarf.MedicosCategoriaActivity;
import com.rocnarf.rocnarf.PlanificacionFragment.OnListFragmentInteractionListener;
import com.rocnarf.rocnarf.R;
import com.rocnarf.rocnarf.RecetasXActivity;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.VentasMensualesClientesActivity;
import com.rocnarf.rocnarf.models.Clientes;
import com.rocnarf.rocnarf.models.VisitaClientes;
import com.rocnarf.rocnarf.models.VisitaClientesFecha;
import com.rocnarf.rocnarf.models.VisitaClientesList;
import com.rocnarf.rocnarf.repository.ClientesRepository;
import com.rocnarf.rocnarf.viewmodel.ClienteDetalleViewModel;

import java.text.SimpleDateFormat;
import java.util.List;


public class PlanificacionRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<VisitaClientesList> mValues;
    private final OnListFragmentInteractionListener mListener;
    Context context;
    private String idCliente, idUsuario, CodigoAsesor,Seccion;
    private PopupMenu popup;
    public  String tipoCliente;
    private ClienteDetalleViewModel clienteDetalleViewModel;
    public  boolean auspicioDoc = false;
    public  boolean cumpleAnyo = false;
    private boolean expadir;
    private ClientesRepository clientesRepository;
    SwipeRefreshLayout swipeRefreshLayout;
    public PlanificacionRecyclerViewAdapter(Context context, OnListFragmentInteractionListener listener) {
        this.context = context;
        mListener = listener;
    }



    public void setItems(List<VisitaClientesList> items) {
        this.mValues = items;

        // Obtener la fecha actual
        Date fechaActual = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", new Locale("es", "ES"));
        String fechaActualStr = dateFormat.format(fechaActual);

        // Buscar la fecha actual en la lista y marcarla como expandida
        for (VisitaClientesList item : mValues) {
            if (item instanceof VisitaClientesFecha) {
                VisitaClientesFecha fechaItem = (VisitaClientesFecha) item;
                String fechaItemStr = dateFormat.format(fechaItem.getFecha());

                if (fechaItemStr.equals(fechaActualStr)) {
                    fechaItem.setExpanded(true); // Marcar como expandida
                }
            }
        }

        notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VisitaClientesList.TYPE_FECHA) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_planificacion_fecha_row, parent, false);
            return new FechaViewHolder(view);
        }else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_planificacion_contenido_row, parent, false);
            return new ContenidoViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        int holderType = this.getItemViewType(position);

        if (holderType == VisitaClientesList.TYPE_FECHA) {
            FechaViewHolder fechaHolder = (FechaViewHolder) holder;
            VisitaClientesFecha planificacionFecha = (VisitaClientesFecha) mValues.get(position);

            // Crear un SimpleDateFormat con el idioma español
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", new Locale("es", "ES"));
            fechaHolder.mFechaView.setText(dateFormat.format(planificacionFecha.getFecha()));

            fechaHolder.mFlecha.setRotation(planificacionFecha.isExpanded() ? 180 : 0);


            // Configurar el clic para alternar la expansión
            fechaHolder.mView.setOnClickListener(v -> {
                // Cambiar el estado de expansión
                planificacionFecha.setExpanded(!planificacionFecha.isExpanded());

                // Notificar al adaptador que los datos han cambiado
                notifyDataSetChanged();
            });
        }else {

            VisitaClientes planificacionItem = (VisitaClientes)mValues.get(position);

            /*Log.d("VisitaClientes", "----- Datos de VisitaClientes -----");
            for (Field field : planificacionItem.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    Log.d("VisitaClientes", field.getName() + " = " + field.get(planificacionItem));
                } catch (IllegalAccessException e) {
                    Log.e("VisitaClientes", "Error al acceder al campo: " + field.getName(), e);
                }
            }
             */
            ContenidoViewHolder contenidoHolder = (ContenidoViewHolder)holder;
            contenidoHolder.mItem = planificacionItem;

            // Obtener la fecha asociada a este contenido
            VisitaClientesFecha fechaAsociada = obtenerFechaAsociada(position);

            // Mostrar u ocultar el contenido según el estado de expansión de la fecha
            if (fechaAsociada != null && !fechaAsociada.isExpanded()) {
                contenidoHolder.ocultarContenido(); // Ocultar contenido y eliminar espacio
            } else {
                contenidoHolder.mostrarContenido(); // Mostrar contenido
            }

            clientesRepository = new ClientesRepository(context, planificacionItem.getCodigoAsesor());
            LiveData<Clientes> clientesCumple = clientesRepository.getClientesIdLocal(planificacionItem.getCodigoCliente());


            // Validar y mostrar icono de revisita
            if (clientesCumple.getValue() != null) {
                Integer revisita = clientesCumple.getValue().getRevisita();
                if (revisita != null && revisita == 1) {
                    contenidoHolder.mRevisitaView.setVisibility(View.VISIBLE);
                } else {
                    contenidoHolder.mRevisitaView.setVisibility(View.GONE);
                }
            } else {
                contenidoHolder.mRevisitaView.setVisibility(View.GONE);
            }

            if(clientesCumple.getValue() != null) {
                if (clientesCumple.getValue().getCumpleAnyos() != null) {
                    if (clientesCumple.getValue().getCumpleAnyos()) {
                        cumpleAnyo = true;
                    }
                } else cumpleAnyo = false;
            }

            if (planificacionItem.getClientes() != null) {
                if (planificacionItem.getClientes().getAuspiciado() != null) {
                    if (planificacionItem.getClientes().getAuspiciado() == true) auspicioDoc = true;
                } else auspicioDoc = false;

            }
            contenidoHolder.mNombreView.setText(planificacionItem.getNombreCliente());
            contenidoHolder.mCodView.setText(planificacionItem.getCodigoCliente());


            SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
            String time = localDateFormat.format(planificacionItem.getFechaVisitaPlanificada());
            String Ciudad ="";

            contenidoHolder.mHoraPlanificada.setText(time);
            if(clientesCumple.getValue() != null) {
                if (clientesCumple.getValue().getCiudad() != null) {
                    Ciudad = clientesCumple.getValue().getCiudad();
                }
            }
            contenidoHolder.mDireccionView.setText(Ciudad + " - " + planificacionItem.getDireccion() );
            contenidoHolder.mEstasoView.setVisibility(View.GONE);
            if ( planificacionItem.getEstado().equals(VisitaClientes.PLANIFICADO)){
                contenidoHolder.mEstadoFilas.setBackgroundColor( Color.parseColor("#ffff00") );
            }else if (planificacionItem.getEstado().equals(VisitaClientes.PEFECTIVA)) {
                contenidoHolder.mEstadoFilas.setBackgroundColor( Color.parseColor("#FFA500") );
            }else{
                contenidoHolder.mEstadoFilas.setBackgroundColor( Color.parseColor("#21d162") );
            }

            String motivoVisita = "";
            motivoVisita += planificacionItem.isVisitaXPedido() ? "Pedido " : "";
            motivoVisita += planificacionItem.isVisitaPromocional() ? (motivoVisita.isEmpty() ? "Visita Promocional" : " - Visita Promocional") : "";
            motivoVisita += planificacionItem.isVisitaXCobro() ? (motivoVisita.isEmpty() ? "Cobro" : " - Cobro")  : "";
            motivoVisita += planificacionItem.isVisitaXSiembraProducto() ? (motivoVisita.isEmpty() ? "Siembra de Producto" : " - Siembra de Producto") : "";
            motivoVisita += planificacionItem.isVisitaPuntoContacto() ? (motivoVisita.isEmpty() ? "Punto de Contacto" : " - Punto de Contacto") : "";
            motivoVisita += planificacionItem.isVisitaXCajasVacias() ? (motivoVisita.isEmpty() ? "Cajas Vacias" : " - Cajas Vacia")  : "";
            motivoVisita += planificacionItem.isVisitaXEntregaPremios() ? (motivoVisita.isEmpty() ? "Entrega de Premios" : " - Entrega de Premios")  : "";
            motivoVisita += planificacionItem.isVisitaXDevolucion() ? (motivoVisita.isEmpty() ? "Devolucion" : " - Devolucion")  : "";

            contenidoHolder.mMotivoVisitaView.setText(motivoVisita);

            String codigoCliente = contenidoHolder.mCodView.getText().toString();
            boolean iniciaConZ = codigoCliente.startsWith("Z");

            actualizarVisibilidadIconos(contenidoHolder,cumpleAnyo,auspicioDoc,iniciaConZ,codigoCliente,context);

        /* Lógica para manejar la visibilidad de los íconos
            if (cumpleAnyo) {
                // Si es el cumpleaños del cliente
                contenidoHolder.mOrigen.setVisibility(View.GONE);
                contenidoHolder.mOrigen2.setVisibility(View.GONE);
                contenidoHolder.mOrigen3.setVisibility(View.VISIBLE);
                contenidoHolder.mOrigenZ.setVisibility(View.GONE);
            } else if (auspicioDoc) {
                // Si el cliente está auspiciado
                contenidoHolder.mOrigen.setVisibility(View.VISIBLE);
                contenidoHolder.mOrigen.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_person_gold_24dp));
                contenidoHolder.mOrigen2.setVisibility(View.GONE);
                contenidoHolder.mOrigen3.setVisibility(View.GONE);
                contenidoHolder.mOrigenZ.setVisibility(View.GONE);
            } else if (iniciaConZ) {
                // Si el código del cliente comienza con 'Z'
                contenidoHolder.mOrigenZ.setVisibility(View.VISIBLE);
                contenidoHolder.mOrigen.setVisibility(View.GONE);
                contenidoHolder.mOrigen2.setVisibility(View.GONE);
                contenidoHolder.mOrigen3.setVisibility(View.GONE);
            } else {
                // Lógica existente basada en la longitud del código del cliente
                if (codigoCliente.length() > 6) {
                    contenidoHolder.mOrigen2.setVisibility(View.VISIBLE);
                    contenidoHolder.mOrigen.setVisibility(View.GONE);
                } else {
                    contenidoHolder.mOrigen.setVisibility(View.VISIBLE);
                    contenidoHolder.mOrigen2.setVisibility(View.GONE);
                }
                contenidoHolder.mOrigen3.setVisibility(View.GONE);
                contenidoHolder.mOrigenZ.setVisibility(View.GONE);
            }
*/


            contenidoHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(planificacionItem);
                    }
                }
            });
        }
    }

    private VisitaClientesFecha obtenerFechaAsociada(int position) {
        for (int i = position; i >= 0; i--) {
            if (mValues.get(i) instanceof VisitaClientesFecha) {
                return (VisitaClientesFecha) mValues.get(i);
            }
        }
        return null;
    }

    private void actualizarVisibilidadIconos(ContenidoViewHolder contenidoHolder, boolean cumpleAnyo, boolean auspicioDoc, boolean iniciaConZ, String codigoCliente, Context context) {
        // Ocultar todos los íconos por defecto
        contenidoHolder.mOrigen.setVisibility(View.GONE);
        contenidoHolder.mOrigen2.setVisibility(View.GONE);
        contenidoHolder.mOrigen3.setVisibility(View.GONE);
        contenidoHolder.mOrigenZ.setVisibility(View.GONE);

        if (cumpleAnyo) {
            // Si es el cumpleaños del cliente, solo se muestra mOrigen3
            contenidoHolder.mOrigen3.setVisibility(View.VISIBLE);
        } else if (iniciaConZ) {
            // Si el código del cliente comienza con 'Z', solo se muestra mOrigenZ
            contenidoHolder.mOrigenZ.setVisibility(View.VISIBLE);
        } else {
            // Lógica basada en la longitud del código del cliente
            if (codigoCliente != null && codigoCliente.length() > 6) {
                contenidoHolder.mOrigen2.setVisibility(View.VISIBLE);
            } else {
                contenidoHolder.mOrigen.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mValues!= null)
            return mValues.size();
        else
            return  0;
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous

        return mValues.get(position).getType();
        //return 1;
    }


    public class FechaViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public final TextView mFechaView;
        public final ImageView mFlecha;
        public boolean isExpanded = false;
        public VisitaClientesFecha mItem;

        public FechaViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;

            mFechaView = (TextView)itemView.findViewById(R.id.tv_fecha_fragment_planificacion_fecha_row);
            mFlecha = (ImageView) itemView.findViewById(R.id.iv_down_pregunta_planificacion);


            mView.setOnClickListener(v -> {
                float currentRotation = mFlecha.getRotation();
                if (currentRotation == 0) {
                    mFlecha.setRotation(180); // Rota hacia abajo
                } else {
                    mFlecha.setRotation(0); // Rota hacia arriba
                }

            });

        }

    }

    public class ContenidoViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mHoraPlanificada;
        public final TextView mNombreView;
        public final TextView mCodView;
        //public final TextView mRepresentanteView;
        public final TextView mDireccionView;
        public final ImageView mEstasoView;
        public final ImageView mRevisitaView;
        public final ImageView mEstadoFilas;
        public final TextView mMotivoVisitaView, mtipoCliente;
        public final ImageView mOrigen, mOrigen2, mOrigen3, mOrigenZ;
        public VisitaClientes mItem;
        public final ConstraintLayout mExpandir;
        public final ImageView mFlecha;
        public final TextView mTipoClienteObs;
        public ContenidoViewHolder(View view) {
            super(view);
            mView = view;

            mHoraPlanificada = (TextView) view.findViewById(R.id.tv_hora_visita_fragment_planifiacion_contenido_row);
            mNombreView = (TextView) view.findViewById(R.id.tv_nombre_cliente_fragment_planifiacion_contenido_row);
            mCodView = (TextView) view.findViewById(R.id.tv_codigo_fragment_planifiacion_contenido_row);
            mtipoCliente =  (TextView) view.findViewById(R.id.tv_tipo_cliente);
            mDireccionView = (TextView) view.findViewById(R.id.tv_direccion_fragment_planifiacion_contenido_row);
            mMotivoVisitaView  = (TextView) view.findViewById(R.id.tv_motivo_visita_fragment_planificacion_contenido_row);
            mEstasoView = (ImageView) view.findViewById(R.id.iv_estado_fragment_planifiacion_contenido_row);
            mEstadoFilas = (ImageView) view.findViewById(R.id.divider3);
            mRevisitaView = (ImageView) view.findViewById(R.id.iv_cliente_revisita);
            mTipoClienteObs =(TextView) view.findViewById(R.id.tv_tipo_cliente_obs);
            mFlecha = (ImageView) view.findViewById(R.id.iv_down_pregunta_planificacion);


            mExpandir = (ConstraintLayout) view.findViewById(R.id.expandir_planificacion);


            mOrigen = (ImageView) view.findViewById(R.id.iv_origen_row_resultado_cliente);
            mOrigenZ = (ImageView) view.findViewById(R.id.iv_origen_row_resultado_cliente_z);
            mOrigen2 = (ImageView) view.findViewById(R.id.iv_origen2_row_resultado_cliente);
            mOrigen3 = (ImageView) view.findViewById(R.id.iv_origen2_row_resultado_cliente_torta);
            ImageButton mMenuPedidosView = (ImageButton) view.findViewById(R.id.ib_pedido_fragment_cliente_detalle_menu);




            mMenuPedidosView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PopupMenu popupMenu = new PopupMenu(context, v);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_pedidos_detalle_cliente, popupMenu.getMenu());
                    String valiZ= mCodView.getText().toString().toUpperCase().substring(0, 1);
                    if (valiZ.equals("Z")) {
                            popupMenu.getMenu().findItem(R.id.action_totales_mes).setVisible(false);
                            popupMenu.getMenu().findItem(R.id.action_cupos_credito).setVisible(false);
                            popupMenu.getMenu().findItem(R.id.action_historial_pedidos).setVisible(false);
                            popupMenu.getMenu().findItem(R.id.action_detalle_productos).setVisible(false);
                            popupMenu.getMenu().findItem(R.id.action_categoria).setVisible(false);
                            popupMenu.getMenu().findItem(R.id.action_recetas).setVisible(false);
                    }else {
                        if (mCodView.length() <= 6)
                            popupMenu.getMenu().findItem(R.id.action_ficha_medico).setVisible(false);
                        if (mCodView.length() <= 6)
                           popupMenu.getMenu().findItem(R.id.action_recetas).setVisible(false);
                        if (mCodView.length() <= 6)
                            popupMenu.getMenu().findItem(R.id.action_categoria).setVisible(false);
                        if (mCodView.length() > 6)
                            popupMenu.getMenu().findItem(R.id.action_historial_pedidos).setVisible(false);
                        if (mCodView.length() > 6)
                            popupMenu.getMenu().findItem(R.id.action_detalle_productos).setVisible(false);
                        if (mCodView.length() > 6)
                            popupMenu.getMenu().findItem(R.id.action_cupos_credito).setVisible(false);
                        if (mCodView.length() > 6)
                            popupMenu.getMenu().findItem(R.id.action_totales_mes).setVisible(false);
                    }


                    popupMenu.show();

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            int itemId = menuItem.getItemId();

                            if (itemId == R.id.action_historial_pedidos) {
                                Intent iFacturas = new Intent(context, ClientesFacturasNotaDebitosActivity.class);
                                iFacturas.putExtra(Common.ARG_IDCLIENTE, mCodView.getText());
                                iFacturas.putExtra(Common.ARG_NOMBRE_CLIENTE, mNombreView.getText());
                                iFacturas.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                context.startActivity(iFacturas);
                                return true;
                            } else if (itemId == R.id.action_detalle_productos) {
                                Intent iFacturaDetalle = new Intent(context, FacturaDetalleActivity.class);
                                iFacturaDetalle.putExtra(Common.ARG_IDCLIENTE, mCodView.getText());
                                iFacturaDetalle.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                context.startActivity(iFacturaDetalle);
                                return true;
                            } else if (itemId == R.id.action_cupos_credito) {
                                Intent i = new Intent(context, ClientesCupoCreditoActivity.class);
                                i.putExtra(Common.ARG_IDCLIENTE, mCodView.getText());
                                i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                context.startActivity(i);
                                return true;
                            } else if (itemId == R.id.action_totales_mes) {
                                Intent iTotalesXMes = new Intent(context, VentasMensualesClientesActivity.class);
                                iTotalesXMes.putExtra(Common.ARG_IDCLIENTE, mCodView.getText());
                                iTotalesXMes.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                context.startActivity(iTotalesXMes);
                                return true;
                            } else if (itemId == R.id.action_ficha_medico) {
                                Intent iFichaMedico = new Intent(context, MedicoFichaActivity.class);
                                iFichaMedico.putExtra(Common.ARG_IDCLIENTE, mCodView.getText());
                                iFichaMedico.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                context.startActivity(iFichaMedico);
                                return true;
                            } else if (itemId == R.id.action_historial_comentarios) {
                                Intent iComentarios = new Intent(context, HistorialComentariosActivity.class);
                                iComentarios.putExtra(Common.ARG_IDCLIENTE, mCodView.getText());
                                iComentarios.putExtra(Common.ARG_IDUSUARIO, CodigoAsesor);
                                iComentarios.putExtra(Common.ARG_SECCIOM, Seccion);
                                Log.d("idUsuario", "idUsuario" + idUsuario);
                                context.startActivity(iComentarios);
                                return true;
                            } else if (itemId == R.id.action_historial_visitas) {
                                Intent iVisitas = new Intent(context, HistorialVisitasActivity.class);
                                iVisitas.putExtra(Common.ARG_IDCLIENTE, mCodView.getText());
                                iVisitas.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                context.startActivity(iVisitas);
                                return true;
                            } else if (itemId == R.id.action_recetas) {
                                Intent iRecetas = new Intent(context, RecetasXActivity.class);
                                iRecetas.putExtra(Common.ARG_IDCLIENTE, mCodView.getText());
                                iRecetas.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                                context.startActivity(iRecetas);
                                return true;
                            } else if (itemId == R.id.action_categoria) {
                                Intent iCategoria = new Intent(context, MedicosCategoriaActivity.class);
                                iCategoria.putExtra(Common.ARG_IDCLIENTE, mCodView.getText());
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


        public void ocultarContenido() {
            // Cambiar el alto de la vista a 0 cuando esté oculta
            ViewGroup.LayoutParams params = mView.getLayoutParams();
            params.height = 0;
            mView.setLayoutParams(params);
        }

        public void mostrarContenido() {
            // Restaurar el alto de la vista cuando esté visible
            ViewGroup.LayoutParams params = mView.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            mView.setLayoutParams(params);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNombreView.getText() + "'";
        }
    }
}
