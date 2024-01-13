package dev.hasangurbuz.flightsearchapi.security;

import dev.hasangurbuz.flightsearchapi.security.model.Operation;
import dev.hasangurbuz.flightsearchapi.security.service.impl.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.List;

import static dev.hasangurbuz.flightsearchapi.security.model.Operation.*;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final UserDetailServiceImpl userDetailServiceImpl;
    private final JwtFilter jwtFilter;
    private final AccessDenyHandler accessDenyHandler;
    private final AuthEntryPoint authEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.
                csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers(authorizedPaths().toArray(new AntPathRequestMatcher[0])).hasAuthority("WRITE")
                .anyRequest().hasAuthority("READ")
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint)
                .accessDeniedHandler(accessDenyHandler)
                .and()
                .build();

    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       BCryptPasswordEncoder bCryptPasswordEncoder,
                                                       UserDetailServiceImpl userDetailServiceImpl)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailServiceImpl)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

    @Bean
    public List<AntPathRequestMatcher> authorizedPaths() {
        List<AntPathRequestMatcher> authorizedPaths = new ArrayList<>();
        List<Operation> authorizedOperations = List.of(CREATE, UPDATE, DELETE);
        for (Operation op : authorizedOperations) {
            String path = "/**/" + op.name().toLowerCase();
            AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher(path);
            authorizedPaths.add(requestMatcher);
        }
        return authorizedPaths;
    }


}
