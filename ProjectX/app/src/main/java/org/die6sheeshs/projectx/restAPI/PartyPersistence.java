package org.die6sheeshs.projectx.restAPI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.die6sheeshs.projectx.entities.EventLocation;
import org.die6sheeshs.projectx.entities.Party;
import org.die6sheeshs.projectx.entities.Ticket;
import org.die6sheeshs.projectx.entities.User;
import org.die6sheeshs.projectx.helpers.PropertyService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class PartyPersistence implements RetrofitPersistence {

    private static final PartyPersistence instance = new PartyPersistence();

    public static PartyPersistence getInstance(){
        return instance;
    }

    private PartyApi partyApi;

    private PartyPersistence() {
        this.partyApi = RetrofitService.getInstance().getRetrofitClient().create(PartyApi.class);
        RetrofitService.getInstance().addPersistence(this);

    }

    public Observable<List<Party>> getAllParties() {
        return this.partyApi.getParties();
    }

    public Observable<Party> getParty(String uuid){
        return this.partyApi.getParty(uuid);
    }

    public Observable<Party> createParty(String name, String description, LocalDateTime start, LocalDateTime end, int max_people) {

        Party p = new Party(name, description, start, end, max_people);

        Observable<Party> observable = partyApi.createParty(p);
        return observable;
    }

    public Observable<EventLocation> getEventLocation(String id){
        return this.partyApi.getEventLocation(id);
    }

    @Override
    public void refreshApi() {
        this.partyApi = RetrofitService.getInstance().getRetrofitClient().create(PartyApi.class);
    }

    public Observable<User> getOwner(String partyId) {
        return this.partyApi.getOwner(partyId);
    }

    public Observable<List<Ticket>> getTickets(String partyId){
        return this.partyApi.getTicketsOfParty(partyId);
    }
}
