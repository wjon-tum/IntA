package de.techwende.api.service.session;

import de.techwende.api.domain.ranking.Ranking;
import de.techwende.api.domain.session.GuestUserID;
import de.techwende.api.domain.session.RankingSession;
import de.techwende.api.domain.session.SessionID;
import de.techwende.exception.SessionErrorException;

import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class SessionService {
    protected static final Map<String, RankingSession> ACTIVE_SESSIONS = new HashMap<>();
    protected static final Map<String, List<GuestUserInformation>> GUEST_USER_RANKING = new HashMap<>();

    protected record GuestUserInformation(GuestUserID guestUserID, SessionID sessionID, @Nullable Ranking ranking) {
    }

    protected RankingSession validateSession(String sessionID) throws SessionErrorException {
        RankingSession session = ACTIVE_SESSIONS.get(sessionID);
        if (session == null) {
            throw new SessionErrorException("Session with id " + sessionID + " does not exist");
        }

        return session;
    }

    protected RankingSession validateSession(String sessionID, String sessionKey) throws SessionErrorException {
        RankingSession session = ACTIVE_SESSIONS.get(sessionID);
        if (session == null || !sessionKey.equals(session.getSessionKey().getSessionKey())) {
            throw new SessionErrorException(
                    "Key " + sessionKey + " is not valid for session " + sessionID + " or session does not exist");
        }

        return session;
    }
}
