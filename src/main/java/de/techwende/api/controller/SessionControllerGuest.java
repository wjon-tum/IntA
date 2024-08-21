package de.techwende.api.controller;

import de.techwende.api.service.SessionService;
import de.techwende.exception.SessionErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class SessionControllerGuest {
    private final SessionService sessionService;

    @Autowired
    public SessionControllerGuest(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/join")
    public ResponseEntity<String> createSession(@PathVariable("id") String sessionID) throws SessionErrorException {
        Optional<String> guestUserJWT = sessionService.joinSession(sessionID);
        return guestUserJWT.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.internalServerError()
                        .body("Session " + sessionID + " doesn't exists"));

    }

    @PostMapping("/delete")
    public ResponseEntity<Boolean> deleteSession(
            @PathVariable("id") String sessionID,
            @PathVariable("key") String sessionKey) throws SessionErrorException {

        return ResponseEntity.ok(sessionService.deleteSession(sessionID, sessionKey));
    }
}
