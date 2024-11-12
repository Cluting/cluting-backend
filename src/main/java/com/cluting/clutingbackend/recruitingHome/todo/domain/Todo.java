package com.cluting.clutingbackend.recruitingHome.todo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long todoId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(length = 255, nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean status; //0:미완료, 1:완료

}