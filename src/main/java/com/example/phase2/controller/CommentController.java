package com.example.phase2.controller;

import com.example.phase2.dto.CommentRequestDto;
import com.example.phase2.dto.CommentResponseDto;
import com.example.phase2.entity.Comment;
import com.example.phase2.mapper.CommentMapper;
import com.example.phase2.service.impl.CommentServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentServiceImpl commentService;

    @PostMapping("/save")
    public ResponseEntity<CommentResponseDto> save(@RequestBody @Valid CommentRequestDto commentRequestDto){
        Comment comment = CommentMapper.INSTANCE.dtoToComment(commentRequestDto);
        Comment savedComment = commentService.saveOrUpdate(comment);
        CommentResponseDto commentResponseDto = CommentMapper.INSTANCE.commentToDto(savedComment);
        return new ResponseEntity<>(commentResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<CommentResponseDto> findById(@PathVariable Long id){
        Comment comment = commentService.findById(id).orElseThrow();
        CommentResponseDto commentResponseDto = CommentMapper.INSTANCE.commentToDto(comment);
        return new ResponseEntity<>(commentResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<CommentResponseDto>> findAll() {
        List<Comment> comments = commentService.findAll();
        List<CommentResponseDto> commentResponseDtos = comments.stream()
                .map(CommentMapper.INSTANCE::commentToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(commentResponseDtos);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long id,
                                                                  @RequestBody @Valid CommentRequestDto commentRequestDto) {
        Comment comment = commentService.findById(id).orElseThrow();
        Comment updatedComment = CommentMapper.INSTANCE.dtoToComment(commentRequestDto);
        updatedComment.setId(comment.getId());
        Comment savedComment = commentService.saveOrUpdate(updatedComment);
        CommentResponseDto commentResponseDto =CommentMapper.INSTANCE.commentToDto(savedComment);
        return ResponseEntity.ok(commentResponseDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Long> deleteComment(@PathVariable Long id) {
        commentService.deleteById(id);
        return ResponseEntity.ok(id);
    }

}
