package com.example.phase2.service.impl;


import com.example.phase2.entity.Customer;
import com.example.phase2.entity.Expert;
import com.example.phase2.repository.ExpertRepository;
import com.example.phase2.service.ExpertService;
import com.example.phase2.validator.PasswordValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ExpertServiceImpl implements ExpertService {

    private final ExpertRepository expertRepository;

    public ExpertServiceImpl(ExpertRepository expertRepository) {
        this.expertRepository = expertRepository;
    }

    @Override
    public Expert saveOrUpdate(Expert expert) {
        return expertRepository.save(expert);
    }

    @Override
    public Optional<Expert> findById(Long id) {
        return expertRepository.findById(id);
    }

    @Override
    public List<Expert> findAll() {
        return expertRepository.findAll();
    }

    @Override
    public List<Expert> saveAll(List<Expert> experts) {
        return expertRepository.saveAll(experts);
    }

    @Override
    public void deleteById(Long id) {
        expertRepository.deleteById(id);
    }

    @Override
    public Expert ChangePasswordByID(String password, Long id) {
        Expert expert = expertRepository.findById(id).orElseThrow();
        if(PasswordValidator.isValidPassword(password))
            expert.setPassword(password);
        else
            throw new RuntimeException("is not valid password!");
        return expertRepository.save(expert);
    }
}
