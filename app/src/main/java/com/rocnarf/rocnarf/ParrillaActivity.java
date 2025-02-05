package com.rocnarf.rocnarf;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.RecyclerView;

import android.widget.ProgressBar;
import android.widget.TextView;


import com.github.barteksc.pdfviewer.PDFView;
import com.rocnarf.rocnarf.Utils.Common;

public class ParrillaActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, nombreCliente, seccion;
    private ProgressBar progressBar;
    private Context context;


    private com.rocnarf.rocnarf.dao.DataBaseHelper DataBaseHelper;
    private SQLiteDatabase sQLiteDatabase;
    public PDFView pdfView;
    public TextView textView;
    public int idGuia;
    public String nombreProducto, mtipo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parrilla);
        pdfView = findViewById(R.id.pdf_parrilla);
        progressBar = findViewById(R.id.progressBar_parrila);
        textView = findViewById(R.id.tv_estado_pdf);

        Intent i = getIntent();
        if (i.getStringExtra(Common.ARG_IDNPLAN) != null) {
            idGuia = Integer.parseInt(i.getStringExtra(Common.ARG_IDNPLAN).trim());
            nombreProducto =  i.getStringExtra(Common.ARG_NOMPBREPLAN);
            mtipo="GUIA";
            this.setTitle(nombreProducto);
        }

        String urlPdf = "https://oficinavirtual.ugr.es/apli/solicitudPAU/test.pdf";
        new PdfLeer(pdfView,progressBar,textView).execute(urlPdf);
//        pdfView.fromAsset().load();

    }


}
