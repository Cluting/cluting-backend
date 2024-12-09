package com.cluting.clutingbackend.plan.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "tb_document_criteria")
public class DocumentCriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Lob
    @Column(length = 2000)
    private String content;

    @Column
    private Integer score;


    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

}
