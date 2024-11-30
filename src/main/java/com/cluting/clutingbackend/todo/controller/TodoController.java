package com.cluting.clutingbackend.todo.controller;

import com.cluting.clutingbackend.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;
}
