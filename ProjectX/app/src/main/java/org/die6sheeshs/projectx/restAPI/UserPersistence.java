package org.die6sheeshs.projectx.restAPI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.die6sheeshs.projectx.entities.LoginRequest;
import org.die6sheeshs.projectx.entities.LoginResponse;
import org.die6sheeshs.projectx.entities.User;
import org.die6sheeshs.projectx.helpers.AuthInterceptor;
import org.die6sheeshs.projectx.helpers.PropertyService;

import java.time.LocalDateTime;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserPersistence implements RetrofitPersistence {

    private static final UserPersistence instance = new UserPersistence();

    public static UserPersistence getInstance() {
        return instance;
    }

    private UserApi userApi;

    private UserPersistence() {
        this.userApi = RetrofitService.getInstance().getRetrofitClient().create(UserApi.class);
        RetrofitService.getInstance().addPersistence(this);
    }

    public Call<List<User>> getAllUsers() {
        return this.userApi.getUsers();
    }

    public Observable<User> createUser(String firstName, String lastName, String email, String nick_name, LocalDateTime birth_date, String profile_pic, String about_me, String password) {
        User user = new User(firstName, lastName, email, nick_name, birth_date, profile_pic, about_me, password);

        Observable<User> observable = userApi.createUser(user);
        return observable;
    }

    public Observable<LoginResponse> userLogin(String email, String password) {
        LoginRequest user = new LoginRequest(email, password);
        Observable<LoginResponse> ob = userApi.login(user);
        return ob;
    }

    public Observable<User> getUserData(String id){
        return this.userApi.getUserById(id);
    }

    public Observable<ResponseBody> uploadPicture(String user_id, MultipartBody.Part file) {
        //RequestBody requestBody = new RequestBody
        return this.userApi.upload(user_id, file);
    }

    public Observable<ResponseBody> downloadProfilePic(String user_id) {
        //RequestBody requestBody = new RequestBody
        return this.userApi.downloadProfilePicture(user_id);
    }

    @Override
    public void refreshApi() {
        this.userApi = RetrofitService.getInstance().getRetrofitClient().create(UserApi.class);
    }
}
