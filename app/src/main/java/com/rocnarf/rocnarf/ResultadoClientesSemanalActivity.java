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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ResultadoClientesSemanalActivity extends AppCompatActivity {

    private Context mContext;
    private String idUsuario, codigoCliente, nombre, tipo , seccion,secciones, representante, ciudad, opcionSeleccionada, rol;
    private int destino, tipoConsulta;
    private Button btnFilterClientType;
    private Button btnFilterVisitStatus;


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
    private List<String> seccionesUsuario;

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

                // Validación si es "MEDICO"
                String claseMostrar = null;

// Lista de roles especiales
                List<String> rolesMultiples = Arrays.asList("GRC", "GVP", "GRA", "JVC", "GRS", "JVS");

                if ("MEDICO".equals(cliente.getOrigen()) && "MEDICO".equals(cliente.getTipoObserv())) {

                    if (seccion != null && !seccion.isEmpty()) {

                        boolean esRolRegional = rolesMultiples.contains(seccion.toUpperCase());

                        if (esRolRegional && seccionesUsuario != null && !seccionesUsuario.isEmpty()) {
                            for (String seccion : seccionesUsuario) {
                                if (seccion != null && !seccion.isEmpty()) {
                                    char primerCaracter = seccion.charAt(0);

                                    if (Character.isDigit(primerCaracter)) {
                                        int numero = Character.getNumericValue(primerCaracter);
                                        if (numero >= 1 && numero <= 6 && cliente.getClase() != null && !cliente.getClase().isEmpty()) {
                                            claseMostrar = cliente.getClase();
                                            break;
                                        } else if (numero >= 7 && numero <= 9 && cliente.getClase3() != null && !cliente.getClase3().isEmpty()) {
                                            claseMostrar = cliente.getClase3();
                                            break;
                                        }
                                    } else if ((primerCaracter == 'A' || primerCaracter == 'B' || primerCaracter == 'C')
                                            && cliente.getClase4() != null && !cliente.getClase4().isEmpty()) {
                                        claseMostrar = cliente.getClase4();
                                        break;
                                    }
                                }
                            }

                        } else {
                            // 🧪 Evaluar una sola sección
                            char primerCaracter = seccion.charAt(0);

                            if (Character.isDigit(primerCaracter)) {
                                int numero = Character.getNumericValue(primerCaracter);
                                if (numero >= 1 && numero <= 6 && cliente.getClase() != null && !cliente.getClase().isEmpty()) {
                                    claseMostrar = cliente.getClase();
                                } else if (numero >= 7 && numero <= 9 && cliente.getClase3() != null && !cliente.getClase3().isEmpty()) {
                                    claseMostrar = cliente.getClase3();
                                }
                            } else if ((primerCaracter == 'A' || primerCaracter == 'B' || primerCaracter == 'C')
                                    && cliente.getClase4() != null && !cliente.getClase4().isEmpty()) {
                                claseMostrar = cliente.getClase4();
                            }
                        }
                    }

                    if (claseMostrar != null && !claseMostrar.isEmpty()) {
                        panelClientes.setClaseMedico("Médico " + claseMostrar);
                    } else {
                        panelClientes.setClaseMedico("Médico PC");
                    }

                } else {
                    if (cliente.getClaseMedico() != null && !cliente.getClaseMedico().isEmpty()) {
                        panelClientes.setClaseMedico(cliente.getClaseMedico());
                    } else {
                        panelClientes.setClaseMedico("");
                    }
                }




                panelClientes.setIdEspecialidades(cliente.getIdEspecialidades());
                panelClientes.setTipo(cliente.getTipo());
                panelClientes.setCumpleAnyos(cliente.getCumpleAnyos());

                int esRevisita = 0;

                if (cliente.getRevisita() != null && cliente.getRevisita() == 1 && seccion.equals(cliente.getSeccion2())) {
                    esRevisita = 1;
                } else if (cliente.getRevisita3() != null && cliente.getRevisita3() == 1 && seccion.equals(cliente.getSeccion3())) {
                    esRevisita = 1;
                } else if (cliente.getRevisita4() != null && cliente.getRevisita4() == 1 && seccion.equals(cliente.getSeccion4())) {
                    esRevisita = 1;
                }

                panelClientes.setRevisita(esRevisita);
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
        secciones = intent.getStringExtra(Common.ARG_SECCIONES);
        if (secciones != null && !secciones.isEmpty()) {
            this.seccionesUsuario = Arrays.asList(secciones.split("\\s*,\\s*"));
        } else {
            this.seccionesUsuario = new ArrayList<>();
        }
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

        setupFilterButtons();

    }

    private void setupFilterButtons() {
        btnFilterClientType = findViewById(R.id.btn_filter_client_type);
        btnFilterVisitStatus = findViewById(R.id.btn_filter_visit_status);

        btnFilterClientType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showClientTypeFilterMenu(view);
            }
        });

        btnFilterVisitStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showVisitStatusFilterMenu(view);
            }
        });
    }

    private void showClientTypeFilterMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.menu_client_type_filter, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                String tipoCliente = "";

                if (id == R.id.filter_all_clients) {
                    tipoCliente = "Todos";
                } else if (id == R.id.filter_clientes) {
                    tipoCliente = "Clientes";
                } else if (id == R.id.filter_medicos) {
                    tipoCliente = "Médicos";
                } else if (id == R.id.filter_clientes_z) {
                    tipoCliente = "Clientes Z";
                }

                btnFilterClientType.setText(tipoCliente);
                clientesAdapter.setFiltroTipoCliente(tipoCliente);
                return true;
            }
        });

        popup.show();
    }

    private void showVisitStatusFilterMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.menu_visit_status_filter, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                String estadoVisita = "";

                if (id == R.id.filter_all_visits) {
                    estadoVisita = "Todos";
                } else if (id == R.id.filter_visited) {
                    estadoVisita = "Visitados";  // EFECT o PEFECT
                } else if (id == R.id.filter_not_visited) {
                    estadoVisita = "No Visitados";  // PLANI o NOEFE
                }

                btnFilterVisitStatus.setText(estadoVisita);
                clientesAdapter.setFiltroEstadoVisita(estadoVisita); // Aplicar filtro
                return true;
            }
        });

        popup.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCantidades();
    }

    public void CargarPanel() {
        clientesRepository = new ClientesRepository(this, idUsuario);

        List<Clientes> clientes = clientesRepository.getClientes(seccion, idUsuario, null, rol);
        clientesAdapter =  new ClientesAdapter(this, listener, clientes,idUsuario, seccion, secciones);
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
                clientesAdapter.filtrarPorTexto(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                clientesAdapter.filtrarPorTexto(query);
                return false;
            }
        });

        return true;
    }


}
