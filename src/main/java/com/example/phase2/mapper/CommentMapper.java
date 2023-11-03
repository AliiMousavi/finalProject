package com.example.phase2.mapper;

import com.example.phase2.dto.CommentRequestDto;
import com.example.phase2.dto.CommentResponseDto;
import com.example.phase2.dto.CustomerRequestDto;
import com.example.phase2.dto.CustomerResponseDto;
import com.example.phase2.entity.Comment;
import com.example.phase2.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    Comment dtoToComment(CommentRequestDto commentRequestDto);

    CommentResponseDto commentToDto(Comment comment);
}
