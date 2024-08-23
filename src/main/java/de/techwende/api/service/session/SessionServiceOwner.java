package de.techwende.api.service.session;

import static de.techwende.api.util.ConstantsUtil.ALPHABET;
import static de.techwende.api.util.ConstantsUtil.SESSION_ID_LENGTH;

import de.techwende.api.domain.agenda.AgendaItem;
import de.techwende.api.domain.ranking.Ranking;
import de.techwende.api.domain.session.RankingSession;
import de.techwende.api.domain.session.SessionID;
import de.techwende.api.domain.session.SessionKey;
import de.techwende.api.service.RankingService;
import de.techwende.exception.RankingFailedException;
import de.techwende.exception.SessionErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SessionServiceOwner extends SessionService {
    private final RankingService rankingService;
    private final long maxSessions;

    @Autowired
    public SessionServiceOwner(RankingService rankingService) {
        this.rankingService = rankingService;
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

    public void deleteSession(String sessionID, String sessionKey) throws SessionErrorException {
        validateSession(sessionID, sessionKey);
        if (ACTIVE_SESSIONS.remove(sessionID) == null) {
            throw new SessionErrorException(
                    "Couldn't delete session with session ID " + sessionID + ", as it does not exist");
        }
    }

    public void openSession(String sessionID, String sessionKey) throws SessionErrorException {
        RankingSession session = validateSession(sessionID, sessionKey);
        session.setOpen(true);
    }

    public void lockSession(String sessionID, String sessionKey) throws SessionErrorException {
        RankingSession session = validateSession(sessionID, sessionKey);
        session.setOpen(false);
    }

    public List<AgendaItem> evaluateRankings(String sessionID, String sessionKey, boolean resultOfExisting)
            throws SessionErrorException, RankingFailedException {
        Set<Ranking> guestUserRankings = collectGuestUserRankings(sessionID, sessionKey);
        return rankingService.computeRankingResultList(List.copyOf(guestUserRankings), resultOfExisting);
    }

    public Set<Ranking> collectGuestUserRankings(String sessionID, String sessionKey) throws SessionErrorException {
        validateSession(sessionID, sessionKey);

        return GUEST_USER_RANKING.get(sessionID).stream()
                .map(GuestUserInformation::ranking)
                .collect(Collectors.toSet());
    }


    private RankingSession validateSession(String sessionID, String sessionKey) throws SessionErrorException {
        RankingSession session = ACTIVE_SESSIONS.get(sessionID);
        if (session == null || !sessionKey.equals(session.getSessionKey().getSessionKey())) {
            throw new SessionErrorException("Key " + sessionKey + " is not valid for session " + sessionID);
        }

        return session;
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
