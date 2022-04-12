package org.die6sheeshs.projectx.entities;

import java.time.LocalDateTime;

public class TicketRequest {
    private String userId;
    private String eventId;
    private LocalDateTime createdOn;

    public TicketRequest(String userId, String eventId, LocalDateTime createdOn) {
        this.userId = userId;
        this.eventId = eventId;
        this.createdOn = createdOn;
    }

    public String getUserId() {
        return userId;
    }

    public String getEventId() {
        return eventId;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    @Override
    public String toString() {
        return "TicketRequest{" +
                "userId='" + userId + '\'' +
                ", eventId='" + eventId + '\'' +
                ", createdOn=" + createdOn +
                '}';
    }
}
