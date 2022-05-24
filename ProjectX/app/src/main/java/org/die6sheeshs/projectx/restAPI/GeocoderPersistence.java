package org.die6sheeshs.projectx.restAPI;


import org.die6sheeshs.projectx.entities.Address;
import org.die6sheeshs.projectx.entities.City;

import java.util.List;

import io.reactivex.Observable;

public class GeocoderPersistence implements RetrofitPersistence {

    private static final GeocoderPersistence instance = new GeocoderPersistence();

    public static GeocoderPersistence getInstance() {
        return instance;
    }

    private GeocoderApi geocoderApi;

    private GeocoderPersistence() {
        this.geocoderApi = RetrofitService.getInstance().getRetrofitClient().create(GeocoderApi.class);
        RetrofitService.getInstance().addPersistence(this);

    }

    public Observable<City> getCityByCoordinate(double latitude, double longitude){
        return this.geocoderApi.getCityByCoordinate(latitude, longitude);
    }

    public Observable<Address> getAddressShort(double latitude, double longitude){
        return this.geocoderApi.getAddressShort(latitude, longitude);
    }

    @Override
    public void refreshApi() {
        this.geocoderApi = RetrofitService.getInstance().getRetrofitClient().create(GeocoderApi.class);
    }
}