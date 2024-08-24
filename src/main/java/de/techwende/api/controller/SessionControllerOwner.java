package de.techwende.api.controller;

import de.techwende.api.domain.agenda.AgendaItem;
import de.techwende.api.domain.session.RankingSession;
import de.techwende.api.service.session.SessionServiceOwner;
import de.techwende.exception.RankingFailedException;
import de.techwende.exception.SessionErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SessionControllerOwner {
    private final SessionServiceOwner sessionServiceOwner;

    @Autowired
    public SessionControllerOwner(SessionServiceOwner sessionServiceOwner) {
        this.sessionServiceOwner = sessionServiceOwner;
    }

    @GetMapping("/new")
    public ResponseEntity<String> createSession() {
        try {
            RankingSession newSession = sessionServiceOwner.createSession();
            return ResponseEntity.ok(
                    "{id:" + newSession.getSessionID().getSessionId() + ",key:" + newSession.getSessionKey()
                            .getSessionKey() + "}");
        } catch (SessionErrorException e) {
            return ResponseEntity.internalServerError().body("Could not create session: " + e.getMessage());
        }
    }

    @GetMapping("/delete")
    public ResponseEntity<String> deleteSession(
            @RequestParam("id") String sessionID,
            @RequestParam("key") String sessionKey) {

        try {
            sessionServiceOwner.deleteSession(sessionID, sessionKey);
            return ResponseEntity.ok(sessionID);
        } catch (SessionErrorException e) {
            return ResponseEntity.internalServerError()
                    .body("Could not delete session with id " + sessionID + ": " + e.getMessage());
        }
    }

    @GetMapping("/open")
    public ResponseEntity<String> openSession(
            @RequestParam("id") String sessionID,
            @RequestParam("key") String sessionKey) {

        try {
            sessionServiceOwner.openSession(sessionID, sessionKey);
            return ResponseEntity.ok(sessionID);
        } catch (SessionErrorException e) {
            return ResponseEntity.internalServerError()
                    .body("Could not open session with id " + sessionID + ": " + e.getMessage());
        }
    }

    @GetMapping("/lock")
    public ResponseEntity<String> lockSession(
            @RequestParam("id") String sessionID,
            @RequestParam("key") String sessionKey) {

        try {
            sessionServiceOwner.lockSession(sessionID, sessionKey);
            return ResponseEntity.ok(sessionID);
        } catch (SessionErrorException e) {
            return ResponseEntity.internalServerError()
                    .body("Could not lock session with id " + sessionID + ": " + e.getMessage());
        }
    }

    @GetMapping("/eval")
    public ResponseEntity<List<AgendaItem>> evaluateSession(
            @RequestParam("id") String sessionID,
            @RequestParam("key") String sessionKey,
            @RequestParam(name = "resExist", required = false, defaultValue = "false") boolean resultOfExisting) {

        try {
            List<AgendaItem> rankingResults =
                    sessionServiceOwner.evaluateRankings(sessionID, sessionKey, resultOfExisting);
            return ResponseEntity.ok(rankingResults);
        } catch (SessionErrorException | RankingFailedException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
