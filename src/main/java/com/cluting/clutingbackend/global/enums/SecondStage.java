package com.cluting.clutingbackend.global.enums;

import com.cluting.clutingbackend.global.exception.CustomException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.cluting.clutingbackend.global.exception.ErrorCode.ALREADY_COMPLETED;

@Getter
@RequiredArgsConstructor
public enum SecondStage {
    STAGE1("합격 인원 설정하기"),
    STAGE2("인재상 구축하기"),
    STAGE3("공고 작성하기"),
    STAGE4("운영진 면접 일정 조정하기"),
    STAGE5("지원서 폼 제작 및 공고 올리기");

    private final String description;
    private CompleteState completeState; // 현재 상태를 저장하는 필드
    // 초기 상태 설정 (진행 전)
    static {
        for (SecondStage stage : SecondStage.values()) {
            stage.completeState = CompleteState.NOT_COMPLETED;
        }
    }
    // 상태 변경 메서드
    public void completeStage() {
        if (this.completeState == CompleteState.COMPLETED) {
            throw new CustomException(ALREADY_COMPLETED," This stage in the Second Stage is already completed.");
        }
        this.completeState = CompleteState.COMPLETED;
    }
    @Getter
    @RequiredArgsConstructor
    public static enum CompleteState {
        NOT_COMPLETED("미완료"),
        COMPLETED("완료");

        private final String stateDescription;
    }
}

