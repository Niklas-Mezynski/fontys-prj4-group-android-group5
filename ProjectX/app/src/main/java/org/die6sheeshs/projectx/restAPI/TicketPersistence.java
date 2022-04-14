package org.die6sheeshs.projectx.restAPI;

import org.die6sheeshs.projectx.entities.Ticket;

import java.util.List;

import io.reactivex.Observable;

public class TicketPersistence implements RetrofitPersistence{

    private static final TicketPersistence instance = new TicketPersistence();

    public static TicketPersistence getInstance() {
        return instance;
    }

    private TicketApi ticketApi;

    private TicketPersistence() {
        this.ticketApi = RetrofitService.getInstance().getRetrofitClient().create(TicketApi.class);
        RetrofitService.getInstance().addPersistence(this);
    }

    public Observable<Void> deleteTicket(String id){
        return ticketApi.deleteTicket(id);
    }

    public Observable<List<Ticket>> getTickets(String id, String jwt) {
        return ticketApi.getTickets(id);
    }

    public Observable<Ticket> getTicketById(String id){
        return ticketApi.getTicketById(id);
    }

    @Override
    public void refreshApi() {
        this.ticketApi = RetrofitService.getInstance().getRetrofitClient().create(TicketApi.class);
    }
}
