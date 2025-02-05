package com.rocnarf.rocnarf;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.PedidosPendienteDetalleAdapter;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.models.PedidosPendiente;
import com.rocnarf.rocnarf.models.PedidosPendienteComentarios;
import com.rocnarf.rocnarf.models.PedidosPendienteComentariosResponse;
import com.rocnarf.rocnarf.models.PedidosPendienteDetalle;
import com.rocnarf.rocnarf.models.PedidosPendienteDetalleResponse;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidosPendienteDetalleActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, nombreCliente, seccion;
    private ProgressBar progressBar;
    private Context context;
    private SearchView searchView;
    private List<PedidosPendienteDetalle> listaPedidosPendienteDetalle;

    private PedidosPendienteDetalleAdapter adapter ;
    private com.rocnarf.rocnarf.dao.DataBaseHelper DataBaseHelper;
    private SQLiteDatabase sQLiteDatabase;
    public  PedidosPendiente pedidoDetalle;
    private TextView mVendedor,mPedido,mfecha,mEstado,mValor,mSaldo,mComentrioVendedor,mComentarioCartera,mNumeroFac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_pendiente_detalle);
        SimpleDateFormat sdf = new SimpleDateFormat(Common.DATE_FORMAT);

        recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_pedidos_pendiente_detalle_pro);
        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion =  i.getStringExtra(Common.ARG_SECCIOM);
        context =this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        final ActionBar actionBar = getSupportActionBar();

        mPedido = (TextView) findViewById(R.id.tv_idFactura_row_clientes_facturas_pedPen);
        mVendedor = (TextView) findViewById(R.id.tv_vendedor_row_clientes_facturas_ped);
        mNumeroFac =(TextView) findViewById(R.id.tv_vendedor_row_clientes_facturas_ped_numero);
        mfecha = (TextView) findViewById(R.id.tv_fecha_row_clientes_facturas_pedPen);
        mEstado = (TextView) findViewById(R.id.tv_estado_desp_pedPen);
        mValor= (TextView) findViewById(R.id.tv_valor_row_clientes_facturas_pedPen);
        mSaldo= (TextView) findViewById(R.id.tv_saldo_row_clientes_facturas_penPed);
        mComentrioVendedor = (TextView) findViewById(R.id.tv_Comentario_vend);
        mComentarioCartera = (TextView) findViewById(R.id.tv_Comentario_cart);
        pedidoDetalle = (PedidosPendiente) i.getSerializableExtra("pedidosPendiente");



        NumberFormat formatter = new DecimalFormat("###,###,##0.00");

        String formattedValor = formatter.format(pedidoDetalle.getValor());
        String formattedSaldo = formatter.format(pedidoDetalle.getValor());

        mPedido.setText(pedidoDetalle.getFactura());

        if(pedidoDetalle.getDespachado() != null) mNumeroFac.setText(pedidoDetalle.getDespachado());
        if(pedidoDetalle.getDespachado() != null) mPedido.setText(pedidoDetalle.getFactura());

        mVendedor.setText(pedidoDetalle.getVendedor() + " - " + pedidoDetalle.getSeccion());
        mfecha.setText(sdf.format(pedidoDetalle.getFecha()));

        if (mNumeroFac.getText().equals("")){
            mEstado.setText("Por Despachar");
            mEstado.setTextColor(Color.RED);
        }else{
            mEstado.setText("Despachado");
            mEstado.setTextColor(Color.GREEN);
        }

        mValor.setText(formattedValor);
        mSaldo.setText(formattedSaldo);
        this.setTitle(pedidoDetalle.getNomcli());

        //Log.d("sincronizar Clientes", t.getMessage());

        PlanesService service = ApiClient.getClient().create(PlanesService.class);
        Log.d("user","getDespachado" + pedidoDetalle.getDespachado());

        Call<PedidosPendienteDetalleResponse> call  = service.GetPedidosPendienteDetalle(pedidoDetalle.getFactura(),pedidoDetalle.getDespachado());
        call.enqueue(new Callback<PedidosPendienteDetalleResponse>() {
            @Override
            public void onResponse(Call<PedidosPendienteDetalleResponse> call, Response<PedidosPendienteDetalleResponse> response) {
                if (response.isSuccessful()){

                    PedidosPendienteDetalleResponse pedidosPendienteDetalleResponse = response.body();
                    //List<PedidosPendienteDetalle> pedidosPendienteDetalles = pedidosPendienteDetalleResponse.items;
                    listaPedidosPendienteDetalle =  pedidosPendienteDetalleResponse.items;
                    CargaComentarios();

                    Log.d("listaPedidosPendiente", "xx" + listaPedidosPendienteDetalle);

                    final PedidosPendienteDetalleAdapter.PedidosPendienteDetalleListener listener =  new PedidosPendienteDetalleAdapter.PedidosPendienteDetalleListener() {
                        @Override
                        public void PedidosPendienteDetalleListener(PedidosPendienteDetalle pedidosPendienteDetalle) {


                        }
                    };

                    recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_pedidos_pendiente_detalle_pro);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    adapter = new PedidosPendienteDetalleAdapter(listaPedidosPendienteDetalle, listener);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<PedidosPendienteDetalleResponse> call, Throwable t) {
                //Log.d("sincronizar Clientes", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(context, PedidosPendienteActivity.class);
                i.putExtra(Common.ARG_IDCLIENTE, idCliente);
                i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                i.putExtra(Common.ARG_SECCIOM, seccion);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void CargaComentarios(){
        PlanesService service = ApiClient.getClient().create(PlanesService.class);
        Call<PedidosPendienteComentariosResponse> call  = service.GetPedidosPendienteComentario(pedidoDetalle.getFactura());
        call.enqueue(new Callback<PedidosPendienteComentariosResponse>() {
            @Override
            public void onResponse(Call<PedidosPendienteComentariosResponse> call, Response<PedidosPendienteComentariosResponse> response) {
                if (response.isSuccessful()){
                    PedidosPendienteComentariosResponse pedidosPendienteComentariosResponse = response.body();
                    PedidosPendienteComentarios pedidosPendienteComentarios = pedidosPendienteComentariosResponse.items.get(0);
                    mComentrioVendedor.setText(pedidosPendienteComentarios.getComentarioVen());
                    mComentarioCartera.setText(pedidosPendienteComentarios.getComentarioCar());


                }
            }

            @Override
            public void onFailure(Call<PedidosPendienteComentariosResponse> call, Throwable t) {
                //Log.d("sincronizar Clientes", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
