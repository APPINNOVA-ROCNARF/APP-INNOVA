package com.rocnarf.rocnarf;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VersionActivity extends AppCompatActivity {
    private static final String TAG = "VersionActivity";
    private static final String VERSION_JSON_URL = "http://200.105.252.218/rocnarf/app/version.json";
    private long downloadId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);

        TextView tvVersion = findViewById(R.id.textViewVersion);
        try {
            String version = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).versionName;
            tvVersion.setText("Versión " + version);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button btnBuscar = findViewById(R.id.btnBuscarActualizacion);
        btnBuscar.setOnClickListener(v -> {
            // Aquí iría tu lógica para verificar la versión en el servidor
            buscarActualizacion();
        });
    }

    private void buscarActualizacion() {
        new Thread(() -> {
            try {
                URL url = new URL(VERSION_JSON_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder content = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) {
                    content.append(line);
                }

                in.close();

                Gson gson = new Gson();
                VersionInfo versionInfo = gson.fromJson(content.toString(), VersionInfo.class);

                String currentVersion = getPackageManager()
                        .getPackageInfo(getPackageName(), 0).versionName;

                if (!currentVersion.equals(versionInfo.ultimaVersion)) {
                    runOnUiThread(() -> mostrarDialogoActualizar(versionInfo));
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Ya tienes la última versión", Toast.LENGTH_SHORT).show());
                }

            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error al verificar actualización", Toast.LENGTH_SHORT).show());
                e.printStackTrace();
            }
        }).start();
    }

    private void mostrarDialogoActualizar(VersionInfo info) {
        new AlertDialog.Builder(this)
                .setTitle("Nueva versión disponible")
                .setMessage("Versión: " + info.ultimaVersion + "\n\n" + info.notas)
                .setPositiveButton("Actualizar", (dialog, which) -> descargarApk(info.url))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void descargarApk(String apkUrl) {
        Uri uri = Uri.parse(apkUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("Descargando actualización");
        request.setDescription("El archivo .apk fue descargado y está disponible en Descargas.");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "miApp_update.apk");
        request.setMimeType("application/vnd.android.package-archive");

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadId = manager.enqueue(request);

        BroadcastReceiver onComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadId == id) {
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "miApp_update.apk");
                    if (!file.exists()) {
                        Toast.makeText(context, "El archivo no fue encontrado", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Uri apkUri = FileProvider.getUriForFile(
                            context,
                            "com.rocnarf.rocnarf.fileprovider",
                            file
                    );

                    Log.d("APK_URI", "URI generado: " + apkUri.toString());

                    Intent openApkIntent = new Intent(Intent.ACTION_VIEW);
                    openApkIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    openApkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    openApkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    try {
                        context.startActivity(openApkIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context, "No se pudo abrir el archivo descargado", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    unregisterReceiver(this);
                }
            }
        };

        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
    // Clase para mapear el JSON
    private static class VersionInfo {
        String ultimaVersion;
        String url;
        String notas;
    }
}