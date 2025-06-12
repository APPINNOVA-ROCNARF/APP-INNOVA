
package com.rocnarf.rocnarf.adapters;

import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.textfield.TextInputEditText;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rocnarf.rocnarf.R;

import com.rocnarf.rocnarf.models.EscalaBonificacion;
import com.rocnarf.rocnarf.models.PedidoDetalle;
import com.rocnarf.rocnarf.models.Producto;
import com.rocnarf.rocnarf.viewmodel.PedidoViewModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductosReciclerViewAdapter extends RecyclerView.Adapter<ProductosReciclerViewAdapter.ViewHolder>
        implements Filterable {
    private final List<Producto> mValues;
    private List<Producto> mValuesFiltrados;
    private AddProductoListener listener;
    private String idCliente;
    private int idLocalPedido;
    private final List<EscalaBonificacion> mEscalas;
    private PedidoViewModel pedidoViewModel;
    private List<PedidoDetalle> pedidoDetalles;

    private Boolean usarPrecioEspecial;
    public ProductosReciclerViewAdapter(List<Producto> mValues, List<EscalaBonificacion> mEscalas, String idCliente, AddProductoListener listener, List<PedidoDetalle> pedidoDetalles, Boolean usarPrecioEspecial){
        this.listener = listener;
        this.mEscalas = mEscalas;
        this.mValues = mValues;
        this.mValuesFiltrados = mValues;
        this.idCliente = idCliente;
        this.pedidoDetalles = pedidoDetalles;
        this.usarPrecioEspecial = usarPrecioEspecial;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_productos, viewGroup, false);
        return new ViewHolder(view, this.listener);



    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        NumberFormat formatoNumero = NumberFormat.getNumberInstance();
        holder.mItem = mValuesFiltrados.get(position);
        holder.mCodigo.setText(mValuesFiltrados.get(position).getIdProducto() + " - " + mValuesFiltrados.get(position).getIdProducto2());
        holder.mTipo.setText("" + mValuesFiltrados.get(position).getTipo());
        double xpvp = mValuesFiltrados.get(position).getPorcentajePvp() == null ? 0 :mValuesFiltrados.get(position).getPorcentajePvp();



        double precio = mValuesFiltrados.get(position).getPrecio();
        double pvp = mValuesFiltrados.get(position).getPvp();
        Double precioEspecial = mValuesFiltrados.get(position).getPrecioEspecial();
        Date FechaVencimiento = mValuesFiltrados.get(position).getFechaVencimiento();


        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("P.V.F.: $ ").append(String.format("%.2f", precio));
        builder.append("  P.V.P.: $ ").append(String.format("%.2f", pvp));

        if (precioEspecial != null) {
            String pveText = "  ESP.: $ " + String.format("%.2f", precioEspecial);
            int start = builder.length();
            builder.append(pveText);
            int end = builder.length();

            builder.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        holder.mPrecio.setText(builder);

        holder.mSaldo.setText("Saldo: " +  formatoNumero.format(mValuesFiltrados.get(position).getSaldo()).toString());

        String escalas = "";
        //Log.d("pro","pro" + mEscalas);
        if (mEscalas != null) {
            for (EscalaBonificacion bonificacion : mEscalas) {
                if (bonificacion.getIdEscala() == mValuesFiltrados.get(position).getIdEscala()) {
                    if (escalas.isEmpty()) {
                        escalas = "" + String.valueOf(bonificacion.getCantidad()) + " + " + String.valueOf(bonificacion.getBonificacion());
                    } else {
                        escalas += " | " + String.valueOf(bonificacion.getCantidad()) + " + " + String.valueOf(bonificacion.getBonificacion());
                    }
                }
            }
        }


        if (usarPrecioEspecial != null && usarPrecioEspecial) {
            holder.mBono.setVisibility(View.GONE);
            holder.mEscalaBonificacion.setVisibility(View.GONE);
            holder.mEscalaBonificacion.setText("");
        } else {
            holder.mBono.setVisibility(View.VISIBLE);
            holder.mEscalaBonificacion.setVisibility(View.VISIBLE);
            holder.mEscalaBonificacion.setText(escalas);
        }

        if (escalas.isEmpty()) holder.mEscalaBonificacion.setVisibility(View.GONE); else holder.mEscalaBonificacion.setVisibility(View.VISIBLE);
        holder.mProducto.setText(mValuesFiltrados.get(position).getNombre());
        final String codigProducto = mValuesFiltrados.get(position).getIdProducto();
        if (this.idCliente != null ){
            holder.mContenedor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Date fechaActual = new Date();

                    boolean layoutVisible = holder.mAgregarLayout.getVisibility() == View.VISIBLE
                            || holder.tvMensajePrecioNoValido.getVisibility() == View.VISIBLE;

                    if (layoutVisible) {
                        // Si alguno de los dos está visible, ocultarlos ambos
                        holder.mAgregarLayout.setVisibility(View.GONE);
                        holder.tvMensajePrecioNoValido.setVisibility(View.GONE);
                    } else {
                        if (FechaVencimiento != null && FechaVencimiento.before(fechaActual)) {
                            // Producto vencido → mostrar solo mensaje
                            holder.tvMensajePrecioNoValido.setVisibility(View.VISIBLE);
                            holder.mAgregarLayout.setVisibility(View.GONE);
                            holder.tvMensajePrecioNoValido.setText("Precio ESP requiere actualización en Sistema FOX\nFavor contactarse con Departamento de ventas");

                        } else {
                            // Producto vigente → mostrar input
                            holder.mAgregarLayout.setVisibility(View.VISIBLE);
                            holder.tvMensajePrecioNoValido.setVisibility(View.GONE);

                            // Cargar cantidad si ya existe en pedido
                            for (int i = 0; i < pedidoDetalles.size(); i++) {
                                if (pedidoDetalles.get(i).getIdProducto().equals(codigProducto)) {
                                    holder.mCantidad.setText(String.valueOf(pedidoDetalles.get(i).getCantidad()));
                                    break;
                                }
                            }
                        }
                    }
                }
            });

        }
        holder.mAgregarLayout.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mValuesFiltrados.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString();

                List<Producto> filtered = new ArrayList<>();
                if (query.isEmpty()) {
                    filtered = mValues;
                } else {
                    for (Producto producto : mValues) {
//                        Log.d("myTag", "producto   " + producto.getTipo());
                        String tipo = (producto.getTipo() ==null ? "": producto.getTipo());
                        String marca = (producto.getMarca() ==null ? "": producto.getMarca());
                        if (producto.getNombre().toLowerCase().contains(query.toLowerCase()) || tipo.toLowerCase().contains(query.toLowerCase())  || marca.toLowerCase().contains(query.toLowerCase())) {
                            filtered.add(producto);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.count = filtered.size();
                results.values = filtered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                mValuesFiltrados = (List<Producto>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ConstraintLayout mContenedor;
        public final TextView mCodigo;
        public final TextView mProducto;
        public final TextView mPrecio;
        public final TextView mSaldo;
        public final TextView mEscalaBonificacion;
        public final TextView mTipo;
        public final TextInputEditText mCantidad;
        public final TextInputEditText  mBono;

        public final LinearLayout mAgregarLayout;
        public final ImageButton mAgregar;
        public Producto mItem;
        TextView tvMensajePrecioNoValido;


        public ViewHolder(View view, final AddProductoListener listener) {
            super(view);
            mView = view;
            mContenedor = (ConstraintLayout) view.findViewById((R.id.cl_contenedor_row_productos));
            mCodigo = (TextView) view.findViewById(R.id.tv_codigo_row_productos);
            mProducto = (TextView) view.findViewById(R.id.tv_producto_row_productos);
            mPrecio = (TextView) view.findViewById(R.id.tv_precio_row_productos);
            mSaldo = (TextView) view.findViewById(R.id.tv_saldo_row_productos);
            mEscalaBonificacion = (TextView) view.findViewById(R.id.tv_escala_row_productos);
            mTipo = (TextView) view.findViewById(R.id.tv_tipo_row_productos);
            mCantidad = (TextInputEditText) view.findViewById(R.id.et_cantidad_row_productos);
            mBono = (TextInputEditText) view.findViewById(R.id.et_bono_row_productos);
            mAgregarLayout = (LinearLayout) view.findViewById(R.id.ll_agregar_row_productos);
            mAgregar = (ImageButton) view.findViewById(R.id.bt_agregar_row_productos);
            tvMensajePrecioNoValido = (TextView) view.findViewById(R.id.tv_mensaje_no_editar);

            mAgregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String  espacio= "";
                    int cantidad= 0; int bono = 0;
                    //if (!mCantidad.getText().toString().isEmpty()) { cantidad = Integer.parseInt( mCantidad.getText().toString()); mCantidad.setText(""); }
                    //if (!mBono.getText().toString().isEmpty()){ bono =Integer.parseInt( mBono.getText().toString()); mBono.setText("");}
                    if (!mCantidad.getText().toString().isEmpty()) { cantidad = Integer.parseInt( mCantidad.getText().toString());  }
                    if (!mBono.getText().toString().isEmpty()){ bono =Integer.parseInt( mBono.getText().toString()); }
                    listener.AddProducto(mItem, cantidad, bono );
                }
            });

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mProducto.getText() + "'";
        }
    }

    public interface AddProductoListener {
        void AddProducto(Producto producto, int cantidad, int bono);

    }
}
