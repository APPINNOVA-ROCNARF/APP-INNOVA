package com.rocnarf.rocnarf;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.GuiaProductosAdapter;
import com.rocnarf.rocnarf.adapters.QuejaAdapter;
import com.rocnarf.rocnarf.adapters.ViaticoAdapter;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.models.ArchivosGuiaProducto;
import com.rocnarf.rocnarf.models.GuiaProductos;
import com.rocnarf.rocnarf.models.GuiaProductosResponse;
import com.rocnarf.rocnarf.models.ListCicloSelect;
import com.rocnarf.rocnarf.models.MotivoQuejas;
import com.rocnarf.rocnarf.models.Parametros;
import com.rocnarf.rocnarf.models.ParametrosResponse;
import com.rocnarf.rocnarf.models.Periodo;
import com.rocnarf.rocnarf.models.Planes;
import com.rocnarf.rocnarf.models.QuejasConsulta;
import com.rocnarf.rocnarf.models.Viatico;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Comparator;
import java.util.Objects;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class ViaticoActivity extends AppCompatActivity  implements ViaticoAdapter.AdapterCallback {
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, idCiclo, seccion;
    private Integer estadoviatico;
    private ProgressBar progressBar;
    private Context context;
    private Planes planes;
    private List<Viatico> listaViatico;
    private SearchView searchView;
    private ViaticoAdapter adapter;
    private Button btnGuardarViatico, btEnviarViatico;
    ListView ListViewPlanes;
    private Spinner sMotivo,sCiclo;
    private boolean historial = false,cons = false;

    public  List<Periodo> periodos;
    private com.rocnarf.rocnarf.dao.DataBaseHelper DataBaseHelper;
    private SQLiteDatabase sQLiteDatabase;
    public PDFView pdfView;
    public TextView mMovilidad,mAlimentacion,mHospedaje,mTotal,mfechaMaxCiclo,mNoMovilidad,mNoAlimentacion,mNoHospedaje,mNoTotal,mestadoCicloText;
    final ArrayList<MotivoQuejas> listaMotivos = new ArrayList<>();
    final ArrayList<ListCicloSelect> listaCiclo = new ArrayList<>();
    private ALodingDialog aLodingDialog;
    private LinearLayout lCabecera;
    private ImageView icoViatico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viatico);
        aLodingDialog = new ALodingDialog(this);
        aLodingDialog.show();

        recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_viatico);
        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion = i.getStringExtra(Common.ARG_SECCIOM);
        idCiclo = i.getStringExtra(Common.ARG_FROM);



        context = this;
        sMotivo = (Spinner) findViewById(R.id.sp_motivos_viaticos);
        sCiclo = (Spinner) findViewById(R.id.sp_ciclos);

        //CargaData();
        btnGuardarViatico = (Button) findViewById(R.id.bt_enviar_viatico);
        btEnviarViatico = (Button) findViewById(R.id.bt_enviar_registro);
        mMovilidad = (TextView) findViewById(R.id.tv_sum_movilidad);
        mAlimentacion = (TextView) findViewById(R.id.tv_sum_alimentacion);
        mHospedaje = (TextView) findViewById(R.id.tv_sum_hospedaje);
        lCabecera = (LinearLayout) findViewById(R.id.id_ped_pen_lay);
        mTotal = (TextView) findViewById(R.id.tv_sum_total);
        mestadoCicloText = (TextView) findViewById(R.id.tv_ciclos);
        mNoMovilidad = (TextView) findViewById(R.id.tv_sum_movilidad_red);
        mNoAlimentacion = (TextView) findViewById(R.id.tv_sum_alimentacion_red);
        mNoHospedaje = (TextView) findViewById(R.id.tv_sum_hospedaje_red);
        mNoTotal = (TextView) findViewById(R.id.tv_sum_total_red);

        mfechaMaxCiclo = (TextView) findViewById(R.id.tv_fechaMaxCiclo);

        if (idCiclo != null){
            historial = true;
            CargaData();
        }else{
            CargarParametrosMotivos();
            CargarPeriodos();
        }

        if(historial){
            lCabecera.setVisibility(View.GONE);

        }else{
            lCabecera.setVisibility(View.VISIBLE);

        }

        sCiclo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el elemento seleccionado
                String selectedItem = parent.getItemAtPosition(position).toString();

                ListCicloSelect cicloSeleccionado = (ListCicloSelect) sCiclo.getSelectedItem();
                SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT, new Locale("es", "ES"));
                if(cicloSeleccionado.getFinPeriodo() != null) {
                    mfechaMaxCiclo.setText(sdf.format(cicloSeleccionado.getFinPeriodo()));
                }
                if(cicloSeleccionado.getEstadoCiclo() != null) {
                    if(cicloSeleccionado.getEstadoCiclo() == 1){
                        btEnviarViatico.setEnabled(false);
                        btnGuardarViatico.setEnabled(false);
                        mestadoCicloText.setText("Registro Enviado");
                    }else{
                        btEnviarViatico.setEnabled(true);
                        btnGuardarViatico.setEnabled(true);
                        mestadoCicloText.setText("Registro Pendiente");
                    }
                }

                idCiclo = String.valueOf(cicloSeleccionado.getId());
                cons = true;


                if (Integer.parseInt(idCiclo) != 0){

                    CargaData();}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se seleccionó nada, no hace falta hacer nada aquí
            }
        });

        btEnviarViatico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ListCicloSelect cicloSeleccionado = (ListCicloSelect) sCiclo.getSelectedItem();

                if(cicloSeleccionado.getId() == 0 ){
                    Toast.makeText(getApplicationContext(), "Seleccion el ciclo ", Toast.LENGTH_LONG).show();
                    return;
                }

                androidx.appcompat.app.AlertDialog.Builder adb = new AlertDialog.Builder(context);
                adb.setTitle("Enviar Registro");
                adb.setMessage("Seguro de enviar los registro?");
                adb.setIcon(android.R.drawable.ic_dialog_alert);
                adb.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        PlanesService service = ApiClient.getClient().create(PlanesService.class);
                        Call<List<Viatico>> call = service.GetViaticoAsesor( 5 , idUsuario, 0, cicloSeleccionado.getId());
                        call.enqueue(new Callback<List<Viatico>>() {
                            @Override
                            public void onResponse(Call<List<Viatico>> call, Response<List<Viatico>> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "El ciclo de" + cicloSeleccionado.getDescripcion() +" se envio con exito", Toast.LENGTH_LONG).show();
                                    recreate();
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Viatico>> call, Throwable t) {
                                //Log.d("sincronizar Clientes", t.getMessage());
                                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });


                    } });

                adb.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    } });
                adb.show();
            }
        });

        btnGuardarViatico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MotivoQuejas motivoSeleccionado = (MotivoQuejas) sMotivo.getSelectedItem();
                ListCicloSelect cicloSeleccionado = (ListCicloSelect) sCiclo.getSelectedItem();
                if(motivoSeleccionado.getId() == 0 ){
                    Toast.makeText(getApplicationContext(), "Seleccion el motivo ", Toast.LENGTH_LONG).show();
                    return;
                }
                if(cicloSeleccionado.getId() == 0 ){
                    Toast.makeText(getApplicationContext(), "Seleccion el ciclo ", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent i = new Intent(context, RegistroViaticoActivity.class);
                i.putExtra(Common.ARG_IDCLIENTE, idCliente);
                i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                i.putExtra(Common.ARG_SECCIOM, seccion);
                i.putExtra(Common.ARG_NOMBRE_CICLO, String.valueOf(cicloSeleccionado.getDescripcion()));
                i.putExtra(Common.ARG_ID_CICLO, String.valueOf(cicloSeleccionado.getId()));
                i.putExtra(Common.ARG_NOMBRE_CONCEPTO, String.valueOf(motivoSeleccionado.getDescripcion()));
                i.putExtra(Common.ARG_ID_CONCEPTO, String.valueOf(motivoSeleccionado.getId()));
                startActivityForResult(i,1);



            }
        });

    }
    @Override
    public void onDataDeleted() {
        // Coloca aquí el código que deseas ejecutar cuando los datos sean eliminados
        // Por ejemplo, para refrescar la actividad principal, podrías llamar a recreate()
        recreate(); // Llama a recreate() en la actividad principal
    }
    public void CargarParametrosMotivos() {
        PlanesService service = ApiClient.getClient().create(PlanesService.class);
        retrofit2.Call<ParametrosResponse> call  = service.GetJefes("MOTIVOSVIATICO");
        call.enqueue(new Callback<ParametrosResponse>() {
            @Override
            public void onResponse(Call<ParametrosResponse> call, Response<ParametrosResponse> response) {
                if (response.isSuccessful()) {
                    ParametrosResponse parametrosResponse = response.body();
                    List<Parametros> parametros = parametrosResponse.items;
                    listaMotivos.add(new MotivoQuejas(0, "Seleccionar Concepto"));

                    for(int indice = 0;indice<parametros.size();indice++){
                        listaMotivos.add(new MotivoQuejas(parametros.get(indice).getIdParametro(), parametros.get(indice).getValor()));
                    }

                    // Utiliza YourActivityName.this en lugar de this
                    final ArrayAdapter<MotivoQuejas> adaptador = new ArrayAdapter<>(ViaticoActivity.this, android.R.layout.simple_list_item_1, listaMotivos);
                    sMotivo.setAdapter(adaptador);

                }
            }


            @Override
            public void onFailure(Call<ParametrosResponse> call, Throwable t) {

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.politicas, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(listaViatico != null ){
                adapter.getFilter().filter(s);}

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    public void CargarPeriodos() {
        PlanesService service = ApiClient.getClient().create(PlanesService.class);
        Call<List<Periodo>> call  = service.GetPeriodos(idUsuario);
        call.enqueue(new Callback<List<Periodo>>() {
            @Override
            public void onResponse(Call<List<Periodo>> call, Response<List<Periodo>> response) {
                if (response.isSuccessful()) {
                    List<Periodo> periodos = response.body();
                    listaCiclo.add(new ListCicloSelect(0, "Seleccionar Ciclo", null, null));

                    for(int indice = 0;indice<periodos.size();indice++){
                        listaCiclo.add(new ListCicloSelect(periodos.get(indice).getIdPeriodo(), periodos.get(indice).getDescripcion() , periodos.get(indice).getFinPeriodo(), periodos.get(indice).getEstadoCiclo()));
                    }

                    // Utiliza YourActivityName.this en lugar de this
                    final ArrayAdapter<ListCicloSelect> adaptador = new ArrayAdapter<>(ViaticoActivity.this, android.R.layout.simple_list_item_1, listaCiclo);
                    sCiclo.setAdapter(adaptador);
                    aLodingDialog.cancel();
                }
            }


            @Override
            public void onFailure(Call<List<Periodo>> call, Throwable t) {

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    final void CargaData() {
        int opcion = 1;
        int paraCiclo = 0;

        if (historial) {
            opcion = 3;
            paraCiclo = Integer.parseInt(idCiclo);
        } else if (cons) {
            opcion = 3;
            paraCiclo = Integer.parseInt(idCiclo);
        }

        PlanesService service = ApiClient.getClient().create(PlanesService.class);
        Call<List<Viatico>> call = service.GetViaticoAsesor(opcion, idUsuario, 0, paraCiclo);
        call.enqueue(new Callback<List<Viatico>>() {
            @Override
            public void onResponse(Call<List<Viatico>> call, Response<List<Viatico>> response) {
                if (response.isSuccessful()) {
                    double total1 = 0;
                    double total2 = 0;
                    double total3 = 0;
                    double totalMovilidadNoEstado2 = 0;
                    double totalAlimentacionNoEstado2 = 0;
                    double totalHospedajeNoEstado2 = 0;

                    List<Viatico> historialComentariosResponse = response.body();

                    if (historialComentariosResponse != null) {
                        for (Viatico viatico : historialComentariosResponse) {
                            Log.d("CargaData", "Viatico recibido: " + viatico.toString());
                        }
                    } else {
                        Log.d("CargaData", "La respuesta es nula.");
                    }

                    Collections.sort(historialComentariosResponse, new Comparator<Viatico>() {
                        @Override
                        public int compare(Viatico v1, Viatico v2) {
                            return v1.getFechaFactura().compareTo(v2.getFechaFactura());
                        }
                    });

                    listaViatico = historialComentariosResponse;



                    for (Viatico viatico : listaViatico) {
                        if (viatico.getEstadoViatico() == 2) {
                            if (viatico.getIdCatalogo() == 128) { // Movilidad
                                total1 += viatico.getTotal();
                            } else if (viatico.getIdCatalogo() == 129) { // Alimentación
                                total2 += viatico.getTotal();
                            } else if (viatico.getIdCatalogo() == 130) { // Hospedaje
                                total3 += viatico.getTotal();
                            }
                        } else {
                            if (viatico.getIdCatalogo() == 128) { // Movilidad
                                totalMovilidadNoEstado2 += viatico.getTotal();
                            } else if (viatico.getIdCatalogo() == 129) { // Alimentación
                                totalAlimentacionNoEstado2 += viatico.getTotal();
                            } else if (viatico.getIdCatalogo() == 130) { // Hospedaje
                                totalHospedajeNoEstado2 += viatico.getTotal();
                            }
                        }
                    }

                    NumberFormat formatter = new DecimalFormat("###,###,##0.00");

                    mMovilidad.setText("$ " + formatter.format(total1));
                    mAlimentacion.setText("$ " + formatter.format(total2));
                    mHospedaje.setText("$ " + formatter.format(total3));
                    mTotal.setText("$ " + formatter.format(total1 + total2 + total3));

                    mNoMovilidad.setText("$ " + formatter.format(totalMovilidadNoEstado2));
                    mNoAlimentacion.setText("$ " + formatter.format(totalAlimentacionNoEstado2));
                    mNoHospedaje.setText("$ " + formatter.format(totalHospedajeNoEstado2));
                    mNoTotal.setText("$ " + formatter.format(totalMovilidadNoEstado2 + totalAlimentacionNoEstado2 + totalHospedajeNoEstado2));

                    aLodingDialog.cancel();

                    final ViaticoAdapter.ViaticoListener listener = new ViaticoAdapter.ViaticoListener() {
                        @Override
                        public void ViaticoListener(Viatico viatico) {
                            Intent i = new Intent(context, RegistroViaticoActivity.class);
                            i.putExtra(Common.ARG_IDCLIENTE, idCliente);
                            i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                            i.putExtra(Common.ARG_HISTORIAL, historial ? "S" : "N");
                            i.putExtra(Common.ARG_SECCIOM, seccion);
                            i.putExtra(Common.ARG_NOMBRE_CICLO, String.valueOf(viatico.getNombreCiclo()));
                            i.putExtra(Common.ARG_ID_CICLO, String.valueOf(viatico.getIdCiclo()));
                            i.putExtra(Common.ARG_NOMBRE_CONCEPTO, String.valueOf(viatico.getNombreCatalogo()));
                            i.putExtra(Common.ARG_ID_CONCEPTO, String.valueOf(viatico.getIdCatalogo()));
                            i.putExtra(Common.ARG_ESTADO_VIATICO, viatico.getEstadoViatico());
                            i.putExtra(Common.ARG_ESTADO_CICLO, viatico.getEstadoCiclo());
                            i.putExtra("Viatico", viatico);
                            startActivityForResult(i, 1);
                        }
                    };

                    recyclerView = findViewById(R.id.rv_list_activity_viatico);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    adapter = new ViaticoAdapter(context, listaViatico, listener, historial);
                    adapter.setAdapterCallback((ViaticoAdapter.AdapterCallback) context);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Viatico>> call, Throwable t) {
                // Manejo del error
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            recreate();
        }
    }


}
