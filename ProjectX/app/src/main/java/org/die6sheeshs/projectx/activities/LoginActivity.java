package org.die6sheeshs.projectx.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.TokenEntity;
import org.die6sheeshs.projectx.entities.User;
import org.die6sheeshs.projectx.restAPI.UserPersistence;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    private Button submit;
    private Button gotoRegister;
    private EditText passwordField;
    private EditText emailField;

    UserPersistence userPersistence = UserPersistence.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        submit = findViewById(R.id.buttonLogin);
        gotoRegister = findViewById(R.id.goto_register_view);
        emailField = findViewById(R.id.loginEmailNickname);
        passwordField = findViewById(R.id.loginPassword);

        gotoRegister.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        UserPersistence userPersistence = UserPersistence.getInstance();
        submit.setOnClickListener(listener -> {
            submitLogin();
        });
    }

    private void submitLogin(){
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        Observable<TokenEntity> ob = userPersistence.userLogin(email, password);
        ob.subscribeOn(Schedulers.io())
                .doOnError((error) -> Log.v("Login, User POST error: ", error.getMessage()))
                .subscribe(token -> {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    Log.v("snens", token.getToken());
                    MainActivity.userToken = token.getToken();
                });


    }


}