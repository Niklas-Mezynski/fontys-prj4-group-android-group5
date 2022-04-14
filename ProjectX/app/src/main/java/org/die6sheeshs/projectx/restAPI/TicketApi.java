package org.die6sheeshs.projectx.restAPI;

import org.die6sheeshs.projectx.entities.Ticket;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TicketApi {

    @GET("users/{id}/tickets")
    Observable<List<Ticket>> getTickets(@Path("id") String id);

    @GET("tickets/{id}")
    Observable<Ticket> getTicketById(@Path("id")String id);

    @DELETE("tickets/{id}")
    Observable<Void> deleteTicket(@Path("id")String id);


}
