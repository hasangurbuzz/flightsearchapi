package dev.hasangurbuz.flightsearchapi.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class UserDetailService implements UserDetailsService {

    private final Map<String, User> mockUsers = new HashMap<>();
    private final BCryptPasswordEncoder passwordEncoder;

    public UserDetailService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        Role adminRole = new Role(RoleName.ADMIN, Set.of(Permission.READ, Permission.WRITE));
        User adminUser = new User("admin", passwordEncoder.encode("admin"), adminRole);

        Role standardRole = new Role(RoleName.STANDARD, Set.of(Permission.READ));
        User standardUser = new User("user", passwordEncoder.encode("1234"), standardRole);

        mockUsers.put(standardUser.getUsername(), standardUser);
        mockUsers.put(adminUser.getUsername(), adminUser);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = mockUsers.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
}
