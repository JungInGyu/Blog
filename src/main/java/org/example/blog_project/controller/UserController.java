package org.example.blog_project.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.blog_project.domain.RefreshToken;
import org.example.blog_project.domain.Role;
import org.example.blog_project.domain.User;
import org.example.blog_project.dto.UserLoginResponseDto;
import org.example.blog_project.service.UserLoginDto;
import org.example.blog_project.dto.UserRegisterDto;
import org.example.blog_project.jwt.JwtTokenizer;
import org.example.blog_project.service.RefreshTokenService;
import org.example.blog_project.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenizer jwtTokenizer;

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
    public ResponseEntity login(@RequestBody @Valid UserLoginDto userLoginDto, HttpServletResponse response, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        User user = userService.findByUid(userLoginDto.getUsername());

        if (!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())){
            return new ResponseEntity("비밀번호가 올바르지 않습니다",HttpStatus.UNAUTHORIZED);
        }

        List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

        String refreshToken = jwtTokenizer.createRefreshToken(user.getUser_id(),user.getEmail(),user.getName(), user.getUid(),roles);
        RefreshToken refreshTokenEntitiy = new RefreshToken();
        refreshTokenEntitiy.setValue((refreshToken));
        refreshTokenEntitiy.setUserId(user.getUser_id());

        refreshTokenService.addRefreshToken(refreshTokenEntitiy);

        UserLoginResponseDto loginResponseDto = UserLoginResponseDto.builder()
                .refreshToken(refreshToken)
                .userId(user.getUser_id())
                .name(user.getName())
                .build();

        Cookie refreshTokenCookie = new Cookie("refreshToken",refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.REFRESH_TOKEN_EXPIRE_TIME/10000));

        response.addCookie(refreshTokenCookie);
        return new ResponseEntity(loginResponseDto, HttpStatus.OK);
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") @Validated User user, BindingResult result) {
        if (result.hasErrors()){
            return "loginform";
        }
        User findUser = userService.findByUid(user.getUid());
        if (findUser != null){
            result.rejectValue("uId",null,"이미 사용중인 아이디입니다.");
            return "userreg";
        }
        userService.registerUser(user);
        return "loginform";
    }

    @GetMapping
    public String myPage(){
        return "mypage";
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