package org.die6sheeshs.projectx.restAPI;

import org.die6sheeshs.projectx.entities.Ticket;

import java.util.List;
import io.reactivex.Observable;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserTicketApi {

    @GET("users/{id}/tickets")
    Observable<List<Ticket>> getTickets(@Path("id") String id);

    @GET("users/{id}/tickets")
    void deleteTicket(@Path("id") String id);
}
