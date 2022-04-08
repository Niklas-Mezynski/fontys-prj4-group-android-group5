package org.die6sheeshs.projectx.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.die6sheeshs.projectx.R;
import org.die6sheeshs.projectx.entities.User;
import org.die6sheeshs.projectx.restAPI.UserPersistence;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button submit;
    private Button gotoRegister;
    private EditText password;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        submit = findViewById(R.id.buttonLogin);
        gotoRegister = findViewById(R.id.goto_register_view);
        email = findViewById(R.id.loginEmailNickname);

        gotoRegister.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        UserPersistence userPersistence = new UserPersistence();
        submit.setOnClickListener(listener -> {
            Call<List<User>> allUsers = userPersistence.getAllUsers();
            allUsers.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    if(!response.isSuccessful()){
                        Log.e("API Response: ", String.valueOf(response.code()));
                        return;
                    }

                    List<User> userList = response.body();
                    for (User user: userList){
                        Log.v("User found: ", user.getFirstName() + " " + user.getLastName());
                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    Log.e("failed", t.toString());
                }
            });
        });
    }
}