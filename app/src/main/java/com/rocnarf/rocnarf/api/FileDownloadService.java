package com.rocnarf.rocnarf.api;

import com.rocnarf.rocnarf.models.AsesorLocation;
import com.rocnarf.rocnarf.models.AsesorLocationBulk;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
public interface FileDownloadService {

    @GET("api/planes/ParrillaFile")
    Call<ResponseBody> downloadFileWithFixedUrl();

    @GET("api/planes/GuiaProductoPdf/")
    Call<ResponseBody> downloadGuia(
            @Query("id") int id
    );

    @GET("api/planes/GetBonificacion")
    Call<ResponseBody> downloadFileBonificacion();

    @GET("api/planes/ReporteVentasPdf/")
    Call<ResponseBody> downloadReporteVentas(
            @Query("id") int id
    );

    @GET("api/planes/GetTiempoEntrega")
    Call<ResponseBody> downloadFileTiempoEntrega();
}
