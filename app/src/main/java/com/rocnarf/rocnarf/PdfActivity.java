package com.rocnarf.rocnarf;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;

//import org.apache.poi.xslf.usermodel.XMLSlideShow;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PdfActivity extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener {

    private PDFView pdfView;
    private String urlPdf;
    private Uri pdfUri;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        pdfView = findViewById(R.id.pdfView);
        progressBar = findViewById(R.id.progressBar_pdf);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null && getIntent().hasExtra("urlPdf")) {
            urlPdf = getIntent().getStringExtra("urlPdf");
            pdfUri = Uri.parse(urlPdf);
            //cargarPDF();
        } else {
            Toast.makeText(this, "URL de PDF no válida", Toast.LENGTH_SHORT).show();
            finish();
        }
        progressBar.setVisibility(View.VISIBLE);

        // Obtén la URL del PDF de los extras del intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            urlPdf = extras.getString("urlPdf");
        }

        // Descarga y muestra el PDF
        new DownloadPdfTask().execute(urlPdf);

    }

    private class DownloadPdfTask extends AsyncTask<String, Void, byte[]> {
        @Override
        protected byte[] doInBackground(String... params) {
            String downloadUrl = params[0];

            try {
                URL url = new URL(downloadUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    InputStream inputStream = urlConnection.getInputStream();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                    byte[] buffer = new byte[1024];
                    int bytesRead;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }

                    return outputStream.toByteArray();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(byte[] documentBytes) {
            progressBar.setVisibility(View.GONE);
            if (documentBytes != null) {
                Log.d("aaaaaaaaa","aaaaaa"+ urlPdf);
                if (urlPdf.endsWith(".pptx")) {
                    Log.d("aaaaaaaaa","ppt"+ urlPdf);

                    // Convertir .ppt a PDF usando Apache POI
                 //   byte[] pdfBytes = convertPptToPdf(documentBytes);
                    showPdf(documentBytes);
                } else {
                    Log.d("aaaaaaaaa","pdf"+ urlPdf);
                    // Si no es .ppt, mostrar el PDF normalmente
                    showPdf(documentBytes);
                }
            }
        }
    }

//    private byte[] convertPptToPdf(byte[] pptBytes) {
//        try {
//            InputStream inputStream = new ByteArrayInputStream(pptBytes);
//            XMLSlideShow ppt = new XMLSlideShow(inputStream);
//
//            // Crear un nuevo PDF
//            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
//            ppt.write(pdfOutputStream);
//            ppt.close();
//
//            return pdfOutputStream.toByteArray();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    private void showPdf(byte[] pdfBytes) {
        if (pdfBytes != null) {
            // Cargar y mostrar el PDF
            pdfView.fromBytes(pdfBytes)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
                    .onLoad(onLoadCompleteListener)
                    .onPageChange(onPageChangeListener)
                    .pageFitPolicy(FitPolicy.WIDTH)
                    .spacing(10)
                    .load();
        }
    }


    private OnLoadCompleteListener onLoadCompleteListener = new OnLoadCompleteListener() {
        @Override
        public void loadComplete(int nbPages) {
            // Manejar la carga completa del PDF si es necesario
        }
    };

    private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageChanged(int page, int pageCount) {
            // Manejar el cambio de página si es necesario
        }
    };
//    private void cargarPDF() {
//        pdfView.fromUri(pdfUri)
//                //.pages(0) // Puedes cambiar el número de páginas a cargar
//                .enableSwipe(true)
//                .swipeHorizontal(false)
//                .enableDoubletap(true)
//                .defaultPage(0)
//                .onLoad(this)
//                .onPageChange(this)
//                .pageFitPolicy(FitPolicy.WIDTH)
//                .spacing(10) // Ajusta el espaciado según sea necesario
//                .load();
//    }

    @Override
    public void loadComplete(int nbPages) {
        // Puedes realizar acciones adicionales después de cargar el PDF
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        // Puedes realizar acciones adicionales al cambiar de página
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Manejar los clics de los elementos del menú de la ActionBar
        switch (item.getItemId()) {
            case android.R.id.home:
                // Acción cuando se hace clic en el botón de retroceso
                onBackPressed(); // Esto cierra la actividad actual y vuelve a la actividad anterior
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

