package org.example.blog_project.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class JwtTokenizer {

    @Value("${jwt.refresh.secret}")
    private String refreshSecretString;

    private byte[] refreshSecret;

    public static final Long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L; // 7일

    @PostConstruct
    public void init() {
        this.refreshSecret = refreshSecretString.getBytes();
    }

    private String createToken(Long id, String email, String name, String uid, List<String> roles, Long expire, byte[] secretKey){
        Claims claims = Jwts.claims().setSubject(email);

        //필요한 정보들을 저장한다.
        claims.put("uid",uid);
        claims.put("name",name);
        claims.put("userId",id);
        claims.put("roles",roles);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+expire))
                .signWith(getSigningKey(secretKey))
                .compact();
    }

    //REFRESH TOKEN 생성
    public String createRefreshToken(Long id, String email, String name, String username, List<String> roles){
        return createToken(id,email,name,username,roles,REFRESH_TOKEN_EXPIRE_TIME,refreshSecret);
    }
    public static Key getSigningKey(byte[] secretKey){
        return Keys.hmacShaKeyFor(secretKey);
    }

    public Long getUserIdFromToken(String token){
        String[] tokenArr = token.split(" ");
        token = tokenArr[1];
        Claims claims = parseToken(token,refreshSecret);
        return Long.valueOf((Integer)claims.get("userId"));
    }

    public Claims parseToken(String token, byte[] secretKey){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims parseRefreshToken(String refreshToken) {
        return parseToken(refreshToken, refreshSecret);
    }
}

