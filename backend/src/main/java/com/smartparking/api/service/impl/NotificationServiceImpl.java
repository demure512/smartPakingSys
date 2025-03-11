package com.smartparking.api.service.impl;

import com.smartparking.api.model.Notification;
import com.smartparking.api.model.User;
import com.smartparking.api.repository.NotificationRepository;
import com.smartparking.api.repository.UserNotificationPreferenceRepository;
import com.smartparking.api.repository.UserRepository;
import com.smartparking.api.service.NotificationService;
import com.smartparking.api.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final UserNotificationPreferenceRepository preferenceRepository;

    @Autowired
    public NotificationServiceImpl(
            NotificationRepository notificationRepository,
            UserRepository userRepository,
            UserNotificationPreferenceRepository preferenceRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.preferenceRepository = preferenceRepository;
    }

    @Override
    public Page<Notification> getUserNotifications(Boolean unreadOnly, Pageable pageable) {
        Long userId = SecurityUtils.getCurrentUserId();
        
        if (unreadOnly != null && unreadOnly) {
            return notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId, pageable);
        } else {
            return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        }
    }

    @Override
    public Long getUnreadCount() {
        Long userId = SecurityUtils.getCurrentUserId();
        return notificationRepository.countByUserIdAndReadFalse(userId);
    }

    @Override
    @Transactional
    public void markAsRead(Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        
        notificationRepository.findByIdAndUserId(id, userId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    @Override
    @Transactional
    public void markAllAsRead() {
        Long userId = SecurityUtils.getCurrentUserId();
        notificationRepository.markAllAsReadForUser(userId);
    }

    @Override
    @Transactional
    public void deleteNotification(Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        notificationRepository.deleteByIdAndUserId(id, userId);
    }

    @Override
    @Transactional
    public void clearAllNotifications() {
        Long userId = SecurityUtils.getCurrentUserId();
        notificationRepository.deleteAllByUserId(userId);
    }

    @Override
    @Transactional
    public void subscribeToNotifications(List<String> notificationTypes) {
        Long userId = SecurityUtils.getCurrentUserId();
        
        notificationTypes.forEach(type -> 
            preferenceRepository.updatePreference(userId, type, true)
        );
    }

    @Override
    @Transactional
    public void unsubscribeFromNotifications(List<String> notificationTypes) {
        Long userId = SecurityUtils.getCurrentUserId();
        
        notificationTypes.forEach(type -> 
            preferenceRepository.updatePreference(userId, type, false)
        );
    }

    @Override
    public Map<String, Boolean> getNotificationPreferences() {
        Long userId = SecurityUtils.getCurrentUserId();
        return preferenceRepository.findByUserId(userId).stream()
                .collect(Collectors.toMap(
                        pref -> pref.getNotificationType(),
                        pref -> pref.isEnabled()
                ));
    }

    @Override
    @Transactional
    public void updateNotificationPreferences(Map<String, Boolean> preferences) {
        Long userId = SecurityUtils.getCurrentUserId();
        
        preferences.forEach((type, enabled) -> 
            preferenceRepository.updatePreference(userId, type, enabled)
        );
    }

    @Override
    @Transactional
    public void sendNotification(Notification notification) {
        if (notification.getUser() == null || notification.getUser().getId() == null) {
            throw new IllegalArgumentException("Notification must have a target user");
        }
        
        Long userId = notification.getUser().getId();
        boolean isSubscribed = preferenceRepository.isUserSubscribedToType(userId, notification.getType());
        
        if (isSubscribed) {
            notification.setCreatedAt(LocalDateTime.now());
            notification.setRead(false);
            notificationRepository.save(notification);
        }
    }

    @Override
    @Transactional
    public void broadcastNotification(Notification notification, List<String> roles) {
        List<User> users;
        
        if (roles != null && !roles.isEmpty()) {
            users = userRepository.findByRoles(roles);
        } else {
            users = userRepository.findAll();
        }
        
        users.forEach(user -> {
            boolean isSubscribed = preferenceRepository.isUserSubscribedToType(user.getId(), notification.getType());
            if (isSubscribed) {
                Notification userNotification = new Notification();
                userNotification.setUser(user);
                userNotification.setTitle(notification.getTitle());
                userNotification.setMessage(notification.getMessage());
                userNotification.setType(notification.getType());
                userNotification.setCreatedAt(LocalDateTime.now());
                userNotification.setRead(false);
                notificationRepository.save(userNotification);
            }
        });
    }
} 