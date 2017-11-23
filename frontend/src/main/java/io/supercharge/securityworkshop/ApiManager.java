package io.supercharge.securityworkshop;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.reactivex.schedulers.Schedulers;
import okhttp3.CertificatePinner;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ApiManager {

    private static ApiManager INSTANCE = new ApiManager();

    private final Api api;
    private final AuthenticationInterceptor interceptor;

    public static ApiManager getInstance() {
        return INSTANCE;
    }

    private ApiManager() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        interceptor = new AuthenticationInterceptor();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .certificatePinner(new CertificatePinner.Builder()
                        .add("api.github.com", "sha256/VRtYBz1boKOXjChfZYssN1AeNZCjywl77l2RTl/v380=")
                        .build())
                .addInterceptor(interceptor)
                .addInterceptor(loggingInterceptor)
                .build();

        ObjectMapper objectMapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();

        api = retrofit.create(Api.class);
    }

    public void setCredentials(String username, String password) {
        interceptor.setCredentials(Credentials.basic(username, password));
    }

    public void clearCredentials() {
        interceptor.setCredentials(null);
    }

    public void setToken(String token) {
        interceptor.setToken(token);
    }

    public Api getApi() {
        return api;
    }
}
