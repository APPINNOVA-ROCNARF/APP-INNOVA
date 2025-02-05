package com.rocnarf.rocnarf;


import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.FileDownloadService;
import com.rocnarf.rocnarf.api.PlanesService;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PdfLeer extends AsyncTask<String,Void, InputStream> {

    public PDFView  pdfView;
    ProgressBar progressBar;
    public TextView textView;
    public String  Mtipo;
    public int  idGuia;
    public PdfLeer(PDFView pdfView, ProgressBar progressBar, TextView textView) {
        this.pdfView = pdfView;
        this.progressBar = progressBar;
        this.textView = textView;
    }

    @Override
    protected InputStream doInBackground(String... strings) {
        InputStream inputStream = null;
//        try {
//            URL url = new URL(strings[0]);
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            if (urlConnection.getResponseCode()==200){
//                inputStream = new BufferedInputStream(urlConnection.getInputStream());
//            }
//        }catch (IOException e){
//            return null;
//        }
        return  inputStream;
    }

    @Override
    protected void onPostExecute(InputStream inputStream) {
            FileDownloadService downloadService = ApiClient.getClient().create(FileDownloadService.class);

            Call<ResponseBody> call = downloadService.downloadFileWithFixedUrl();

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d("file", "server contacted and has file" + response);
                    if (response.body() != null){
                    pdfView.fromStream(response.body().byteStream()).load();
                    progressBar.setVisibility(View.GONE);
                    }else{
                        Log.d("file", "no hay") ;
                        textView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
//                if (response.isSuccess()) {
//                    Log.d("file", "server contacted and has file");
//
//                    boolean writtenToDisk = writeResponseBodyToDisk(response.body());
//
//                    Log.d("file", "file download was a success? " + writtenToDisk);
//                } else {
//                    Log.d("file", "server contact failed");
//                }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("file", "error");
                }
            });



//            Log.d("idGuia", "idGuia" + this.idGuia);
//            FileDownloadService downloadService = ApiClient.getClient().create(FileDownloadService.class);
//            Call<ResponseBody> call = downloadService.downloadGuia(this.idGuia);
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    Log.d("file", "server contacted and has file");
//                    pdfView.fromStream(response.body().byteStream()).load();
//                    progressBar.setVisibility(View.GONE);
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    Log.e("file", "error");
//                }
//            });
//
//        Log.d("pdf","nombre ----> " + inputStream);
//        pdfView.fromStream(inputStream) // all pages are displayed by default
//                .enableSwipe(true) // allows to block changing pages using swipe
//                .swipeHorizontal(false)
//                .enableDoubletap(true)
//                .defaultPage(0)
//                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
//                .password(null)
//                .scrollHandle(null)
//                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
//                // spacing between pages in dp. To define spacing color, set view background
//                .spacing(0)
//                .load();
//        if (this.Mtipo == "GUIA"){
//        }else {
//        }
//        progressBar.setVisibility(View.GONE);
    }

}
