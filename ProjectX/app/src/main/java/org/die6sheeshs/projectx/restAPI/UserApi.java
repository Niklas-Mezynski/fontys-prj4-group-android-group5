package org.die6sheeshs.projectx.restAPI;

import org.die6sheeshs.projectx.entities.LoginRequest;
import org.die6sheeshs.projectx.entities.LoginResponse;
import org.die6sheeshs.projectx.entities.User;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface UserApi {

    @GET("users/")
    Call<List<User>> getUsers();

    @POST("users/")
    Observable<User> createUser(@Body User user);

    @POST("users/login")
    Observable<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("users/{id}")
    Observable<User> getUserById(@Path("id") String id);

    @Multipart
    @POST("/users/{id}/files")
    Observable<ResponseBody> upload(
            @Path("id") String user_id,
            @Part MultipartBody.Part file
            );

    @GET("/users/{id}/files")
    Observable<ResponseBody> downloadProfilePicture(@Path("id") String id);

}
