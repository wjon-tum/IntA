package de.techwende.api.controller;

import de.techwende.api.domain.session.RankingSession;
import de.techwende.api.service.SessionService;
import de.techwende.exception.SessionErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SessionControllerOwner {
    private final SessionService sessionService;

    @Autowired
    public SessionControllerOwner(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/new")
    public ResponseEntity<RankingSession> createSession() throws SessionErrorException {
        return ResponseEntity.ok(sessionService.createSession());
    }

    @PostMapping("/delete")
    public ResponseEntity<Boolean> deleteSession(
            @PathVariable("id") String sessionID,
            @PathVariable("key") String sessionKey) throws SessionErrorException {

        return ResponseEntity.ok(sessionService.deleteSession(sessionID, sessionKey));
    }
}
