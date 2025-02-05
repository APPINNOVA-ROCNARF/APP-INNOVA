package com.rocnarf.rocnarf;

import android.app.DatePickerDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.PedidoProductoRecyclerViewAdapter;
import com.rocnarf.rocnarf.models.Pedido;
import com.rocnarf.rocnarf.models.PedidoDetalle;
import com.rocnarf.rocnarf.viewmodel.PedidoViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PedidoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PedidoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PedidoFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private String idUsuario, seccion, idCliente;
    private int idLocalPedido;
    TextView mPedido, mFecha, mTotal, mDescuento, mFinal;
    TextView mTotalF3, mDescuentoF3, mFinalF3;
    TextView mTotalF2, mDescuentoF2, mFinalF2;
    TextView mTotalGEN, mDescuentoGEN, mFinalGEN;
    Context context;
    TextInputEditText mRecibo, mFactura, mValorCheque, mcheques, mFechaCheque, mEfectivo, mNotasCredito, mObservaciones;
   // ImageButton mAgregarDescuento;
    FloatingActionButton mAgregarDescuento ;
    Button mGuardar;
    private PedidoViewModel pedidoViewModel;
    Pedido pedidoExistemte;
    List<PedidoDetalle> pedidoDetalleExistente;
    Calendar mCalendar;
    DatePickerDialog mDatePickerDialog;
    RecyclerView recyclerView ;
    int anioCalendar, mesCalendar, diaCalendar;
    Button mCobros;

    public PedidoFragment() {
        // Required empty public constructor
    }



    public static PedidoFragment newInstance(String idUsuario, String seccion, String idCliente, int idLocalPedido) {
        PedidoFragment fragment = new PedidoFragment();
        Bundle args = new Bundle();
        args.putString(Common.ARG_IDUSUARIO, idUsuario);
        args.putString(Common.ARG_SECCIOM, seccion);
        args.putString(Common.ARG_IDCLIENTE, idCliente);
        args.putInt(Common.ARG_IDPEDIDO, idLocalPedido);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idUsuario = getArguments().getString(Common.ARG_IDUSUARIO);
            seccion = getArguments().getString(Common.ARG_SECCIOM);
            idCliente = getArguments().getString(Common.ARG_IDCLIENTE);
            idLocalPedido = getArguments().getInt(Common.ARG_IDPEDIDO);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //setup the listener for the fragment A
//        setDateField();
        pedidoViewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(PedidoViewModel.class);

        pedidoViewModel.getPedidoByIdLocal(idLocalPedido)
                .observe((FragmentActivity) getActivity(), new Observer<Pedido>() {
                    @Override
                    public void onChanged(@Nullable Pedido pedido) {
                        pedidoExistemte = pedido;
                        SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);
                        mPedido.setText("Pedido: " + String.valueOf(pedido.getIdPedido()));
                        mFecha.setText(sdf.format(pedido.getFechaPedido()));
                        mTotal.setText(String.format("%.2f", pedido.getPrecioTotal()));
                        mDescuento.setText(String.format("%.2f", pedido.getDescuento()));
                        mFinal.setText(String.format("%.2f", pedido.getPrecioFinal()));



                        /*mRecibo.setText(pedido.getReciboCobro());
                        mFactura.setText(pedido.getFactura());
                        mcheques.setText(pedido.getCheques());
                        if (pedido.getValorCheque() != null)
                            mValorCheque.setText(String.valueOf(pedido.getValorCheque()));
                        mFechaCheque.setText(pedido.getFechaCheque());
                        if (pedido.getEfectvo() != null)
                            mEfectivo.setText(String.valueOf(pedido.getEfectvo()));
                        mNotasCredito.setText(pedido.getNotaCredito());
                        mObservaciones.setText(pedido.getObservaciones());*/

      //                  mAgregarDescuento.setVisibility(View.VISIBLE);

//                        if (pedido.isConBonos()){
//                            mAgregarDescuento.setVisibility(View.GONE);
//                        }else {
//                            mAgregarDescuento.setVisibility(View.VISIBLE);
//                        }
                    }
                });


        final PedidoProductoRecyclerViewAdapter.PedidoProductoEliminarListener pedidoProductoEliminarListener
                = new PedidoProductoRecyclerViewAdapter.PedidoProductoEliminarListener() {
            @Override
            public void PedidoProductoEliminar(final PedidoDetalle pedidoDetalle) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Seguro de eliminar el producto " + pedidoDetalle.getNombre() + " del pedido")
                        .setTitle("Eliminar");
                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        pedidoViewModel.deletePedidoDetalle(pedidoDetalle);
                        Toast.makeText(getActivity(), pedidoDetalle.getNombre() + " eliminado del pedido" , Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                builder.create().show();

            }
        };

        final PedidoProductoRecyclerViewAdapter.PedidoProductoEditarListener pedidoProductoEditarListener
                = new PedidoProductoRecyclerViewAdapter.PedidoProductoEditarListener() {
            @Override
            public void PedidoProductoEditar(final PedidoDetalle pedidoDetalle) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View dialogView  = inflater.inflate(R.layout.dialog_pedido_cantidad, null);
                    final TextView porcentajeDescuento = dialogView.findViewById(R.id.et_descuento_dialog_pedido_cantidad);
                    builder.setTitle("Cantidad");
                    builder.setView(dialogView);
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //pedidoViewModel.deletePedidoDetalle(pedidoDetalleExistente);
                            if (porcentajeDescuento.getText() != "") {
                                if (Double.parseDouble(porcentajeDescuento.getText().toString()) < 0  || Double.parseDouble(porcentajeDescuento.getText().toString()) > 100){
                                    Toast.makeText(getActivity(), "Cantidad no valido " , Toast.LENGTH_LONG).show();
                                    return;
                                }
                                pedidoDetalle.setCantidad(Integer.parseInt(porcentajeDescuento.getText().toString()));
                                Log.d("producto","proc" +  pedidoDetalle.getCantidad());
                                Log.d("producto","proc" +  pedidoDetalle.getNombre());
                                Toast.makeText(getActivity(), " Cantidad asignado" , Toast.LENGTH_LONG).show();
                            }
                            pedidoViewModel.updatePedidoDetalle(pedidoDetalle);

                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
                    builder.create().show();

                }


        };


        pedidoViewModel.detallesPedido
                .observe((FragmentActivity) getActivity(), new Observer<List<PedidoDetalle>>() {
                    @Override
                    public void onChanged(@Nullable List<PedidoDetalle> pedidoDetalles) {
                        PedidoProductoRecyclerViewAdapter adapter
                                = new PedidoProductoRecyclerViewAdapter(pedidoDetalles, pedidoProductoEliminarListener, pedidoProductoEditarListener);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                    }
                });

        pedidoViewModel.detallesPedido.observe((FragmentActivity) getActivity(), new Observer<List<PedidoDetalle>>() {
            @Override
            public void onChanged(@Nullable List<PedidoDetalle> pedidoDetalles) {
                pedidoDetalleExistente = pedidoDetalles;
                CalcularTotalesXLinea();

            }
        });

        mAgregarDescuento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView  = inflater.inflate(R.layout.dialog_pedido_descuento, null);
                final TextView porcentajeDescuento = dialogView.findViewById(R.id.et_descuento_dialog_pedido_descuento);
                builder.setTitle("Descuento");
                builder.setView(dialogView);
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //pedidoViewModel.deletePedidoDetalle(pedidoDetalleExistente);
                        if (porcentajeDescuento.getText() != "") {
                            if (Double.parseDouble(porcentajeDescuento.getText().toString()) < 0  || Double.parseDouble(porcentajeDescuento.getText().toString()) > 100){
                                Toast.makeText(getActivity(), "Porcentaje no valido " , Toast.LENGTH_LONG).show();
                                return;
                            }
                            pedidoExistemte.setPorcentajeDescuento(Double.parseDouble(porcentajeDescuento.getText().toString()));
                            pedidoExistemte.setDescuento((Double.parseDouble(porcentajeDescuento.getText().toString())/100) * pedidoExistemte.getPrecioTotal());
                            pedidoExistemte.setPrecioFinal(pedidoExistemte.getPrecioTotal() - pedidoExistemte.getDescuento());
                            pedidoViewModel.updatePedido(pedidoExistemte);
                            CalcularTotalesXLinea();
                            Toast.makeText(getActivity(), " Descuento asignado" , Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                builder.create().show();
            }
        });

        mCobros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                Intent i = new Intent(getActivity(), ClientesFacturasActivity.class);
                i.putExtra(Common.ARG_IDCLIENTE, idCliente);
                i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                i.putExtra(Common.ARG_SECCIOM, seccion);
                i.putExtra(Common.ARG_TIPO_PEDIDO, Common.TIPO_PEDIDO_COBRO);
                i.putExtra(Common.ARG_FACTURAS_SELECCION, true);
                startActivity(i);

            }
        });

     /*   mGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Dentro de Pedido, una vez digitado Recibo de Cobro #, marcar como obligatorios los campos de Factura, Cheque o Efectivo de acuerdo sea el caso.
                // (Ej: Si ponemos pago en efectivo no nos debería de exigir cheque. Si ponemos pago en cheque, no nos debería de exigir efectivo)
                if (!mRecibo.getText().toString().isEmpty()) {
                    if (mFactura.getText().toString().isEmpty() || (mValorCheque.getText().toString().isEmpty() && mEfectivo.getText().toString().isEmpty())) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.myDialog));
                        alert.setTitle("ERROR");
                        alert.setMessage("En caso de cobro debes registrar la(s) factura(s) cobradas y el valor en efectivo o cheque");
                        alert.setPositiveButton("OK",null);
                        alert.show();
                        //Toast.makeText(getActivity(), "ERROR: En caso de cobro debes registrar la(s) factura(s) cobradas y el valor en efectivo o cheque", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                pedidoExistemte.setReciboCobro(mRecibo.getText().toString());
                pedidoExistemte.setFactura(mFactura.getText().toString());
                pedidoExistemte.setCheques(mcheques.getText().toString());
                if (!mValorCheque.getText().toString().isEmpty())
                    pedidoExistemte.setValorCheque(Double.parseDouble(mValorCheque.getText().toString()));
                pedidoExistemte.setFechaCheque(mFechaCheque.getText().toString());
                if (!mEfectivo.getText().toString().isEmpty())
                    pedidoExistemte.setEfectvo(Double.parseDouble(mEfectivo.getText().toString()));
                pedidoExistemte.setNotaCredito(mNotasCredito.getText().toString());
                pedidoExistemte.setObservaciones(mObservaciones.getText().toString());
                pedidoViewModel.updatePedido(pedidoExistemte);
                Toast.makeText(getActivity(),"Los datos del pedido se han guardado con exito", Toast.LENGTH_LONG).show();
            }
        });*/
    }

    private void CalcularTotalesXLinea() {
        double totalF3 =0, descuentoF3 = 0, finalF3 = 0;
        for (PedidoDetalle detalle: pedidoDetalleExistente) {
            if (detalle.getTipo().equals("F3")){
                totalF3 += detalle.getPrecioTotal();
            }
        }
        descuentoF3 = totalF3 * (pedidoExistemte.getPorcentajeDescuento()/100);
        finalF3 = totalF3 - descuentoF3;
        mTotalF3.setText(String.format("%.2f", totalF3));
        mDescuentoF3.setText(String.format("%.2f", descuentoF3));
        mFinalF3.setText(String.format("%.2f", finalF3));

        double totalF2 =0, descuentoF2 = 0, finalF2 = 0;
        for (PedidoDetalle detalle: pedidoDetalleExistente) {
            if (detalle.getTipo().equals("F2")){
                totalF2 += detalle.getPrecioTotal();
            }
        }
        descuentoF2 = totalF2 * (pedidoExistemte.getPorcentajeDescuento()/100);
        finalF2 = totalF2 - descuentoF2;
        mTotalF2.setText(String.format("%.2f", totalF2));
        mDescuentoF2.setText(String.format("%.2f", descuentoF2));
        mFinalF2.setText(String.format("%.2f", finalF2));

        double totalGEN =0, descuentoGEN = 0, finalGEN = 0;
        for (PedidoDetalle detalle: pedidoDetalleExistente) {
            if (detalle.getTipo().equals("GE")){
                totalGEN += detalle.getPrecioTotal();
            }
        }
        descuentoGEN = totalGEN * (pedidoExistemte.getPorcentajeDescuento()/100);
        finalGEN = totalGEN - descuentoGEN;
        mTotalGEN.setText(String.format("%.2f", totalGEN));
        mDescuentoGEN.setText(String.format("%.2f", descuentoGEN));
        mFinalGEN.setText(String.format("%.2f", finalGEN));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pedido, container, false);
        mPedido = (TextView) view.findViewById(R.id.tv_id_fragment_pedido);
        mFecha = (TextView) view.findViewById(R.id.tv_fecha_fragment_pedido);
        mTotal = (TextView) view.findViewById(R.id.tv_total_fragment_pedido);
        mDescuento = (TextView) view.findViewById(R.id.tv_descuento_fragment_pedido);
        mFinal = (TextView) view.findViewById(R.id.tv_final_fragment_pedido);
        //mGuardar = (Button) view.findViewById(R.id.bt_guardar_fragment_pedido);
        mCobros = (Button) view.findViewById(R.id.bt_cobro_pedido);

       // mAgregarDescuento = (FloatingActionButton) view.findViewById(R.id.fab_proforma);

        /*mRecibo= (TextInputEditText) view.findViewById(R.id.ti_recibo_fragment_pedido);
        mFactura= (TextInputEditText) view.findViewById(R.id.ti_factura_fragment_pedido);
        mcheques = (TextInputEditText) view.findViewById(R.id.ti_cheques_fragment_pedido);
        mValorCheque = (TextInputEditText) view.findViewById(R.id.ti_valor_cheque_fragment_pedido);
        mFechaCheque= (TextInputEditText) view.findViewById(R.id.ti_fecha_cheque_fragment_pedido);
        mEfectivo= (TextInputEditText) view.findViewById(R.id.ti_efectivo_fragment_pedido);
        mNotasCredito= (TextInputEditText) view.findViewById(R.id.ti_nota_credito_fragment_pedido);
        mObservaciones= (TextInputEditText) view.findViewById(R.id.ti_observaciones_fragment_pedido);*/

        mTotalF3 = (TextView)view.findViewById(R.id.tv_total_f3_fragment_pedido);
        mDescuentoF3 = (TextView)view.findViewById(R.id.tv_descuento_f3_fragment_pedido);
        mFinalF3 = (TextView) view.findViewById(R.id.tv_final_f3_fragment_pedido);

        mTotalF2 = (TextView)view.findViewById(R.id.tv_total_f2_fragment_pedido);
        mDescuentoF2 = (TextView)view.findViewById(R.id.tv_descuento_f2_fragment_pedido);
        mFinalF2 = (TextView) view.findViewById(R.id.tv_final_f2_fragment_pedido);

        mTotalGEN = (TextView)view.findViewById(R.id.tv_total_gen_fragment_pedido);
        mDescuentoGEN= (TextView)view.findViewById(R.id.tv_descuento_gen_fragment_pedido);
        mFinalGEN = (TextView) view.findViewById(R.id.tv_final_gen_fragment_pedido);






        View view2 = inflater.inflate(R.layout.fragment_pedido_producto, container, false);

        // Set the adapter
        if (view2 instanceof RecyclerView) {
            Context context = view2.getContext();
            recyclerView = (RecyclerView) view2;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

        }

        return view;
    }


//    private void setDateField(){
//
//        mCalendar = Calendar.getInstance();
//        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                anioCalendar = year; mesCalendar = month; diaCalendar = dayOfMonth;
//                mCalendar.set(Calendar.YEAR, year);
//                mCalendar.set(Calendar.MONTH, month);
//                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT, Locale.US);
//                mFechaCheque.setText(sdf.format(mCalendar.getTime()));
//            }};
//
//
//        mDatePickerDialog = new DatePickerDialog(getActivity(), date, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
//
//        mFechaCheque.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mDatePickerDialog.show();
//            }
//        });
//    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
