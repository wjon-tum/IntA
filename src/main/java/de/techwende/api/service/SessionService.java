package de.techwende.api.service;

import de.techwende.api.domain.session.RankingSession;
import de.techwende.api.domain.session.SessionID;
import de.techwende.api.domain.session.SessionKey;
import de.techwende.exception.SessionErrorException;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SessionService {
    private final Map<String, RankingSession> activeSessions;
    private final long maxSessions;

    private static final int ID_LENGTH = 4;
    private static final int KEY_LENGTH = 20;
    private static final Character[] ALPHABET = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z'
    };

    public SessionService() {
        activeSessions = new HashMap<>();
        maxSessions = (long) Math.pow(ALPHABET.length, ID_LENGTH) - 2;
    }

    public String createSession() throws SessionErrorException {
        SessionID sessionID = createID();
        RankingSession newSession = new RankingSession(sessionID, createKey());
        activeSessions.put(sessionID.sessionId(), newSession);
        return sessionID.toString();
    }

    public boolean deleteSession(String sessionID) {
        return activeSessions.remove(sessionID) != null;
    }

    private SessionID createID() throws SessionErrorException {
        if (activeSessions.size() > maxSessions) {
            throw new SessionErrorException("Couldn't create a new session, as all sessions are in use");
        }

        Random random = new Random();
        char[] newID = new char[ID_LENGTH];

        for (int i = 0; i < ID_LENGTH; i++) {
            List<Character> possibleChars = new ArrayList<>(List.of(ALPHABET));
            for (RankingSession session : activeSessions.values()) {
                possibleChars.remove(session.sessionID().sessionId().charAt(i));
            }

            if (possibleChars.isEmpty()) {
                throw new SessionErrorException("Couldn't create a new session, as there are no IDs left");
            }

            int randIndex = random.nextInt(possibleChars.size());
            newID[i] = possibleChars.get(randIndex);
        }

        return new SessionID(String.valueOf(newID));
    }

    private SessionKey createKey() {
        SecureRandom random = new SecureRandom();
        char[] newKey = new char[KEY_LENGTH];

        for (int i = 0; i < KEY_LENGTH; i++) {
            newKey[i] = ALPHABET[random.nextInt(ALPHABET.length)];
        }

        return new SessionKey(String.valueOf(newKey));
    }
}
