package org.example.blog_project.security;

import lombok.RequiredArgsConstructor;
import org.example.blog_project.domain.User;
import org.example.blog_project.service.UserService;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUid(username);
        if (user == null) {
            throw new UsernameNotFoundException(username+"의 사용자가 없습니다.");
        }
        UserBuilder userBuilder = org.springframework.security.core.userdetails.User.withUsername(username);
        userBuilder.password(user.getPassword());
        userBuilder.roles(user.getRoles().stream()
                .map(role -> role.getName())
                .toArray(String[]::new));
        return userBuilder.build();
    }
}
