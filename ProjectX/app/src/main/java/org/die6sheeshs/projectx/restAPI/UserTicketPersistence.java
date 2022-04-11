package org.die6sheeshs.projectx.restAPI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.die6sheeshs.projectx.entities.Ticket;
import org.die6sheeshs.projectx.helpers.PropertyService;

import java.time.LocalDateTime;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserTicketPersistence {
    private Retrofit retrofit;
    private UserTicketApi userTicketApi;

    private static final UserTicketPersistence instance = new UserTicketPersistence();

    public static UserTicketPersistence getInstance(){
        return instance;
    }

    private UserTicketPersistence() {
        String baseUrl = PropertyService.readProperty("baseUrl");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter())
                .create();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();


        this.userTicketApi = retrofit.create(UserTicketApi.class);
    }

    public Observable<List<Ticket>> getTickets(String id){
        return userTicketApi.getTickets(id);
    }

    public void deleteTicket(String id){
        userTicketApi.deleteTicket(id);
    }
}
