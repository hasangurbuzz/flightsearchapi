package dev.hasangurbuz.flightsearchapi.api.controller;

import dev.hasangurbuz.flightsearchapi.security.model.Role;
import dev.hasangurbuz.flightsearchapi.security.model.User;
import dev.hasangurbuz.flightsearchapi.security.service.impl.JwtService;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.AuthApi;
import org.openapitools.model.AuthLoginRequestDTO;
import org.openapitools.model.PermissionDTO;
import org.openapitools.model.RoleDTO;
import org.openapitools.model.TokenDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AuthApiController implements AuthApi {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<TokenDTO> login(AuthLoginRequestDTO authLoginRequestDTO) {
        Authentication authenticate = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authLoginRequestDTO.getUsername(),
                        authLoginRequestDTO.getPassword()
                )
        );

        User user = (User) authenticate.getPrincipal();
        Role role = user.getRole();
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setName(RoleDTO.NameEnum.fromValue(role.getName().name()));

        List<PermissionDTO> permissionDTOS = role.getPermissions()
                .stream()
                .map(permission -> PermissionDTO.fromValue(permission.name()))
                .collect(Collectors.toList());

        roleDTO.setPermissions(permissionDTOS);

        String token = jwtService.generateToken(user.getUsername());
        Date validUntil = jwtService.extractExpiration(token);
        TokenDTO response = new TokenDTO();
        response.setValue(token);
        response.setRole(roleDTO);
        response.setValidUntil(validUntil.toInstant().atOffset(ZoneOffset.UTC));

        return ResponseEntity.ok(response);
    }
}
