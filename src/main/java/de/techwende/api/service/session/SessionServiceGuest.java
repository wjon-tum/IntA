package de.techwende.api.service.session;

import de.techwende.api.auth.JWTService;
import de.techwende.api.domain.ranking.Ranking;
import de.techwende.api.domain.session.GuestUserID;
import de.techwende.api.domain.session.RankingSession;
import de.techwende.api.domain.session.SessionID;
import de.techwende.exception.SessionErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;


@Service
public class SessionServiceGuest extends SessionService {

    private final JWTService jwtService;


    @Autowired
    public SessionServiceGuest(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    public Optional<String> joinSession(String sessionID) {
        RankingSession session = ACTIVE_SESSIONS.get(sessionID);
        if (session == null) {
            return Optional.empty();
        }

        return Optional.of(jwtService.generateTokenForGuestUser(new GuestUserID(session.getSessionID())));
    }

    public boolean addGuestUserVote(GuestUserID guestUserID, Ranking ranking) throws SessionErrorException {
        SessionID sessionID = guestUserID.getSessionID();
        RankingSession session = ACTIVE_SESSIONS.get(sessionID.getSessionId());

        if (session == null) {
            throw new SessionErrorException("Session with id " + sessionID + " does not exist");
        }

        if (!session.isOpen()) {
            return false;
        }

        GUEST_USER_RANKING.computeIfAbsent(sessionID.getSessionId(), s -> new ArrayList<>());
        GUEST_USER_RANKING.get(sessionID.getSessionId()).add(new GuestUserInformation(guestUserID, sessionID, ranking));
        return true;
    }


}
