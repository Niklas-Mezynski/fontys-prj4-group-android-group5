package org.die6sheeshs.projectx.restAPI;

import org.die6sheeshs.projectx.entities.LoginRequest;
import org.die6sheeshs.projectx.entities.LoginResponse;
import org.die6sheeshs.projectx.entities.User;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserApi {

    @GET("users/")
    Call<List<User>> getUsers();

    @POST("users/")
    Observable<User> createUser(@Body User user);

    @POST("users/login")
    Observable<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("users/{id}")
    Observable<User> getUserById(@Path("id") String id);

    @PATCH("users/{id}")
    Observable<ResponseBody> updateUser(@Path("id") String id, @Body User user);

    @GET("/users/{id}/files")
    Observable<ResponseBody> downloadProfilePicture(@Path("id") String id);

}
