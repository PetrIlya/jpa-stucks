package com.github.petrilya.jpastucks.service;

import com.github.petrilya.jpastucks.entity.User;
import com.github.petrilya.jpastucks.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("userService")
public class UserService {
    @Autowired
    @Qualifier("userRepository")
    private UserRepository userRepository;

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Transactional
    public void update(User editedUser) {
        save(editedUser);
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void deleteById(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        }
    }
}
