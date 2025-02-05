package com.rocnarf.rocnarf;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.HistorialViaticoAdapter;
import com.rocnarf.rocnarf.adapters.ViaticoAdapter;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.models.HistorialViatico;
import com.rocnarf.rocnarf.models.MotivoQuejas;
import com.rocnarf.rocnarf.models.Parametros;
import com.rocnarf.rocnarf.models.ParametrosResponse;
import com.rocnarf.rocnarf.models.Periodo;
import com.rocnarf.rocnarf.models.Planes;
import com.rocnarf.rocnarf.models.Viatico;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistorialViaticoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, nombreCliente, seccion;
    private ProgressBar progressBar;
    private Context context;
    private Planes planes;
    private List<HistorialViatico> listaViatico;
    private SearchView searchView;
    private HistorialViaticoAdapter adapter;
    private Button btnGuardarViatico;
    ListView ListViewPlanes;
    private Spinner sMotivo,sCiclo;

    public  List<Periodo> periodos;
    private com.rocnarf.rocnarf.dao.DataBaseHelper DataBaseHelper;
    private SQLiteDatabase sQLiteDatabase;
    public PDFView pdfView;
    public TextView mMovilidad,mAlimentacion,mHospedaje;
    private ALodingDialog aLodingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viatico_historial);
        aLodingDialog = new ALodingDialog(this);
        aLodingDialog.show();
        recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_viatico_historial);
        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion = i.getStringExtra(Common.ARG_SECCIOM);
        context = this;

        CargaData();





    }
    final void CargaData() {

        PlanesService service = ApiClient.getClient().create(PlanesService.class);
        Call<List<HistorialViatico>> call = service.GetHistorialViatico(idUsuario);
        call.enqueue(new Callback<List<HistorialViatico>>() {
            @Override
            public void onResponse(Call<List<HistorialViatico>> call, Response<List<HistorialViatico>> response) {
                if (response.isSuccessful()) {
                    double total1 = 0;
                    double total2 = 0;
                    double total3 = 0;

                    List<HistorialViatico> historialComentariosResponse = response.body();
                    listaViatico = historialComentariosResponse;
                    aLodingDialog.cancel();




                    final HistorialViaticoAdapter.HistorialViaticoListener listener = new HistorialViaticoAdapter.HistorialViaticoListener() {
                        @Override
                        public void HistorialViaticoListener(HistorialViatico historialViatico) {
                            Intent i = new Intent(context, ViaticoActivity.class);
                            i.putExtra(Common.ARG_IDCLIENTE, idCliente);
                            i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                            i.putExtra(Common.ARG_SECCIOM, seccion);
                            Log.d("historial","histiral" + historialViatico.getIdCiclo());
                            i.putExtra(Common.ARG_FROM, String.valueOf(historialViatico.getIdCiclo()));// Historial
                            context.startActivity(i);


                        }
                    };

                    recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_viatico_historial);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    adapter = new HistorialViaticoAdapter(context, listaViatico, listener);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<HistorialViatico>> call, Throwable t) {
                //Log.d("sincronizar Clientes", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            CargaData();
        }
    }

}
