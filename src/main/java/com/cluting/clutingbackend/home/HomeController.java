package com.cluting.clutingbackend.home;

import com.cluting.clutingbackend.plan.dto.response.ClubResponseDto;
import com.cluting.clutingbackend.util.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/api/v1/home")
@RequiredArgsConstructor
public class HomeController {
    private final HomeService homeService;

    @Operation(description = "로그인 된 사용자가 가입한 동아리 목록 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "동아리 목록 조회 성공"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/clubs")
    public List<ClubResponseDto> findClubs(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return homeService.userClubs(userDetails.getUser());
    }

    @Operation(description = "동아리 id로 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "동아리 조회 성공"),
            @ApiResponse(responseCode = "404", description = "Club not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/club/{clubId}")
    public ClubResponseDto findClub(@PathVariable("clubId") Long clubId) {
        return homeService.findClub(clubId);
    }
}
