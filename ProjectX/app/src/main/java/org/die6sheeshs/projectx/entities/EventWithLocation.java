package org.die6sheeshs.projectx.entities;

import android.media.metrics.Event;

import java.time.LocalDateTime;

public class EventWithLocation extends Party {

    double latitude, longitude;
    String event_id;

    public EventWithLocation(String name, String description, LocalDateTime start, LocalDateTime end, Double price, int max_people, double latitude, double longitude, String event_id, String user_id) {
        super(name, description, start, end, max_people, user_id, price, new EventLocation(latitude, longitude, LocalDateTime.now(), event_id));
        super.setId(event_id);
    }

    public EventWithLocation(String name, String description, LocalDateTime start, LocalDateTime end, Double price, int max_people, double latitude, double longitude, String event_id) {
        this(name, description, start, end, price, max_people, latitude, longitude, event_id, null);
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
