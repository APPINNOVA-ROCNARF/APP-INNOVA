package com.rocnarf.rocnarf;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.QuejaAdapter;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.models.Estadistica;
import com.rocnarf.rocnarf.models.HistorialComentarios;
import com.rocnarf.rocnarf.models.PedidosPendiente;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EstadisticaActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, nombreCliente, seccion;
    private List<PedidosPendiente> listaPedidosPendiente;
    private Context context;
    private QuejaAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar pgsBar;
    private List<HistorialComentarios> listaComentario;
    private LinearLayout LyCoberturaMedica;

    private TextView mActVisVal,mActVisPor,mCliActNoVal,mCliActNoPor,mCliActTotalVal,mCliActTotalPor,
    mCliActFacVal,mCliActFacPor,mCliActSinFacVal,mCliActSinFacPor,mCliActSinTotalVal,mCliActSinTotalPor,
            mCliInac,mCliZ,mCliCupUtiVal,mCliCupUtiPor,mCliCredVal,mCliCredPor,mCliCupTotalVal,mCliCupTotalPoc
            ,mCobVal,mCobPor,mSdiVal,mSdiPor,mIntVal,mIntPor,mDerVal,mDerPor,mTopVal,mTopPor,mTotalVal,mTotalPor;

    // % Médicos Visitados
    TextView porcentajeMedicosVisitados, totalMedicosVisitados;

    // Pts obtenidos a la fecha
    TextView puntosObtenidos, totalPuntos;

    // Médicos Visitados
    TextView medicosVisitados, porcentajeMedicosVisitadosTotal;

    // Médicos No Visitados
    TextView medicosNoVisitados, porcentajeMedicosNoVisitadosTotal;

    // Médicos Total
    TextView medicosTotal, porcentajeMedicosTotal;

    // Médicos A
    TextView medicosA, puntosMedicosA;

    // Médicos B
    TextView medicosB, puntosMedicosB;

    // Médicos C
    TextView medicosC, puntosMedicosC;

    // Médicos sin Categoría
    TextView medicosSinCategoria, puntosMedicosSinCategoria;

    // Médicos Total
    TextView medicosTotalCategoria, puntosTotalCategoria;

    // Médicos A Visitados
    TextView medicosAVisitados, puntosMedicosAVisitados;

    // Médicos B Visitados
    TextView medicosBVisitados, puntosMedicosBVisitados;

    // Médicos C Visitados
    TextView medicosCVisitados, puntosMedicosCVisitados;

    // Médicos sin Categoría Visitados
    TextView medicosSinCategoriaVisitados, puntosMedicosSinCategoriaVisitados;

    // Médicos Visitados Total
    TextView medicosVisitadosTotal, puntosMedicosVisitadosTotal;

    // MEG
    TextView medicosMEG, porcentajeMEG;

    // PED
    TextView medicosPED, porcentajePED;

    // OTO
    TextView medicosOTO, porcentajeOTO;

    // TRA
    TextView medicosTRA, porcentajeTRA;

    // CAR
    TextView medicosCAR, porcentajeCAR;
    TextView medicosURO, porcentajeURO;
    TextView medicosEND, porcentajeEND;
    TextView medicosGAS, porcentajeGAS;
    TextView medicosGIO, porcentajeGIO;
    TextView medicosCIP, porcentajeCIP;
    TextView medicosNEA, porcentajeNEA;
    TextView medicosCIR, porcentajeCIR;
    TextView medicosDER, porcentajeDER;
    LinearLayout ly_meg, ly_ped, ly_oto, ly_tra, ly_car, ly_uro, ly_end, ly_gas,ly_gio, ly_cip, ly_nea, ly_cir, ly_der;

    // Total
    TextView totalMedicosEspecialidad, porcentajeTotalEspecialidad;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadistica);
        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion = i.getStringExtra(Common.ARG_SECCIOM);

        context = this;

        mActVisVal = (TextView) findViewById(R.id.cli_act_vis_val);
        mActVisPor = (TextView) findViewById(R.id.cli_act_vis_por);
        mCliActNoVal = (TextView) findViewById(R.id.cli_act_no_vis_val);
        mCliActNoPor = (TextView) findViewById(R.id.cli_act_no_vis_por);
        mCliActTotalVal = (TextView) findViewById(R.id.cli_act_total_val);
        mCliActTotalPor = (TextView) findViewById(R.id.cli_act_total_poc);
        mCliActFacVal = (TextView) findViewById(R.id.cli_act_fac_val);
        mCliActFacPor = (TextView) findViewById(R.id.cli_act_fac_por);
        mCliActSinFacVal = (TextView) findViewById(R.id.cli_act_sin_fac_val);
        mCliActSinFacPor = (TextView) findViewById(R.id.cli_act_sin_fac_por);
        mCliActSinTotalVal = (TextView) findViewById(R.id.cli_act_sin_total_val);
        mCliActSinTotalPor = (TextView) findViewById(R.id.cli_act_sin_total_poc);

        mCliInac = (TextView) findViewById(R.id.cli_inac);
        mCliZ = (TextView) findViewById(R.id.cli_z);
        mCliCupUtiVal = (TextView) findViewById(R.id.cli_cup_uti_val);
        mCliCupUtiPor = (TextView) findViewById(R.id.cli_cup_uti_por);
        mCliCredVal = (TextView) findViewById(R.id.cli_cred_val);
        mCliCredPor = (TextView) findViewById(R.id.cli_cred_por);
        mCliCupTotalVal = (TextView) findViewById(R.id.cli_cup_total_val);
        mCliCupTotalPoc = (TextView) findViewById(R.id.cli_cup_total_poc);

        mCobVal = (TextView) findViewById(R.id.cli_COB_val);
        mCobPor = (TextView) findViewById(R.id.cli_COB_por);
        mSdiVal = (TextView) findViewById(R.id.cli_SDI_val);
        mSdiPor = (TextView) findViewById(R.id.cli_SDI_por);
        mIntVal = (TextView) findViewById(R.id.cli_INT_val);
        mIntPor = (TextView) findViewById(R.id.cli_INT_por);
        mDerVal = (TextView) findViewById(R.id.cli_DER_val);
        mDerPor = (TextView) findViewById(R.id.cli_DER_por);
        mTopVal = (TextView) findViewById(R.id.cli_TOP_val);
        mTopPor = (TextView) findViewById(R.id.cli_TOP_por);
        mTotalVal = (TextView) findViewById(R.id.cli_total_val);
        mTotalPor = (TextView) findViewById(R.id.cli_total_poc);

        LyCoberturaMedica = (LinearLayout) findViewById(R.id.layoutCoberturaMedica);

        porcentajeMedicosVisitados = (TextView) findViewById(R.id.medicos_visitados);
        totalMedicosVisitados = (TextView) findViewById(R.id.medicos_visitados_porcentaje);
        puntosObtenidos = (TextView) findViewById(R.id.puntos_obtenidos);
        totalPuntos = (TextView) findViewById(R.id.puntos_obtenidos_porcentaje);
        medicosVisitados = (TextView) findViewById(R.id.medicos_visitados_total);
        porcentajeMedicosVisitadosTotal = (TextView) findViewById(R.id.medicos_visitados_porcentaje2);
        medicosNoVisitados = (TextView) findViewById(R.id.medicos_no_visitados_total);
        porcentajeMedicosNoVisitadosTotal = (TextView) findViewById(R.id.medicos_no_visitados_porcentaje2);
        medicosTotal = (TextView) findViewById(R.id.medicos__total);
        porcentajeMedicosTotal = (TextView) findViewById(R.id.medicos__porcentaje);
        medicosA = (TextView) findViewById(R.id.medicos_A);
        puntosMedicosA = (TextView) findViewById(R.id.medicos_A_puntos);
        medicosB = (TextView) findViewById(R.id.medicos_B);
        puntosMedicosB = (TextView) findViewById(R.id.medicos_B_puntos);
        medicosC = (TextView) findViewById(R.id.medicos_C);
        puntosMedicosC = (TextView) findViewById(R.id.medicos_C_puntos);
        medicosSinCategoria = (TextView) findViewById(R.id.medicos_PC);
        puntosMedicosSinCategoria = (TextView) findViewById(R.id.medicos_PC_puntos);
        medicosTotalCategoria = (TextView) findViewById(R.id.medicos_total_puntos);
        puntosTotalCategoria = (TextView) findViewById(R.id.medicos_total_puntos2);
        medicosAVisitados = (TextView) findViewById(R.id.medicos_A_visitados);
        puntosMedicosAVisitados = (TextView) findViewById(R.id.medicos_A_puntos_visitados);
        medicosBVisitados  = (TextView) findViewById(R.id.medicos_B_visitados);
        puntosMedicosBVisitados = (TextView) findViewById(R.id.medicos_B_puntos_visitados);
        medicosCVisitados = (TextView) findViewById(R.id.medicos_C_visitados);
        puntosMedicosCVisitados = (TextView) findViewById(R.id.medicos_C_puntos_visitados);
        medicosSinCategoriaVisitados = (TextView) findViewById(R.id.medicos_PC_visitados);
        puntosMedicosSinCategoriaVisitados = (TextView) findViewById(R.id.medicos_PC_puntos_visitados);
        medicosVisitadosTotal = (TextView) findViewById(R.id.medicos_total_puntos_visitados);
        puntosMedicosVisitadosTotal  = (TextView) findViewById(R.id.medicos_total_puntos2_visitaods);
        medicosMEG  = (TextView) findViewById(R.id.cli_MEG_val);
        porcentajeMEG = (TextView) findViewById(R.id.cli_MEG_por);
        medicosPED  = (TextView) findViewById(R.id.cli_PED_val);
        porcentajePED = (TextView) findViewById(R.id.cli_PED_por);
        medicosOTO= (TextView) findViewById(R.id.cli_OTO_val);
        porcentajeOTO= (TextView) findViewById(R.id.cli_OTO_por);
        medicosTRA= (TextView) findViewById(R.id.cli_TRA_val);
        porcentajeTRA= (TextView) findViewById(R.id.cli_TRA_por);
        medicosCAR= (TextView) findViewById(R.id.cli_CAR_val);
        porcentajeCAR= (TextView) findViewById(R.id.cli_CAR_por);
        medicosURO = (TextView) findViewById(R.id.cli_URO_val);
        porcentajeURO = (TextView) findViewById(R.id.cli_URO_por);
        medicosEND = (TextView) findViewById(R.id.cli_END_val);
        porcentajeEND = (TextView) findViewById(R.id.cli_END_por);

        medicosGAS = (TextView) findViewById(R.id.cli_GAS_val);
        porcentajeGAS = (TextView) findViewById(R.id.cli_GAS_por);

        medicosGIO = (TextView) findViewById(R.id.cli_GIO_val);
        porcentajeGIO = (TextView) findViewById(R.id.cli_GIO_por);

        medicosCIP = (TextView) findViewById(R.id.cli_CIP_val);
        porcentajeCIP = (TextView) findViewById(R.id.cli_CIP_por);

        medicosNEA = (TextView) findViewById(R.id.cli_NEA_val);
        porcentajeNEA = (TextView) findViewById(R.id.cli_NEA_por);

        medicosCIR = (TextView) findViewById(R.id.cli_CIR_val);
        porcentajeCIR = (TextView) findViewById(R.id.cli_CIR_por);

        medicosDER = (TextView) findViewById(R.id.cli_DERMA_val);
        porcentajeDER = (TextView) findViewById(R.id.cli_DERMA_por);

        totalMedicosEspecialidad= (TextView) findViewById(R.id.med_total_val);
        porcentajeTotalEspecialidad = (TextView) findViewById(R.id.med_total_poc);

        ly_meg = (LinearLayout) findViewById(R.id.ly_meg);
        ly_ped = (LinearLayout) findViewById(R.id.ly_ped);
        ly_oto = (LinearLayout) findViewById(R.id.ly_oto);
        ly_tra = (LinearLayout) findViewById(R.id.ly_tra);
        ly_car = (LinearLayout) findViewById(R.id.ly_car);
        ly_uro = (LinearLayout) findViewById(R.id.ly_uro);
        ly_end = (LinearLayout) findViewById(R.id.ly_end);
        ly_gas = (LinearLayout) findViewById(R.id.ly_gas);
        ly_gio = (LinearLayout) findViewById(R.id.ly_gio);
        ly_cip = (LinearLayout) findViewById(R.id.ly_cip);
        ly_nea = (LinearLayout) findViewById(R.id.ly_nea);
        ly_cir = (LinearLayout) findViewById(R.id.ly_cir);
        ly_der = (LinearLayout) findViewById(R.id.ly_der);


       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        //pgsBar = (ProgressBar) findViewById(R.id.pBarQueja);
        //final ActionBar actionBar = getSupportActionBar();

     //   swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshQueja);

