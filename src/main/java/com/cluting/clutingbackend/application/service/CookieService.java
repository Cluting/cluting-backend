package com.cluting.clutingbackend.application.service;

import com.cluting.clutingbackend.application.dto.response.ClubResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CookieService {
    private static final String RECENT_RECRUITS_COOKIE = "recentRecruits"; // 쿠키 이름
    private static final int MAX_RECENT_RECRUITS_SIZE = 5; // 최대 저장할 공고 개수
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 최근 본 공고 저장
    public void saveRecentRecruitToCookie(Long recruitId, String title, HttpServletResponse response, HttpServletRequest request) throws IOException {
        List<ClubResponseDto> recentRecruits = getRecentRecruitsFromCookie(request);

        // 중복 제거 및 최신 공고를 맨 앞에 추가
        recentRecruits.removeIf(recruit -> recruit.getRecruitName().equals(recruitId));
        recentRecruits.add(0, new ClubResponseDto(recruitId, title));

        // 최대 개수 초과 시 오래된 항목 제거
        if (recentRecruits.size() > MAX_RECENT_RECRUITS_SIZE) {
            recentRecruits.remove(recentRecruits.size() - 1);
        }

        // JSON 문자열로 직렬화 및 인코딩
        String recentRecruitsJson = objectMapper.writeValueAsString(recentRecruits);
        String encodedJson = URLEncoder.encode(recentRecruitsJson, StandardCharsets.UTF_8);

        // 쿠키 생성
        Cookie cookie = new Cookie(RECENT_RECRUITS_COOKIE, encodedJson);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 쿠키 유효기간: 1일
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    // 쿠키에서 최근 본 공고 목록 가져오기
    public List<ClubResponseDto> getRecentRecruitsFromCookie(HttpServletRequest request) throws IOException {
        String cookieValue = getCookieValue(request.getCookies(), RECENT_RECRUITS_COOKIE);

        if (cookieValue != null) {
            String decodedValue = URLDecoder.decode(cookieValue, StandardCharsets.UTF_8);
            return objectMapper.readValue(decodedValue, new TypeReference<List<ClubResponseDto>>() {});
        }

        return new ArrayList<>();
    }

    // 특정 쿠키 값 가져오기
    private String getCookieValue(Cookie[] cookies, String cookieName) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
