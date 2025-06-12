package com.rocnarf.rocnarf;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.GuiaProductosAdapter;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.PlanesService;
import com.rocnarf.rocnarf.models.ArchivosGuiaProducto;
import com.rocnarf.rocnarf.models.GuiaProductos;
import com.rocnarf.rocnarf.models.GuiaProductosResponse;
import com.rocnarf.rocnarf.models.Planes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuiasProductosActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String idUsuario, idCliente, nombreCliente, seccion;
    private ProgressBar progressBar;
    private Context context;
    private Planes planes;
    private List<GuiaProductos> listaGuiaProductos;
    private SearchView searchView;
    private GuiaProductosAdapter adapter;
    ListView ListViewPlanes;

    private com.rocnarf.rocnarf.dao.DataBaseHelper DataBaseHelper;
    private SQLiteDatabase sQLiteDatabase;
    public PDFView pdfView;
    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia_productos);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
        pdfView = findViewById(R.id.pdf_parrilla);
        textView = findViewById(R.id.tv_estado_pdf);

        recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_guia_productos);
        Intent i = getIntent();
        idUsuario = i.getStringExtra(Common.ARG_IDUSUARIO);
        seccion = i.getStringExtra(Common.ARG_SECCIOM);
        //idCliente =  i.getStringExtra(Common.ARG_IDCLIENTE);
        //nombreCliente =  i.getStringExtra(Common.ARG_NOMBRE_CLIENTE);
        context = this;
        PlanesService service = ApiClient.getClient().create(PlanesService.class);
        Call<GuiaProductosResponse> call = service.GetGuiaProductos(true);
        call.enqueue(new Callback<GuiaProductosResponse>() {
            @Override
            public void onResponse(Call<GuiaProductosResponse> call, Response<GuiaProductosResponse> response) {
                if (response.isSuccessful()) {
                    GuiaProductosResponse guiaProductosResponse = response.body();
                    List<GuiaProductos> guiaProductos = guiaProductosResponse.items;
                    listaGuiaProductos = guiaProductosResponse.items;
//
                    final GuiaProductosAdapter.GuiaProductosListener listener = new GuiaProductosAdapter.GuiaProductosListener() {
                        @Override
                        public void GuiaProductosListener(GuiaProductos guiaProductos) {

                            PlanesService service = ApiClient.getClient().create(PlanesService.class);
                            Call<List<ArchivosGuiaProducto>> call = service.GetArchivosGuiaProductos(guiaProductos.getIdGuia());
                            call.enqueue(new Callback<List<ArchivosGuiaProducto>>() {
                                @Override
                                public void onResponse(Call<List<ArchivosGuiaProducto>> call, Response<List<ArchivosGuiaProducto>> response) {
                                    if (response.isSuccessful()) {
                                        List<ArchivosGuiaProducto> archivosGuiaProducto = response.body();

                                        AlertDialog.Builder builder = new AlertDialog.Builder(GuiasProductosActivity.this);
                                        LayoutInflater inflater = GuiasProductosActivity.this.getLayoutInflater();
                                        View view = inflater.inflate(R.layout.dialog_archivo_guia, null);

                                        builder.setView(view)
                                                .setTitle("Selecciona un archivo")
                                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        // Cerrar el diálogo
                                                    }
                                                });

                                        // Configurar el ListView
                                        List<ArchivosGuiaProducto> archivos = archivosGuiaProducto;

// Si hay URL de video válida, la agregamos como un archivo especial
                                        if (guiaProductos.getUrlVideo() != null && !guiaProductos.getUrlVideo().trim().isEmpty()) {
                                            ArchivosGuiaProducto videoArchivo = new ArchivosGuiaProducto();
                                            videoArchivo.setNombre(guiaProductos.getMarca() + " - Video");
                                            // Puedes usar un campo extra o una convención para marcar que es video (ej. nombre)
                                            archivos.add(videoArchivo);
                                        }

                                        String[] nombresArchivos = new String[archivos.size()];
                                        for (int i = 0; i < archivos.size(); i++) {
                                            nombresArchivos[i] = archivos.get(i).getNombre();
                                        }
                                        //final String[] nombresArchivos = {"Archivo1.txt", "Archivo2.pdf", "Archivo3.ppt"};
                                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, nombresArchivos);
                                        ListView listView = view.findViewById(R.id.listView);
                                        listView.setAdapter(adapter);

                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                                // Aquí puedes iniciar la descarga del archivo asociado al nombre seleccionado
                                                ArchivosGuiaProducto selectedArchivo = archivos.get(position);
                                                String archivoId = selectedArchivo.getNombre();

                                                // Verificar si es un video (terminado en " - Video")
                                                if (archivoId.endsWith(" - Video")) {
                                                    String urlVideo = guiaProductos.getUrlVideo();
                                                    if (urlVideo == null || urlVideo.trim().isEmpty()) {
                                                        Toast.makeText(context, "URL de vídeo no válida", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        // Redireccionar a YouTube o navegador
                                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlVideo));
                                                        try {
                                                            startActivity(intent);
                                                        } catch (Exception e) {
                                                            Toast.makeText(context, "No se pudo abrir el enlace", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                    return; // Importante: salir del método aquí
                                                }

                                                // Código para manejar archivos normales (PDF, PPT, etc.)
                                                String guiaId = selectedArchivo.getIdGuia();
                                                String extension = archivoId.substring(archivoId.lastIndexOf(".") + 1);
                                                Log.d("xxx","xxx"+"http://200.105.252.218/rocnarf/api/Planes/getFileGuiaProductos/"+ guiaId + "/"+ archivoId);

                                                if(extension.toLowerCase().equals("pdf")){
                                                    Intent intent = new Intent(context, PdfActivity.class);
                                                    intent.putExtra("urlPdf", "http://200.105.252.218/rocnarf/api/Planes/getFileGuiaProductos/"+ guiaId + "/"+ archivoId);
                                                    startActivity(intent);
                                                } else if (extension.toLowerCase().equals("ppt") || extension.toLowerCase().equals("pptx")) {
                                                    descargarArchivo(archivoId, guiaId);
                                                } else {
                                                    descargarArchivo(archivoId, guiaId);
                                                }
                                            }
                                        });

                                        builder.create().show();

                                    }
                                }

                                @Override
                                public void onFailure(Call<List<ArchivosGuiaProducto>> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

//                            Intent i = new Intent(context, PdfGuiasProductosActivity.class);
//                            i.putExtra(Common.ARG_IDCLIENTE, idCliente);
//                            i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
//                            i.putExtra(Common.ARG_SECCIOM, seccion);
//                            i.putExtra(Common.ARG_IDNPLAN,  String.valueOf(guiaProductos.getIdGuia()));
//                            i.putExtra(Common.ARG_NOMPBREPLAN, guiaProductos.getMarca() + " - " + guiaProductos.getNombre());
//                            context.startActivity(i);

                        }
                    };
                    recyclerView = (RecyclerView) findViewById(R.id.rv_list_activity_guia_productos);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    adapter = new GuiaProductosAdapter(context, guiaProductos, listener);
                    recyclerView.setAdapter(adapter);


                }
            }

            @Override
            public void onFailure(Call<GuiaProductosResponse> call, Throwable t) {
                //Log.d("sincronizar Clientes", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    private void abrirPptConAplicacionExterna(String archivoId, String guiaId) {
        try {
            // Obtén la ruta completa del archivo descargado
            String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + archivoId;

            // Crear un intent para abrir el archivo con una aplicación externa
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", new File(filePath));
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");

            // Asegúrate de que la aplicación pueda manejar el intent
            List<ResolveInfo> resolvedIntentActivities = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (resolvedIntentActivities != null && !resolvedIntentActivities.isEmpty()) {
                // Iniciar la actividad para abrir el archivo PPT
                startActivity(intent);
            } else {
                Toast.makeText(context, "No se encontró una aplicación compatible para abrir archivos PPT", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error al abrir archivo PPT", Toast.LENGTH_SHORT).show();
        }
    }


    private void descargarArchivo(String archivoId, String guiaId) {
        Log.d("idddddd","ddddd"+ archivoId);
        Log.d("idddddd","ddddd"+ guiaId);
        // Replace with your actual API endpoint and destination path
        String downloadUrl = "http://200.105.252.218/rocnarf/api/Planes/getFileGuiaProductos/"+ guiaId + "/"+ archivoId;
        String destinationPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + archivoId;

        new DownloadFileTask().execute(downloadUrl, destinationPath);
    }

    public class DownloadFileTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String downloadUrl = params[0];
            String destinationPath = params[1];

            try {
                URL url = new URL(downloadUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    InputStream inputStream = urlConnection.getInputStream();
                    FileOutputStream outputStream = new FileOutputStream(destinationPath);

                    try {
                        byte[] buffer = new byte[1024];
                        int bytesRead;

                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    } finally {
                        outputStream.close();
                    }
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // File has been downloaded, you can add any UI update code here
            Toast.makeText(context, "Descarga completa", Toast.LENGTH_SHORT).show();

            Log.d("descar","completa");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.politicas, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private String obtenerTipoDeArchivo(String nombreArchivo) {
        if (nombreArchivo != null && nombreArchivo.contains(".")) {
            // Obtener la extensión del archivo
            String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf(".") + 1);

            // Mapear la extensión a un tipo de archivo conocido
            switch (extension.toLowerCase()) {
                case "txt":
                    return "Archivo de texto";
                case "pdf":
                    return "Documento PDF";
                case "ppt":
                case "pptx":
                    return "Presentación de PowerPoint";
                // Agrega más casos según sea necesario para otras extensiones
                default:
                    return "Tipo de archivo desconocido";
            }
        }

        return "Nombre de archivo no válido";
    }

}
