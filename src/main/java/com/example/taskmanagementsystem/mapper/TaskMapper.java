package com.example.taskmanagementsystem.mapper;

import com.example.taskmanagementsystem.dto.task.TaskResponseDto;
import com.example.taskmanagementsystem.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "authorName", expression = "java(task.getAuthor().getFirstname() + \" \" + task.getAuthor().getLastname())")
    @Mapping(target = "executorName", expression = "java(task.getExecutor() != null ? task.getExecutor().getFirstname() + \" \" + task.getExecutor().getLastname() : null)")
    TaskResponseDto toTaskResponseDto(Task task);
}
