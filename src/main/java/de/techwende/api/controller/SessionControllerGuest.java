package de.techwende.api.controller;

import de.techwende.api.service.session.SessionServiceGuest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.URI;
import java.util.Optional;

@Controller
public class SessionControllerGuest {
    private final SessionServiceGuest sessionServiceGuest;

    @Autowired
    public SessionControllerGuest(SessionServiceGuest sessionServiceGuest) {
        this.sessionServiceGuest = sessionServiceGuest;
    }

    @GetMapping("/s/{sessionID}")
    public ResponseEntity<Void> redirect(@PathVariable("sessionID") String sessionID) {
        String redirectUrl = "/join?id=" + sessionID;
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectUrl))
                .build();
    }

    @GetMapping("/join")
    public ResponseEntity<String> joinSession(@PathVariable("id") String sessionID) {
        Optional<String> guestUserJWT = sessionServiceGuest.joinSession(sessionID);
        return guestUserJWT.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.internalServerError()
                        .body("Session " + sessionID + " doesn't exists"));

    }
}
