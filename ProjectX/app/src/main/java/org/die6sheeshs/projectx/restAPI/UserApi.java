package org.die6sheeshs.projectx.restAPI;

import org.die6sheeshs.projectx.entities.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserApi {

    @GET("users/")
    Call<List<User>> getUsers();

    @POST("users/")
    Call<User> createUser(@Body User user);
}
