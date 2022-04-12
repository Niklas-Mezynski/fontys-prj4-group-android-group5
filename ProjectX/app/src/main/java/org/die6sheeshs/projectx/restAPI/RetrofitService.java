package org.die6sheeshs.projectx.restAPI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.die6sheeshs.projectx.helpers.AuthInterceptor;
import org.die6sheeshs.projectx.helpers.PropertyService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private static RetrofitService instance = new RetrofitService();

    private Retrofit retrofit;
    private final String baseUrl;
    private List<RetrofitPersistence> persistences = new ArrayList<>();

    public RetrofitService() {
        baseUrl = PropertyService.readProperty("baseUrl");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter())
                .create();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new AuthInterceptor()).build();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

    }

    public Retrofit getRetrofitClient() {
        return this.retrofit;
    }

    public static RetrofitService getInstance() {
        return instance;
    }

    public void addAuthInterceptor() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter())
                .create();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new AuthInterceptor()).build();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        refreshAllApis();
    }

    public void addPersistence(RetrofitPersistence persistence) {
        persistences.add(persistence);
    }

    private void refreshAllApis() {
        for (RetrofitPersistence persistence : persistences) {
            persistence.refreshApi();
        }
    }


}
