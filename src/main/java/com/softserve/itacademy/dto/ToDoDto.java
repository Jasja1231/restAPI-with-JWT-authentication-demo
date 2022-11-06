package com.softserve.itacademy.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ToDoDto {

    private long id;
    private String title;
    private LocalDateTime createdAt;
    private long ownerId;
}
