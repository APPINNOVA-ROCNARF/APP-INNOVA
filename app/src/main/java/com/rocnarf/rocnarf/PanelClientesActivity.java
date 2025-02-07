package com.rocnarf.rocnarf;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.rocnarf.rocnarf.Utils.Common;
import java.util.List;

import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ListView;

import com.rocnarf.rocnarf.adapters.PanelClienteAdapter;
import com.rocnarf.rocnarf.models.PanelClientes;
import com.rocnarf.rocnarf.repository.PanelClientesRepository;

import android.widget.TextView;
import android.widget.Toast;

public class PanelClientesActivity extends AppCompatActivity {


    private String idUsuario, codigoCliente, nombre, tipo , sector, representante, ciudad;
    private int destino, tipoConsulta;
    private TextView mMedicos;
    private TextView mClientes,mClientesZ;


    private ListView lstPaneles;
    private Button btnIrPedidos;
    private String NombreCliente, idCliente, Representante;
    private PanelClientesRepository panelClientesRepository;
    private PanelClienteAdapter.OnPanelClientesClickListener panelClientesClickListener = new PanelClienteAdapter.OnPanelClientesClickListener() {
        @Override
        public void onPanelClientesClick(PanelClientes clientes) {
            Toast.makeText(getApplicationContext(), "Planificar visita a cliente: " + clientes.getNombreCliente()
                    , Toast.LENGTH_LONG).show();

            Intent in = new Intent(getApplicationContext(), PlanificacionCrearActivity.class);
            in.putExtra(Common.ARG_IDCLIENTE, clientes.getIdCliente());
            in.putExtra(Common.ARG_IDUSUARIO, idUsuario);
            in.putExtra(Common.ARG_SECCIOM, sector);
            in.putExtra(Common.ARG_ESTADOVISTA,  clientes.getEstadoVisita());
            in.putExtra(Common.ARG_REVISITA, clientes.getRevisita());
            in.putExtra(Common.ARG_ORIGEN_PLANIFICACION_VISITA, Common.VISITA_DESDE_PANEL);
            startActivity(in);

        }
    };

    private PanelClienteAdapter.OnPanelClientesLongClickListener panelClientesLongClickListener = new PanelClienteAdapter.OnPanelClientesLongClickListener() {
        @Override
        public void OnPanelClientesLongClick(final PanelClientes clientes) {


            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(PanelClientesActivity.this, R.style.myDialog));
            LayoutInflater inflater = getLayoutInflater();
            builder.setMessage("Seguro de eliminar a " + clientes.getNombreCliente() + " del panel de planificacion");
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    clientes.setEstadoSeleccion("1");
                    panelClientesRepository.delete(clientes);
                    Toast.makeText(getApplicationContext(), "cliente " + clientes.getNombreCliente() + " eliminado del panel"
                            , Toast.LENGTH_LONG).show();
                    CargarPanel();
                    setCantidades();

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            builder.create().show();


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_clientes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Manejar eventos de clic del botón de retroceso del Toolbar
        toolbar.setNavigationOnClickListener(view -> onBackPressed());



        panelClientesRepository = new PanelClientesRepository(this);

        Intent intent = getIntent();
        idUsuario = intent.getStringExtra("idUsuario");
        sector = intent.getStringExtra(Common.ARG_SECCIOM);

        CargarPanel();
        mMedicos = (TextView)findViewById(R.id.tv_medicos_activity_panel_clientes);
        mClientes = (TextView)findViewById(R.id.tv_clientes_activity_panel_clientes);
        mClientesZ = (TextView)findViewById(R.id.tv_clientez_activity_panel_clientes);
        setCantidades();


    }

    public void setCantidades(){
//        List<PanelClientes> medicos = panelClientesRepository.getPanelClientesXOrigen(idUsuario, "MEDICO");
//        List<PanelClientes> clientes = panelClientesRepository.getPanelClientesXOrigen(idUsuario, "FARMA");
//        if (medicos!= null)  mMedicos.setText("Medicos: " + medicos.size());
//        if (clientes!= null)  mClientes.setText("   Clientes: " + clientes.size());

        List<PanelClientes> medicos = panelClientesRepository.getPanelClientesXOrigen(idUsuario, "MEDICO");
        List<PanelClientes> clientes = panelClientesRepository.getPanelClientesXOrigen(idUsuario, "FARMA");//ACTIVO
        List<PanelClientes> clientesZ = panelClientesRepository.getPanelClientesXTipo(idUsuario);
        if (medicos!= null)  mMedicos.setText("Medicos: " + medicos.size());
        if (clientes!= null)  mClientes.setText("Clientes: " + clientes.size());
        if (clientesZ!= null)  mClientesZ.setText("Clientes Z: " + clientesZ.size());

    }

    public void CargarPanel() {
        List<PanelClientes> list_consulta_panel = panelClientesRepository.getPanelClientes(idUsuario);
        final RecyclerView lstPaneles = (RecyclerView) findViewById(R.id.list);
        lstPaneles.setLayoutManager(new LinearLayoutManager(this));
        lstPaneles.setAdapter(new PanelClienteAdapter (this, panelClientesClickListener, panelClientesLongClickListener, list_consulta_panel));
    }

    /*public void onBackPressed(){
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
        i.putExtra(Common.ARG_SECCIOM, sector);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
*/
}
