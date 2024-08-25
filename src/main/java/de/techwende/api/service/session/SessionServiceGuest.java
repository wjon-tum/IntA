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


@Service
public class SessionServiceGuest extends SessionService {

    private final JWTService jwtService;


    @Autowired
    public SessionServiceGuest(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    public String joinSession(String sessionID) throws SessionErrorException {
        RankingSession session = validateSession(sessionID);
        GuestUserID guestUserID = new GuestUserID(session.getSessionID());
        return jwtService.generateTokenForGuestUser(guestUserID);
    }

    public void addGuestUserVote(GuestUserID guestUserID, Ranking ranking) throws SessionErrorException {
        SessionID sessionID = guestUserID.getSessionID();
        RankingSession session = validateSession(sessionID.getSessionId());

        if (!session.isOpen()) {
            throw new SessionErrorException(
                    "Session with id " + guestUserID.getSessionID().getSessionId() + " is closed");
        }

        GUEST_USER_RANKING.computeIfAbsent(sessionID.getSessionId(), s -> new ArrayList<>());
        GUEST_USER_RANKING.get(sessionID.getSessionId()).add(new GuestUserInformation(guestUserID, sessionID, ranking));
    }
}
