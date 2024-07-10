package org.example.blog_project.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UserRegisterDto {
    private String uid;
    private String password;
    private String confirmPassword;
    private String name;
    private String email;
}
