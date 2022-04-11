package org.die6sheeshs.projectx.restAPI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.die6sheeshs.projectx.entities.Party;
import org.die6sheeshs.projectx.entities.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class PartyPersistence {

    private static final PartyPersistence instance = new PartyPersistence();

    public static PartyPersistence getInstance(){
        return instance;
    }

    private Retrofit retrofit;
    private final String baseUrl = "http://aertac.tk:3000/";
    private PartyApi partyApi;

    private PartyPersistence() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter())
                .create();
        this.retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();


        this.partyApi = retrofit.create(PartyApi.class);
    }

    public Observable<List<Party>> getAllParties() {
        return this.partyApi.getParties();
    }

    public Observable<Party> getParty(UUID uuid){
        return this.partyApi.getParty(uuid.toString());
    }

    public Observable<Party> createParty(String name, String description, LocalDateTime start, LocalDateTime end, int max_people) {

        Party p = new Party(name, description, start, end, max_people);

        Observable<Party> observable = partyApi.createParty(p);
        return observable;
    }
}
