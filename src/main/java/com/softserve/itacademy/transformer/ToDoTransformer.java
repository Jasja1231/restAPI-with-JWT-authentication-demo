package com.softserve.itacademy.transformer;

import com.softserve.itacademy.dto.ToDoDto;
import com.softserve.itacademy.model.ToDo;
import com.softserve.itacademy.model.User;
import org.springframework.stereotype.Component;

@Component
public class ToDoTransformer {

    public ToDoDto toDto(ToDo model) {
        ToDoDto dto = new ToDoDto();
        dto.setId(model.getId());
        dto.setTitle(model.getTitle());
        dto.setCreatedAt(model.getCreatedAt());
        dto.setOwnerId(model.getOwner().getId());

        return dto;
    }

    public ToDo toModel(ToDoDto dto) {
        User owner = new User();
        owner.setId(dto.getOwnerId());

        ToDo model = new ToDo();
        model.setId(dto.getId());
        model.setTitle(dto.getTitle());
        model.setCreatedAt(dto.getCreatedAt());
        model.setOwner(owner);

        return model;
    }
}
