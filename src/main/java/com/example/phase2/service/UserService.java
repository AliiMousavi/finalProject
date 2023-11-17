package com.example.phase2.service;

import com.example.phase2.dto.UserFilterCriteria;
import com.example.phase2.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
//    List<User> searchUsers(UserSearchCriteria searchCriteria);

    Page<User> filterUsers(UserFilterCriteria criteria, Pageable pageable);
}
