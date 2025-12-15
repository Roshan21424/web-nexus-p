package com.personal.nexus.security;

import com.personal.nexus.entity.User;
import com.personal.nexus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if (principal instanceof UserDetails userDetails) {

            String username = userDetails.getUsername();

            return userService.findByName(username)
                    .orElseThrow(() -> new UsernameNotFoundException(
                            "User not found: " + username
                    ));
        }

        throw new UsernameNotFoundException("User not authenticated");
    }

}
