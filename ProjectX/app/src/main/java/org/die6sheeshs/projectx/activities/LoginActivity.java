package org.die6sheeshs.projectx.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.User;
import org.die6sheeshs.projectx.restAPI.UserPersistence;

import java.util.List;
import java.util.Observable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button submit;
    private Button gotoRegister;
    private EditText passwordField;
    private EditText emailField;

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

        UserPersistence userPersistence = new UserPersistence();
        submit.setOnClickListener(listener -> {
            submitLogin();
        });

    }

    private void submitLogin(){
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}