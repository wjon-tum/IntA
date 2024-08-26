package de.techwende.api.service.session;

import de.techwende.api.auth.JWTService;
import de.techwende.api.domain.agenda.AgendaItem;
import de.techwende.api.domain.ranking.Ranking;
import de.techwende.api.domain.session.GuestUserID;
import de.techwende.api.domain.session.RankingSession;
import de.techwende.api.domain.session.SessionID;
import de.techwende.exception.SessionErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


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

    public List<AgendaItem> fetchAgendaItemsForSession(String sessionID) throws SessionErrorException {
        RankingSession session = validateSession(sessionID);
        return session.getAgendaItems();
    }

    public void addGuestUserVote(GuestUserID guestUserID, Ranking ranking) throws SessionErrorException {
        SessionID sessionID = guestUserID.getSessionID();
        RankingSession session = validateSession(sessionID.getSessionId());

        if (!session.isOpen()) {
            throw new SessionErrorException(
                    "Session with id " + guestUserID.getSessionID().getSessionId() + " is closed");
        }

        for (AgendaItem item : ranking.getItems()) {
            if (!session.getAgendaItems().contains(item)) {
                throw new SessionErrorException("Ranked on invalid agenda item");
            }
        }

        GUEST_USER_RANKING.computeIfAbsent(sessionID.getSessionId(), _ -> new ArrayList<>());
        GUEST_USER_RANKING.get(sessionID.getSessionId()).add(new GuestUserInformation(guestUserID, sessionID, ranking));
    }
}
