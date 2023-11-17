package com.example.phase2.service.impl;

import com.example.phase2.dto.UserFilterCriteria;
import com.example.phase2.entity.User;
import com.example.phase2.repository.UserRepository;
import com.example.phase2.repository.UserSpecifications;
import com.example.phase2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Page<User> filterUsers(UserFilterCriteria criteria, Pageable pageable) {
        Specification<User> specification = Specification.where(null);

        if (criteria.getRole() != null) {
            specification = specification.and(UserSpecifications.withRole(criteria.getRole()));
        }

        if (criteria.getFirstName() != null) {
            specification = specification.and(UserSpecifications.withFirstName(criteria.getFirstName()));
        }

        if (criteria.getLastName() != null) {
            specification = specification.and(UserSpecifications.withLastName(criteria.getLastName()));
        }

        if (criteria.getEmail() != null) {
            specification = specification.and(UserSpecifications.withEmail(criteria.getEmail()));
        }

        if (criteria.getStartDate() != null && criteria.getEndDate() != null) {
            specification = specification.and(UserSpecifications.byDateOfRegistration(criteria.getStartDate(), criteria.getEndDate()));
        }

        return userRepository.findAll(specification, pageable);
    }


//    @Override
//    public List<User> searchUsers(UserSearchCriteria searchCriteria) {
//        Specification<User> specification = UserSpecifications.withSearchCriteria(searchCriteria);
//        return userRepository.findAll(specification);
//    }
}
