package com.rocnarf.rocnarf;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.dao.RocnarfDatabase;
import com.rocnarf.rocnarf.dao.UsuariosDao;
import com.rocnarf.rocnarf.models.Parametros;
import com.rocnarf.rocnarf.models.ParametrosResponse;
import com.rocnarf.rocnarf.models.Usuario;

import java.util.ArrayList;
import java.util.List;

import static com.rocnarf.rocnarf.ResultadoVisitaActivity.ACOMPANADO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioActivity extends AppCompatActivity {
    private UsuariosDao usuariosDao;
    private ListView list;
    private ListView lstPaneles;
    private List<Usuario> ListUsuariosResult;
    private ArrayList<Usuario> list_consulta_formulario;
    private String codigoCliente, idUsuario, seccion, nombreCliente, nombreAcompanate;
    private  int idLocal;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        Intent intent = getIntent();
        codigoCliente = intent.getStringExtra(Common.ARG_IDCLIENTE);
        idLocal = intent.getIntExtra(Common.ARG_IDVISITALOCAL, 0);
        idUsuario = intent.getStringExtra(Common.ARG_IDUSUARIO);
        seccion = intent.getStringExtra(Common.ARG_SECCIOM);
        nombreCliente = intent.getStringExtra(Common.ARG_NOMBRE_CLIENTE);
        CargarPanel();
        lstPaneles = findViewById(R.id.list);

        lstPaneles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int i, long l) {

                try {
//                    Log.d("myTag", "tocaste" + adapterView.getItemAtPosition(i) );
                    Intent intent = new Intent();
                    String nombre = adapterView.getItemAtPosition(i).toString();
                    intent.putExtra(ACOMPANADO, nombre);
//                    Intent in =  new Intent(getApplicationContext(), ResultadoVisitaActivity.class);
//                    nombreAcompanate = adapterView.getItemAtPosition(i);
//                    in.putExtra(Common.adapterView.getItemAtPosition(i), codigoCliente);
//                    resultIntent = new Intent(null);
//                    resultIntent.putExtra(PUBLIC_STATIC_STRING_IDENTIFIER, enteredTextValue);
                    setResult(RESULT_OK, intent);
//                    i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
//                    i.putExtra("idLocal", idLocal);
//                    startActivity(in);
                    finish();
//                    clientesDao = RocnarfDatabase.getDatabase(getApplicationContext()).ClientesDao();
//                    codigoCliente =
//                            ((PanelClientes)adapterView.getItemAtPosition(i)).getIdCliente();
//                    opcionSeleccionada =
//                            ((PanelClientes)adapterView.getItemAtPosition(i)).getNombreCliente();
//
//                    Intent in = new Intent(getApplicationContext(), PlanificacionCrearActivity.class);
//                    in.putExtra(Common.ARG_IDCLIENTE, codigoCliente);
//                    in.putExtra(Common.ARG_IDUSUARIO, idUsuario);
//                    in.putExtra(Common.ARG_SECCIOM, sector);
//                    in.putExtra(Common.ARG_ORIGEN_PLANIFICACION_VISITA, "origenPlanificacionVisita");
//                    startActivity(in);

                }catch(Exception ex)
                {
//                    Toast.makeText(getApplicationContext(), "A ocurrido un error al intentar agregar el cliente: " + opcionSeleccionada
//                            + "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private ArrayList<Usuario> getlist_consulta_formulario() {
        list_consulta_formulario = new ArrayList<Usuario>();

        this.usuariosDao = RocnarfDatabase.getDatabase(getApplicationContext()).UsuariosDao();
        ListUsuariosResult = new ArrayList<>();
//        Log.d("myTag", "entro " +  this.usuariosDao.getAllUser());
        ListUsuariosResult =  this.usuariosDao.getAllUser();
//        Log.d("myTag", "aaray " +  ListUsuariosResult);
        Usuario consulta_formulario_objeto = new Usuario();

        for(int indice = 0;indice<ListUsuariosResult.size();indice++)
        {

            consulta_formulario_objeto.setNombre(ListUsuariosResult.get(indice).getNombre());
//            Log.d("myTag", "aaray 1 " +  consulta_formulario_objeto.getNombre());
//            consulta_formulario_objeto.setIdCliente(ListUsuariosResult.get(indice).getIdCliente());
//            consulta_formulario_objeto.setRepresentante(ListUsuariosResult.get(indice).getRepresentante());
            //consulta_formulario_objeto.setSector(lista.get(indice).getCiudad() + "-" + lista.get(indice).getDireccion());
            list_consulta_formulario.add(consulta_formulario_objeto);
        }
//        Log.d("myTag", "aaray 2 " +  list_consulta_formulario);

        return list_consulta_formulario;
    }


    public void CargarPanel() {
        final ArrayList<String> listaNombre = new ArrayList<>();
        PlanesService service = ApiClient.getClient().create(PlanesService.class);
        retrofit2.Call<ParametrosResponse> call  = service.GetJefes("JEFES");
        call.enqueue(new Callback<ParametrosResponse>() {
            @Override
            public void onResponse(Call<ParametrosResponse> call, Response<ParametrosResponse> response) {
                if (response.isSuccessful()) {
                    ParametrosResponse parametrosResponse = response.body();
                    List<Parametros> parametros = parametrosResponse.items;
                    for(int indice = 0;indice<parametros.size();indice++){


                        listaNombre.add(parametros.get(indice).getDescripcion());
                        }

                    lstPaneles = (ListView) findViewById(R.id.list);
                    ArrayAdapter adaptador = new ArrayAdapter(context, android.R.layout.simple_list_item_1,listaNombre);
                    lstPaneles.setAdapter(adaptador);
                }
            }


            @Override
            public void onFailure(Call<ParametrosResponse> call, Throwable t) {

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}

