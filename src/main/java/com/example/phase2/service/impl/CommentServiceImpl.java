package com.example.phase2.service.impl;

import com.example.phase2.entity.Comment;
import com.example.phase2.repository.CommentRepository;
import com.example.phase2.service.CommentService;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public Comment saveOrUpdate(Comment comment){
        try{
            return commentRepository.save(comment);
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Comment update(Comment comment) {
        try{
            return commentRepository.save(comment);
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    @Transactional
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    @Transactional
    public List<Comment> saveAll(List<Comment> comments) {
        return commentRepository.saveAll(comments);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
