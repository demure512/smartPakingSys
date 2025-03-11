package com.smartparking.api.repository;

import com.smartparking.api.model.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SystemLogRepository extends JpaRepository<SystemLog, Integer> {
    List<SystemLog> findByUserId(Integer userId);
    
    List<SystemLog> findByActionType(String actionType);
    
    List<SystemLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    List<SystemLog> findByUserIdAndTimestampBetween(Integer userId, LocalDateTime start, LocalDateTime end);
} 