package org.die6sheeshs.projectx.entities;

import android.media.metrics.Event;

import java.time.LocalDateTime;

public class EventWithLocation extends Party {

    double latitude, longitude;

    private EventWithLocation(String name, String description, LocalDateTime start, LocalDateTime end, double price, int max_people, double latitude, double longitude) {
        super(name, description, start, end, max_people, price, new EventLocation(latitude, longitude, LocalDateTime.now(), ""));
        super.getEventLocation().setEvent_id(super.getId());
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
