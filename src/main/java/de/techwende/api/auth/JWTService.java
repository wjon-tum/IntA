package de.techwende.api.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import de.techwende.api.domain.session.GuestUserID;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * JSON Web Token (JWT) service
 */
@Log4j2
@Service
public class JWTService {
    private static final String JWT_SUBJECT = "inta";
    private static final String JWT_ISSUER = JWT_SUBJECT + ".techwende.de";

    private final JWTVerifier jwtVerifier;
    private final Algorithm algorithm;
    private final Long tokenLifetimeHours;

    @Autowired
    JWTService(@Value("${inta.jwt.auth.secret}") String tokenServerSecret,
            @Value("${inta.jwt.auth.token.lifetime.hours}") Long hours) {
        algorithm = Algorithm.HMAC256(tokenServerSecret);
        jwtVerifier = JWT.require(algorithm)
                .withSubject(JWT_SUBJECT)
                .withIssuer(JWT_ISSUER)
                .build();
        this.tokenLifetimeHours = hours;
    }

    /**
     * Checks if the provided token is valid for authentication.
     *
     * @param token token
     * @return {@link DecodedJWT} the decoded JWT to extract claims
     */
    public Optional<DecodedJWT> validateToken(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        try {
            DecodedJWT jwt = jwtVerifier.verify(token);
            LOG.debug("Successfully validated token with payload {}", jwt.getPayload());
            return Optional.of(jwt);
        } catch (JWTVerificationException e) {
            LOG.info("Invalid token provided", e);
            return Optional.empty();
        }
    }

    /**
     * Generates a new token specifically for the provided {@link GuestUserID}.
     *
     * @param guestUserID {@link GuestUserID}
     * @return generated token
     */
    public String generateTokenForGuestUser(GuestUserID guestUserID) {
        LOG.debug("Generate token for user {}", guestUserID.getGuestUserID());
        return JWT.create()
                .withSubject(JWT_SUBJECT)
                .withIssuer(JWT_ISSUER)
                .withClaim("guest_user_id", guestUserID.getGuestUserID())
                .withClaim("guest_user_session", guestUserID.getSessionID().getSessionId())
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(tokenLifetimeHours, ChronoUnit.HOURS))
                .sign(algorithm);
    }
}
