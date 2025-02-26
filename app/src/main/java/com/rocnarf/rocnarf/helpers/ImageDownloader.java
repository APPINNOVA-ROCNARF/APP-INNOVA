package com.rocnarf.rocnarf.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageDownloader {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler uiHandler = new Handler(Looper.getMainLooper());

    public void descargarImagen(final String url, final ImageView imageView) {
        executorService.execute(() -> {
            try {
                URL imageUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                connection.setDoInput(true);
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();

                // ✅ Setear la imagen en el hilo principal
                uiHandler.post(() -> {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                        Log.e("ImageDownloader", "Descarga completa");
                    } else {
                        Log.e("ImageDownloader", "La imagen descargada es nula");
                    }
                });

            } catch (Exception e) {
                Log.e("ImageDownloader", "Error al descargar la imagen", e);
            }
        });
    }
}
