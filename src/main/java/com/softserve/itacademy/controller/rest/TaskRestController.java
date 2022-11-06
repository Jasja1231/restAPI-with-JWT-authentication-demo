package com.softserve.itacademy.controller.rest;

import com.softserve.itacademy.dto.TaskDto;
import com.softserve.itacademy.dto.TaskTransformer;
import com.softserve.itacademy.model.Priority;
import com.softserve.itacademy.model.Task;
import com.softserve.itacademy.service.StateService;
import com.softserve.itacademy.service.TaskService;
import com.softserve.itacademy.service.ToDoService;
import com.softserve.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{u_id}/todos/{t_id}/tasks")
public class TaskRestController {
    @Autowired
 TaskService taskService;
    @Autowired
    ToDoService toDoService;
    @Autowired
    StateService stateService;
    @Autowired
    UserService userService;

@GetMapping
List<TaskDto> getAll(){
    return taskService.getAll().stream()
            .map(TaskDto::new)
            .collect(Collectors.toList());
}
@GetMapping("/{id}")
@ResponseStatus(HttpStatus.OK)
TaskDto getById(@PathVariable long id){return TaskTransformer.convertToDto(taskService.readById(id));}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<?>create(@RequestBody TaskDto taskDto){
        Task task = new Task();
        task.setName(taskDto.getName());
        task.setPriority(Priority.valueOf(taskDto.getPriority()));
        task.setTodo(toDoService.readById(taskDto.getTodoId()));
        task.setState(stateService.readById(taskDto.getStateId()));
        taskService.create(task);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(taskDto.getId())
                .toUri();
        return ResponseEntity.created(location).build();

}
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<Object> put(@PathVariable long id,@RequestBody TaskDto taskDto) {

        Task task = new Task();
        task.setName(taskDto.getName());
        task.setPriority(Priority.valueOf(taskDto.getPriority()));
        task.setTodo(toDoService.readById(taskDto.getTodoId()));
        task.setState(stateService.readById(taskDto.getStateId()));
        taskService.create(task);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .buildAndExpand(taskDto.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable long id) {
        taskService.delete(id);
    }

}

