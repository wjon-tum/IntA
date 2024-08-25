package de.techwende.api.domain.session;

import static de.techwende.api.util.Constants.ALPHABET;
import static de.techwende.api.util.Constants.SESSION_ID_LENGTH;

import lombok.Getter;

import java.util.Random;

@Getter
public class SessionID {
    private final String sessionId;

    public SessionID() {
        Random random = new Random();
        char[] newSessionID = new char[SESSION_ID_LENGTH];

        for (int i = 0; i < SESSION_ID_LENGTH; i++) {
            newSessionID[i] = ALPHABET[random.nextInt(ALPHABET.length)];
        }

        sessionId = String.valueOf(newSessionID);
    }
}
