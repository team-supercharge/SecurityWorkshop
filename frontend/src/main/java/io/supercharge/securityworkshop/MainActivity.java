package io.supercharge.securityworkshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText emailView;
    private EditText passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupViews();

        safetynetCheck();
    }

    private void setupViews() {
        setContentView(R.layout.activity_main);

        emailView = findViewById(R.id.email);

        passwordView = findViewById(R.id.password);
        passwordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                login();
                return true;
            }

            return false;
        });

        Button emailSignInButton = findViewById(R.id.email_sign_in_button);
        emailSignInButton.setOnClickListener(view -> login());
    }

    private void login() {
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        ApiManager apiManager = ApiManager.getInstance();

        apiManager.setCredentials(email, password);

        apiManager.getApi().authenticate(new AuthenticationRequest())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(apiManager::clearCredentials)
                .subscribe(authenticationResponse -> {
                    apiManager.setToken(authenticationResponse.getToken());

                    Intent intent = new Intent(this, UserActivity.class);
                    startActivity(intent);
                    finish();
                }, Throwable::printStackTrace);
    }

    private void safetynetCheck() {
        if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
                != ConnectionResult.SUCCESS) {

            Log.e(TAG, "Google Play Services is not available!");

            return;
        }

        String nonceData = "Safety Net: " + System.currentTimeMillis(); // this should come from server

        byte[] nonce = getRequestNonce(nonceData);

        SafetyNetClient client = SafetyNet.getClient(this);

        client.attest(nonce, BuildConfig.SAFETYNET_API_KEY)
                .addOnSuccessListener(this, attestationResponse -> {
                    String result = attestationResponse.getJwsResult();

                    Log.i(TAG, decodeJws(result));

                    Api api = ApiManager.getInstance().getApi();

                    // we should call the our trusted server
                    AppVerificationRequest request = new AppVerificationRequest();
                    request.setSignedAttestation(result);

                    api.verifyApp(BuildConfig.SAFETYNET_API_KEY, request)
                            .subscribe(appVerificationResponse
                                    -> Log.i(TAG, "verified: " + appVerificationResponse.isValidSignature()));
                })
                .addOnFailureListener(this, e -> Log.e(TAG, "SafetyNet error", e));
    }

    private byte[] getRequestNonce(String data) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[24];

        Random random = new Random();

        random.nextBytes(bytes);
        try {
            byteStream.write(bytes);
            byteStream.write(data.getBytes());
        } catch (IOException e) {
            return null;
        }

        return byteStream.toByteArray();
    }

    public String decodeJws(String jwsResult){
        if (jwsResult == null) {
            return null;
        }
        final String[] jwtParts = jwsResult.split("\\.");
        if (jwtParts.length == 3) {
            return new String(Base64.decode(jwtParts[1], Base64.DEFAULT));
        } else {
            return null;
        }
    }

}
