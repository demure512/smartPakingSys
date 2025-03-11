package com.smartparking.api.service;

import com.smartparking.api.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface NotificationService {
    /**
     * Get notifications for the current authenticated user
     * @param unreadOnly Whether to fetch only unread notifications
     * @param pageable Pagination information
     * @return Page of notifications
     */
    Page<Notification> getUserNotifications(Boolean unreadOnly, Pageable pageable);
    
    /**
     * Get count of unread notifications for the current authenticated user
     * @return Count of unread notifications
     */
    Long getUnreadCount();
    
    /**
     * Mark a notification as read
     * @param id ID of the notification
     */
    void markAsRead(Long id);
    
    /**
     * Mark all notifications of the current user as read
     */
    void markAllAsRead();
    
    /**
     * Delete a specific notification
     * @param id ID of the notification to delete
     */
    void deleteNotification(Long id);
    
    /**
     * Clear all notifications for the current authenticated user
     */
    void clearAllNotifications();
    
    /**
     * Subscribe the current user to specific notification types
     * @param notificationTypes List of notification types to subscribe to
     */
    void subscribeToNotifications(List<String> notificationTypes);
    
    /**
     * Unsubscribe the current user from specific notification types
     * @param notificationTypes List of notification types to unsubscribe from
     */
    void unsubscribeFromNotifications(List<String> notificationTypes);
    
    /**
     * Get notification preferences for the current authenticated user
     * @return Map of notification type to subscription status
     */
    Map<String, Boolean> getNotificationPreferences();
    
    /**
     * Update notification preferences for the current authenticated user
     * @param preferences Map of notification type to subscription status
     */
    void updateNotificationPreferences(Map<String, Boolean> preferences);
    
    /**
     * Send a notification to a specific user or group of users
     * @param notification The notification to send
     */
    void sendNotification(Notification notification);
    
    /**
     * Broadcast a notification to all users with specific roles
     * @param notification The notification to broadcast
     * @param roles Optional list of roles to target (null for all users)
     */
    void broadcastNotification(Notification notification, List<String> roles);
} 