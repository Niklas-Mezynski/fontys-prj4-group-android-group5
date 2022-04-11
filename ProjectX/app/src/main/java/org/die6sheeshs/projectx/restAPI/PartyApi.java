package org.die6sheeshs.projectx.restAPI;

import org.die6sheeshs.projectx.entities.Party;
import org.die6sheeshs.projectx.entities.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PartyApi {

    @GET("events/")
    Call<List<Party>> getParties();

    @POST("events/")
    Observable<Party> createParty(@Body Party party);

}
