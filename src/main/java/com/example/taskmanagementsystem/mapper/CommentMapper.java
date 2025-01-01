package com.example.taskmanagementsystem.mapper;

import com.example.taskmanagementsystem.dto.comment.CommentResponseDto;
import com.example.taskmanagementsystem.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "task.id", target = "taskId")
    @Mapping(source = "user.id", target = "userId")
    CommentResponseDto toCommentResponseDto(Comment comment);
}
