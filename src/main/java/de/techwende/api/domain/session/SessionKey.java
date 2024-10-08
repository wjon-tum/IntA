package de.techwende.api.domain.session;

import lombok.Getter;

import java.security.SecureRandom;

import static de.techwende.api.util.Constants.ALPHABET;
import static de.techwende.api.util.Constants.SESSION_KEY_LENGTH;

@Getter
public class SessionKey {
    private final String sessionKey;

    public SessionKey() {
        SecureRandom random = new SecureRandom();
        char[] newKey = new char[SESSION_KEY_LENGTH];

        for (int i = 0; i < SESSION_KEY_LENGTH; i++) {
            newKey[i] = ALPHABET[random.nextInt(ALPHABET.length)];
        }

        this.sessionKey = String.valueOf(newKey);

    }
}
