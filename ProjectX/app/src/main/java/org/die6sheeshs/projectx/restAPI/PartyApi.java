package org.die6sheeshs.projectx.restAPI;

import org.die6sheeshs.projectx.entities.Count;
import com.fasterxml.jackson.annotation.JsonFilter;

import org.die6sheeshs.projectx.entities.EventLocation;
import org.die6sheeshs.projectx.entities.EventWithLocation;
import org.die6sheeshs.projectx.entities.Party;
import org.die6sheeshs.projectx.entities.Pictures;
import org.die6sheeshs.projectx.entities.Ticket;
import org.die6sheeshs.projectx.entities.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PartyApi {

    @GET("events/{id}")
    Observable<Party> getParty(@Path("id") String id, @Query("filter") String meetingFilter);

    @GET("events/")
    Observable<List<Party>> getParties();

    @POST("events/")
    Observable<Party> createParty(@Body Party party);

    @GET("eventsByLocationRadius/{lat},{lon},{radius}")
    Observable<List<EventWithLocation>> getPartyByLocation(@Path("lat") double user_lat, @Path("lon") double user_lon, @Path("radius") int search_radius);

    @POST("events/{id}/event-location")
    Observable<EventLocation> createEventLocation(@Path("id") String event_id, @Body EventLocation eventLocation);

    @GET("events/{id}/event-location")
    Observable<EventLocation> getEventLocation(@Path("id") String id);

    @GET("events/{id}/user")
    Observable<User> getOwner(@Path("id") String partyId);

    @GET("events/{id}/tickets")
    Observable<List<Ticket>> getTicketsOfParty(@Path("id") String partyId);

    @GET("events/{id}/tickets/count")
    Observable<List<Count>> getCountTicketsOfParty(@Path("id") String partyId);

    @GET("users/{id}/events")
    Observable<List<Party>> getPartiesFromUser(@Path("id") String id);

    @DELETE("events/{id}/tickets")
    Observable<Count> deleteTicketsFromEvent(@Path("id") String partyId);

    @DELETE("events/{id}")
    Observable<Void> deleteEvent(@Path("id") String partyId);

    @GET("events/{id}/pictures")
    Observable<List<Pictures>> getPartyPictures(@Path("id") String id);
}
