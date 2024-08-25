package de.techwende.api.domain.session;

import de.techwende.api.domain.agenda.AgendaItem;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
public class RankingSession {
    @Setter
    private boolean open;
    private final SessionID sessionID;
    private final SessionKey sessionKey;
    private final Instant creationTime;
    private final List<AgendaItem> agendaItems;

    public RankingSession(SessionID sessionID, SessionKey sessionKey, boolean open) {
        this.sessionID = sessionID;
        this.sessionKey = sessionKey;
        this.creationTime = Instant.now();
        this.open = open;

        agendaItems = new ArrayList<>();
    }

    public void addAgendaItem(AgendaItem agendaItem) {
        agendaItems.add(agendaItem);
    }

    public boolean deleteAgendaItem(AgendaItem agendaItem) {
        return agendaItems.remove(agendaItem);
    }

}
