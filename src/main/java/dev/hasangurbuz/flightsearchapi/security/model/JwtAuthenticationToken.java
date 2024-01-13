package dev.hasangurbuz.flightsearchapi.security.model;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public JwtAuthenticationToken(UserDetails user) {
        super(user, null, user.getAuthorities());
    }
}
