package com.example.phase2.service;

import com.example.phase2.entity.Comment;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface CommentService {
    Comment saveOrUpdate(Comment comment);
    Comment update(Comment comment);
    Optional<Comment> findById(Long id);
    List<Comment> findAll();
    List<Comment> saveAll(List<Comment> comments);
    void deleteById(Long id);
}
