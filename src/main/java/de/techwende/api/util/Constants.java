package de.techwende.api.util;

public final class Constants {
    public static final int USER_ID_LENGTH = 8;             // Has to be longer than SESSION_ID_LENGTH + 2
    public static final int SESSION_ID_LENGTH = 4;
    public static final int SESSION_KEY_LENGTH = 20;
    public static final Character[] ALPHABET = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z'
    };

    private Constants() {
    }
}
