package com.personal.nexus.security;

import com.personal.nexus.entity.User;
import com.personal.nexus.repository.UserRepository;
import com.personal.nexus.security.jwtutils.AuthTokenFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Optional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);


    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

        User user = userRepository.findByName(name)
                .orElseThrow(() -> {
                    logger.warn("Login attempt for unknown username={}", name);
                    return new UsernameNotFoundException("User not found: " + name);
                });

        System.out.println("User found: " + user.getName());

        String role = "ROLE_" + user.getRole().name();

        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority(role))
        );
    }

}
