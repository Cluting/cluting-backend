package com.cluting.clutingbackend.user.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserApplicatedClubResponseDto {
    private Long clubId;
    private String clubName;
    private String clubProfile;

    private Long recruitId;
    private String currentStage;
    private LocalDate stage1End;
    private LocalDate stage1Start;
    private LocalDate stage2Start;
    private LocalDate stage2End;
    private LocalDate stage3Start;
    private LocalDate stage3End;
    private LocalDate stage4Start;
    private LocalDate stage4End;
    private LocalDate stage5Start;
    private LocalDate stage5End;
    private LocalDate stage6Start;
    private LocalDate stage6End;
    private LocalDate stage7Start;
    private LocalDate stage7End;
    private LocalDate stage8Start;
    private LocalDate stage8End;
}
