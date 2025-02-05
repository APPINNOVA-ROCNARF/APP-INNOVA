package com.rocnarf.rocnarf.api;


import com.rocnarf.rocnarf.models.AccessTokenRequest;
import com.rocnarf.rocnarf.models.AccessTokenResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface AutenticacionService {
    //@POST("/rocnarfoauth/connect/token/")
    @POST("/rocnarfoauth/connect/token/")
    Call<AccessTokenResponse> getAccessToken(@Body AccessTokenRequest accessTokenRequest);

}
