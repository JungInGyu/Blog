package org.example.blog_project.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.blog_project.domain.User;
import org.example.blog_project.dto.UserDto;
import org.example.blog_project.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/")
    public String index() {
        return "loginform";
    }

    @GetMapping("/loginform")
    public String loginForm() {
        return "loginform";
    }

    @GetMapping("/blog")
    public String blog(HttpServletRequest request, Model model) {
        String userId = getUserIdFromCookies(request);
        if (userId != null){
            model.addAttribute("userId",userId);
        }
        return "blog";
    }

    @PostMapping("/login")
    public String login(@RequestBody String id, @RequestBody String password, HttpServletResponse response) throws IOException {
        boolean success = userService.login(id, password);
        if (success) {
            Cookie cookie = new Cookie("userId", id);
            cookie.setPath("/");
            cookie.setMaxAge(60*2);
            response.addCookie(cookie);
            return "redirect:/blog";
        } else {
            return "redirect:/loginform";
        }
    }

    @PostMapping("/register")
    public String register(@RequestBody @Validated UserDto userDto, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/loginform";
        }
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            return "redirect:/loginform";
        }

        boolean success = userService.registerUser(userDto);
        if (success) {
            return "redirect:/welcome";
        } else {
            return "redirect:/loginform";
        }
    }

    private String getUserIdFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("userId".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}