package org.die6sheeshs.projectx.helpers;

import org.die6sheeshs.projectx.entities.EventLocation;
import org.die6sheeshs.projectx.entities.EventWithLocation;
import org.die6sheeshs.projectx.entities.Party;

import java.time.LocalDateTime;
import java.util.Optional;

public class EntityHelpers {
    public static Optional<EventLocation> partyToEnvLocation(Party party) {
        if (party instanceof EventWithLocation) {
            EventWithLocation eventWithLocation = (EventWithLocation) party;
            return Optional.of(new EventLocation(eventWithLocation.getLatitude(), eventWithLocation.getLongitude(), LocalDateTime.now(), party.getId()));
        }
        if (party.getEventLocation() == null)
            return Optional.empty();
        return Optional.of(party.getEventLocation());
    }
}
