package com.rocnarf.rocnarf;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.FileDownloadService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PdfGuiasProductosActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, nombreCliente, seccion;
    private ProgressBar progressBar;
    private Context context;


    private com.rocnarf.rocnarf.dao.DataBaseHelper DataBaseHelper;
    private SQLiteDatabase sQLiteDatabase;
    public PDFView pdfView;
    public int idGuia;
    public String nombreProducto, mtipo;
    public TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parrilla);
        pdfView = findViewById(R.id.pdf_parrilla);
        progressBar = findViewById(R.id.progressBar_parrila);
        textView = findViewById(R.id.tv_estado_pdf);

        Intent i = getIntent();
            idGuia = Integer.parseInt(i.getStringExtra(Common.ARG_IDNPLAN).trim());
            nombreProducto =  i.getStringExtra(Common.ARG_NOMPBREPLAN);
            mtipo="GUIA";
            this.setTitle(nombreProducto);


        String urlPdf = "https://oficinavirtual.ugr.es/apli/solicitudPAU/test.pdf";
//        new PdfLeer(pdfView,progressBar).execute(urlPdf);
                    Log.d("idGuia", "idGuia" + idGuia);
            FileDownloadService downloadService = ApiClient.getClient().create(FileDownloadService.class);
            Call<ResponseBody> call = downloadService.downloadGuia(idGuia);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.body() != null){
                    pdfView.fromStream(response.body().byteStream()).load();
                    }else{
                        textView.setVisibility(View.VISIBLE);
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("file", "error");
                }
            });

    }


}
