package com.rocnarf.rocnarf;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.dao.ClientesDao;
import com.rocnarf.rocnarf.models.Clientes;
import com.rocnarf.rocnarf.adapters.ClientesAdapter;
import com.rocnarf.rocnarf.models.PanelClientes;
import com.rocnarf.rocnarf.repository.ClientesRepository;
import com.rocnarf.rocnarf.repository.PanelClientesRepository;

import java.util.List;


public class ResultadoClientesSemanalActivity extends AppCompatActivity {

    private Context mContext;
    private String idUsuario, codigoCliente, nombre, tipo , seccion, representante, ciudad, opcionSeleccionada, rol;
    private int destino, tipoConsulta;


    private Button btPanelCliente;
    private TextView mMedicos;
    private TextView mClientes,mClientesZ;
    private TextView mClientesCobertura, mMedicoCobertura;
    private android.widget.ProgressBar ProgressBar, ProgressBar2;
    private ClientesDao clientesDao;
    private SearchView searchView;
    private RecyclerView lstPaneles;
    private Clientes cliente;
    private PanelClientesRepository panelClientesRepository;
    private Spinner mEspecialidadSpi;

    private ClientesRepository clientesRepository;
    private ClientesAdapter clientesAdapter;
    private ClientesAdapter.OnClienteClickListener listener = new ClientesAdapter.OnClienteClickListener() {
        @Override
        public void onClienteClick(Clientes cliente) {
            try {
                PanelClientes panelClientes = new PanelClientes();

                panelClientes.setIdCliente(cliente.getIdCliente());
                panelClientes.setNombreCliente(cliente.getNombreCliente());
                panelClientes.setRepresentante(cliente.getRepresentante());
                panelClientes.setCiudad(cliente.getCiudad());
                panelClientes.setDireccion(cliente.getDireccion());
                panelClientes.setIdUsuario(idUsuario);
                panelClientes.setOrigen(cliente.getOrigen());
                panelClientes.setEstadoSeleccion(cliente.getEstadoSeleccion());
                panelClientes.setEstadoVisita(cliente.getEstadoVisita());
                panelClientes.setClaseMedico(cliente.getClaseMedico());
                panelClientes.setIdEspecialidades(cliente.getIdEspecialidades());
                panelClientes.setTipo(cliente.getTipo());
                panelClientes.setCumpleAnyos(cliente.getCumpleAnyos());
                panelClientes.setRevisita(cliente.getRevisita());
                Log.d("xxxx","xxxx"+ panelClientes.getCumpleAnyos());
                if (cliente.getEstadoSeleccion() != null) {
                    panelClientesRepository.addPanelCliente(panelClientes);
                    Toast.makeText(getApplicationContext(), "Cliente agregado al panel para planificar: " + cliente.getNombreCliente()
                            , Toast.LENGTH_LONG).show();
                }else{
                    panelClientesRepository.deletePanelCliente(cliente.getIdCliente());
                    Toast.makeText(getApplicationContext(), "Cliente eliminado del panel para planificar: " + cliente.getNombreCliente()
                            , Toast.LENGTH_LONG).show();
                }
                //List<PanelClientes> clientes =
                setCantidades();
            }catch(Exception ex)
            {
                Toast.makeText(getApplicationContext(), "A ocurrido un error al intentar agregar el cliente: " + cliente.getNombreCliente()
                        + "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_clientes_semanal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        idUsuario= intent.getStringExtra("idUsuario");
        seccion = intent.getStringExtra(Common.ARG_SECCIOM);
        rol = intent.getStringExtra(Common.ARG_ROL);
        panelClientesRepository = new PanelClientesRepository(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        CargarPanel();

        mMedicos = (TextView)findViewById(R.id.tv_medico_activity_cliente_semanal);
        mClientes = (TextView)findViewById(R.id.tv_clientes_activity_cliente_semanal);
        mClientesZ = (TextView)findViewById(R.id.tv_cliente_z_activity_cliente_semanal);

        //mEspecialidadSpi = (Spinner) findViewById(R.id.especialidad_panel_control);
        setCantidades();
        btPanelCliente = (Button)findViewById(R.id.bt_panel_clientes);
        btPanelCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Seccion para planificar clientes seleccionados."
                        , Toast.LENGTH_LONG).show();

                /*Se va para pantalla de planificacion semanal -- ini*/
                Intent i = new Intent(getApplicationContext(), PanelClientesActivity.class);
                i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                i.putExtra(Common.ARG_SECCIOM, seccion);
                startActivity(i);
            }
        });

//        final ArrayList<String> listaNombre = new ArrayList<>();
//        listaNombre.add("Ninguno");
//        final ArrayAdapter adaptador = new ArrayAdapter(this, android.R.layout.simple_list_item_1,listaNombre);
//        PlanesService service = ApiClient.getClient().create(PlanesService.class);
//        retrofit2.Call<ParametrosResponse> call  = service.GetJefes("ESPECIALIDADES");
//        call.enqueue(new Callback<ParametrosResponse>() {
//            @Override
//            public void onResponse(retrofit2.Call<ParametrosResponse> call, Response<ParametrosResponse> response) {
//                if (response.isSuccessful()) {
//                    ParametrosResponse parametrosResponse = response.body();
//                    List<Parametros> parametros = parametrosResponse.items;
//                    for(int indice = 0;indice<parametros.size();indice++){
//
//
//                        listaNombre.add(parametros.get(indice).getDescripcion());
//                    }
//
//                    mEspecialidadSpi.setAdapter(adaptador);
//
//
//                }
//            }
//
//
//            @Override
//            public void onFailure(Call<ParametrosResponse> call, Throwable t) {
//
//                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });


//        mEspecialidadSpi.setOnItemSelectedListener(
//         new AdapterView.OnItemSelectedListener() {
//                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//
//                    Object item = parent.getItemAtPosition(pos);
//                    System.out.println(item.toString());     //prints the text in spinner item.
//
//                }
//                public void onNothingSelected(AdapterView<?> parent) {
//                }
//            });

    }
    @Override
    protected void onResume() {
        super.onResume();
        setCantidades();
    }

