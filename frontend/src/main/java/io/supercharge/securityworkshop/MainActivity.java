package io.supercharge.securityworkshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    private EditText emailView;
    private EditText passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupViews();
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

}
