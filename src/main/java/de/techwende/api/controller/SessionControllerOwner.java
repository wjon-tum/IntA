package de.techwende.api.controller;

import de.techwende.api.domain.session.RankingSession;
import de.techwende.api.service.session.SessionServiceOwner;
import de.techwende.exception.SessionErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SessionControllerOwner {
    private final SessionServiceOwner sessionServiceOwner;

    @Autowired
    public SessionControllerOwner(SessionServiceOwner sessionServiceOwner) {
        this.sessionServiceOwner = sessionServiceOwner;
    }

    @GetMapping("/new")
    public ResponseEntity<RankingSession> createSession() throws SessionErrorException {
        return ResponseEntity.ok(sessionServiceOwner.createSession());
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteSession(
            @PathVariable("id") String sessionID,
            @PathVariable("key") String sessionKey) throws SessionErrorException {

        if (sessionServiceOwner.deleteSession(sessionID, sessionKey)) {
            return ResponseEntity.ok(sessionID);
        } else {
            return ResponseEntity.internalServerError().body("Could not delete session with id " + sessionID);
        }
    }
}
