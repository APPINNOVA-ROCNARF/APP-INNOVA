package com.rocnarf.rocnarf;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.dao.ClientesDao;
import com.rocnarf.rocnarf.dao.RocnarfDatabase;
import com.rocnarf.rocnarf.models.Clientes;
import com.rocnarf.rocnarf.models.PanelClientes;

import java.util.ArrayList;
import java.util.List;

public class ClientesBusquedaActivity extends AppCompatActivity {

    private Context mContext;
    private  List<Clientes> ListClientesResult;
    private RecyclerView recyclerView;
    private String idUsuario, codigoCliente, nombre, tipo , sector, representante, ciudad, opcionSeleccionada;
    private int destino, tipoConsulta;

    private com.rocnarf.rocnarf.dao.DataBaseHelper DataBaseHelper;
    private SQLiteDatabase sQLiteDatabase;

    private String NombreCliente, idCliente, Representante;
    private ClientesDao clientesDao;
    private SearchView searchView;
    private ListView lstPaneles;
    private ArrayList<PanelClientes> list_consulta_formulario;
    private List<Clientes> lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes_busqueda);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        idUsuario = intent.getStringExtra("idUsuario");
        destino = intent.getIntExtra("destino", 0);
        sector = intent.getStringExtra(Common.ARG_SECCIOM);

        CargarPanel();

        lstPaneles = findViewById(R.id.list);
        lstPaneles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int i, long l) {

                try {
                    clientesDao = RocnarfDatabase.getDatabase(getApplicationContext()).ClientesDao();
                    codigoCliente =
                            ((PanelClientes)adapterView.getItemAtPosition(i)).getIdCliente();
                    opcionSeleccionada =
                            ((PanelClientes)adapterView.getItemAtPosition(i)).getNombreCliente();

                    Intent in = new Intent(getApplicationContext(), PlanificacionCrearActivity.class);
                    in.putExtra(Common.ARG_IDCLIENTE, codigoCliente);
                    in.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    in.putExtra(Common.ARG_SECCIOM, sector);
                    in.putExtra(Common.ARG_ORIGEN_PLANIFICACION_VISITA, "origenPlanificacionVisita");
                    startActivity(in);

                }catch(Exception ex)
                {
                    Toast.makeText(getApplicationContext(), "A ocurrido un error al intentar agregar el cliente: " + opcionSeleccionada
                            + "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private ArrayList<PanelClientes> getlist_consulta_formulario() {
        list_consulta_formulario = new ArrayList<PanelClientes>();

        this.clientesDao = RocnarfDatabase.getDatabase(getApplicationContext()).ClientesDao();
        lista = new ArrayList<>();
        lista = this.clientesDao.get(null, null, null, sector, null, null, idUsuario);

        for(int indice = 0;indice<lista.size();indice++)
        {
            PanelClientes consulta_formulario_objeto = new PanelClientes();
            consulta_formulario_objeto.setNombreCliente(lista.get(indice).getNombreCliente());
            consulta_formulario_objeto.setIdCliente(lista.get(indice).getIdCliente());
            consulta_formulario_objeto.setRepresentante(lista.get(indice).getRepresentante());
            //consulta_formulario_objeto.setSector(lista.get(indice).getCiudad() + "-" + lista.get(indice).getDireccion());
            list_consulta_formulario.add(consulta_formulario_objeto);
        }

        return list_consulta_formulario;
    }

    public void CargarPanel() {

        ArrayList<PanelClientes> list_consulta_panel = getlist_consulta_formulario();
        lstPaneles = (ListView) findViewById(R.id.list);
        //lstPaneles.setAdapter(new PanelClienteAdapter (this, list_consulta_panel));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.clientes, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //adapter.getFilter(ListClientes).filter(query);
                ListClientesResult = getFilter2(lista, query);
                CargarPanelFilter(ListClientesResult);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                ListClientesResult = getFilter2(lista, query);
                CargarPanelFilter(ListClientesResult);
                return false;
            }
        });

        return true;
    }

    public List<Clientes> getFilter2(List<Clientes> Values, String chquery) {

        String query = chquery;
        List<Clientes> filtered = new ArrayList<>();

        if (query.isEmpty()) {
            filtered = Values;
        } else {
            for (Clientes Busqueda : Values) {
                if (Busqueda.getNombreCliente().toLowerCase().contains(query.toLowerCase())
                        || Busqueda.getIdCliente().toLowerCase().contains(query.toLowerCase())
                        || Busqueda.getCiudad().toLowerCase().contains(query.toLowerCase())
                        || Busqueda.getDireccion().toLowerCase().contains(query.toLowerCase())
                        || Busqueda.getTipoObserv().toLowerCase().contains(query.toLowerCase())
                        || Busqueda.getClaseMedico().toLowerCase().contains(query.toLowerCase())
                        || Busqueda.getOrigen().toLowerCase().contains(query.toLowerCase())
                        /* || Busqueda.getRepresentante().toLowerCase().contains(query.toLowerCase())*/

                ) {
                    filtered.add(Busqueda);
                }
            }
        }

        return filtered;

    }

    private ArrayList<PanelClientes> getfilter(List<Clientes> Values) {
        list_consulta_formulario = new ArrayList<PanelClientes>();


        for(int indice = 0;indice<Values.size();indice++)
        {
            PanelClientes consulta_formulario_objeto = new PanelClientes();
            consulta_formulario_objeto.setNombreCliente(Values.get(indice).getNombreCliente());
            consulta_formulario_objeto.setIdCliente(Values.get(indice).getIdCliente());
            consulta_formulario_objeto.setRepresentante(Values.get(indice).getRepresentante());
            consulta_formulario_objeto.setDireccion(Values.get(indice).getDireccion());
            consulta_formulario_objeto.setTipoObserv(Values.get(indice).getTipoObserv());


            //consulta_formulario_objeto.setSector(Values.get(indice).getCiudad() + "-" + Values.get(indice).getDireccion());
            list_consulta_formulario.add(consulta_formulario_objeto);
        }

        return list_consulta_formulario;
    }

    public void CargarPanelFilter(List<Clientes> Values) {

        ArrayList<PanelClientes> list_consulta_panel = getfilter(Values);
        lstPaneles = (ListView) findViewById(R.id.list);
        //lstPaneles.setAdapter(new PanelClienteAdapter (this, list_consulta_panel));
    }
}
