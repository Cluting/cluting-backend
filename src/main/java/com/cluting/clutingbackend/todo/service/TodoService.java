package com.cluting.clutingbackend.todo.service;

import com.cluting.clutingbackend.todo.domain.Todo;
import com.cluting.clutingbackend.todo.dto.TodoRequest;
import com.cluting.clutingbackend.todo.dto.TodoResponse;
import com.cluting.clutingbackend.todo.repository.TodoRepository;
import com.cluting.clutingbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    // [리크루팅 홈] 투두 작성하기
    public TodoResponse createTodo(Long clubUserId, TodoRequest request) {
        var user = userRepository.findById(clubUserId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 인증 토큰"));

        Todo todo = Todo.builder()
                .user(user)
                .content(request.getContent())
                .status(false)
                .build();

        return TodoResponse.fromEntity(todoRepository.save(todo));
    }

    // [리크루팅 홈] 투두 삭제하기
    public void deleteTodo(Long clubUserId, Long todoId) {
        Todo todo = todoRepository.findByIdAndUserId(todoId, clubUserId)
                .orElseThrow(() -> new IllegalArgumentException("해당 투두 항목을 찾을 수 없음"));
        todoRepository.delete(todo);
    }

    // [리크루팅 홈] 투두 완료 상태 바꾸기
    public void toggleTodoStatus(Long clubUserId, Long todoId) {
        Todo todo = todoRepository.findByIdAndUserId(todoId, clubUserId)
                .orElseThrow(() -> new IllegalArgumentException("해당 투두 항목을 찾을 수 없음"));
        todo.setStatus(!todo.getStatus());
        todoRepository.save(todo);
    }

    // [리크루팅 홈] 투두 내용 변경하기
    public void updateTodoContent(Long clubUserId, Long todoId, String updatedContent) {
        Todo todo = todoRepository.findByIdAndUserId(todoId, clubUserId)
                .orElseThrow(() -> new IllegalArgumentException("해당 투두 항목을 찾을 수 없음"));
        todo.setContent(updatedContent);
        todoRepository.save(todo);
    }

    // [리크루팅 홈] 투두 리스트 완료/미완료 분리해서 가져오기
    public Map<String, List<TodoResponse>> getTodosByStatus(Long clubUserId) {
        // 미완료 리스트
        List<TodoResponse> incompleteTodos = todoRepository.findByUserIdAndStatus(clubUserId, false)
                .stream()
                .map(TodoResponse::fromEntity)
                .collect(Collectors.toList());

        // 완료 리스트
        List<TodoResponse> completeTodos = todoRepository.findByUserIdAndStatus(clubUserId, true)
                .stream()
                .map(TodoResponse::fromEntity)
                .collect(Collectors.toList());

        // 결과를 Map으로 반환
        Map<String, List<TodoResponse>> todos = new HashMap<>();
        todos.put("미완료 리스트", incompleteTodos);
        todos.put("완료 리스트", completeTodos);

        return todos;
    }
}
