package dev.hasangurbuz.flightsearchapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final String JWT_HEADER = "Authorization";
    private final JwtService jwtService;
    private final UserDetailService userDetailsService;
    private String token;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        if (request.getRequestURI().equals("/api/auth/login")){
            return true;
        }

        boolean authenticated = SecurityContextHolder.getContext().getAuthentication() != null;
        if (authenticated) {
            return true;
        }

        token = request.getHeader(JWT_HEADER);
        if (StringUtils.isBlank(token)) {
            return true;
        }

        if (!token.startsWith("Bearer ")) {
            return true;
        }

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        UserDetails userDetails = null;
        boolean isTokenValid = false;

        token = token.substring(7);

        try {
            String username = jwtService.extractUsername(token);
            userDetails = userDetailsService.loadUserByUsername(username);
            jwtService.validateToken(token, userDetails.getUsername());
        } catch (Exception ex) {
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);

    }
}
