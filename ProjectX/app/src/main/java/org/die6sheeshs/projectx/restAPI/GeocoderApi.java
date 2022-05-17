package org.die6sheeshs.projectx.restAPI;

import org.die6sheeshs.projectx.entities.City;
import org.die6sheeshs.projectx.entities.Friend;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GeocoderApi {

    @GET("/toCity/{latitude},{longitude}")
    Observable<City> getCityByCoordinate(@Path("latitude") double latitude, @Path("longitude") double longitude);

}
