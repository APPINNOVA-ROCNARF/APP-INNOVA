package com.rocnarf.rocnarf;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.NotaCreditoRecyclerViewAdapter;
import com.rocnarf.rocnarf.models.NotaCredito;
import com.rocnarf.rocnarf.viewmodel.CobroViewModel;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class NotaCreditoFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, idFactura;
    private ProgressBar progressBar;
    private NotaCreditoRecyclerViewAdapter adapter ;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NotaCreditoFragment() {

    }


    public static NotaCreditoFragment newInstance(String idUsuario, String idCliente, String idFactura) {
        NotaCreditoFragment fragment = new NotaCreditoFragment();
        Bundle args = new Bundle();
        args.putString(Common.ARG_IDUSUARIO, idUsuario);
        args.putString(Common.ARG_IDCLIENTE, idCliente);
        args.putString(Common.ARG_IDFACTURA , idFactura);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            idUsuario = getArguments().getString(Common.ARG_IDUSUARIO);
            idCliente = getArguments().getString(Common.ARG_IDCLIENTE);
            idFactura = getArguments().getString(Common.ARG_IDFACTURA);
        }

//        final NotaCreditoRecyclerViewAdapter.NcListener listener = new NotaCreditoRecyclerViewAdapter.NcListener() {
//            @Override
//            public void NcListener(NotaCredito notaCredito) {
//                Log.d("aqui","dev");
//            }
//        };

//        final  NotaCreditoRecyclerViewAdapter.OnListFragmentInteractionListener listener1 = new NotaCreditoRecyclerViewAdapter.OnListFragmentInteractionListener() {
//            @Override
//            public void onListFragmentInteraction(NotaCredito notaCredito) {
//                Log.d("aqui 222222","dev");
//            }
//        };

//        final NotaCreditoRecyclerViewAdapter.OnListFragmentInteractionListener notaCreditoItemListener
//                = new NotaCreditoRecyclerViewAdapter.OnListFragmentInteractionListener() {
//            @Override
//                public void onListFragmentInteraction(final NotaCredito notaCredito) {
//
//                Log.d("aqui","dev");
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
////                builder.setMessage("Seguro de eliminar el producto " + pedidoDetalle.getNombre() + " del pedido")
////                        .setTitle("Eliminar");
//                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
////                        pedidoViewModel.deletePedidoDetalle(pedidoDetalle);
////                        Toast.makeText(getActivity(), pedidoDetalle.getNombre() + " eliminado del pedido" , Toast.LENGTH_LONG).show();
////                        mListener.onListFragmentInteraction(pedidoDetalle);
//                    }
//                });
//                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User cancelled the dialog
//                    }
//                });
//                builder.create().show();
//
//            }
//        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notacredito, container, false);

        // Set the adapter

        final Context context = view.getContext();
        progressBar = view.findViewById(R.id.pr_list_fragment_nc);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list_fragment_nc);
        recyclerView.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        Observer<List<NotaCredito>> observer =  new Observer<List<NotaCredito>>() {
            @Override
            public void onChanged(@Nullable List<NotaCredito> notaCredito) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(new NotaCreditoRecyclerViewAdapter(notaCredito, null));



            }

//            final NotaCreditoRecyclerViewAdapter.OnListFragmentInteractionListener notaCreditoItemListener
//                    = new NotaCreditoRecyclerViewAdapter.OnListFragmentInteractionListener() {
//                @Override
//                public void onListFragmentInteraction(final NotaCredito notaCredito) {
//
//                    Log.d("aqui","dev");
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
////                builder.setMessage("Seguro de eliminar el producto " + pedidoDetalle.getNombre() + " del pedido")
////                        .setTitle("Eliminar");
//                    builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
////                        pedidoViewModel.deletePedidoDetalle(pedidoDetalle);
////                        Toast.makeText(getActivity(), pedidoDetalle.getNombre() + " eliminado del pedido" , Toast.LENGTH_LONG).show();
////                        mListener.onListFragmentInteraction(pedidoDetalle);
//                        }
//                    });
//                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            // User cancelled the dialog
//                        }
//                    });
//                    builder.create().show();
//
//                }
//            };
        };
        Log.d("aqui","idFactura+++"+ idFactura);


        CobroViewModel cobroViewModel = ViewModelProviders.of( getActivity()).get(CobroViewModel.class);
        cobroViewModel.setIdUsuario(idUsuario);
        cobroViewModel.getNcXCliente(idFactura,idCliente).observe(this, observer);


        return view;


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
//        final NotaCreditoRecyclerViewAdapter.OnListFragmentInteractionListener notaCreditoItemListener
//                = new NotaCreditoRecyclerViewAdapter.OnListFragmentInteractionListener() {
//            @Override
//            public void onListFragmentInteraction(final NotaCredito notaCredito) {
//
//                Log.d("aqui","dev");
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
////                builder.setMessage("Seguro de eliminar el producto " + pedidoDetalle.getNombre() + " del pedido")
////                        .setTitle("Eliminar");
//                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
////                        pedidoViewModel.deletePedidoDetalle(pedidoDetalle);
////                        Toast.makeText(getActivity(), pedidoDetalle.getNombre() + " eliminado del pedido" , Toast.LENGTH_LONG).show();
////                        mListener.onListFragmentInteraction(pedidoDetalle);
//                    }
//                });
//                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User cancelled the dialog
//                    }
//                });
//                builder.create().show();
//
//            }
//        };
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
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

        // TODO: Update argument type and name
        void onListFragmentInteraction(NotaCredito item);

    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        final NotaCreditoRecyclerViewAdapter.OnListFragmentInteractionListener notaCreditoItemListener
//                = new NotaCreditoRecyclerViewAdapter.OnListFragmentInteractionListener() {
//            @Override
//            public void onListFragmentInteraction(final NotaCredito notaCredito) {
//
//                Log.d("aqui","dev");
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
////                builder.setMessage("Seguro de eliminar el producto " + pedidoDetalle.getNombre() + " del pedido")
////                        .setTitle("Eliminar");
//                builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
////                        pedidoViewModel.deletePedidoDetalle(pedidoDetalle);
////                        Toast.makeText(getActivity(), pedidoDetalle.getNombre() + " eliminado del pedido" , Toast.LENGTH_LONG).show();
////                        mListener.onListFragmentInteraction(pedidoDetalle);
//                    }
//                });
//                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User cancelled the dialog
//                    }
//                });
//                builder.create().show();
//
//            }
//        };
//
//    }


}
