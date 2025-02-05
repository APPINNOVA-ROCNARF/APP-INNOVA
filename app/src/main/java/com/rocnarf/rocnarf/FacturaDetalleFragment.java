package com.rocnarf.rocnarf;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.FacturaDetalleRecyclerViewAdapter;
import com.rocnarf.rocnarf.models.FacturaDetalle;
import com.rocnarf.rocnarf.viewmodel.FacturaDetalleViewModel;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FacturaDetalleFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FacturaDetalleViewModel facturaDetalleViewModel;
    private OnListFragmentInteractionListener mListener;
    private String idUsuario, idCliente, idFactura;
    private MenuItem menuItem;
    private SearchView searchView;
    private FacturaDetalleRecyclerViewAdapter facturaDetalleRecyclerViewAdapter;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FacturaDetalleFragment() {
    }

    @SuppressWarnings("unused")
    public static FacturaDetalleFragment newInstance(String idUsuario, String idCliente, String idFactura) {
        FacturaDetalleFragment fragment = new FacturaDetalleFragment();
        Bundle args = new Bundle();
        args.putString(Common.ARG_IDUSUARIO, idUsuario);
        args.putString(Common.ARG_IDCLIENTE, idCliente);
        args.putString(Common.ARG_IDFACTURA , idFactura);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            idUsuario = getArguments().getString(Common.ARG_IDUSUARIO);
            idCliente = getArguments().getString(Common.ARG_IDCLIENTE);
            idFactura = getArguments().getString(Common.ARG_IDFACTURA);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_factura_detalle, container, false);

        // Set the adapter

            Context context = view.getContext();
            progressBar = view.findViewById(R.id.pr_list_fragment_factura_detalle);
            progressBar.setVisibility(View.VISIBLE);
            recyclerView = (RecyclerView) view.findViewById(R.id.rv_list_fragment_factura_detalle);
            recyclerView.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            Observer<List<FacturaDetalle>> observer = new Observer<List<FacturaDetalle>>() {
                @Override
                public void onChanged(@Nullable List<FacturaDetalle> facturaDetalles) {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    facturaDetalleRecyclerViewAdapter = new FacturaDetalleRecyclerViewAdapter(facturaDetalles, mListener);
                    //recyclerView.setAdapter(new FacturaDetalleRecyclerViewAdapter(facturaDetalles, mListener));
                    recyclerView.setAdapter(facturaDetalleRecyclerViewAdapter);
                }
            };

            facturaDetalleViewModel = ViewModelProviders.of( getActivity()).get(FacturaDetalleViewModel.class);
            facturaDetalleViewModel.setIdUsuario(idUsuario);
            if (idFactura == null){
                    facturaDetalleViewModel.getDetallesFacturasXCliente(idCliente).observe(this, observer);
            }else {
                facturaDetalleViewModel.getFacturaDetalles(idFactura).observe(this, observer);
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

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.clientes, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                facturaDetalleRecyclerViewAdapter.getFilter().filter(s);
                return false;
            }
        });
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

        void onListFragmentInteraction(FacturaDetalle item);
    }


}
