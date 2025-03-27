package com.rocnarf.rocnarf.adapters;

import java.util.List;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

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
import com.rocnarf.rocnarf.models.PanelClientes;

public class PanelClienteAdapter  extends RecyclerView.Adapter<PanelClienteAdapter.PanelClienteViewHolder>  {
    Context context;
    private List<PanelClientes> listconsulta_panel_objeto;
    private LayoutInflater layoutInflater;
    OnPanelClientesClickListener clickListener;
    OnPanelClientesLongClickListener longClickListener;
    private String Seccion;


    public PanelClienteAdapter(Context context, OnPanelClientesClickListener clickListener,
                               OnPanelClientesLongClickListener longClickListener,
                               List<PanelClientes> listconsulta_formulario_objeto, String seccion) {
        this.listconsulta_panel_objeto = listconsulta_formulario_objeto;
        this.context = context;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
        this.Seccion = seccion;
    }



    @NonNull
    @Override
    public PanelClienteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.row_panel_cliente, viewGroup, false);
        return new PanelClienteViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PanelClienteViewHolder panelClienteViewHolder, int i) {
        PanelClientes cliente = listconsulta_panel_objeto.get(i);
        if (cliente!=null){
            panelClienteViewHolder.bindTo(cliente, this.clickListener, this.longClickListener);
        } else {

        }

    }

    public long getItemId(int indice) {
        return indice;
    }

    @Override
    public int getItemCount() {
        return listconsulta_panel_objeto.size();
    }




    public interface OnPanelClientesClickListener {
        void onPanelClientesClick(PanelClientes clientes);
    }

    public interface OnPanelClientesLongClickListener{
        void OnPanelClientesLongClick(PanelClientes clientes);
    }



    public class PanelClienteViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        TextView lblNombreCliente,mTipoCliente,mTipoClienteObs;
        TextView lblCodigoCliente;
        TextView lblRepresentante;
        TextView lblSector;
        ImageView imgPersona;
        ImageView imgFarma,mEstadoFilas,imgCumple, imgFarmaZ, imgRevisita;

        ImageButton btnMenuPopup; // Botón para mostrar el menú popup



        public PanelClientes mItem;

        public PanelClienteViewHolder(View view) {
            super(view);
            mView = view;
            lblNombreCliente = (TextView) view.findViewById(R.id.tv_nombre_cliente_row_resultado_cliente);
            lblCodigoCliente = (TextView) view.findViewById(R.id.tv_codigo_row_resultado_cliente);
            lblRepresentante = (TextView) view.findViewById(R.id.tv_representante_row_resultado_cliente);
            mTipoCliente = (TextView) view.findViewById(R.id.tv_codigo_row_tipo_cliente_planificar);
            mTipoClienteObs = (TextView) view.findViewById(R.id.tv_codigo_row_tipo_cliente_obs_planificar);
            lblSector = (TextView) view.findViewById(R.id.tv_direccion_row_resultado_cliente);
            imgPersona = (ImageView) view.findViewById(R.id.iv_origen_row_resultado_cliente);
            imgFarmaZ = (ImageView) view.findViewById(R.id.iv_origen_row_resultado_cliente_z);
            imgFarma = (ImageView) view.findViewById(R.id.iv_origen2_row_resultado_cliente);
            imgCumple = (ImageView) view.findViewById(R.id.iv_origen2_row_resultado_panel_cli);
            mEstadoFilas = (ImageView) view.findViewById(R.id.divider3_panel_cli);
            imgRevisita = (ImageView) view.findViewById(R.id.iv_cliente_revisita);
            btnMenuPopup = view.findViewById(R.id.id_pedido_fragment_cliente_detalle_menu); // Agregar un botón al layout

        }

        public void bindTo(final PanelClientes panelClientes, final OnPanelClientesClickListener clickListener, final OnPanelClientesLongClickListener longClickListener){
            String nombreClienteL = panelClientes.getNombreCliente();
            if (nombreClienteL.length()>= 30) {
                nombreClienteL = nombreClienteL.substring(0, 30);
            }

            String direccionClienteL = "";
            if (panelClientes.getDireccion() != null) {

                direccionClienteL = panelClientes.getDireccion();

                if (direccionClienteL.length() >= 30) {
                    direccionClienteL = direccionClienteL.substring(0, 30);
                }
            }

            if (TextUtils.isEmpty(panelClientes.getEstadoVisita())) {
                mEstadoFilas.setBackgroundColor(Color.parseColor("#FF0000"));
            } else if (panelClientes.getEstadoVisita().equals("EFECT")) {
                mEstadoFilas.setBackgroundColor(Color.parseColor("#21d162"));
            } else if (panelClientes.getEstadoVisita().equals("PEFECT")) {
                mEstadoFilas.setBackgroundColor(Color.parseColor("#FFA500")); // Naranja
            } else {
                mEstadoFilas.setBackgroundColor(Color.parseColor("#ffff00")); // Amarillo
            }



            lblNombreCliente.setText(nombreClienteL);
//            lblNombreCliente.setText(panelClientes.getNombreCliente());
            lblCodigoCliente.setText(panelClientes.getIdCliente());
            lblRepresentante.setText(panelClientes.getRepresentante());
            lblSector.setText(panelClientes.getCiudad() + " - " + direccionClienteL);
            mTipoCliente.setText(panelClientes.getClaseMedico());
            mTipoClienteObs.setText(panelClientes.getTipo());
            String codigoCliente = panelClientes.getIdCliente();
            boolean iniciaConZ = codigoCliente.startsWith("Z");

// Inicializar visibilidad (todas ocultas por defecto)
            imgPersona.setVisibility(View.GONE);
            imgFarma.setVisibility(View.GONE);
            imgFarmaZ.setVisibility(View.GONE);
            imgCumple.setVisibility(View.GONE);

            String claseMostrar = null;


            // Validar y mostrar icono de revisita
            if (panelClientes.getRevisita() != null && panelClientes.getRevisita() == 1 && panelClientes.getClaseMedico().equals("Médico A")) {
                imgRevisita.setVisibility(View.VISIBLE);
            } else {
                imgRevisita.setVisibility(View.GONE);
            }


// Si el cliente está de cumpleaños, mostrar solo imgCumple
            if (Boolean.TRUE.equals(panelClientes.getCumpleAnyos())) {
                imgCumple.setVisibility(View.VISIBLE);
            } else if (iniciaConZ) {
                // Si el ID del cliente comienza con 'Z', usar imgFarmaZ
                imgFarmaZ.setVisibility(View.VISIBLE);
            } else {
                // Lógica existente para mostrar imgPersona o imgFarma
                if (codigoCliente.length() == 6) {
                    imgPersona.setVisibility(View.VISIBLE);
                } else if (codigoCliente.length() == 8) {
                    imgFarma.setVisibility(View.VISIBLE);
                }
            }

// Configurar mTipoClienteObs (sin afectar la lógica anterior)
            String valiZ = codigoCliente.substring(0, 1);
            if (valiZ.equals("Z") || panelClientes.getOrigen().equals("MEDICO")) {
                mTipoClienteObs.setText("");
                if (panelClientes.getOrigen().equals("MEDICO")) {
                    mTipoClienteObs.setText(panelClientes.getIdEspecialidades());
                }
            }

            btnMenuPopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context, view);
                    MenuInflater inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.menu_pedidos_detalle_cliente, popupMenu.getMenu());
                    Log.d("Tipo de Cliente:", panelClientes.getOrigen());

                    // Mostrar/ocultar opciones según el tipo de cliente
                    if (panelClientes.getOrigen().equals("FARMA")) {
                        popupMenu.getMenu().findItem(R.id.action_ficha_medico).setVisible(false);
                        popupMenu.getMenu().findItem(R.id.action_recetas).setVisible(false);
                        popupMenu.getMenu().findItem(R.id.action_categoria).setVisible(false);
                    } else {
                        popupMenu.getMenu().findItem(R.id.action_historial_pedidos).setVisible(false);
                        popupMenu.getMenu().findItem(R.id.action_detalle_productos).setVisible(false);
                        popupMenu.getMenu().findItem(R.id.action_totales_mes).setVisible(false);
                    }

                    String valiZ= panelClientes.getIdCliente().toUpperCase().substring(0, 1);
                    if (valiZ.equals("Z")) {
                        popupMenu.getMenu().findItem(R.id.action_totales_mes).setVisible(false);
                        popupMenu.getMenu().findItem(R.id.action_historial_pedidos).setVisible(false);
                        popupMenu.getMenu().findItem(R.id.action_detalle_productos).setVisible(false);
                        popupMenu.getMenu().findItem(R.id.action_categoria).setVisible(false);
                        popupMenu.getMenu().findItem(R.id.action_recetas).setVisible(false);
                    }else {
                        if (panelClientes.getIdCliente().length() <= 6)
                            popupMenu.getMenu().findItem(R.id.action_ficha_medico).setVisible(false);
                        if (panelClientes.getIdCliente().length() <= 6)
                            popupMenu.getMenu().findItem(R.id.action_recetas).setVisible(false);
                        if (panelClientes.getIdCliente().length() <= 6)
                            popupMenu.getMenu().findItem(R.id.action_categoria).setVisible(false);
                        if (panelClientes.getIdCliente().length() > 6)
                            popupMenu.getMenu().findItem(R.id.action_historial_pedidos).setVisible(false);
                        if (panelClientes.getIdCliente().length() > 6)
                            popupMenu.getMenu().findItem(R.id.action_detalle_productos).setVisible(false);
                        if (panelClientes.getIdCliente().length() > 6)
                            popupMenu.getMenu().findItem(R.id.action_totales_mes).setVisible(false);
                    }

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            int itemId = menuItem.getItemId();
                            Intent intent = null;

                            if (itemId == R.id.action_ficha_medico) {
                                intent = new Intent(context, MedicoFichaActivity.class);
                            } else if (itemId == R.id.action_historial_pedidos) {
                                intent = new Intent(context, ClientesFacturasNotaDebitosActivity.class);
                            } else if (itemId == R.id.action_detalle_productos) {
                                intent = new Intent(context, FacturaDetalleActivity.class);
                            } else if (itemId == R.id.action_totales_mes) {
                                intent = new Intent(context, VentasMensualesClientesActivity.class);
                            } else if (itemId == R.id.action_historial_comentarios) {
                                intent = new Intent(context, HistorialComentariosActivity.class);
                            } else if (itemId == R.id.action_historial_visitas) {
                                intent = new Intent(context, HistorialVisitasActivity.class);
                            } else if (itemId == R.id.action_recetas) {
                                intent = new Intent(context, RecetasXActivity.class);
                            } else if (itemId == R.id.action_categoria) {
                                intent = new Intent(context, MedicosCategoriaActivity.class);
                            }

                            if (intent != null) {
                                intent.putExtra(Common.ARG_IDCLIENTE, panelClientes.getIdCliente());
                                context.startActivity(intent);
                            }
                            return true;
                        }
                    });

                    popupMenu.show();
                }
            });

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onPanelClientesClick(panelClientes);

                }
            });
            mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longClickListener.OnPanelClientesLongClick(panelClientes);
                    return true;
                }
            });
        }


    }

}
