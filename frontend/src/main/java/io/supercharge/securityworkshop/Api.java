package io.supercharge.securityworkshop;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    @POST("authorizations")
    Single<AuthenticationResponse> authenticate(@Body AuthenticationRequest request);

    @GET("user")
    Single<UserResponse> getUser();
}
