package org.die6sheeshs.projectx.entities;

import java.time.LocalDateTime;

public class TicketRequest {
    private String userId;
    private String partyId;
    private LocalDateTime createdOn;

    public TicketRequest(String userId, String partyId, LocalDateTime createdOn) {
        this.userId = userId;
        this.partyId = partyId;
        this.createdOn = createdOn;
    }

    public String getUserId() {
        return userId;
    }

    public String getPartyId() {
        return partyId;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    @Override
    public String toString() {
        return "TicketRequest{" +
                "userId='" + userId + '\'' +
                ", partyId='" + partyId + '\'' +
                ", createdOn=" + createdOn +
                '}';
    }
}