    public void CargarPanel() {
        clientesRepository = new ClientesRepository(this, idUsuario);

        List<Clientes> clientes = clientesRepository.getClientes(seccion, idUsuario, null, rol);
        clientesAdapter =  new ClientesAdapter(this, listener, clientes,idUsuario);
        lstPaneles = (RecyclerView) findViewById(R.id.list);
        lstPaneles.setLayoutManager(new LinearLayoutManager(this));
        lstPaneles.setAdapter(clientesAdapter);
        int contadorCliente = 0;
        int contadorMedico = 0;
        int contadorClienteEfec = 0;
        int contadorMedicoEfec = 0;
        for(int indice = 0;indice<clientes.size();indice++)
        {
            if (clientes.get(indice).getOrigen().equals("MEDICO")) {
                contadorMedico++;
                if (clientes.get(indice).getEstadoVisita() != null && clientes.get(indice).getEstadoVisita().equals("EFECT")) {
                    contadorMedicoEfec++;
                }
            } else {
                contadorCliente++;
                if (clientes.get(indice).getEstadoVisita() != null  && clientes.get(indice).getEstadoVisita().equals("EFECT")) {
                    contadorClienteEfec++;
                }
            }
        }
        mClientesCobertura = (TextView)findViewById(R.id.cobertura_cliente);
        mMedicoCobertura = (TextView)findViewById(R.id.cobertura_medico);

        mClientesCobertura.setText(contadorCliente > 0 ?  ("Clientes   " + (contadorClienteEfec * 100 /contadorCliente)+ "%" ): "Clientes   0%" );
        mMedicoCobertura.setText(contadorMedicoEfec > 0 ? ("Medicos    " + (contadorMedicoEfec * 100 /contadorMedico)+ "%" ) :  "Medicos    0%");

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.my_progressBar);
        ProgressBar progressBar2 = (ProgressBar) findViewById(R.id.my_progressBar2);
//        Log.d("RocnarfDatabase", "populating with data..." + (contadorMedicoEfec * 100 /contadorCliente));
        progressBar.setProgress(contadorCliente > 0 ? (contadorClienteEfec * 100 /contadorCliente) : 0);
        progressBar2.setProgress(contadorMedicoEfec > 0 ? (contadorMedicoEfec * 100 /contadorMedico) : 0);
    }

    public void setCantidades(){
        List<PanelClientes> medicos = panelClientesRepository.getPanelClientesXOrigen(idUsuario, "MEDICO");
        List<PanelClientes> clientes = panelClientesRepository.getPanelClientesXOrigen(idUsuario, "FARMA");//ACTIVO
        List<PanelClientes> clientesZ = panelClientesRepository.getPanelClientesXTipo(idUsuario);
        if (medicos!= null)  mMedicos.setText("Medicos: " + medicos.size());
        if (clientes!= null)  mClientes.setText("Clientes: " + clientes.size());
        if (clientesZ!= null)  mClientesZ.setText("Clientes Z: " + clientesZ.size());

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
                clientesAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                clientesAdapter.getFilter().filter(query);
                return false;
            }
        });

        return true;
    }


}
