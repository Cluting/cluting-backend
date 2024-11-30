package com.cluting.clutingbackend.domain.todo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
}
