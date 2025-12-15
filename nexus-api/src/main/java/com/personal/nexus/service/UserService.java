package com.personal.nexus.service;


import com.personal.nexus.entity.User;
import com.personal.nexus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
