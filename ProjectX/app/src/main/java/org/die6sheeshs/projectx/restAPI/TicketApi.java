package org.die6sheeshs.projectx.restAPI;

import io.reactivex.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

public interface TicketApi {
    @DELETE("tickets/{id}")
    public Observable<Void> deleteTicket(@Path("id")String id);
}
