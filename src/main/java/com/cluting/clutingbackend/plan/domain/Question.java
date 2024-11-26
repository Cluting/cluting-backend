package com.cluting.clutingbackend.plan.domain;


import com.cluting.clutingbackend.part.Part;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "question")
    private List<Answer> answers;

    @OneToMany(mappedBy = "question")
    private List<Option> options; // ClubUser가 가진 TimeSlot 리스트

    @ManyToOne
    @JoinColumn(name = "part_id", nullable = true)
    private Part part; // 공통 질문인 경우 NULL, 파트별 질문인 경우 특정 Part와 연결

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category; // 질문의 범주 (공통 질문인지, 파트별 질문인지)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type; // 질문 타입 (객관식, 주관식)

    @Column(length = 255, nullable = false)
    private String content; // 질문 내용

    @Column(nullable = false)
    private Boolean docOrInterview; // TRUE: 문서 질문, FALSE: 면접 질문

    // 추가: 선택지 리스트
//    @ElementCollection
//    @CollectionTable(name = "question_choices", joinColumns = @JoinColumn(name = "question_id"))
//    @Column(name = "choice")
//    private List<String> choices;

    @Column
    private Boolean multipleChoice;


    public enum Type {
        OBJECT, SUBJECT
    }

    public enum Category {
        COMMON, PART_SPECIFIC
    }
}
