package com.rocnarf.rocnarf;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import java.util.List;

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
    private TextView mActVisVal,mActVisPor,mCliActNoVal,mCliActNoPor,mCliActTotalVal,mCliActTotalPor,
    mCliActFacVal,mCliActFacPor,mCliActSinFacVal,mCliActSinFacPor,mCliActSinTotalVal,mCliActSinTotalPor,
            mCliInac,mCliZ,mCliCupUtiVal,mCliCupUtiPor,mCliCredVal,mCliCredPor,mCliCupTotalVal,mCliCupTotalPoc
            ,mCobVal,mCobPor,mSdiVal,mSdiPor,mIntVal,mIntPor,mDerVal,mDerPor,mTopVal,mTopPor,mTotalVal,mTotalPor;


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

                    mCliInac.setText(estadistica.getCliInt().toString());
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
                }
            }

            @Override
            public void onFailure(Call<Estadistica> call, Throwable t) {
                //Log.d("sincronizar Clientes", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
