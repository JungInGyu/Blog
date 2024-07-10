package org.example.blog_project.service;

import lombok.RequiredArgsConstructor;
import org.example.blog_project.domain.Role;
import org.example.blog_project.dto.UserDto;
import org.example.blog_project.domain.User;
import org.example.blog_project.repository.RoleRepository;
import org.example.blog_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

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
    public boolean registerUser(UserDto userDto) {
        if (isIdDuplicate(userDto.getUid()) || isEmailDuplicate(userDto.getEmail())){
            return false;
        }
        Role userRole = roleRepository.findByName("ROLE_USER");
        User user = new User();
        user.setRoles(Collections.singleton(userRole));
        user.setUid(userDto.getUid());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setRegistrationDate(LocalDateTime.now());

        userRepository.save(user);
        return true;
    }

    public User findByUid(String uid){
        return userRepository.findByUid(uid);
    }

    public boolean isIdDuplicate(String uid){
        return userRepository.existsByUid(uid);
    }

    public boolean isEmailDuplicate(String email){
        return userRepository.existsByEmail(email);
    }
}
