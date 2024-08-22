package de.techwende.api.service.session;

import de.techwende.api.domain.session.RankingSession;

import java.util.HashMap;
import java.util.Map;

public abstract class SessionService {
    protected static final Map<String, RankingSession> ACTIVE_SESSIONS = new HashMap<>();
}
