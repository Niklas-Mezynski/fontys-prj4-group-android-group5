package org.die6sheeshs.projectx.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.LoginResponse;
import org.die6sheeshs.projectx.helpers.SessionManager;
import org.die6sheeshs.projectx.restAPI.UserPersistence;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    private Button submit;
    private Button gotoRegister;
    private EditText passwordField;
    private EditText emailField;
    private TextView invalidPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        submit = findViewById(R.id.buttonLogin);
        gotoRegister = findViewById(R.id.goto_register_view);
        emailField = findViewById(R.id.loginEmailNickname);
        passwordField = findViewById(R.id.loginPassword);
        invalidPassword = findViewById(R.id.invalid_password);

        gotoRegister.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        submit.setOnClickListener(listener -> {
            invalidPassword.setVisibility(View.GONE);
            submitLogin();
        });
    }

    private void submitLogin() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        Observable<LoginResponse> ob = UserPersistence.getInstance().userLogin(email, password);
        ob.subscribeOn(Schedulers.io())
                .subscribe((token -> {
                    //Storing the user's session information
                    SessionManager.getInstance().saveAuthToken(token.getToken());
                    SessionManager.getInstance().setUserId(token.getUser_id());

                    //Starting the main activity
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    Log.v("New JWT token", token.getToken());
                    Log.v("Logged in user: ", SessionManager.getInstance().getUserId());
                }), error -> {
                    runOnUiThread(() -> invalidPassword.setVisibility(View.VISIBLE));
                    Log.v("Login error", " " + error.getMessage());
                });
    }


}