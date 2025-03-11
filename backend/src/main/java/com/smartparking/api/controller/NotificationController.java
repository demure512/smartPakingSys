package com.smartparking.api.controller;

import com.smartparking.api.model.Notification;
import com.smartparking.api.payload.ApiResponse;
import com.smartparking.api.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public Page<Notification> getUserNotifications(
            @RequestParam(required = false) Boolean unreadOnly,
            Pageable pageable) {
        return notificationService.getUserNotifications(unreadOnly, pageable);
    }

    @GetMapping("/count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> getUnreadCount() {
        return ResponseEntity.ok(notificationService.getUnreadCount());
    }

    @PostMapping("/mark-read/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(new ApiResponse(true, "Notification marked as read"));
    }

    @PostMapping("/mark-all-read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> markAllAsRead() {
        notificationService.markAllAsRead();
        return ResponseEntity.ok(new ApiResponse(true, "All notifications marked as read"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(new ApiResponse(true, "Notification deleted successfully"));
    }

    @DeleteMapping("/clear-all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> clearAllNotifications() {
        notificationService.clearAllNotifications();
        return ResponseEntity.ok(new ApiResponse(true, "All notifications cleared successfully"));
    }

    @PostMapping("/subscribe")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> subscribeToNotifications(@RequestBody List<String> notificationTypes) {
        notificationService.subscribeToNotifications(notificationTypes);
        return ResponseEntity.ok(new ApiResponse(true, "Subscribed to notifications successfully"));
    }

    @PostMapping("/unsubscribe")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> unsubscribeFromNotifications(@RequestBody List<String> notificationTypes) {
        notificationService.unsubscribeFromNotifications(notificationTypes);
        return ResponseEntity.ok(new ApiResponse(true, "Unsubscribed from notifications successfully"));
    }

    @GetMapping("/preferences")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Boolean>> getNotificationPreferences() {
        return ResponseEntity.ok(notificationService.getNotificationPreferences());
    }

    @PutMapping("/preferences")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateNotificationPreferences(@RequestBody Map<String, Boolean> preferences) {
        notificationService.updateNotificationPreferences(preferences);
        return ResponseEntity.ok(new ApiResponse(true, "Notification preferences updated successfully"));
    }

    @PostMapping("/send")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> sendNotification(@Valid @RequestBody Notification notification) {
        notificationService.sendNotification(notification);
        return ResponseEntity.ok(new ApiResponse(true, "Notification sent successfully"));
    }

    @PostMapping("/broadcast")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> broadcastNotification(
            @Valid @RequestBody Notification notification,
            @RequestParam(required = false) List<String> roles) {
        notificationService.broadcastNotification(notification, roles);
        return ResponseEntity.ok(new ApiResponse(true, "Notification broadcasted successfully"));
    }
} 