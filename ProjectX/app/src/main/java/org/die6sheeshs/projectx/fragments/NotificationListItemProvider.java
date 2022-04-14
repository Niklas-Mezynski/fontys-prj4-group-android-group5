package org.die6sheeshs.projectx.fragments;

import org.die6sheeshs.projectx.entities.TicketRequest;

public class NotificationListItemProvider {
    public static TicketRequestListItem newTicketRequestListItem(TicketRequest ticketRequest) {
        return TicketRequestListItem.newInstance(ticketRequest);
    }
}
