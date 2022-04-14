package org.die6sheeshs.projectx.entities;

import java.time.LocalDateTime;

public class TicketRequest {
    private LocalDateTime created_on;
    private String user_id;
    private String event_id;

    public TicketRequest(LocalDateTime createdOn, String userId, String partyId) {
        this.created_on = createdOn;
        this.user_id = userId;
        this.event_id = partyId;
    }

    public LocalDateTime getCreatedOn() {
        return created_on;
    }

    public String getUserId() {
        return user_id;
    }

    public String getPartyId() {
        return event_id;
    }

    @Override
    public String toString() {
        return "TicketRequest{" +
                "created_on='" + created_on + '\'' +
                ", user_id='" + user_id + '\'' +
                ", event_id=" + event_id +
                '}';
    }
}
