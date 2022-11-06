package com.softserve.itacademy.controller.rest;

import com.softserve.itacademy.convertor.ToDoConvertor;
import com.softserve.itacademy.dto.ToDoDto;
import com.softserve.itacademy.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ToDoRestController {

    private final ToDoService toDoService;
    private final ToDoConvertor toDoConvertor;

    @Autowired
    public ToDoRestController(ToDoService toDoService, ToDoConvertor toDoConvertor) {
        this.toDoService = toDoService;
        this.toDoConvertor = toDoConvertor;
    }

    @PostMapping("/todos")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody ToDoDto todo) {
        todo.setCreatedAt(LocalDateTime.now());
        toDoService.create(toDoConvertor.toModel(todo));
    }

    @GetMapping("/todos/{id}")
    public ToDoDto getById(@PathVariable("id") long id) {
        return toDoConvertor.toDto(toDoService.readById(id));
    }

    @PostMapping("/todos/{id}")
    public void update(@PathVariable("id") long id, @RequestBody ToDoDto todo) {
        todo.setId(id);
        toDoService.update(toDoConvertor.toModel(todo));
    }

    @DeleteMapping("/todos/{id}")
    public void delete(@PathVariable("id") long id) {
        toDoService.delete(id);
    }

    @GetMapping("/todos")
    public List<ToDoDto> getAll() {
        return toDoService.getAll()
            .stream()
            .map(toDoConvertor::toDto)
            .collect(Collectors.toList());
    }

    @GetMapping("/users/{user_id}/todos")
    public List<ToDoDto> getByUserId(@PathVariable("user_id") long userId) {
        return toDoService.getByUserId(userId)
            .stream()
            .map(toDoConvertor::toDto)
            .collect(Collectors.toList());
    }
}
