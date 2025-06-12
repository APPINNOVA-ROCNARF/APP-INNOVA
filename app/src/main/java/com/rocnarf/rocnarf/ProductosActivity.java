package com.rocnarf.rocnarf;

import android.app.SearchManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.ProductosReciclerViewAdapter;
import com.rocnarf.rocnarf.dao.DataBaseHelper;
import com.rocnarf.rocnarf.models.EscalaBonificacion;
import com.rocnarf.rocnarf.models.Pedido;
import com.rocnarf.rocnarf.models.PedidoDetalle;
import com.rocnarf.rocnarf.models.Producto;
import com.rocnarf.rocnarf.viewmodel.PedidoViewModel;
import com.rocnarf.rocnarf.viewmodel.ProductosViewModel;

import java.text.SimpleDateFormat;
import java.util.List;

import rx.Completable;
import rx.Subscription;

public class ProductosActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, nombreCliente, seccion;
    private ProgressBar progressBar;
    private Context context;
    private Pedido pedidoCliente;
    private List<EscalaBonificacion> listaEscalas;
    List<PedidoDetalle> pedidoDetalles;
    private LiveData<Pedido> pedidoExistemte;

    private SearchView searchView;
    private MenuItem icPedido;
    private ProductosReciclerViewAdapter adapter;
    private PedidoViewModel pedidoViewModel;

    private com.rocnarf.rocnarf.dao.DataBaseHelper DataBaseHelper;
    private SQLiteDatabase sQLiteDatabase;
    private boolean usarPrecioEspecial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);

        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion = i.getStringExtra(Common.ARG_SECCIOM);
        idCliente = i.getStringExtra(Common.ARG_IDCLIENTE);
        nombreCliente = i.getStringExtra(Common.ARG_NOMBRE_CLIENTE);
        usarPrecioEspecial = getIntent().getBooleanExtra(Common.ARG_USAR_PRECIO_ESPECIAL, false);
        context = this;

        final ActionBar actionBar = getSupportActionBar();
        if (nombreCliente != null) {
            if (!nombreCliente.isEmpty()) {
                actionBar.setTitle(nombreCliente);
                actionBar.setSubtitle(idCliente);
            }
        }
        progressBar = (ProgressBar) findViewById(R.id.pr_list_activity_productos);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_productos);
        recyclerView.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        final ProductosViewModel productosViewModel = ViewModelProviders.of(this).get(ProductosViewModel.class);
        productosViewModel.setIdUsuario(idUsuario);

        productosViewModel.listaEscalasBonificaciones.observe(this, new Observer<List<EscalaBonificacion>>() {
            @Override
            public void onChanged(@Nullable List<EscalaBonificacion> escalaBonificacions) {
                listaEscalas = escalaBonificacions;
            }
        });
        productosViewModel.getListaEscalasBonificaciones();

        if (idCliente != null) {
            productosViewModel.getPedido(idCliente, nombreCliente, Common.TIPO_PEDIDO_PEDIDO).subscribe(new Completable.CompletableSubscriber() {
                @Override
                public void onCompleted() {
                    pedidoCliente = productosViewModel.pedido.getValue();

                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onSubscribe(Subscription d) {

                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
        }

        pedidoViewModel = ViewModelProviders.of(this).get(PedidoViewModel.class);

        if(pedidoCliente != null){
        pedidoViewModel.init(pedidoCliente.getIdLocalPedido());
        pedidoViewModel.getPedidoByIdLocal(pedidoCliente.getIdLocalPedido());
        pedidoDetalles = pedidoViewModel.getDetallesPedido(pedidoCliente.getIdLocalPedido()).getValue();
        }
        final ProductosReciclerViewAdapter.AddProductoListener listenerAddProducto = new ProductosReciclerViewAdapter.AddProductoListener() {
            @Override
            public void AddProducto(Producto producto, int cantidad, int bono) {
                try {
                    pedidoViewModel.init(pedidoCliente.getIdLocalPedido());
                    pedidoViewModel.getPedidoByIdLocal(pedidoCliente.getIdLocalPedido());
                    pedidoDetalles = pedidoViewModel.getDetallesPedido(pedidoCliente.getIdLocalPedido()).getValue();
                    ///Log.d("pedidooooo", "ssssss" + pedidoDetalles);

                    //Log.d("pedidooooo", "pedidoDetalles" +  pedidoDetalles);
                    for (int i = 0; i < pedidoDetalles.size(); i++) {
                        if( pedidoDetalles.get(i).getIdProducto().equals(producto.getIdProducto())){
                            pedidoDetalles.get(i).setCantidad(cantidad);
                            pedidoDetalles.get(i).setBono(bono);
                            pedidoViewModel.updatePedidoDetalle(pedidoDetalles.get(i));
                            Toast.makeText(context, "Datos actualizados", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }


                    SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);
                    if (cantidad <= 0) {
                        Toast.makeText(context, "Debes indicar la cantidad", Toast.LENGTH_LONG).show();
                        return;
                    }
                    PedidoDetalle pedidoDetalle = new PedidoDetalle();
                    pedidoDetalle.setIdLocalPedido(pedidoCliente.getIdLocalPedido());
                    pedidoDetalle.setIdPedido(0);
                    pedidoDetalle.setIdProducto(producto.getIdProducto());
                    pedidoDetalle.setTipo(producto.getTipo());
                    pedidoDetalle.setCantidad(cantidad);
                    pedidoDetalle.setBono(bono);
                    pedidoDetalle.setNombre(producto.getNombre());
                    pedidoDetalle.setPrecio(producto.getPrecio());
                    pedidoDetalle.setPvp(producto.getPvp());
                    pedidoDetalle.setPvf(producto.getPrecio());
                    pedidoDetalle.setEsp(producto.getPrecioEspecial());
                    if(usarPrecioEspecial) {
                        pedidoDetalle.setPrecioTotal(cantidad * producto.getPrecioEspecial());
                    }else{
                        pedidoDetalle.setPrecioTotal(cantidad * producto.getPrecio());
                    }

                    productosViewModel.addDetallePedido(pedidoDetalle);


                    Toast.makeText(context, producto.getNombre() + " a sido agregrado al pedido", Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Toast.makeText(context, " Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        };


        productosViewModel.listaProductos.observe(this, new Observer<List<Producto>>() {
            @Override
            public void onChanged(@Nullable List<Producto> productos) {
//                if (pedidoCliente == null) progressBar.setVisibility(View.GONE);
                
                recyclerView.setVisibility(View.VISIBLE);
                adapter = new ProductosReciclerViewAdapter(productos, listaEscalas, idCliente, listenerAddProducto, pedidoDetalles, usarPrecioEspecial);
                recyclerView.setAdapter(adapter);
            }
        });

        productosViewModel.getListaProductos(usarPrecioEspecial, idCliente);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.producto, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        if (nombreCliente == null) {
            icPedido = (MenuItem) menu.findItem(R.id.action_pedidos);
            icPedido.setVisible(false);
        }


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_pedidos) {
            //Intent i = new Intent(this, PedidoActivity.class);
            //Pequeño cambio de la forma de pantalla por solicitud del cliente
            Intent i = new Intent(this, PedidoSimpleActivity.class);
            i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
            i.putExtra(Common.ARG_SECCIOM, seccion);
            i.putExtra(Common.ARG_IDCLIENTE, idCliente);
            i.putExtra(Common.ARG_NOMBRE_CLIENTE, nombreCliente);
            i.putExtra(Common.ARG_IDPEDIDO, pedidoCliente.getIdLocalPedido());
            i.putExtra(Common.ARG_USAR_PRECIO_ESPECIAL, usarPrecioEspecial);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
