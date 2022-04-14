package org.die6sheeshs.projectx.entities;

import java.time.LocalDateTime;

public class Party {

    private String id, name, description, user_id;
    private LocalDateTime start, end;
    private int max_people;
    private EventLocation eventLocation;
    private Double price;
    public Party(String name, String description, LocalDateTime start, LocalDateTime end, int max_people, Double price, EventLocation eventLocation){
        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        this.max_people = max_people;
        this.eventLocation = eventLocation;
        this.price = price;
    }
    public Party(String name, String description, LocalDateTime start, LocalDateTime end, int max_people, String user_id, Double price, EventLocation eventLocation){
        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        this.max_people = max_people;
        this.eventLocation = eventLocation;
        this.price = price;
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public int getMax_people() {
        return max_people;
    }

    public void setMax_people(int max_people) {
        this.max_people = max_people;
    }

    @Override
    public String toString() {
        return "Party{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", max_people=" + max_people +
                ", eventLocation=" + eventLocation +
                ", price=" + price +
                '}';
    }

    public EventLocation getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(EventLocation eventLocation) {
        this.eventLocation = eventLocation;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
