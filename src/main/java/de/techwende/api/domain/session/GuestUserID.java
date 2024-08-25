package de.techwende.api.domain.session;

import lombok.Getter;

import java.util.Random;

import static de.techwende.api.util.Constants.ALPHABET;
import static de.techwende.api.util.Constants.SESSION_ID_LENGTH;
import static de.techwende.api.util.Constants.USER_ID_LENGTH;

@Getter
public class GuestUserID {
    private final String guestUserID;
    private final SessionID sessionID;

    public GuestUserID(SessionID sessionID) {
        this.sessionID = sessionID;
        Random random = new Random();
        char[] newGuestUserId = new char[USER_ID_LENGTH];
        for (int i = 0; i < SESSION_ID_LENGTH; i++) {
            newGuestUserId[i] = sessionID.getSessionId().charAt(i);
        }

        newGuestUserId[SESSION_ID_LENGTH] = '-';

        for (int i = SESSION_ID_LENGTH + 1; i < USER_ID_LENGTH; i++) {
            newGuestUserId[i] = ALPHABET[random.nextInt(ALPHABET.length)];
        }

        guestUserID = String.valueOf(newGuestUserId);
    }
}
