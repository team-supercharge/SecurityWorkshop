package io.supercharge.securityworkshop;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {

    private String credentials;
    private String token;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (!request.url().toString().contains("api.github.com")) {
            return chain.proceed(request);
        }

        Request authenticatedRequest = request.newBuilder()
                .header("Authorization", createAuthenticationHeader()).build();
        return chain.proceed(authenticatedRequest);
    }

    private String createAuthenticationHeader() {
        if (!TextUtils.isEmpty(credentials)) {
            return credentials;
        }

        return "token " + token;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
