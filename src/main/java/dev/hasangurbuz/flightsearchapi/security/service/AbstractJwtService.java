package dev.hasangurbuz.flightsearchapi.security.service;

import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;


public abstract class AbstractJwtService {

    public abstract String generateToken(String uniqueParam);

    protected abstract String createToken(Map<String, Object> claims, String subject);

    public abstract boolean validateToken(String token, String uniqueParam);

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    protected abstract Claims extractAllClaims(String token);

    protected Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
