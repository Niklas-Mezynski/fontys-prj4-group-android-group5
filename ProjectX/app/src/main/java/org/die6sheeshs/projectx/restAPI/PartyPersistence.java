package org.die6sheeshs.projectx.restAPI;

import org.die6sheeshs.projectx.entities.Count;
import org.die6sheeshs.projectx.entities.EventLocation;
import org.die6sheeshs.projectx.entities.EventWithLocation;
import org.die6sheeshs.projectx.entities.Party;
import org.die6sheeshs.projectx.entities.Pictures;
import org.die6sheeshs.projectx.entities.Ticket;
import org.die6sheeshs.projectx.entities.User;

import java.time.LocalDateTime;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class PartyPersistence implements RetrofitPersistence {

    private static final PartyPersistence instance = new PartyPersistence();

    public static PartyPersistence getInstance() {
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

    public Observable<List<EventWithLocation>> getPartiesByLocation(double lat, double lon, int radius) {
        return this.partyApi.getPartyByLocation(lat, lon, radius);
    }

    public Observable<Party> getParty(String uuid) {
        return this.partyApi.getParty(uuid, "{\"include\":[\"eventLocation\"]}");
    }

    public Observable<Party> createParty(String name, String description, LocalDateTime start, LocalDateTime end, int max_people, String user_id, Double price, EventLocation eventLocation) {

        Party p = new Party(name, description, start, end, max_people, user_id, price, eventLocation);
        Observable<Party> observable = partyApi.createParty(p);
        return observable;
    }

    public Observable<EventLocation> getEventLocation(String id) {
        return this.partyApi.getEventLocation(id);
    }

    public Observable<EventLocation> createEventLocation(EventLocation eventLocation) {
        return this.partyApi.createEventLocation(eventLocation.getEvent_id(), eventLocation);
    }

    public Observable<Party> createEventWithLocation(EventWithLocation eventWithLocation) {
        return this.partyApi.createEventWithLocation(eventWithLocation);
    }

    @Override
    public void refreshApi() {
        this.partyApi = RetrofitService.getInstance().getRetrofitClient().create(PartyApi.class);
    }

    public Observable<User> getOwner(String partyId) {
        return this.partyApi.getOwner(partyId);
    }

    public Observable<List<Ticket>> getTickets(String partyId) {
        return this.partyApi.getTicketsOfParty(partyId);
    }

    public Observable<List<Count>> getCountTicketsOfParty(String partyId) {
        return this.partyApi.getCountTicketsOfParty(partyId);
    }

    public Observable<List<Party>> getPartiesFromUser(String id) {
        return this.partyApi.getPartiesFromUser(id);
    }

    public Observable<Count> deleteTicketsFromParty(String id){
        return this.partyApi.deleteTicketsFromEvent(id);
    }

    public Observable<Void> deleteEvent(String id){
        return this.partyApi.deleteEvent(id);
    }

    public Observable<List<Pictures>> getPartyPictures(String partyId) {return this.partyApi.getPicturesOfParty(partyId);}

    public Observable<Pictures> uploadPartyPictures(String event_id, String base64){
        Pictures pictures = new Pictures(base64);
        return this.partyApi.uploadPictures(event_id, pictures);
    }
}
