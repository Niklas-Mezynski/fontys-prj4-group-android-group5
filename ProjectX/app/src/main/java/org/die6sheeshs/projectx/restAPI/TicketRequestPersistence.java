package org.die6sheeshs.projectx.restAPI;

import org.die6sheeshs.projectx.entities.TicketRequest;

import java.time.LocalDateTime;
import java.util.List;

import io.reactivex.Observable;

public class TicketRequestPersistence implements RetrofitPersistence {

    private static final TicketRequestPersistence instance = new TicketRequestPersistence();

    public static TicketRequestPersistence getInstance(){
        return instance;
    }

    private TicketRequestApi ticketRequestApi;

    private TicketRequestPersistence() {
        this.ticketRequestApi = RetrofitService.getInstance().getRetrofitClient().create(TicketRequestApi.class);
        RetrofitService.getInstance().addPersistence(this);
    }

    @Override
    public void refreshApi() {
        this.ticketRequestApi = RetrofitService.getInstance().getRetrofitClient().create(TicketRequestApi.class);
    }

    public Observable<List<TicketRequest>> getAllTicketRequests(String meetingFilter) {
        return this.ticketRequestApi.getTicketRequests(meetingFilter);
    }

    public Observable<Integer> getCountOfTicketRequests(String meetingFilter) {
        return this.ticketRequestApi.getCountTicketRequests(meetingFilter);
    }

    public Observable<TicketRequest> createTicketRequest(String userId, String partyId, LocalDateTime createdOn) {
        TicketRequest tq = new TicketRequest(userId, partyId, createdOn);
        Observable<TicketRequest> observable = this.ticketRequestApi.createTicketRequest(tq);
        return observable;
    }

    public Observable<List<TicketRequest>> getTicketRequestsOfParty(String partyId) {
        return this.ticketRequestApi.getTicketRequestsOfParty(partyId, "");
    }

    public Observable<List<TicketRequest>> getTicketRequestsOfUser(String userId) {
        return this.ticketRequestApi.getTicketRequestsOfUser(userId, "");
    }
}