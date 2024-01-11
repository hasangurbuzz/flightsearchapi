package dev.hasangurbuz.flightsearchapi.security.service.impl;

import dev.hasangurbuz.flightsearchapi.security.model.Permission;
import dev.hasangurbuz.flightsearchapi.security.model.Role;
import dev.hasangurbuz.flightsearchapi.security.model.RoleName;
import dev.hasangurbuz.flightsearchapi.security.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final Map<String, User> mockUsers = new HashMap<>();
    private final BCryptPasswordEncoder passwordEncoder;

    public UserDetailServiceImpl(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        Role adminRole = new Role(RoleName.ADMIN, Set.of(Permission.READ, Permission.WRITE));
        User adminUser = new User();
        adminUser.setRole(adminRole);
        adminUser.setUsername("admin");
        adminUser.setPassword(passwordEncoder.encode("admin"));

        Role standardRole = new Role(RoleName.STANDARD, Set.of(Permission.READ));
        User standardUser = new User();
        standardUser.setRole(standardRole);
        standardUser.setUsername("user");
        standardUser.setPassword(passwordEncoder.encode("1234"));

        mockUsers.put(standardUser.getUsername(), standardUser);
        mockUsers.put(adminUser.getUsername(), adminUser);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = mockUsers.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }



        Set<GrantedAuthority> authorityList = new HashSet<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_"+user.getRole().getAuthority()));
        for (Permission permission : user.getRole().getPermissions()) {
            authorityList.add(new SimpleGrantedAuthority(permission.name()));
        }

        user.setAuthorities(authorityList);
        return user;
    }
}
