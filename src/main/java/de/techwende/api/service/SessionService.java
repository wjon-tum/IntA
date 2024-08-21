package de.techwende.api.service;

import static de.techwende.api.util.ConstantsUtil.ALPHABET;
import static de.techwende.api.util.ConstantsUtil.SESSION_ID_LENGTH;

import de.techwende.api.auth.JWTTokenService;
import de.techwende.api.domain.session.GuestUserID;
import de.techwende.api.domain.session.RankingSession;
import de.techwende.api.domain.session.SessionID;
import de.techwende.api.domain.session.SessionKey;
import de.techwende.exception.SessionErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class SessionService {
    private final JWTTokenService jwtTokenService;
    private final Map<String, RankingSession> activeSessions;
    private final long maxSessions;

    @Autowired
    public SessionService(JWTTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;

        maxSessions = (long) Math.pow(ALPHABET.length, SESSION_ID_LENGTH) - 2;
        activeSessions = new HashMap<>();
    }

    public RankingSession createSession() throws SessionErrorException {
        if (activeSessions.size() >= maxSessions) {
            throw new SessionErrorException("Couldn't create a new session, as all sessions are in use");
        }

        SessionID sessionID = generateSessionID();
        RankingSession newSession = new RankingSession(sessionID, new SessionKey());
        activeSessions.put(sessionID.getSessionId(), newSession);
        return newSession;
    }

    public boolean deleteSession(String sessionID, String sessionKey) throws SessionErrorException {
        RankingSession session = activeSessions.get(sessionID);
        if (session != null && !sessionKey.equals(session.sessionKey().getSessionKey())) {
            throw new SessionErrorException("Key " + sessionKey + " is not valid for session " + sessionID);
        }

        return activeSessions.remove(sessionID) != null;
    }

    public Optional<String> joinSession(String sessionID) {
        if (!activeSessions.containsKey(sessionID)) {
            return Optional.empty();
        }

        return Optional.of(jwtTokenService.generateTokenForGuestUser(new GuestUserID(sessionID)));
    }

    private SessionID generateSessionID() throws SessionErrorException {
        SessionID sessionID = new SessionID();
        for (int i = 0; i < 1000; i++) {
            if (!activeSessions.containsKey(sessionID.getSessionId())) {
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
