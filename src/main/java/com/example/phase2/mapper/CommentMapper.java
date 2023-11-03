package com.example.phase2.mapper;

import com.example.phase2.dto.*;
import com.example.phase2.entity.Comment;
import com.example.phase2.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    Comment dtoToComment(CommentRequestDto commentRequestDto);
    Comment justScoredtoToComment(CommentRequestDtoWithoutComment commentRequestDtoWithoutComment);

    CommentResponseDto commentToDto(Comment comment);
}
