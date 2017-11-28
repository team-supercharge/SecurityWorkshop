package io.supercharge.securityworkshop;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @POST("authorizations")
    Single<AuthenticationResponse> authenticate(@Body AuthenticationRequest request);

    @GET("user")
    Single<UserResponse> getUser();

    @POST("https://www.googleapis.com/androidcheck/v1/attestations/verify")
    Single<AppVerificationResponse> verifyApp(@Query("key") String apiKey, @Body AppVerificationRequest request);
}
