package org.die6sheeshs.projectx.entities;

import android.media.metrics.Event;

import java.time.LocalDateTime;

public class EventWithLocation extends Party {


    private EventWithLocation(String name, String description, LocalDateTime start, LocalDateTime end, int max_people, double latitude, double longitude) {
        super(name, description, start, end, max_people, new EventLocation(latitude, longitude, LocalDateTime.now(), null));
        super.getEventLocation().setEvent_id(super.getId());
    }
}
