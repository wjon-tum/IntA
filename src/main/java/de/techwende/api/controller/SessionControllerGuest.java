package de.techwende.api.controller;

import static de.techwende.api.util.WebUtils.sanitizeString;

import de.techwende.api.domain.ranking.Ranking;
import de.techwende.api.domain.session.GuestUserID;
import de.techwende.api.service.session.SessionServiceGuest;
import de.techwende.exception.SessionErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class SessionControllerGuest {
    private final SessionServiceGuest sessionServiceGuest;

    @Autowired
    public SessionControllerGuest(SessionServiceGuest sessionServiceGuest) {
        this.sessionServiceGuest = sessionServiceGuest;
    }

    @GetMapping("/s/{sessionID}")
    public ResponseEntity<Void> redirect(@PathVariable("sessionID") String sessionID) {
        sessionID = sanitizeString(sessionID);
        String redirectUrl = "/join?id=" + sessionID;
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectUrl))
                .build();
    }

    @GetMapping("/join")
    public ResponseEntity<String> joinSession(@RequestParam("id") String sessionID) {
        sessionID = sanitizeString(sessionID);
        try {
            String guestUserId = sessionServiceGuest.joinSession(sessionID);
            return ResponseEntity.ok(guestUserId);
        } catch (SessionErrorException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @PostMapping("/vote")
    public ResponseEntity<Void> vote(@RequestParam("id") GuestUserID guestUserId, @RequestBody Ranking ranking) {
        try {
            sessionServiceGuest.addGuestUserVote(guestUserId, ranking);
            return ResponseEntity.ok().build();
        } catch (SessionErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
