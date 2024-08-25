package de.techwende.api.util;

import org.owasp.encoder.Encode;
import org.springframework.http.ResponseEntity;

public final class WebUtils {

    private WebUtils() {
    }

    public static String sanitizeString(String str) {
        return Encode.forHtml(str);
    }

    public static ResponseEntity<String> responseOk(String body) {
        return ResponseEntity.ok().body(sanitizeString(body));
    }

    public static ResponseEntity<String> responseInternalServerError(String message) {
        return ResponseEntity.internalServerError().body(sanitizeString(message));
    }
}
