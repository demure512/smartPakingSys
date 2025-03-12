package com.smartparking.api.service.impl;

import com.smartparking.api.model.User;
import com.smartparking.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class ServiceUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public void CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findByUsername(username);
        
        User user = userOpt.orElseThrow(() -> 
            new UsernameNotFoundException("User not found with username: " + username)
        );
        
        // Create authorities based on role
        String role = user.getRole() != null ? user.getRole().getName() : "USER";
        
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }
} 