//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                CargaData();
//                swipeRefreshLayout.setRefreshing(false);
//
//            }
//
//        });


        pgsBar = findViewById(R.id.progressBar);
        pgsBar.setVisibility(View.VISIBLE);
        CargaData();


    }




    final void CargaData() {

        PlanesService service = ApiClient.getClient().create(PlanesService.class);
        Call<Estadistica> call = service.GetEstadistica(idUsuario);
        call.enqueue(new Callback<Estadistica>() {
            @Override
            public void onResponse(Call<Estadistica> call, Response<Estadistica> response) {
                if (response.isSuccessful()) {
                    Estadistica estadistica = response.body();
                    mActVisVal.setText(estadistica.getClienteActVis().toString());
                    mCliActNoVal.setText(estadistica.getClienteActSinVis().toString());
                    int porActVisitado = 0;
                    if((estadistica.getClienteActVis() + estadistica.getClienteActSinVis()) != 0) porActVisitado = estadistica.getClienteActVis() * 100 / (estadistica.getClienteActVis() + estadistica.getClienteActSinVis());
                    mActVisPor.setText(String.valueOf(porActVisitado + "%"));
                    int porActNoVisitado = 0;
                    if((estadistica.getClienteActVis() + estadistica.getClienteActSinVis()) != 0) porActNoVisitado = estadistica.getClienteActSinVis() * 100 / (estadistica.getClienteActVis() + estadistica.getClienteActSinVis());
                    mCliActNoPor.setText(String.valueOf(porActNoVisitado + "%"));
                    mCliActTotalVal.setText(String.valueOf(estadistica.getClienteActVis() + estadistica.getClienteActSinVis()));
                    mCliActTotalPor.setText("100%");

                    mCliActFacVal.setText(estadistica.getClientefac().toString());
                    int porCliActFacVal = 0;
                    if((estadistica.getClientefac() + estadistica.getClienteSinFac()) != 0) porCliActFacVal = estadistica.getClientefac() * 100 / (estadistica.getClientefac() + estadistica.getClienteSinFac());
                    mCliActFacPor.setText(String.valueOf(porCliActFacVal + "%"));
                    mCliActSinFacVal.setText(estadistica.getClienteSinFac().toString());
                    int porCliActSinFacVal = 0;
                    if((estadistica.getClientefac() + estadistica.getClienteSinFac()) != 0) porCliActSinFacVal = estadistica.getClienteSinFac() * 100 / (estadistica.getClientefac() + estadistica.getClienteSinFac());
                    mCliActSinFacPor.setText(String.valueOf(porCliActSinFacVal + "%"));
                    mCliActSinTotalVal.setText(String.valueOf(estadistica.getClientefac() + estadistica.getClienteSinFac()));
                    mCliActSinTotalPor.setText(porCliActSinFacVal + porCliActFacVal + "%");

                    mCliInac.setText(estadistica.getClienteInactivo().toString());
                    mCliZ.setText(estadistica.getClienteZ().toString());

                    NumberFormat formatter = new DecimalFormat("###,###,##0.00");

                    String mCliCupUtiValFormat = formatter.format(estadistica.getCupoUtilizado());
                    mCliCupUtiVal.setText(mCliCupUtiValFormat);
                    double porCliCupUti = 0;
                    if((estadistica.getCupoUtilizado() + estadistica.getCupoDisponible()) != 0) porCliCupUti = estadistica.getCupoUtilizado() * 100 / (estadistica.getCupoUtilizado() + estadistica.getCupoDisponible());
                    mCliCupUtiPor.setText(Double.valueOf(porCliCupUti).intValue() + "%");
                    String mCliCredValFormat = formatter.format(estadistica.getCupoDisponible());
                    mCliCredVal.setText(mCliCredValFormat);
                    double porCliCred = 0;
                    if((estadistica.getCupoUtilizado() + estadistica.getCupoDisponible()) != 0) porCliCred = estadistica.getCupoDisponible() * 100 / (estadistica.getCupoUtilizado() + estadistica.getCupoDisponible());
                    mCliCredPor.setText(Double.valueOf(porCliCred).intValue() +"%");
                    String mCliCupTotalValFormat = formatter.format(estadistica.getCupoUtilizado() + estadistica.getCupoDisponible());
                    mCliCupTotalVal.setText(mCliCupTotalValFormat);


                    mCliCupTotalPoc.setText(Double.valueOf(porCliCupUti+porCliCred).intValue() + "%");

                    mCobVal.setText(estadistica.getCliCob().toString());
                    int pormCobVal = 0;
                    int pormDerVal = 0;
                    int pormSdiVal = 0;
                    int pormTopVal = 0;
                    int pormIntVal = 0;

                    if((estadistica.getCliCob() + estadistica.getCliDer() + estadistica.getCliSdi() + estadistica.getCliTop() +estadistica.getCliInt()) != 0)
                    {
                        pormCobVal = estadistica.getCliCob() * 100 / (estadistica.getCliCob() + estadistica.getCliDer() + estadistica.getCliSdi() + estadistica.getCliTop() +estadistica.getCliInt());
                        pormDerVal = estadistica.getCliDer() * 100 / (estadistica.getCliCob() + estadistica.getCliDer() + estadistica.getCliSdi() + estadistica.getCliTop() +estadistica.getCliInt());
                        pormSdiVal = estadistica.getCliSdi() * 100 / (estadistica.getCliCob() + estadistica.getCliDer() + estadistica.getCliSdi() + estadistica.getCliTop() +estadistica.getCliInt());
                        pormTopVal = estadistica.getCliTop() * 100 / (estadistica.getCliCob() + estadistica.getCliDer() + estadistica.getCliSdi() + estadistica.getCliTop() +estadistica.getCliInt());
                        pormIntVal = estadistica.getCliInt() * 100 / (estadistica.getCliCob() + estadistica.getCliDer() + estadistica.getCliSdi() + estadistica.getCliTop() +estadistica.getCliInt());
                    }
                    mCobPor.setText(pormCobVal + "%");

                    mSdiVal.setText(estadistica.getCliSdi().toString());;
                    mSdiPor.setText(pormSdiVal +"%");;
                    mIntVal.setText(estadistica.getCliInt().toString());;
                    mIntPor.setText(pormIntVal + "%");
                    mDerVal.setText(estadistica.getCliDer().toString());;
                    mDerPor.setText(pormDerVal + "%");;
                    mTopVal.setText(estadistica.getCliTop().toString());;
                    mTopPor.setText(pormTopVal + "%");;
                    mTotalVal.setText(String.valueOf(estadistica.getCliCob() + estadistica.getCliDer() + estadistica.getCliSdi() + estadistica.getCliTop() +estadistica.getCliInt()));;
                    mTotalPor.setText(pormTopVal + pormCobVal + pormSdiVal + pormIntVal + pormDerVal +"%");;

                    int MEDTOTALSERVER = estadistica.getMedTotal();

                    if (MEDTOTALSERVER != 0){
                        LyCoberturaMedica.setVisibility(View.VISIBLE);
                        // PORCENTAJE DE MEDICOS VISITADOS
                        int PORCENTAJEMEDICOSVISITADOS = 0;

                        if (estadistica.getMedVis() != 0) {
                            PORCENTAJEMEDICOSVISITADOS = (int) ((estadistica.getMedVis() / (double) estadistica.getMedTotal()) * 100);
                        }

                        String VISITADOSXTOTAL = String.valueOf(estadistica.getMedVis()) + "/" + String.valueOf(estadistica.getMedTotal());

                        porcentajeMedicosVisitados.setText(VISITADOSXTOTAL);

                        totalMedicosVisitados.setText(PORCENTAJEMEDICOSVISITADOS + "%");



                        //MEDICOS VISITADOS
                        medicosVisitados.setText(estadistica.getMedVis().toString());
                        medicosNoVisitados.setText(estadistica.getMedSinVis().toString());
                        int MEDTOTAL = estadistica.getMedVis() + estadistica.getMedSinVis();

                        medicosTotal.setText(String.valueOf(MEDTOTAL));

                        int MEDICOSVISITADOSXTOTAL = 0;
                        int MEDICOSNOVISITADOSXTOTAL = 0;
                        int MEDICOSTOTAL = 0;

                        if (estadistica.getMedVis() + estadistica.getMedSinVis() + estadistica.getMedTotal() != 0){
                            MEDICOSVISITADOSXTOTAL =  (int) ((estadistica.getMedVis() / (double) estadistica.getMedTotal()) * 100);
                            MEDICOSNOVISITADOSXTOTAL =  (int) ((estadistica.getMedSinVis() / (double) estadistica.getMedTotal()) * 100);
                            MEDICOSTOTAL =  MEDICOSVISITADOSXTOTAL + MEDICOSNOVISITADOSXTOTAL;
                        }

                        porcentajeMedicosVisitadosTotal.setText(String.valueOf(MEDICOSVISITADOSXTOTAL) + "%");
                        porcentajeMedicosNoVisitadosTotal.setText(String.valueOf(MEDICOSNOVISITADOSXTOTAL) + "%");
                        porcentajeMedicosTotal.setText(String.valueOf(MEDICOSTOTAL) + "%");


                        // MEDICOS POR CATEGORIA
                        int medA = estadistica.getMedA();
                        int medB = estadistica.getMedB();
                        int medC = estadistica.getMedC();
                        int medPC = estadistica.getMedPC();

                        medicosA.setText(String.valueOf(medA));
                        medicosB.setText(String.valueOf(medB));
                        medicosC.setText(String.valueOf(medC));
                        medicosSinCategoria.setText(String.valueOf(medPC));
                        int MEDCATEGORIATOTAL = (medA + medB + medC + medPC);

                        medicosTotalCategoria.setText(String.valueOf(MEDCATEGORIATOTAL));

                        int MEDICOSAPUNTOS = 0;
                        int MEDICOSBPUNTOS = 0;
                        int MEDICOSCPUNTOS = 0;
                        int MEDICOSPCPUNTOS = 0;
                        int MEDICOSCATEGORIATOTAL = 0;

                        if (medA + medB + medC + medPC != 0 ){
                            MEDICOSAPUNTOS = medA * 3;
                            MEDICOSBPUNTOS = medB * 2;
                            MEDICOSCPUNTOS = medC;
                            MEDICOSPCPUNTOS = medPC * 2;
                            MEDICOSCATEGORIATOTAL = MEDICOSAPUNTOS + MEDICOSBPUNTOS + MEDICOSCPUNTOS + MEDICOSPCPUNTOS;
                        }

                        puntosMedicosA.setText(String.valueOf(MEDICOSAPUNTOS) + "Pts");
                        puntosMedicosB.setText(String.valueOf(MEDICOSBPUNTOS) + "Pts");
                        puntosMedicosC.setText(String.valueOf(MEDICOSCPUNTOS) + "Pts");
                        puntosMedicosSinCategoria.setText(String.valueOf(MEDICOSPCPUNTOS) + "Pts");
                        puntosTotalCategoria.setText(String.valueOf(MEDICOSCATEGORIATOTAL) + "Pts");


                        // MEDICOS VISITADOS POR CATEGORIA

                        int medvisA = estadistica.getMedVisA();
                        int medvisB = estadistica.getMedVisB();
                        int medvisC = estadistica.getMedVisC();
                        int medvisPC = estadistica.getMedVisPC();

                        medicosAVisitados.setText(String.valueOf(medvisA));
                        medicosBVisitados.setText(String.valueOf(medvisB));
                        medicosCVisitados.setText(String.valueOf(medvisC));
                        medicosSinCategoriaVisitados.setText(String.valueOf(medvisPC));
                        int MEDVISCATEGORIATOTAL = (medvisA + medvisB + medvisC + medvisPC);

                        medicosVisitadosTotal.setText(String.valueOf(MEDVISCATEGORIATOTAL));

                        int MEDICOSVISITADOSAPUNTOS = 0;
                        int MEDICOSVISITADOSBPUNTOS = 0;
                        int MEDICOSVISITADOSCPUNTOS = 0;
                        int MEDICOSVISITADOSPCPUNTOS = 0;
                        int MEDICOSVISITADOSTOTALPUNTOS = 0;

                        if (medvisA + medvisB + medvisC + medvisPC != 0 ){
                            MEDICOSVISITADOSAPUNTOS = medvisA * 3;
                            MEDICOSVISITADOSBPUNTOS = medvisB * 2;
                            MEDICOSVISITADOSCPUNTOS = medvisC;
                            MEDICOSVISITADOSPCPUNTOS = medvisPC * 2;
                            MEDICOSVISITADOSTOTALPUNTOS = MEDICOSVISITADOSAPUNTOS + MEDICOSVISITADOSBPUNTOS + MEDICOSVISITADOSCPUNTOS + MEDICOSVISITADOSPCPUNTOS;
                        }

                        puntosMedicosAVisitados.setText(String.valueOf(MEDICOSVISITADOSAPUNTOS) + "Pts");
                        puntosMedicosBVisitados.setText(String.valueOf(MEDICOSVISITADOSBPUNTOS) + "Pts");
                        puntosMedicosCVisitados.setText(String.valueOf(MEDICOSVISITADOSCPUNTOS) + "Pts");
                        puntosMedicosSinCategoriaVisitados.setText(String.valueOf(MEDICOSVISITADOSPCPUNTOS) + "Pts");
                        puntosMedicosVisitadosTotal.setText(String.valueOf(MEDICOSVISITADOSTOTALPUNTOS) + "Pts");

                        // PUNTOS OBTENIDOS A LA FECHA

                        puntosObtenidos.setText(String.valueOf(MEDICOSVISITADOSTOTALPUNTOS) + "/" + String.valueOf(MEDICOSCATEGORIATOTAL));

                        int PUNTOSOBTENIDOSPORCENTAJE = 0;

                        if ( MEDICOSVISITADOSTOTALPUNTOS + MEDCATEGORIATOTAL != 0){
                            PUNTOSOBTENIDOSPORCENTAJE = (int) (( MEDICOSVISITADOSTOTALPUNTOS / (double) MEDCATEGORIATOTAL * 100));
                        }

                        totalPuntos.setText(String.valueOf(PUNTOSOBTENIDOSPORCENTAJE) + "%");

                        //MEDICOS POR ESPECIALIDAD

                        int medMEG = estadistica.getMedMeg();
                        int medPED = estadistica.getMedPed();
                        int medOTO = estadistica.getMedOto();
                        int medTRA = estadistica.getMedTra();
                        int medCAR = estadistica.getMedCar();
                        int medURO = estadistica.getMedUro();
                        int medEND = estadistica.getMedEnd();
                        int medGAS = estadistica.getMedGas();
                        int medGIO = estadistica.getMedGio();
                        int medCIP = estadistica.getMedCip();
                        int medNEA = estadistica.getMedNea();
                        int medCIR = estadistica.getMedCir();
                        int medDER = estadistica.getMedDer();

                        Map<LinearLayout, Integer> lyMap = new HashMap<>();
                        lyMap.put(ly_meg, estadistica.getMedMeg());
                        lyMap.put(ly_ped, estadistica.getMedPed());
                        lyMap.put(ly_oto, estadistica.getMedOto());
                        lyMap.put(ly_tra, estadistica.getMedTra());
                        lyMap.put(ly_car, estadistica.getMedCar());
                        lyMap.put(ly_uro, estadistica.getMedUro());
                        lyMap.put(ly_end, estadistica.getMedEnd());
                        lyMap.put(ly_gas, estadistica.getMedGas());
                        lyMap.put(ly_gio, estadistica.getMedGio());
                        lyMap.put(ly_cip, estadistica.getMedCip());
                        lyMap.put(ly_nea, estadistica.getMedNea());
                        lyMap.put(ly_cir, estadistica.getMedCir());
                        lyMap.put(ly_der, estadistica.getMedDer());

                        for (Map.Entry<LinearLayout, Integer> entry : lyMap.entrySet()) {
                            if (entry.getValue() == 0) {
                                entry.getKey().setVisibility(View.GONE);
                            }
                        }



                        medicosMEG.setText(String.valueOf(medMEG));
                        medicosPED.setText(String.valueOf(medPED));
                        medicosOTO.setText(String.valueOf(medOTO));
                        medicosTRA.setText(String.valueOf(medTRA));
                        medicosCAR.setText(String.valueOf(medCAR));
                        medicosURO.setText(String.valueOf(medURO));
                        medicosEND.setText(String.valueOf(medEND));
                        medicosGAS.setText(String.valueOf(medGAS));
                        medicosGIO.setText(String.valueOf(medGIO));
                        medicosCIP.setText(String.valueOf(medCIP));
                        medicosNEA.setText(String.valueOf(medNEA));
                        medicosCIR.setText(String.valueOf(medCIR));
                        medicosDER.setText(String.valueOf(medDER));


                        int MEDICOSESPECIALIDADTOTAL = (medMEG + medPED + medOTO + medTRA + medURO + medCAR + medEND + medGAS + medGIO + medCIP + medNEA + medCIR + medDER);

                        totalMedicosEspecialidad.setText(String.valueOf(MEDICOSESPECIALIDADTOTAL));

                        int MEDICOSMEGPORCENTAJE = 0;
                        int MEDICOSPEDPORCENTAJE = 0;
                        int MEDICOSOTOPORCENTAJE = 0;
                        int MEDICOSTRAPORCENTAJE = 0;
                        int MEDICOSCARPORCENTAJE = 0;
                        int MEDICOSUROPORCENTAJE = 0;
                        int MEDICOSENDPORCENTAJE = 0;
                        int MEDICOSGASPORCENTAJE = 0;
                        int MEDICOSGIOPORCENTAJE = 0;
                        int MEDICOSCIPPORCENTAJE = 0;
                        int MEDICOSNEAPORCENTAJE = 0;
                        int MEDICOSCIRPORCENTAJE = 0;
                        int MEDICOSDERPORCENTAJE = 0;
                        int MEDICOSESPECIALIDADTOTALPORCENTAJE = 0;

                        if (MEDICOSESPECIALIDADTOTAL != 0) {
                            MEDICOSMEGPORCENTAJE = (int) ((medMEG / (double) MEDICOSESPECIALIDADTOTAL) * 100);
                            MEDICOSPEDPORCENTAJE = (int) ((medPED / (double) MEDICOSESPECIALIDADTOTAL) * 100);
                            MEDICOSOTOPORCENTAJE = (int) ((medOTO / (double) MEDICOSESPECIALIDADTOTAL) * 100);
                            MEDICOSUROPORCENTAJE = (int) ((medURO / (double) MEDICOSESPECIALIDADTOTAL) * 100);
                            MEDICOSTRAPORCENTAJE = (int) ((medTRA / (double) MEDICOSESPECIALIDADTOTAL) * 100);
                            MEDICOSCARPORCENTAJE = (int) ((medCAR / (double) MEDICOSESPECIALIDADTOTAL) * 100);
                            MEDICOSENDPORCENTAJE = (int) ((medEND / (double) MEDICOSESPECIALIDADTOTAL) * 100);
                            MEDICOSGASPORCENTAJE = (int) ((medGAS / (double) MEDICOSESPECIALIDADTOTAL) * 100);
                            MEDICOSGIOPORCENTAJE = (int) ((medGIO / (double) MEDICOSESPECIALIDADTOTAL) * 100);
                            MEDICOSCIPPORCENTAJE = (int) ((medCIP / (double) MEDICOSESPECIALIDADTOTAL) * 100);
                            MEDICOSNEAPORCENTAJE = (int) ((medNEA / (double) MEDICOSESPECIALIDADTOTAL) * 100);
                            MEDICOSCIRPORCENTAJE = (int) ((medCIR / (double) MEDICOSESPECIALIDADTOTAL) * 100);
                            MEDICOSDERPORCENTAJE = (int) ((medDER / (double) MEDICOSESPECIALIDADTOTAL) * 100);

                            MEDICOSESPECIALIDADTOTALPORCENTAJE = MEDICOSMEGPORCENTAJE + MEDICOSPEDPORCENTAJE + MEDICOSOTOPORCENTAJE +
                                    MEDICOSTRAPORCENTAJE + MEDICOSCARPORCENTAJE + MEDICOSENDPORCENTAJE +
                                    MEDICOSUROPORCENTAJE +
                                    MEDICOSGASPORCENTAJE + MEDICOSGIOPORCENTAJE + MEDICOSCIPPORCENTAJE +
                                    MEDICOSNEAPORCENTAJE + MEDICOSCIRPORCENTAJE + MEDICOSDERPORCENTAJE;
                        }

                        porcentajeMEG.setText(String.valueOf(MEDICOSMEGPORCENTAJE) + "%");
                        porcentajePED.setText(String.valueOf(MEDICOSPEDPORCENTAJE) + "%");
                        porcentajeOTO.setText(String.valueOf(MEDICOSOTOPORCENTAJE) + "%");
                        porcentajeTRA.setText(String.valueOf(MEDICOSTRAPORCENTAJE) + "%");
                        porcentajeCAR.setText(String.valueOf(MEDICOSCARPORCENTAJE) + "%");
                        porcentajeURO.setText(String.valueOf(MEDICOSUROPORCENTAJE) + "%");
                        porcentajeEND.setText(String.valueOf(MEDICOSENDPORCENTAJE) + "%");
                        porcentajeGAS.setText(String.valueOf(MEDICOSGASPORCENTAJE) + "%");
                        porcentajeGIO.setText(String.valueOf(MEDICOSGIOPORCENTAJE) + "%");
                        porcentajeCIP.setText(String.valueOf(MEDICOSCIPPORCENTAJE) + "%");
                        porcentajeNEA.setText(String.valueOf(MEDICOSNEAPORCENTAJE) + "%");
                        porcentajeCIR.setText(String.valueOf(MEDICOSCIRPORCENTAJE) + "%");
                        porcentajeDER.setText(String.valueOf(MEDICOSDERPORCENTAJE) + "%");
                        porcentajeTotalEspecialidad.setText(String.valueOf(MEDICOSESPECIALIDADTOTALPORCENTAJE) + "%");

                    }else{
                        LyCoberturaMedica.setVisibility(View.GONE);
                    }
                    pgsBar.setVisibility(View.GONE);
                }else{
                    Toast.makeText(getApplicationContext(), "Error al obtener datos", Toast.LENGTH_LONG).show();
                    pgsBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<Estadistica> call, Throwable t) {
                //Log.d("sincronizar Clientes", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                pgsBar.setVisibility(View.GONE); // Ocultar en caso de error
            }
        });
    }
    @Override
    public void onBackPressed() {
        finish(); // Finaliza esta actividad y regresa a la anterior
    }


}
