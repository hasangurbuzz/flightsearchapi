package dev.hasangurbuz.flightsearchapi.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Getter
@AllArgsConstructor
public class Role implements GrantedAuthority {

    private RoleName name;
    private Set<Permission> permissions;

    @Override
    public String getAuthority() {
        return name.name();
    }
}
