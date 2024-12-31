package com.cluting.clutingbackend.global.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CurrentStage {
    PREP("계획 세우기"),
    PLAN("모집 준비하기"),
    DOC("서류 평가하기"),
    DOC_PASS("서류합격자 및 면접안내"),
    EVAL("면접 평가하기"),
    FINAL_PASS("최종합격자 및 활동안내");
    private final String description;
    private StageState currentState; // 현재 상태를 저장하는 필드
    // 초기 상태 설정 (진행 전)
    static {
        for (CurrentStage stage : CurrentStage.values()) {
            stage.currentState = StageState.BEFORE;
        }
    }
    // 상태 변경 메서드
    public void updateState(StageState newState) {
        this.currentState = newState;
    }
    @Getter
    @RequiredArgsConstructor
    public enum StageState {
        BEFORE("진행 전"),
        IN_PROGRESS("진행 중"),
        AFTER("진행 후");

        private final String stateDescription;
    }
}
