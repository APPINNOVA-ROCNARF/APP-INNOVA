package com.rocnarf.rocnarf;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActivityVisorDoc extends AppCompatActivity {
    private String urlPdf;
    private Uri pdfUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visor_doc);

        WebView webView = findViewById(R.id.webView);

        // Ruta del archivo que deseas abrir
        String filePath = "https://ruta-al-archivo/documento.pdf";

        // Configurar la WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);  // Habilitar JavaScript si es necesario

        // Cargar el documento
        //webView.loadUrl("https://k4f4w9c2.stackpathcdn.com/wp-content/uploads/01_big_files_kim7/2019_best_ppt/industry%204.0-Revolution-PowerPoint-Templates.pptx");
        // Ruta del archivo PPT que deseas abrir
        String pptUrl = "https://ruta-al-archivo/tu_archivo.pptx";

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            urlPdf = extras.getString("urlPdf");
        }

        // Iniciar la descarga del archivo PPT
        new DownloadPptTask().execute(urlPdf);



    }

    private class DownloadPptTask extends AsyncTask<String, Void, byte[]> {
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
        protected void onPostExecute(byte[] result) {
            // El archivo PPT ha sido descargado, ahora puedes cargarlo en la WebView
            mostrarPptEnWebView(result);
        }

        private void mostrarPptEnWebView(byte[] pptData) {
            if (pptData != null) {
                // Convertir los bytes a un objeto String con el formato "data:application/vnd.ms-powerpoint;base64,<base64_data>"
                String base64Ppt = "data:application/vnd.ms-powerpoint;base64," + Base64.encodeToString(pptData, Base64.DEFAULT);

                // Obtener la referencia de la WebView
                WebView webView = findViewById(R.id.webView);

                // Configurar la WebView
                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);

                // Cargar el archivo PPT en la WebView
                webView.loadData(base64Ppt, "application/vnd.ms-powerpoint", "base64");
            } else {
                // Manejar el caso en que la descarga del archivo PPT falló
                //Toast.makeText(this, "Error al descargar el archivo PPT", Toast.LENGTH_SHORT).show();
            }
        }
    }
}