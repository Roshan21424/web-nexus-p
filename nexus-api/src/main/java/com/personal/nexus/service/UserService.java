package com.personal.nexus.service;


import com.personal.nexus.entity.User;
import com.personal.nexus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }


}
