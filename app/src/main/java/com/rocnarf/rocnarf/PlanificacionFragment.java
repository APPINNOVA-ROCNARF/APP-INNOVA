package com.rocnarf.rocnarf;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;


import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.PlanificacionRecyclerViewAdapter;
import com.rocnarf.rocnarf.models.Clientes;
import com.rocnarf.rocnarf.models.Enviroment;
import com.rocnarf.rocnarf.models.VisitaClientes;
import com.rocnarf.rocnarf.models.VisitaClientesList;
import com.rocnarf.rocnarf.repository.ClientesRepository;
import com.rocnarf.rocnarf.viewmodel.PlanificacionViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PlanificacionFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;

    private SwipeRefreshLayout mSwipeLayout;
    private PlanificacionRecyclerViewAdapter planificacionRecyclerViewAdapter;
    private PlanificacionViewModel planificacionViewModel;
    private Context context;
    Animation fromsmall;
    LinearLayout myKonten;
    private ClientesRepository clientesRepository;
    Button btnClose;
    private String idAsesor, seccion, rol, modalCumple,showModal;
    ListView listaCumple;
    private RecyclerView recyclerView;
    ArrayAdapter<String> adapter;
    int totalReg = 0;
    Enviroment singleToneClass;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */


    public PlanificacionFragment() {
    }

    @SuppressWarnings("unused")
    public static PlanificacionFragment newInstance() {
        PlanificacionFragment fragment = new PlanificacionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.idAsesor = getArguments().getString(Common.ARG_IDUSUARIO);
            this.seccion = getArguments().getString(Common.ARG_SECCIOM);
            modalCumple =  getArguments().getString(Common.ARG_SHOW_MODAL_CUMPLE);

            rol = getArguments().getString(Common.ARG_ROL);
        }

        singleToneClass = com.rocnarf.rocnarf.models.Enviroment.getInstance();
        showModal = singleToneClass.getModalCumple();


        cargarVisitas();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planificacion_list, container, false);

        // Set the adapter
        final Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list_fragment_planificacion_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        myKonten = (LinearLayout) view.findViewById(R.id.modalCumple);
        btnClose = (Button) view.findViewById(R.id.btnCloseCumple);
        fromsmall = AnimationUtils.loadAnimation(context, R.anim.fromsmall);
        listaCumple = (ListView) view.findViewById(R.id.listCumple);


       // myKonten.setAlpha(0);


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myKonten.setAlpha(0);
                myKonten.setVisibility(View.GONE);
                Intent iFacturas = new Intent(context, MainActivity.class);
                Common.ARG_SHOW_MODAL_CUMPLE = "S";
            }
        });
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutPartidos);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                planificacionViewModel.getVisitasPlanificadasList(idAsesor);
                mSwipeLayout.setRefreshing(false);

            }
        });
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
        this.context = context;
        planificacionRecyclerViewAdapter = new PlanificacionRecyclerViewAdapter(context, mListener);
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
        void onListFragmentInteraction(VisitaClientes item);
    }

    public void CargaModalCumple(){
        myKonten.setVisibility(View.VISIBLE);
        clientesRepository = new ClientesRepository(context, idAsesor);
        List<Clientes> clientesCumple = clientesRepository.getClientesCumples(seccion);
        Log.d("xxxx","xxxxxx"+ clientesCumple.size());

        final ArrayList<String> listaNombre = new ArrayList<>();

        for (int i = 0; i < clientesCumple.size(); i++) {
            listaNombre.add(clientesCumple.get(i).getNombreCliente());
        }
        final ArrayAdapter adaptador = new ArrayAdapter(context, android.R.layout.simple_list_item_1,listaNombre);

        listaCumple.setAdapter(adaptador);

        myKonten.startAnimation(fromsmall);
        singleToneClass.setModalCumple("S");
    }

    public void cargarVisitas() {

        planificacionViewModel = ViewModelProviders.of(getActivity()).get(PlanificacionViewModel.class);
        planificacionViewModel.visitasLiveData.observe(this, new Observer<List<VisitaClientesList>>() {
            @Override
            public void onChanged(@Nullable List<VisitaClientesList> visitaClientesLists) {
                planificacionRecyclerViewAdapter = new PlanificacionRecyclerViewAdapter(context, mListener);
                planificacionRecyclerViewAdapter.setItems(visitaClientesLists);

                totalReg = visitaClientesLists.size();
                recyclerView.setAdapter(planificacionRecyclerViewAdapter);


                if(totalReg > 0 && showModal != "S") {
                    CargaModalCumple();
                }
            }
        });
        planificacionViewModel.getVisitasPlanificadasList(idAsesor);

//        .observe(this, new Observer<List<VisitaClientesList>>() {
//            @Override
//            public void onChanged(@Nullable List<VisitaClientesList> listaVisitaClientes) {
//                planificacionRecyclerViewAdapter.setItems(listaVisitaClientes);
//                //mSwipeLayout.setRefreshing(false);
//            }
//        });
    }

}
