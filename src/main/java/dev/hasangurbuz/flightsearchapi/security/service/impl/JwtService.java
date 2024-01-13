package dev.hasangurbuz.flightsearchapi.security.service.impl;

import dev.hasangurbuz.flightsearchapi.security.service.AbstractJwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService extends AbstractJwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    @Override
    protected String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    @Override
    public boolean validateToken(String token, String username) {
        String tokenUsername = extractSubject(token);
        if (!tokenUsername.equals(username)) {
            return false;
        }
        if (isTokenExpired(token)) {
            return false;
        }
        return true;
    }

    @Override
    public String extractSubject(String token) {
        return super.extractSubject(token);
    }

    @Override
    public Date extractExpiration(String token) {
        return super.extractExpiration(token);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return super.extractClaim(token, claimsResolver);
    }

    @Override
    protected Claims extractAllClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            throw new JwtException("Token is not valid");
        }
    }

    @Override
    protected Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
