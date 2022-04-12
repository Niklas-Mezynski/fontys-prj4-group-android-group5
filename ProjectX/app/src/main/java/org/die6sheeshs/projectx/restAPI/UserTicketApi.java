package org.die6sheeshs.projectx.restAPI;

import org.die6sheeshs.projectx.entities.Ticket;

import java.util.List;

import io.reactivex.Observable;

import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserTicketApi {

    @GET("users/{id}/tickets")
    Observable<List<Ticket>> getTickets(@Path("id") String id);

    @DELETE("users/{id}/tickets")
    void deleteTicket(@Path("id") String id);
}
