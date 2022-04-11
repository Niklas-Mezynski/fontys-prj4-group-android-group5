package org.die6sheeshs.projectx.restAPI;

import org.die6sheeshs.projectx.entities.LoginRequest;
import org.die6sheeshs.projectx.entities.LoginResponse;
import org.die6sheeshs.projectx.entities.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserApi {

    @GET("users/")
    Call<List<User>> getUsers();

    @POST("users/")
    Observable<User> createUser(@Body User user);

    @POST("users/login")
    Observable<LoginResponse> login(@Body LoginRequest loginRequest);
}
