package com.rocnarf.rocnarf;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.CobroRecyclerViewAdapter;
import com.rocnarf.rocnarf.models.Cobro;
import com.rocnarf.rocnarf.viewmodel.CobroViewModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class CobroFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, idFactura;
    private ProgressBar progressBar;
    private CobroRecyclerViewAdapter cobroRecyclerViewAdapter;
    private SearchView searchView;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CobroFragment() {
    }


    public static CobroFragment newInstance(String idUsuario, String idCliente, String idFactura) {
        CobroFragment fragment = new CobroFragment();
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
        setHasOptionsMenu(true);

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
                List<Cobro> listaAgrupada = agruparCobrosPorCheque(cobros);
                cobroRecyclerViewAdapter = new CobroRecyclerViewAdapter(listaAgrupada, mListener, idFactura);
                recyclerView.setAdapter(cobroRecyclerViewAdapter);
            }
        };


        CobroViewModel cobroViewModel = ViewModelProviders.of( getActivity()).get(CobroViewModel.class);
        cobroViewModel.setIdUsuario(idUsuario);
        if (idFactura == null){
            cobroViewModel.getCobrosXCliente(idCliente).observe(this, observer);
        }else {
            cobroViewModel.geGetCobrosXFactura(idFactura).observe(this, observer);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) item.getActionView();

        if (cobroRecyclerViewAdapter != null) {
            configurarFiltro();
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void configurarFiltro() {
        if (searchView == null || cobroRecyclerViewAdapter == null) return;

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                cobroRecyclerViewAdapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                cobroRecyclerViewAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    private List<Cobro> agruparCobrosPorCheque(List<Cobro> cobrosOriginal) {
        Map<String, List<Cobro>> agrupados = new HashMap<>();

        for (Cobro cobro : cobrosOriginal) {
            if (cobro.getNumeroCheque() == null || cobro.getNumeroCheque().trim().isEmpty()) {
                agrupados.put(UUID.randomUUID().toString(), List.of(cobro));
                continue;
            }

            String clave = cobro.getNumeroCheque();

            if (!agrupados.containsKey(clave)) {
                agrupados.put(clave, new ArrayList<Cobro>());
            }
            agrupados.get(clave).add(cobro);
        }

        List<Cobro> resultado = new ArrayList<>();
        for (List<Cobro> grupo : agrupados.values()) {
            BigDecimal total = BigDecimal.ZERO;
            for (Cobro c : grupo) {
                total = total.add(c.getValor());
            }

            Cobro original = grupo.get(0);
            Cobro resumen = new Cobro();

            resumen.setNumeroCheque(original.getNumeroCheque());
            resumen.setBanco(original.getBanco());
            resumen.setCuenta(original.getCuenta());
            resumen.setCobrador(original.getCobrador());
            resumen.setRecibo(original.getRecibo());
            resumen.setFecha(original.getFecha());
            resumen.setIdFactura(original.getIdFactura());
            resumen.setValor(total);
            resumen.setPedidosRelacionados(grupo);

            resultado.add(resumen);
        }

        return resultado;
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
