package com.rocnarf.rocnarf;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.CobroRecyclerViewAdapter;
import com.rocnarf.rocnarf.models.Cobro;
import com.rocnarf.rocnarf.viewmodel.CobroViewModel;

import java.util.List;

public class ChequeFechaFragment extends Fragment {

    private CobroFragment.OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, idFactura;
    private ProgressBar progressBar;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ChequeFechaFragment() {
    }

    public static ChequeFechaFragment newInstance(String idUsuario, String idCliente, String idFactura) {
        ChequeFechaFragment fragment = new ChequeFechaFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cobro, container, false);

        // Set the adapter

        Context context = view.getContext();
        progressBar = view.findViewById(R.id.pr_list_fragment_cobro);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list_fragment_cobro);
        recyclerView.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        Observer<List<Cobro>> observer =  new Observer<List<Cobro>>() {
            @Override
            public void onChanged(@Nullable List<Cobro> cobros) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(new CobroRecyclerViewAdapter(cobros, mListener));
            }
        };


        CobroViewModel cobroViewModel = ViewModelProviders.of( getActivity()).get(CobroViewModel.class);
        cobroViewModel.setIdUsuario(idUsuario);
        if (idFactura == null){
            cobroViewModel.getChequeFechaXCliente(idCliente).observe(this, observer);
        }else {
            cobroViewModel.geGetCobrosXFactura(idFactura).observe(this, observer);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CobroFragment.OnListFragmentInteractionListener) {
            mListener = (CobroFragment.OnListFragmentInteractionListener) context;
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
        // TODO: Update argument type and name
        void onListFragmentInteraction(Cobro item);
    }
}
