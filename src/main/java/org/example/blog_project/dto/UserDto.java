package org.example.blog_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class UserDto {
    private String uid;
    private String password;
    private String confirmPassword;
    private String name;
    private String email;
}
