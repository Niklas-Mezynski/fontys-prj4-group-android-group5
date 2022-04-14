package org.die6sheeshs.projectx.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.LoginResponse;
import org.die6sheeshs.projectx.entities.Party;
import org.die6sheeshs.projectx.helpers.PropertyService;
import org.die6sheeshs.projectx.helpers.SessionManager;
import org.die6sheeshs.projectx.restAPI.PartyPersistence;
import org.die6sheeshs.projectx.restAPI.RetrofitService;
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

        PropertyService.registerContext(this);

        if (getIntent().hasCategory("register_success")) {
            Toast.makeText(this, "Account successfully created", Toast.LENGTH_SHORT).show();
        }

        submit = findViewById(R.id.buttonLogin);
        gotoRegister = findViewById(R.id.goto_register_view);
        emailField = findViewById(R.id.loginEmailNickname);
        passwordField = findViewById(R.id.loginPassword);
        invalidPassword = findViewById(R.id.invalid_password);

        String debugMode = PropertyService.readProperty("debugMode");
        if (debugMode != null && debugMode.equals("true")) {
            submitLogin(PropertyService.readProperty("email"), PropertyService.readProperty("password"));
        }

        if (getIntent().hasCategory("register_success")) {
            Toast.makeText(this, "Account successfully created", Toast.LENGTH_SHORT).show();
        }


        gotoRegister.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        submit.setOnClickListener(listener -> {
            invalidPassword.setVisibility(View.GONE);
            submitLogin(emailField.getText().toString(), passwordField.getText().toString());
        });
    }

    private void submitLogin(String email, String password) {
        ProgressDialog dialog = ProgressDialog.show(this, "",
                "Logging in. Please wait...", true);

        Observable<LoginResponse> ob = UserPersistence.getInstance().userLogin(email, password);
        ob.subscribeOn(Schedulers.io())
                .subscribe((token -> {
                    //Storing the user's session information
                    SessionManager.getInstance().saveAuthToken(token.getToken());
                    SessionManager.getInstance().setUserId(token.getUser_id());
                    RetrofitService.getInstance().addAuthInterceptor();

                    //Starting the main activity
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    Log.v("New JWT token", token.getToken());
                    Log.v("Logged in user: ", SessionManager.getInstance().getUserId());
                    dialog.dismiss();
                }), error -> {
                    runOnUiThread(() -> invalidPassword.setVisibility(View.VISIBLE));
                    Log.v("Login error", " " + error.getMessage());
                    dialog.dismiss();
                });
    }


}