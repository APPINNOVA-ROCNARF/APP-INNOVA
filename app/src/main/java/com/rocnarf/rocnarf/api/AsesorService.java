package com.rocnarf.rocnarf.api;

import com.rocnarf.rocnarf.models.AsesorLocation;
import com.rocnarf.rocnarf.models.AsesorLocationBulk;
import com.rocnarf.rocnarf.models.VisitaClientes;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface AsesorService {

    @POST("api/Asesores/Location/")
    Observable<AsesorLocation> Post(
            @Body AsesorLocation asesorLocation
    );

    @POST("api/Asesores/Location/bulk")
    Observable<ResponseBody> PostBulk(
            @Body AsesorLocationBulk asesorLocations
    );



}
