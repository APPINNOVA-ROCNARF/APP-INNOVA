package com.rocnarf.rocnarf;


import android.app.Fragment;
import android.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.legacy.app.FragmentPagerAdapter;
import androidx.legacy.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.models.ClientesCupoCredito;
import com.rocnarf.rocnarf.models.Factura;
import com.rocnarf.rocnarf.models.Pedido;
import com.rocnarf.rocnarf.models.PedidoDetalle;
import com.rocnarf.rocnarf.viewmodel.PedidoViewModel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class PedidoActivity extends AppCompatActivity implements ActionBar.TabListener, PedidoFragment.OnFragmentInteractionListener, PedidoProductoFragment.OnListFragmentInteractionListener {

    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private PedidoViewModel pedidoViewModel;
    private String idUsuario, seccion, idCliente;
    private String mensajeAntesEnviarPedido = "Desea Enviar Pedido?";
    private ClientesCupoCredito cupoCredito;
    private int idLocalPedido;
    private List<Factura> facturasCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);


        Intent intent = getIntent();
        idUsuario = intent.getStringExtra(Common.ARG_IDUSUARIO);
        seccion =  intent.getStringExtra(Common.ARG_SECCIOM);
        idCliente = intent.getStringExtra(Common.ARG_IDCLIENTE);
        idLocalPedido = intent.getIntExtra(Common.ARG_IDPEDIDO, 0);


        pedidoViewModel = ViewModelProviders.of(this).get(PedidoViewModel.class);
        pedidoViewModel.init(idLocalPedido);

        pedidoViewModel.setIdUsuario(this.idUsuario);
        pedidoViewModel.getCupoCredito(this.idCliente).observe(this, new Observer<ClientesCupoCredito>() {
            @Override
            public void onChanged(@Nullable ClientesCupoCredito clientesCupoCredito) {
                cupoCredito = clientesCupoCredito;
            }
        });
        pedidoViewModel.getFacturas(idCliente, seccion).observe(this, new Observer<List<Factura>>() {
            @Override
            public void onChanged(@Nullable List<Factura> facturas) {
                facturasCliente = facturas;
            }
        });

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager(), idUsuario, seccion, idCliente, idLocalPedido);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pedido, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_eliminar_pedido) {
            eliminarPedido();
            return true;
        }else if (id == R.id.action_agregar_producto){
            agregarProducto();
            return true;
        }else if (id == R.id.action_enviar_pedido){
            syncronizarPedido();
        }

        return super.onOptionsItemSelected(item);
    }


    private void eliminarPedido(){
        pedidoViewModel.deletePedido();
        Intent i = new Intent(this, PedidoListaActivity.class);
        i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
        i.putExtra(Common.ARG_SECCIOM, seccion);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

    }

    private void agregarProducto(){
        Intent i = new Intent(this, ProductosActivity.class);
        i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
        i.putExtra(Common.ARG_SECCIOM, seccion);
        i.putExtra(Common.ARG_IDCLIENTE, pedidoViewModel.pedido.getValue().getIdCliente());
        i.putExtra(Common.ARG_NOMBRE_CLIENTE, pedidoViewModel.pedido.getValue().getNombreCliente());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

    }

    private void syncronizarPedido(){

        if (pedidoViewModel.detallesPedido.getValue().size() == 0 ){
            AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
            alert.setTitle("ERROR");
            alert.setMessage("El pedido no contiene productos");
            alert.setPositiveButton("OK",null);
            alert.show();
            return;
            //Toast.makeText(getApplicationContext(), "El pedido no contiene productos", Toast.LENGTH_LONG).show();
        }

//        if (pedidoViewModel.detallesPedido.getValue().size() >20 ){
//            AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
//            alert.setTitle("ERROR");
//            alert.setMessage("El pedido no debe contener mas de 20 productos");
//            alert.setPositiveButton("OK",null);
//            alert.show();
//            return;
//            //Toast.makeText(getApplicationContext(), "El pedido no debe contener mas de 20 productos", Toast.LENGTH_LONG).show();
//        }


        mensajeAntesEnviarPedido = "Desea Enviar Pedido?";
        //Verificar si tiene cupo credito
        if (cupoCredito != null ){
            if (pedidoViewModel.pedido.getValue().getPrecioTotal().compareTo(cupoCredito.getCupoCredito().doubleValue()) > 0 ) {
                mensajeAntesEnviarPedido = "Pedido Supera Cupo de Crédito Disponible. Desea Enviar Pedido?";
            }
        }

        if (facturasCliente  != null){
            for (Factura factura:facturasCliente) {
                BigDecimal saldo = factura.getValor().subtract(factura.getAbonos());
                saldo = saldo.subtract(factura.getNotaCredito());
                if (saldo.compareTo(new BigDecimal(0)) > 0 ) {
                    mensajeAntesEnviarPedido = "Cliente con Cartera Vencida. Desea Enviar Pedido?";
                }
            }
        }

        pedidoViewModel.sincronizado.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s.equals("OK")){
                    //pedidoViewModel.deletePedido();
                    Pedido pedidoCliente = pedidoViewModel.pedido.getValue();
                    pedidoCliente.setFechaPedido(new Date());
                    pedidoCliente.setEstado(Common.PED_SINCRONIZADO);
                    pedidoViewModel.updatePedido(pedidoCliente);
                    Intent i = new Intent(getApplicationContext(), PedidoListaActivity.class);
                    i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    i.putExtra(Common.ARG_SECCIOM, seccion);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);

                    Toast.makeText(getApplicationContext(), "El pedido ha sido enviado", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                }
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setMessage(mensajeAntesEnviarPedido);
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                pedidoViewModel.Sync();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.create().show();

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    // Interaccion con el fragment de productos del pedido
    @Override
    public void onListFragmentInteraction(PedidoDetalle item) {

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private String idUsuario, seccion, idCliente;
        private int idLocalPedido;


        public SectionsPagerAdapter(FragmentManager fm,  String idUsuario, String seccion, String idCliente, int idLocalPedido) {
            super(fm);
            this.idUsuario = idUsuario;
            this.seccion = seccion;
            this.idCliente = idCliente;
            this.idLocalPedido = idLocalPedido;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    //return PedidoFragment.newInstance(this.idUsuario, this.seccion, this.idCliente, this.idLocalPedido);
                    return PedidoFragment.newInstance(this.idUsuario, this.seccion, this.idCliente, this.idLocalPedido);
                case 1:
                    //return PedidoProductoFragment.newInstance(this.idUsuario, this.seccion, this.idCliente, this.idLocalPedido);
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            //return 2;
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Resumen del Pedido";
               /* case 1:
                    return "Detalles";*/
            }
            return null;
        }
    }
}
