package org.example.blog_project.service;

import lombok.RequiredArgsConstructor;
import org.example.blog_project.domain.Role;
import org.example.blog_project.dto.UserRegisterDto;
import org.example.blog_project.domain.User;
import org.example.blog_project.repository.RoleRepository;
import org.example.blog_project.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean login(String uid, String password){
        User user = userRepository.findByUid(uid);
        if (user != null && user.getPassword().equals(password)){
            return true;
        }
        return false;
    }


    //회원가입
    @Transactional
    public User registerUser(User user) {
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.setRoles(Collections.singleton(userRole));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRegistrationDate(LocalDateTime.now());

        return userRepository.save(user);
    }

    public User findByUid(String uid){
        return userRepository.findByUid(uid);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUser(Long id){
        return userRepository.findById(id);
    }
}
