package com.cluting.clutingbackend.util.security;

import com.cluting.clutingbackend.util.redis.RedisUtil;
import com.cluting.clutingbackend.util.StaticValue;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    private final CustomUserDetailsService userDetailsService;
    private final RedisUtil redisUtil;

    @PostConstruct
    protected void init() {
        this.secretKey = Base64.getEncoder().encodeToString(this.secretKey.getBytes());
    }

    public String createAccessToken(String email) {
        Date now = new Date();
        Claims claims = Jwts.claims().setSubject(email);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + StaticValue.JWT_ACCESS_TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, this.secretKey)
                .compact();
    }

    public String createRefreshToken() {
        Date now = new Date();
        return Jwts.builder()
                .setExpiration(new Date(now.getTime() + StaticValue.JWT_REFRESH_TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, this.secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        String email = getUserEmail(token);
        UserDetails userDetails = userDetailsService.loadUserByUserId(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public String getUserEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }


    public String resolveToken(HttpServletRequest request) {
        String bearerToken =  request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    //ACCESS TOKEN만 Validate합니다.
    public boolean validateToken(String jwtToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(this.secretKey)
                    .build()
                    .parseClaimsJws(jwtToken);
            return true;

        } catch (ExpiredJwtException e) {
            throw new CredentialsExpiredException("Expired JWT token", e);
        } catch (JwtException | IllegalArgumentException e) {
            throw new BadCredentialsException("Invalid JWT token", e);
        }
    }
}
