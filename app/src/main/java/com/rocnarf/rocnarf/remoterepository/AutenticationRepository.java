package com.rocnarf.rocnarf.remoterepository;

import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.AutenticacionService;
import com.rocnarf.rocnarf.models.AccessTokenRequest;
import com.rocnarf.rocnarf.models.AccessTokenResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutenticationRepository {

    public static void Autenticar() {

        AccessTokenRequest accessTokenRequest = new AccessTokenRequest();
        accessTokenRequest.setClient_id("rocnarf");
        accessTokenRequest.setClient_secret("RocnarfAPIsSinergiaSS");
        accessTokenRequest.setGrant_type("password");
        accessTokenRequest.setUsername("AAA");
        accessTokenRequest.setPassword("1234");
        AutenticacionService service = ApiClient.getAuthenticationClient().create(AutenticacionService.class);


        Call<AccessTokenResponse> call = service.getAccessToken(accessTokenRequest);
//                "AAA",
//                "1234",
//                "rocnarf",
//                "RocnarfAPIsSinergiaSS",
//                "RocnarfAPIs",
//                "password");

        call.enqueue(new Callback<AccessTokenResponse>() {
            @Override
            public void onResponse(Call<AccessTokenResponse> call, Response<AccessTokenResponse> response) {
                AccessTokenResponse myItem=response.body();
            }

            @Override
            public void onFailure(Call<AccessTokenResponse> call, Throwable t) {
                //Handle failure
                call.cancel();
            }
        });




    }


}
