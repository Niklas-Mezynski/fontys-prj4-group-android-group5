package org.die6sheeshs.projectx.restAPI;

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

    @Override
    public void refreshApi() {
        this.ticketApi = RetrofitService.getInstance().getRetrofitClient().create(TicketApi.class);
    }
}
