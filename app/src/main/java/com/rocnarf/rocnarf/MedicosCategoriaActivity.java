package com.rocnarf.rocnarf;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.RecetasXAdapter;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.models.MedicosEvaluacion;
import com.rocnarf.rocnarf.models.Recetas;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicosCategoriaActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, nombreCliente, seccion, categoriaMed;
    private ProgressBar progressBar;
    private Context context;
    private Recetas recetas;
    private MedicosEvaluacion listaMedico;
    private SearchView searchView;
    private Button btnRocnarf, btnTodos, btnMercado;
    private RecetasXAdapter adapter;
    private ProgressBar pgsBar;
    private TextView categoria,totalPuntos,quitilRx,quitilRxInfo,rxroc,rxrocInfo,afinidad,afinidadInfo,compra,compraInfo,aseguradora,aseguradoraInfo;
    private com.rocnarf.rocnarf.dao.DataBaseHelper DataBaseHelper;
    private SQLiteDatabase sQLiteDatabase;
    int opcionBtn = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicos_categoria);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        final ActionBar actionBar = getSupportActionBar();

        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion = i.getStringExtra(Common.ARG_SECCIOM);
        idCliente = i.getStringExtra(Common.ARG_IDCLIENTE);
        categoriaMed = i.getStringExtra(Common.ARG_CATEGORIAMED);
        pgsBar = (ProgressBar) findViewById(R.id.pBar_recetas);

        context = this;
        categoria = (TextView)findViewById(R.id.textView2_md_cat_value);
        totalPuntos = (TextView)findViewById(R.id.textView2_md_total_value);
        quitilRx = (TextView)findViewById(R.id.textView2_md_cqui_value);
        quitilRxInfo = (TextView)findViewById(R.id.textView4_med_cat);
        rxroc = (TextView)findViewById(R.id.textView2_md_rxroc_value);
        rxrocInfo = (TextView)findViewById(R.id.textView5_med_cat);
        afinidad = (TextView)findViewById(R.id.textView2_md_Afinidad_value);
        afinidadInfo = (TextView)findViewById(R.id.textView5_med_catAfinidad);
        compra = (TextView)findViewById(R.id.textView2_md_Compra_value);
        compraInfo = (TextView)findViewById(R.id.textView5_med_catCompra);
        aseguradora = (TextView)findViewById(R.id.textView2_md_Aseguradora_value);
        aseguradoraInfo = (TextView)findViewById(R.id.textView5_med_catAseguradora);

        CargaData("1");

    }


     final void CargaData(String opcion){
//         pgsBar.setVisibility(View.VISIBLE);

         PlanesService service = ApiClient.getClient().create(PlanesService.class);

Log.d("ssssssssssss","ssss" + seccion);
        Call<MedicosEvaluacion> call = service.GetMedicosCategoria(idCliente, seccion);
        call.enqueue(new Callback<MedicosEvaluacion>() {
            @Override
            public void onResponse(Call<MedicosEvaluacion> call, Response<MedicosEvaluacion> response) {
                if (response.isSuccessful()) {
                    MedicosEvaluacion recetasXResponse = response.body();

                    listaMedico = recetasXResponse;
                    listaMedico.setCategoriaMedico("MEDICO "+ categoriaMed);
                    categoria.setText(listaMedico.getCategoriaMedico());

                    DecimalFormat df = new DecimalFormat("0.00");
                    DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
                    ///sym.setDecimalSeparator(',');
                    df.setDecimalFormatSymbols(sym);

                    totalPuntos.setText(df.format(listaMedico.getTotal()).toString() + " pts");


                    quitilRx.setText(String.valueOf(listaMedico.getQuintil()));
                    quitilRxInfo.setText(df.format(listaMedico.getQuiltelClose()) + " / 2.00 pts");

                    rxroc.setText(df.format(listaMedico.getRxRocnarf()).toString() +"%");
                    rxrocInfo.setText(df.format(listaMedico.getPxRocnarf()) + " / 1.25 pts");

                    afinidad.setText(String.valueOf(listaMedico.getAfinidad()));
                    afinidadInfo.setText(df.format(listaMedico.getAfinidadRelacion()) + " / 1.00 pts");

                    compra.setText(listaMedico.getCompra());
                    compraInfo.setText(df.format(listaMedico.getCompraNo()) +  " / 0.50 pts");

                    aseguradora.setText(listaMedico.getPrestadora());
                    aseguradoraInfo.setText(df.format(listaMedico.getPrestadoraSalud()) +  " / 0.25 pts");

                }
            }

            @Override
            public void onFailure(Call<MedicosEvaluacion> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
