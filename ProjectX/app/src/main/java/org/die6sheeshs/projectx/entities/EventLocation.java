package org.die6sheeshs.projectx.entities;

import java.time.LocalDateTime;

public class EventLocation {

    private double latitude, longitude;
    LocalDateTime created_on;
    String event_id;


    public EventLocation(double latitude, double longitude, LocalDateTime created_on, String event_id){
        this.latitude = latitude;
        this.longitude = longitude;
        this.created_on = created_on;
        this.event_id = event_id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longitude;
    }

    public void setLongtitude(double longitude) {
        this.longitude = longitude;
    }

    public LocalDateTime getCreated_on() {
        return created_on;
    }

    public void setCreated_on(LocalDateTime created_on) {
        this.created_on = created_on;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    @Override
    public String toString() {
        return "EventLocation{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", created_on=" + created_on +
                ", event_id='" + event_id + '\'' +
                '}';
    }
}
