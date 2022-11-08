package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.exception.NullEntityReferenceException;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.repository.ToDoRepository;
import com.softserve.itacademy.security.JwtUser;
import com.softserve.itacademy.service.ToDoService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ToDoServiceImpl implements ToDoService {

    private ToDoRepository todoRepository;

    public ToDoServiceImpl(ToDoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public ToDo create(ToDo role) {
        if (role != null) {
            return todoRepository.save(role);
        }
        throw new NullEntityReferenceException("ToDo cannot be 'null'");
    }

    @Override
    public ToDo readById(long id) {
        return todoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("ToDo with id " + id + " not found"));
    }

    @Override
    public ToDo update(ToDo role) {
        if (role != null) {
            readById(role.getId());
            return todoRepository.save(role);
        }
        throw new NullEntityReferenceException("ToDo cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        todoRepository.delete(readById(id));
    }

    @Override
    public List<ToDo> getAll() {
        List<ToDo> todos = todoRepository.findAll();
        return todos.isEmpty() ? new ArrayList<>() : todos;
    }

    @Override
    public List<ToDo> getByUserId(long userId) {
        List<ToDo> todos = todoRepository.getByUserId(userId);
        return todos.isEmpty() ? new ArrayList<>() : todos;
    }

    public boolean canAccessToDo(long todoId) {
        long userId = ((JwtUser) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal())
                .getId();
        ToDo todo = readById(todoId);

        if (todo.getOwner().getId() == userId)
            return true;
        return todo.getCollaborators()
            .stream()
            .anyMatch(x -> x.getId() == userId);
    }

    public boolean isToDoOwner(long todoId) {
        long userId = ((JwtUser) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal())
                .getId();

       return readById(todoId).getOwner().getId() == userId;
    }
}
