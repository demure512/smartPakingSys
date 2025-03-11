package com.smartparking.api.service;

import com.smartparking.api.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    
    Optional<User> getUserById(Integer id);
    
    Optional<User> getUserByUsername(String username);
    
    User createUser(User user);
    
    User updateUser(User user);
    
    void deleteUser(Integer id);
    
    boolean changePassword(Integer userId, String oldPassword, String newPassword);
    
    boolean existsByUsername(String username);
} 