package com.example.phase2.service.impl;

import com.example.phase2.entity.Comment;
import com.example.phase2.repository.CommentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.mockito.Mockito.*;
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        commentService = new CommentServiceImpl(commentRepository);
    }

    @Test
    public void testSaveOrUpdate() {
        Comment comment = new Comment();
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment result = commentService.saveOrUpdate(comment);

        Assertions.assertEquals(comment, result);
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    public void testFindById() {
        Long commentId = 1L;
        Comment comment = new Comment();
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        Optional<Comment> result = commentService.findById(commentId);

        Assertions.assertEquals(Optional.of(comment), result);
        verify(commentRepository, times(1)).findById(commentId);
    }

    @Test
    public void testFindAll() {
        List<Comment> comments = new ArrayList<>();
        when(commentRepository.findAll()).thenReturn(comments);

        List<Comment> result = commentService.findAll();

        Assertions.assertEquals(comments, result);
        verify(commentRepository, times(1)).findAll();
    }

    @Test
    public void testSaveAll() {
        List<Comment> comments = new ArrayList<>();
        when(commentRepository.saveAll(anyList())).thenReturn(comments);

        List<Comment> result = commentService.saveAll(comments);

        Assertions.assertEquals(comments, result);
        verify(commentRepository, times(1)).saveAll(comments);
    }

    @Test
    public void testDeleteById() {
        Long commentId = 1L;

        commentService.deleteById(commentId);

        verify(commentRepository, times(1)).deleteById(commentId);
    }

}