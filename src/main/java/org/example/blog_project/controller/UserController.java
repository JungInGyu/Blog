package org.example.blog_project.controller;

import io.jsonwebtoken.Claims;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String login(@ModelAttribute @Valid UserLoginDto userLoginDto, BindingResult bindingResult, HttpServletResponse response, RedirectAttributes redirectAttributes) throws IOException {
        if (bindingResult.hasErrors()){
            return "loginform"; // 로그인 폼으로 다시 이동
        }

        User user = userService.findByUid(userLoginDto.getUsername());

        if (user == null || !passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다");
            return "redirect:/loginform"; // 로그인 페이지로 리다이렉트
        }

        List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

        String accessToken = jwtTokenizer.createAccessToken(user.getUser_id(),user.getEmail(),user.getName(),user.getUid(),roles);
        String refreshToken = jwtTokenizer.createRefreshToken(user.getUser_id(), user.getEmail(), user.getName(), user.getUid(), roles);

        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setValue(refreshToken);
        refreshTokenEntity.setUserId(user.getUser_id());

        refreshTokenService.addRefreshToken(refreshTokenEntity);


        Cookie accessTokenCookie = new Cookie("refreshToken", refreshToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.ACCESS_TOKEN_EXPIRE_COUNT/1000));

        Cookie refreshTokenCookie = new Cookie("accessToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.REFRESH_TOKEN_EXPIRE_COUNT/1000));

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        // 로그인 성공 후 메인 페이지나 대시보드로 리다이렉트
        return "redirect:/blog";
    }

    @PostMapping("/refreshToken")
    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response){
        String refreshToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies){
                if ("refreshToken".equals(cookie.getName())){
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        if (refreshToken == null){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Claims claims = jwtTokenizer.parseRefreshToken(refreshToken);
        Long userId = Long.valueOf ((Integer)claims.get("userId"));

        User user = userService.getUser(userId).orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못했습니다."));

        List roles = (List)claims.get("roles");

        String accessToken = jwtTokenizer.createAccessToken(userId, user.getEmail(), user.getName(), user.getUid(), roles);

        Cookie accessTokenCookie = new Cookie("accessToken",accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.ACCESS_TOKEN_EXPIRE_COUNT / 1000));

        response.addCookie(accessTokenCookie);

        UserLoginResponseDto responseDto = UserLoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .name(user.getName())
                .userId(user.getUser_id())
                .build();


        return new ResponseEntity(responseDto, HttpStatus.OK);
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

    @GetMapping("/mypage")
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