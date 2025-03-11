package com.smartparking.api.repository;

import com.smartparking.api.model.UserNotificationPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNotificationPreferenceRepository extends JpaRepository<UserNotificationPreference, Long> {
    
    List<UserNotificationPreference> findByUserId(Long userId);
    
    @Query("SELECT p.enabled FROM UserNotificationPreference p WHERE p.user.id = :userId AND p.notificationType = :type")
    boolean isUserSubscribedToType(Long userId, String type);
    
    @Modifying
    @Query("UPDATE UserNotificationPreference p SET p.enabled = :enabled WHERE p.user.id = :userId AND p.notificationType = :type")
    void updatePreference(Long userId, String type, boolean enabled);
} 