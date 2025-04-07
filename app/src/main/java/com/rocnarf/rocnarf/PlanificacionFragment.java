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

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.PlanificacionRecyclerViewAdapter;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.models.Clientes;
import com.rocnarf.rocnarf.models.Enviroment;
import com.rocnarf.rocnarf.models.MedicosCumpleanyos;
import com.rocnarf.rocnarf.models.MedicosCumpleanyosResponse;
import com.rocnarf.rocnarf.models.VisitaClientes;
import com.rocnarf.rocnarf.models.VisitaClientesList;
import com.rocnarf.rocnarf.repository.ClientesRepository;
import com.rocnarf.rocnarf.viewmodel.PlanificacionViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
        recyclerView = view.findViewById(R.id.rv_list_fragment_planificacion_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context)); // Asegúrate de tener un LayoutManager
        if (planificacionRecyclerViewAdapter == null) {
            planificacionRecyclerViewAdapter = new PlanificacionRecyclerViewAdapter(context, mListener);
        }

        recyclerView.setAdapter(planificacionRecyclerViewAdapter);

        cargarVisitas();

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
                Log.d("SwipeRefresh", "Usuario ha iniciado el refresh");
                cargarVisitas();
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
        if (planificacionRecyclerViewAdapter == null) {
            planificacionRecyclerViewAdapter = new PlanificacionRecyclerViewAdapter(context, mListener);
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
        void onListFragmentInteraction(VisitaClientes item);
    }
    public void CargaModalCumple() {
        myKonten.setVisibility(View.VISIBLE);

        // Llamar al servicio API
        PlanesService service = ApiClient.getClient().create(PlanesService.class);
        Call<MedicosCumpleanyosResponse> call = service.GetCumpleanyos(seccion);

        call.enqueue(new Callback<MedicosCumpleanyosResponse>() {
            @Override
            public void onResponse(Call<MedicosCumpleanyosResponse> call, Response<MedicosCumpleanyosResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MedicosCumpleanyosResponse medicosCumpleanyosResponse = response.body();

                    if (!medicosCumpleanyosResponse.items.isEmpty()) {
                        List<Map<String, String>> listaCumpleData = new ArrayList<>();

                        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd MMM", Locale.getDefault());

                        for (MedicosCumpleanyos medico : medicosCumpleanyosResponse.items) {
                            Map<String, String> item = new HashMap<>();

                            // Convertir la fecha de nacimiento de tipo Date a formato "dd MMM"
                            String fechaFormateada = formatoFecha.format(medico.getFechaNacimiento());

                            item.put("fecha", fechaFormateada);
                            item.put("nombre", medico.getNombre());
                            listaCumpleData.add(item);
                        }

                        // Configurar el adaptador con un layout personalizado
                        SimpleAdapter adapter = new SimpleAdapter(
                                context,
                                listaCumpleData,
                                R.layout.item_cumpleaneros, // Archivo XML personalizado
                                new String[]{"fecha", "nombre"},
                                new int[]{R.id.txtFecha, R.id.txtNombre}
                        );

                        listaCumple.setAdapter(adapter);

                        // Animación y actualización de estado
                        myKonten.startAnimation(fromsmall);
                        singleToneClass.setModalCumple("S");
                    } else {
                        Log.d("CargaModalCumple", "No hay médicos con cumpleaños en esta sección.");
                    }
                } else {
                    Log.e("CargaModalCumple", "Respuesta fallida: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MedicosCumpleanyosResponse> call, Throwable t) {
                Log.e("CargaModalCumple", "Error en la solicitud: " + t.getMessage());
            }
        });
    }




    public void cargarVisitas() {
        planificacionViewModel = ViewModelProviders.of(getActivity()).get(PlanificacionViewModel.class);

        planificacionViewModel.visitasLiveData.observe(this, new Observer<List<VisitaClientesList>>() {
            @Override
            public void onChanged(@Nullable List<VisitaClientesList> visitaClientesLists) {
                if (visitaClientesLists != null && !visitaClientesLists.isEmpty()) {
                    Log.d("cargarVisitas", "Datos recibidos, tamaño de la lista: " + visitaClientesLists.size());

                    // 🔥 Asegurar que el adapter está configurado antes de asignar los datos
                    if (planificacionRecyclerViewAdapter == null) {
                        planificacionRecyclerViewAdapter = new PlanificacionRecyclerViewAdapter(getContext(), mListener);
                        recyclerView.setAdapter(planificacionRecyclerViewAdapter);
                    }

                    // 🔄 Actualizar el adapter con los nuevos datos
                    planificacionRecyclerViewAdapter.setItems(visitaClientesLists);

                    totalReg = visitaClientesLists.size();

                    // 🚀 Forzar redibujado para evitar problemas de scroll
                    recyclerView.post(() -> recyclerView.invalidate());

                    // Mostrar modal de cumpleaños si aplica
                    if (totalReg > 0 && !"S".equals(showModal)) {
                        CargaModalCumple();
                    }
                } else {
                    Log.d("cargarVisitas", "La lista de visitas está vacía o es nula.");
                }

                // ✅ Asegurar que el SwipeRefresh se detiene después de actualizar
                if (mSwipeLayout.isRefreshing()) {
                    mSwipeLayout.post(() -> mSwipeLayout.setRefreshing(false));
                    Log.d("SwipeRefresh", "Refresh detenido");
                }
            }
        });

        // ✅ Llamar la función para obtener datos sin necesidad de un SwipeRefreshLayout
        planificacionViewModel.getVisitasPlanificadasList(idAsesor);
    }



}
