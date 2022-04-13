package org.die6sheeshs.projectx.restAPI;

import org.die6sheeshs.projectx.entities.TicketRequest;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TicketRequestApi {
    @GET("ticket-requests/")
    Observable<List<TicketRequest>> getTicketRequests(@Query("filter") String meetingFilter);
    @GET("ticket-requests/count")
    Observable<Integer> getCountTicketRequests(@Query("filter") String meetingFilter);
    @GET("ticket-requests/event/{id}")
    Observable<List<TicketRequest>> getTicketRequestsOfParty(@Path("id") String partyId, @Query("filter") String meetingFilter);
    @GET("ticket-requests/user/{id}")
    Observable<List<TicketRequest>> getTicketRequestsOfUser(@Path("id") String userId, @Query("filter") String meetingFilter);
    @POST("ticket-requests/")
    Observable<TicketRequest> createTicketRequest(@Body TicketRequest ticketRequest);
}
