package nyan.ecommerce.spreeze.auth;

import java.time.Instant;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.bson.types.ObjectId;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import nyan.ecommerce.spreeze.auth.token.AuthToken;
import nyan.ecommerce.spreeze.auth.token.VerifiedToken;
import nyan.ecommerce.spreeze.user.UserRole;

@Component
public class AuthHelper {

    private String JWT_SECRET = "upsaf63yeeefblgawye20rgwdlgd";
    private String JWT_ISSUER = "nyanpham-ecommerce";

    public AuthToken createToken(ObjectId userId, UserRole userRole) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            String token = JWT.create()
                    .withClaim("id", userId.toHexString())
                    .withClaim("userRole", userRole.toString())
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plusSeconds(60 * 60 * 24))
                    .withIssuer(JWT_ISSUER)
                    .sign(algorithm);

            return new AuthToken(token);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to sign access token");
        }
    }

    /**
     * @param token
     * @return
     */
    public VerifiedToken validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(JWT_ISSUER)
                    .build();

            DecodedJWT decodedJWT = verifier.verify(token);
            ObjectId id = new ObjectId(decodedJWT.getClaim("id").asString());
            UserRole userRole = UserRole.valueOf(decodedJWT.getClaim("userRole").asString());
            Date issuedAt = decodedJWT.getIssuedAt();
            Date expiresAt = decodedJWT.getExpiresAt();

            return new VerifiedToken(id, userRole, issuedAt, expiresAt);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to sign access token");
        }
    }

    public Cookie createCookie(String name, Object value, Boolean isSecure,
            Boolean isHttpOnly,
            Integer daysToExpire, Integer secToExpire) {
        Cookie cookie = new Cookie(name, name);

        if (daysToExpire != null)
            cookie.setMaxAge(daysToExpire);
        else if (secToExpire != null)
            cookie.setMaxAge(secToExpire);
        else
            throw new IllegalArgumentException("Cookie max age is required");

        cookie.setHttpOnly(isHttpOnly);
        cookie.setSecure(isSecure);

        return cookie;
    }
}
