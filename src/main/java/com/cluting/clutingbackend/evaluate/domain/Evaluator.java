package com.cluting.clutingbackend.evaluate.domain;

import com.cluting.clutingbackend.part.Part;
import com.cluting.clutingbackend.plan.domain.ClubUser;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Evaluator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long evaluatorId;

    private String partName; // 파트 이름

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluator_id")
    private Part part;

    @OneToMany
    private List<ClubUser> evaluators;

    public void addStaff(ClubUser clubUser) {
        evaluators.add(clubUser);
    }

    public void removeStaff(ClubUser clubUser) {
        evaluators.remove(clubUser);
    }
}
