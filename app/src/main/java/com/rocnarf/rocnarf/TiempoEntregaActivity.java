package com.rocnarf.rocnarf;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.rocnarf.rocnarf.adapters.EscalaBonificacionRecyclerViewAdapter;
import com.rocnarf.rocnarf.models.EscalaBonificacion;

import java.util.List;

public class TiempoEntregaActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Context context;
    private String idUsuario, seccion;
    public PDFView pdfView;
    private List<EscalaBonificacion> listaEscalas;
    public TextView textView;
    private EscalaBonificacionRecyclerViewAdapter adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_escala_bonificacion);
//
//        Intent i = getIntent();
//        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
//        seccion =  i.getStringExtra(Common.ARG_SECCIOM);
//        context =this;
//
//        progressBar = (ProgressBar)findViewById(R.id.pr_list_activity_escala_bonificacion);
//        progressBar.setVisibility(View.VISIBLE);
//        recyclerView = (RecyclerView) findViewById(R.id.rv_activity_escala_bonificacion);
//        recyclerView.setVisibility(View.GONE);
//        recyclerView.setLayoutManager(new LinearLayoutManager(context));
//
//
//        final ProductosViewModel productosViewModel = ViewModelProviders.of( this).get(ProductosViewModel.class);
//        productosViewModel.setIdUsuario(idUsuario);
//
//        productosViewModel.listaEscalasBonificaciones.observe(this, new Observer<List<EscalaBonificacion>>() {
//            @Override
//            public void onChanged(@Nullable List<EscalaBonificacion> escalaBonificacions) {
//                progressBar.setVisibility(View.GONE);
//                recyclerView.setVisibility(View.VISIBLE);
//                adapter = new EscalaBonificacionRecyclerViewAdapter(escalaBonificacions);
//                recyclerView.setAdapter(adapter);
//            }
//        });
//        productosViewModel.getListaEscalasBonificaciones();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parrilla);
        pdfView = findViewById(R.id.pdf_parrilla);
        progressBar = findViewById(R.id.progressBar_parrila);
        textView = findViewById(R.id.tv_estado_pdf);
        Intent i = getIntent();

        String urlPdf = "https://oficinavirtual.ugr.es/apli/solicitudPAU/test.pdf";
        new PdfTiempoEntrega(pdfView,progressBar,textView).execute();
    }
}
