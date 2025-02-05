package com.rocnarf.rocnarf;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.PedidoProductoRecyclerViewAdapter;
import com.rocnarf.rocnarf.models.PedidoDetalle;
import com.rocnarf.rocnarf.viewmodel.PedidoViewModel;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PedidoProductoFragment extends Fragment {
    private String idUsuario, seccion, idCliente;
    private int idLocalPedido;
    private OnListFragmentInteractionListener mListener;
    private PedidoViewModel pedidoViewModel;
    private   PedidoProductoRecyclerViewAdapter adapter;
    RecyclerView recyclerView ;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PedidoProductoFragment() {
    }



    public static PedidoProductoFragment newInstance(String idUsuario, String seccion, String idCliente, int idLocalPedido) {
        PedidoProductoFragment fragment = new PedidoProductoFragment();
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

            pedidoViewModel = ViewModelProviders.of(this).get(PedidoViewModel.class);
            pedidoViewModel.init(idLocalPedido);

            pedidoViewModel.setIdUsuario(this.idUsuario);
        }
    }

    public void setTipo(String tipo){
        //asignamos valor al adapter

        recyclerView.setAdapter(null);
        adapter.tipo = tipo;
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
                        mListener.onListFragmentInteraction(pedidoDetalle);
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
                final TextView cantidad = dialogView.findViewById(R.id.et_descuento_dialog_pedido_cantidad);
                final TextView bono = dialogView.findViewById(R.id.et_descuento_dialog_pedido_bono);
                cantidad.setText(String.valueOf(pedidoDetalle.getCantidad()));
                bono.setText(String.valueOf(pedidoDetalle.getBono()));

                builder.setTitle("Cantidad");
                builder.setView(dialogView);
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (cantidad.getText() != "") {
                            if (Double.parseDouble(cantidad.getText().toString()) < 0  || Double.parseDouble(cantidad.getText().toString()) > 100000){
                                Toast.makeText(getActivity(), "Cantidad no valido " , Toast.LENGTH_LONG).show();
                                return;
                            }
                            if (Double.parseDouble(bono.getText().toString()) < 0  || Double.parseDouble(bono.getText().toString()) > 100000){
                                Toast.makeText(getActivity(), "Bono no valido " , Toast.LENGTH_LONG).show();
                                return;
                            }
                            pedidoDetalle.setCantidad(Integer.parseInt(cantidad.getText().toString()));
                            pedidoDetalle.setBono(Integer.parseInt(bono.getText().toString()));
                            Toast.makeText(getActivity(), " Cantidad y Bono asignado" , Toast.LENGTH_LONG).show();
                        }
                        pedidoViewModel.updatePedidoDetalle(pedidoDetalle);
                        mListener.onListFragmentInteraction(pedidoDetalle);

                        ((PedidoSimpleActivity)getActivity()).CalcularTotalesXLineaPvf();
                        if(pedidoViewModel.pedido.getValue().getTipoPrecio().equals("P.V.F")){
                            ((PedidoSimpleActivity)getActivity()).CalcularTotalesXLineaPvf();
                        }else if(pedidoViewModel.pedido.getValue().getTipoPrecio().equals("P.V.P")){
                            ((PedidoSimpleActivity)getActivity()).CalcularTotalesXLineaPvp();
                        }

                        ///setTipo(pedidoViewModel.pedido.getValue().getTipoPrecio());
                        recyclerView.setAdapter(null);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
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

        //setup the listener for the fragment A
//        pedidoViewModel = ViewModelProviders.of(this)
//                .get(PedidoViewModel.class);
        pedidoViewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(PedidoViewModel.class);
        //Se agrega la consulta del pedido para detalles en el view
        pedidoViewModel.getPedidoByIdLocal(idLocalPedido);
        pedidoViewModel.detallesPedido
                .observe((FragmentActivity) getActivity(), new Observer<List<PedidoDetalle>>() {
                    @Override
                    public void onChanged(@Nullable List<PedidoDetalle> pedidoDetalles) {
                        adapter
                                = new PedidoProductoRecyclerViewAdapter(pedidoDetalles, pedidoProductoEliminarListener ,pedidoProductoEditarListener);
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pedido_producto, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(PedidoDetalle item);
    }


}
