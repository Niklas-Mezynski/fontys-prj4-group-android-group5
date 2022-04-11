package org.die6sheeshs.projectx.entities;

public class Ticket {
    private String id, event_id, user_id;

    public Ticket(String id,String event_id,String user_id){
        this.id = id;
        this.event_id = event_id;
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public String getEvent_id() {
        return event_id;
    }

    public String getUser_id() {
        return user_id;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id='" + id + '\'' +
                ", event_id='" + event_id + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
