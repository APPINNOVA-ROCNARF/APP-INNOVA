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
import com.rocnarf.rocnarf.models.HistorialVisitas;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistorialVisitasActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, nombreCliente, seccion;
    private ProgressBar progressBar;
    private Context context;
    private HistorialComentarios historialComentarios;
    private List<HistorialComentarios> listaComentario;
    private SearchView searchView;
    private HistorialComentariosAdapter adapter ;
    ListView ListViewPlanes;
    public  List<HistorialVisitas> historialVisitas;
    private com.rocnarf.rocnarf.dao.DataBaseHelper DataBaseHelper;
    private SQLiteDatabase sQLiteDatabase;
    TableRow tableRow;
    TextView textView;
    View view;
    private Calendar mCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_visitas);

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
        //nombreCliente =  i.getStringExtra(Common.ARG_NOMBRE_CLIENTE);
        context =this;
        PlanesService service = ApiClient.getClient().create(PlanesService.class);
        Call<List<HistorialVisitas>> call  = service.getHistorialVisita(idCliente);
        call.enqueue(new Callback<List<HistorialVisitas>>() {
            @Override
            public void onResponse(Call<List<HistorialVisitas>> call, Response<List<HistorialVisitas>> response) {
                if (response.isSuccessful()){
                    historialVisitas = response.body();


//                    Array HistorialVisitas = response.body().string();
//                    Log.d("HistorialVisitas", "HistorialVisitas"+ myResponse  );
//                    final JSONArray myResponse;
//                    try {
//                        myResponse = new JSONArray(response.body());
//
//                        for(int indice = 0;indice<myResponse.length();indice++){
//                            myResponse.getJSONObject(indice);
//                            Log.d("sincronizar Clientes", "xxx"+  myResponse.getJSONObject(indice));
//
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }


//                    List<HistorialComentarios> historialComentarios = historialComentariosResponse.items;
//                    listaComentario =  historialComentariosResponse.items;
//
//                    final HistorialComentariosAdapter.HistoralComentariosListener listener =  new HistorialComentariosAdapter.HistoralComentariosListener() {
//                        @Override
//                        public void HistoralComentariosListener(HistorialComentarios historialComentarios) {
//
////                            Log.d( "ver", "planes.getIdCodigo() " + planes.getIdCodigo() );
//
//                        }
//                    };
        TableLayout tableLayout = (TableLayout)findViewById(R.id.tableLayout_visita);
//        TableLayout tableLayout = new TableLayout(getApplicationContext());
        mCalendar = Calendar.getInstance();
        int fecha = 1;
        int acuTotal = 0;
        for (int x = 0; x < 14; x++) {
            tableRow = new TableRow(getApplicationContext());
            view = new View(getApplicationContext());
            view.setBackgroundColor(Color.GREEN);
            view.layout(1,1,1,1);
//            view.setBackground(context.getResources(R.values));

            for (int j = 0; j < 1; j++) {
                textView = new TextView(getApplicationContext());
                if(x == 0) {
                    textView.setText("Fecha" );
                    textView.setTextColor(Color.BLACK);
                    textView.setPadding(20, 20, 20, 20);
                    tableRow.addView(textView);
                }else if(x == 13) {
                    textView.setText("Total " );
                    textView.setTextColor(Color.BLACK);
                    textView.setPadding(20, 20, 20, 20);
                    tableRow.addView(textView);
                } else {
                    textView.setText(fecha + "/" + mCalendar.get(Calendar.YEAR) );
                    textView.setTextColor(Color.GRAY);
                    textView.setPadding(20, 20, 20, 20);
                    tableRow.addView(textView);
                    fecha = fecha  + 1;

                }
            }

            for(int indice = 0;indice<historialVisitas.size();indice++)
            {
                textView = new TextView(getApplicationContext());
                if(x == 0) {
                    textView.setText(historialVisitas.get(indice).getIdAsesor());
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    textView.setTextColor(Color.BLACK);
                    textView.setPadding(20, 20, 20, 20);
                    tableRow.addView(textView);

                }else {
//                    acuTotal = 0;
                    switch (x) {
                        case 1:

                            textView.setText(String.valueOf(historialVisitas.get(indice).getEnero()));
                            textView.setTextColor(Color.GRAY);
                            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            textView.setPadding(20, 20, 20, 20);
                            tableRow.addView(textView);
                            break;
                        case 2:

                            textView.setText(String.valueOf(historialVisitas.get(indice).getFebrero()));
                            textView.setTextColor(Color.GRAY);
                            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            textView.setPadding(20, 20, 20, 20);
                            tableRow.addView(textView);
                            break;
                        case 3:

                            textView.setText(String.valueOf(historialVisitas.get(indice).getMarzo()));
                            textView.setTextColor(Color.GRAY);
                            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            textView.setPadding(20, 20, 20, 20);
                            tableRow.addView(textView);
                            break;
                        case 4:

                            textView.setText(String.valueOf(historialVisitas.get(indice).getAbril()));
                            textView.setTextColor(Color.GRAY);
                            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            textView.setPadding(20, 20, 20, 20);
                            tableRow.addView(textView);
                            break;
                        case 5:

                            textView.setText(String.valueOf(historialVisitas.get(indice).getMayo()));
                            textView.setTextColor(Color.GRAY);
                            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            textView.setPadding(20, 20, 20, 20);
                            tableRow.addView(textView);
                            break;
                        case 6:

                            textView.setText(String.valueOf(historialVisitas.get(indice).getJunio()));
                            textView.setTextColor(Color.GRAY);
                            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            textView.setPadding(20, 20, 20, 20);
                            tableRow.addView(textView);
                            break;
                        case 7:

                            textView.setText(String.valueOf(historialVisitas.get(indice).getJulio()));
                            textView.setTextColor(Color.GRAY);
                            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            textView.setPadding(20, 20, 20, 20);
                            tableRow.addView(textView);
                            break;
                        case 8:

                            textView.setText(String.valueOf(historialVisitas.get(indice).getAgosto()));
                            textView.setTextColor(Color.GRAY);
                            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            textView.setPadding(20, 20, 20, 20);
                            tableRow.addView(textView);
                            break;
                        case 9:

                            textView.setText(String.valueOf(historialVisitas.get(indice).getSeptiembre()));
                            textView.setTextColor(Color.GRAY);
                            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            textView.setPadding(20, 20, 20, 20);
                            tableRow.addView(textView);
                            break;
                        case 10:

                            textView.setText(String.valueOf(historialVisitas.get(indice).getOctubre()));
                            textView.setTextColor(Color.GRAY);
                            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            textView.setPadding(20, 20, 20, 20);
                            tableRow.addView(textView);
                            break;
                        case 11:

                            textView.setText(String.valueOf(historialVisitas.get(indice).getNoviembre()));
                            textView.setTextColor(Color.GRAY);
                            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            textView.setPadding(20, 20, 20, 20);
                            tableRow.addView(textView);
                            break;
                        case 12:

                            textView.setText(String.valueOf(historialVisitas.get(indice).getDiciembre()));
                            textView.setTextColor(Color.GRAY);
                            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            textView.setPadding(20, 20, 20, 20);
                            tableRow.addView(textView);
                            break;
                        case 13:
                            acuTotal = 0;
//                            for(int indicex = 0;indicex<historialVisitas.size();indicex++)

//                            {
                                if(historialVisitas.get(indice).getEnero() > 0 ){ acuTotal = acuTotal + historialVisitas.get(indice).getEnero();}
                                if(historialVisitas.get(indice).getFebrero() > 0 ){ acuTotal = acuTotal + historialVisitas.get(indice).getFebrero();}
                                if(historialVisitas.get(indice).getMarzo() > 0 ){ acuTotal = acuTotal + historialVisitas.get(indice).getMarzo();}
                                if(historialVisitas.get(indice).getAbril() > 0 ){ acuTotal = acuTotal + historialVisitas.get(indice).getAbril();}
                                if(historialVisitas.get(indice).getMayo() > 0 ){ acuTotal = acuTotal + historialVisitas.get(indice).getMayo();}
                                if(historialVisitas.get(indice).getJunio() > 0 ){ acuTotal = acuTotal + historialVisitas.get(indice).getJunio();}
                                if(historialVisitas.get(indice).getJulio() > 0 ){ acuTotal = acuTotal + historialVisitas.get(indice).getJulio();}
                                if(historialVisitas.get(indice).getAgosto() > 0 ){ acuTotal = acuTotal + historialVisitas.get(indice).getAgosto();}
                                if(historialVisitas.get(indice).getSeptiembre() > 0 ){ acuTotal = acuTotal + historialVisitas.get(indice).getSeptiembre();}
                                if(historialVisitas.get(indice).getOctubre() > 0 ){ acuTotal = acuTotal + historialVisitas.get(indice).getOctubre();}
                                if(historialVisitas.get(indice).getNoviembre() > 0 ){ acuTotal = acuTotal + historialVisitas.get(indice).getNoviembre();}
                                if(historialVisitas.get(indice).getDiciembre() > 0 ){ acuTotal = acuTotal + historialVisitas.get(indice).getDiciembre();}
//                            }
                            Log.d( "ver", "acuTotal ---> " + acuTotal );
                           // if(historialVisitas.get(indice).getDiciembre() == 1){ acuTotal = acuTotal + 1;}
//                            Log.d( "ver", "acuTotal ---> " + historialVisitas.get(indice).getAsesor() );

                            textView.setText(String.valueOf(acuTotal));
                            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            textView.setTextColor(Color.BLACK);
                            textView.setPadding(20, 20, 20, 20);
                            tableRow.addView(textView);
//                        default:
                            break;

                    }

                }
            }

            tableLayout.addView(tableRow);
        }
//        setContentView(tableLayout);

//                    TableLayout tbl = (TableLayout)findViewById(R.id.tableLayout_visita);
////// delcare a new row
//                    TableRow newRow = new TableRow(context);
////// add views to the row
//                    textView = new TextView(getApplicationContext());
//                    textView.setText("test");
//                    textView.setTextColor(Color.BLACK);
//                    textView.setPadding(20, 20, 20, 20);
//                    tableRow.addView(textView);
//                    newRow.addView(new TextView(context)); // you would actually want to set properties on this before adding it
////// add the row to the table layout
//                    tbl.addView(newRow);

//                    recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_comentarios);
//                    recyclerView.setVisibility(View.VISIBLE);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
//                    adapter = new HistorialComentariosAdapter(listaComentario, listener);
//                    recyclerView.setAdapter(adapter);


                }
            }
//
            @Override
            public void onFailure(Call<List<HistorialVisitas>> call, Throwable t) {
                //Log.d("sincronizar Clientes", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }


}
