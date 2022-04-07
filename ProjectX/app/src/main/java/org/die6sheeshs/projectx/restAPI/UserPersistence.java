package org.die6sheeshs.projectx.restAPI;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.die6sheeshs.projectx.entities.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserPersistence {
    private Retrofit retrofit;
    private final String baseUrl = "http://10.0.2.2:3000/";
    private UserApi userApi;

    public UserPersistence() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter())
                .create();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        this.userApi = retrofit.create(UserApi.class);
    }

    public Call<List<User>> getAllUsers() {
        return this.userApi.getUsers();
    }

    public void createUser(String firstName, String lastName, String email, String nick_name, LocalDateTime birth_date, String profile_pic, String about_me, String password) {

        User user = new User(firstName, lastName, email, nick_name, birth_date, profile_pic, about_me, "someSalt", password);
        Call<User> newUserCall = this.userApi.createUser(user);
        newUserCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    try {
                        Log.e("API Response", response.errorBody().string());
                    } catch (IOException e) {
                        Log.e("API Response", String.valueOf(response.code()));
                    }
                    return;
                }
                User body = response.body();
                Log.v("New User ID", body.getId());

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Failure getting users", t.toString());
            }
        });
    }
}
