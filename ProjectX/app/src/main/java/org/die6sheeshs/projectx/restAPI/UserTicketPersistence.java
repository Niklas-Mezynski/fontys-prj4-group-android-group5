package org.die6sheeshs.projectx.restAPI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.die6sheeshs.projectx.entities.Count;
import org.die6sheeshs.projectx.entities.Ticket;
import org.die6sheeshs.projectx.helpers.PropertyService;

import java.time.LocalDateTime;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserTicketPersistence implements RetrofitPersistence {

    public void setUserTicketApi(UserTicketApi userTicketApi) {
        this.userTicketApi = userTicketApi;
    }

    private UserTicketApi userTicketApi;

    private static final UserTicketPersistence instance = new UserTicketPersistence();

    public static UserTicketPersistence getInstance() {
        return instance;
    }

    private UserTicketPersistence() {
        this.userTicketApi = RetrofitService.getInstance().getRetrofitClient().create(UserTicketApi.class);
        RetrofitService.getInstance().addPersistence(this);
    }

    public Observable<List<Ticket>> getTickets(String id, String jwt) {
        return userTicketApi.getTickets(id);
    }

    public Observable<Count> deleteTicket(String id) {
        return userTicketApi.deleteTicket(id);
    }

    @Override
    public void refreshApi() {
        this.userTicketApi = RetrofitService.getInstance().getRetrofitClient().create(UserTicketApi.class);
    }
}
