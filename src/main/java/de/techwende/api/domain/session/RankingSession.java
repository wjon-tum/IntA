package de.techwende.api.domain.session;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
public class RankingSession {
    private final SessionID sessionID;
    private final SessionKey sessionKey;
    private final Instant creationTime;
    @Setter
    private boolean open;

    public RankingSession(SessionID sessionID, SessionKey sessionKey, boolean open) {
        this.sessionID = sessionID;
        this.sessionKey = sessionKey;
        this.creationTime = Instant.now();
        this.open = open;
    }

}
