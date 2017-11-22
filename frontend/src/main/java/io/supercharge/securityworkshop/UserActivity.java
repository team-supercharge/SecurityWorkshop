package io.supercharge.securityworkshop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class UserActivity extends AppCompatActivity {

    private TextView userNameView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);

        userNameView = findViewById(R.id.user_name_view);

        ApiManager apiManager = ApiManager.getInstance();

        apiManager.getApi().getUser()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userResponse -> userNameView.setText(userResponse.getLogin()), Throwable::printStackTrace);
    }
}
