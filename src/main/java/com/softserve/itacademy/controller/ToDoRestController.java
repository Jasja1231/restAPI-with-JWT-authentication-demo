package com.softserve.itacademy.controller;

import com.softserve.itacademy.security.JwtUser;
import com.softserve.itacademy.transformer.ToDoTransformer;
import com.softserve.itacademy.dto.CollaboratorDto;
import com.softserve.itacademy.dto.ToDoDto;
import com.softserve.itacademy.dto.UserDto;
import com.softserve.itacademy.transformer.UserTransformer;
import com.softserve.itacademy.exception.DuplicateEntityException;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ToDoRestController {

    private final ToDoService toDoService;
    private final UserService userService;
    private final ToDoTransformer toDoTransformer;

    @Autowired
    public ToDoRestController(ToDoService toDoService, UserService userService, ToDoTransformer toDoTransformer) {
        this.toDoService = toDoService;
        this.userService = userService;
        this.toDoTransformer = toDoTransformer;
    }

    @PostMapping("/todos")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody ToDoDto todo,
                       Authentication authentication,
                       SecurityContextHolderAwareRequestWrapper request) {

        todo.setCreatedAt(LocalDateTime.now());
        if (request.isUserInRole("USER"))
            todo.setOwnerId(((JwtUser) authentication.getPrincipal()).getId());
        toDoService.create(toDoTransformer.toModel(todo));
    }

    @GetMapping("/todos/{id}")
    @PreAuthorize("hasRole('ADMIN') or @toDoServiceImpl.canAccessToDo(#id)")
    public ToDoDto getById(@PathVariable("id") long id) {
        return toDoTransformer.toDto(toDoService.readById(id));
    }

    @PutMapping("/todos/{id}")
    @PreAuthorize("hasRole('ADMIN') or @toDoServiceImpl.isToDoOwner(#id)")
    public void update(@PathVariable("id") long id,
                       @RequestBody ToDoDto todo,
                       Authentication authentication,
                       SecurityContextHolderAwareRequestWrapper request) {

        todo.setId(id);
        if (request.isUserInRole("USER"))
            todo.setOwnerId(((JwtUser) authentication.getPrincipal()).getId());
        toDoService.update(toDoTransformer.toModel(todo));
    }

    @DeleteMapping("/todos/{id}")
    @PreAuthorize("hasRole('ADMIN') or @toDoServiceImpl.isToDoOwner(#id)")
    public void delete(@PathVariable("id") long id) {
        toDoService.delete(id);
    }

    @GetMapping("/todos")
    public List<ToDoDto> getMy(Authentication authentication) {
        return toDoService.getByUserId(((JwtUser) authentication.getPrincipal()).getId())
            .stream()
            .map(toDoTransformer::toDto)
            .collect(Collectors.toList());
    }

    @GetMapping("/todos/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ToDoDto> getAll() {
        return toDoService.getAll()
            .stream()
            .map(toDoTransformer::toDto)
            .collect(Collectors.toList());
    }

    @GetMapping("/users/{user_id}/todos")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ToDoDto> getByUserId(@PathVariable("user_id") long userId) {
        return toDoService.getByUserId(userId)
            .stream()
            .map(toDoTransformer::toDto)
            .collect(Collectors.toList());
    }

    @GetMapping("/todos/{todo_id}/collaborators")
    @PreAuthorize("hasRole('ADMIN') or @toDoServiceImpl.canAccessToDo(#todoId)")
    public List<UserDto> getCollaborators(@PathVariable("todo_id") long todoId) {
        return toDoService.readById(todoId)
            .getCollaborators()
            .stream()
            .map(UserTransformer::toUserDto)
            .collect(Collectors.toList());
    }

    @PostMapping("/todos/{todo_id}/collaborators")
    @PreAuthorize("hasRole('ADMIN') or @toDoServiceImpl.isToDoOwner(#todoId)")
    @ResponseStatus(HttpStatus.CREATED)
    public void addCollaborator(@PathVariable("todo_id") long todoId, @RequestBody CollaboratorDto collaboratorDto) {
        ToDo todo = toDoService.readById(todoId);
        if (todo.getCollaborators().stream().anyMatch(x -> x.getId() == collaboratorDto.getCollaboratorId()))
            throw new DuplicateEntityException();
        todo.getCollaborators().add(userService.readById(collaboratorDto.getCollaboratorId()));
        toDoService.update(todo);
    }

    @DeleteMapping("/todos/{todo_id}/collaborators/{user_id}")
    @PreAuthorize("hasRole('ADMIN') or @toDoServiceImpl.isToDoOwner(#todoId)")
    public void removeCollaborator(@PathVariable("todo_id") long todoId, @PathVariable("user_id") long userId) {
        ToDo todo = toDoService.readById(todoId);
        todo.getCollaborators().remove(todo.getCollaborators()
            .stream()
            .filter(x -> x.getId() == userId)
            .findFirst()
            .orElseThrow(EntityNotFoundException::new));
        toDoService.update(todo);
    }
}
