package de.techwende.api.service.session;

import de.techwende.api.domain.ranking.Ranking;
import de.techwende.api.domain.session.GuestUserID;
import de.techwende.api.domain.session.RankingSession;
import de.techwende.api.domain.session.SessionID;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class SessionService {
    protected static final Map<String, RankingSession> ACTIVE_SESSIONS = new HashMap<>();
    protected static final Map<String, List<GuestUserInformation>> GUEST_USER_RANKING = new HashMap<>();

    protected record GuestUserInformation(GuestUserID guestUserID, SessionID sessionID, @Nullable Ranking ranking) {
    }
}
