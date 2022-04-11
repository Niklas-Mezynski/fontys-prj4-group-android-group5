package org.die6sheeshs.projectx.restAPI;

import org.die6sheeshs.projectx.entities.EventLocation;
import org.die6sheeshs.projectx.entities.Party;
import org.die6sheeshs.projectx.entities.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PartyApi {

    @GET("events/{id}")
    Observable<Party> getParty(@Path("id") String id);

    @GET("events/")
    Observable<List<Party>> getParties();

    @POST("events/")
    Observable<Party> createParty(@Body Party party);

    @GET("events/{id}/event-location")
    Observable<EventLocation> getEventLocation(@Path("id") String id);

}
