package com.rocnarf.rocnarf;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.HistorialComentariosAdapter;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.models.HistorialComentarios;
import com.rocnarf.rocnarf.models.Promocionado;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistorialProductosPromocionadoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, nombreCliente, seccion,idVisita;
    private ProgressBar progressBar;
    private Context context;
    private HistorialComentarios historialComentarios;
    private List<HistorialComentarios> listaComentario;
    private SearchView searchView;
    private HistorialComentariosAdapter adapter ;
    ListView ListViewPlanes;
    public  List<Promocionado> historialVisitas;
    private com.rocnarf.rocnarf.dao.DataBaseHelper DataBaseHelper;
    private SQLiteDatabase sQLiteDatabase;
    TableRow tableRow;
    TextView textView;
    private Calendar mCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_producto_promocionado);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
        //recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_comentarios);
        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion =  i.getStringExtra(Common.ARG_SECCIOM);
        idCliente =  i.getStringExtra(Common.ARG_IDCLIENTE);
        idVisita = i.getStringExtra(Common.ARG_IDVISITA);
        //nombreCliente =  i.getStringExtra(Common.ARG_NOMBRE_CLIENTE);
        context =this;
        PlanesService service = ApiClient.getClient().create(PlanesService.class);
        Log.d("sincronizar Clientes", "idVisita"+ idVisita );
        Call<List<Promocionado>> call  = service.GetPromocionado(idVisita);
        call.enqueue(new Callback<List<Promocionado>>() {
            @Override
            public void onResponse(Call<List<Promocionado>> call, Response<List<Promocionado>> response) {
                if (response.isSuccessful()){
                    historialVisitas = response.body();

        TableLayout tableLayout = (TableLayout)findViewById(R.id.tableLayout_producto_promocionado);
//        TableLayout tableLayout = new TableLayout(getApplicationContext());
        mCalendar = Calendar.getInstance();
        int fecha = 1;
        for (int x = 0; x < historialVisitas.size(); x++) {
            tableRow = new TableRow(getApplicationContext());

            textView = new TextView(getApplicationContext());
            textView.setText(historialVisitas.get(x).getProducto());
            textView.setTextColor(Color.BLACK);
//            textView.setPadding(20, 20, 20, 20);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tableRow.addView(textView);

            textView = new TextView(getApplicationContext());
            textView.setText(historialVisitas.get(x).getItem());
            textView.setTextColor(Color.BLACK);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//            textView.setPadding(20, 20, 20, 20);
            tableRow.addView(textView);
//            TableRow tableRow = (TableRow)findViewById(R.id.table_producto_promocionado);
//            for (int j = 0; j < 1; j++) {
//                textView = new TextView(getApplicationContext());
////                if(x == 0) {
////                    textView.setText("Fecha" );
////                    textView.setTextColor(Color.BLACK);
////                    textView.setPadding(20, 20, 20, 20);
////                    tableRow.addView(textView);
////                }else {
//
//                    textView.setText(historialVisitas.get(j).getProducto());
//                    textView.setTextColor(Color.BLACK);
//                    textView.setPadding(20, 20, 20, 20);
//                    tableRow.addView(textView);
//                    fecha = fecha  + 1;
////                }
//            }


            tableLayout.addView(tableRow);
        }
//        setContentView(tableLayout);


                }
            }
//
            @Override
            public void onFailure(Call<List<Promocionado>> call, Throwable t) {
                //Log.d("sincronizar Clientes", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }


}
