package de.techwende.api.service.session;

import static de.techwende.api.util.ConstantsUtil.ALPHABET;
import static de.techwende.api.util.ConstantsUtil.SESSION_ID_LENGTH;

import de.techwende.api.domain.session.RankingSession;
import de.techwende.api.domain.session.SessionID;
import de.techwende.api.domain.session.SessionKey;
import de.techwende.exception.SessionErrorException;
import org.springframework.stereotype.Service;

@Service
public class SessionServiceOwner extends SessionService {
    private final long maxSessions;

    public SessionServiceOwner() {
        maxSessions = (long) Math.pow(ALPHABET.length, SESSION_ID_LENGTH) - 2;
    }

    public RankingSession createSession() throws SessionErrorException {
        if (ACTIVE_SESSIONS.size() >= maxSessions) {
            throw new SessionErrorException("Couldn't create a new session, as all sessions are in use");
        }

        SessionID sessionID = generateSessionID();
        RankingSession newSession = new RankingSession(sessionID, new SessionKey(), true);
        ACTIVE_SESSIONS.put(sessionID.getSessionId(), newSession);
        return newSession;
    }

    public boolean deleteSession(String sessionID, String sessionKey) throws SessionErrorException {
        RankingSession session = ACTIVE_SESSIONS.get(sessionID);
        if (session == null || !sessionKey.equals(session.getSessionKey().getSessionKey())) {
            throw new SessionErrorException("Key " + sessionKey + " is not valid for session " + sessionID);
        }

        return ACTIVE_SESSIONS.remove(sessionID) != null;
    }

    public void openSession(String sessionID, String sessionKey) throws SessionErrorException {
        RankingSession session = ACTIVE_SESSIONS.get(sessionID);
        if (session == null || !sessionKey.equals(session.getSessionKey().getSessionKey())) {
            throw new SessionErrorException("Key " + sessionKey + " is not valid for session " + sessionID);
        }

        ACTIVE_SESSIONS.get(sessionID).setOpen(true);
    }

    public void lockSession(String sessionID, String sessionKey) throws SessionErrorException {
        RankingSession session = ACTIVE_SESSIONS.get(sessionID);
        if (session == null || !sessionKey.equals(session.getSessionKey().getSessionKey())) {
            throw new SessionErrorException("Key " + sessionKey + " is not valid for session " + sessionID);
        }

        ACTIVE_SESSIONS.get(sessionID).setOpen(false);
    }

    private SessionID generateSessionID() throws SessionErrorException {
        SessionID sessionID = new SessionID();
        for (int i = 0; i < 1000; i++) {
            if (!ACTIVE_SESSIONS.containsKey(sessionID.getSessionId())) {
                break;
            }

            if (i == 999) {
                throw new SessionErrorException("Couldn't create a new session, as all IDs are in use");
            }

            sessionID = new SessionID();
        }

        return sessionID;
    }
}
