package com.smartparking.api.service;

import com.smartparking.api.model.SystemLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface SystemLogService {
    @Transactional
    SystemLog createLog(Integer userId, String actionType, String details);

    List<SystemLog> getLogsByUser(Integer userId);

    List<SystemLog> getLogsByActionType(String actionType);

    /**
     * Get all system logs with optional filters
     * @param start Optional start date
     * @param end Optional end date
     * @param logLevel Optional log level filter
     * @param component Optional component filter
     * @param pageable Pagination information
     * @return Page of filtered logs
     */
    Page<SystemLog> getAllLogs(LocalDateTime start, LocalDateTime end, String logLevel, String component, Pageable pageable);
    
    /**
     * Get only error logs
     * @param start Optional start date
     * @param end Optional end date
     * @param pageable Pagination information
     * @return Page of error logs
     */
    Page<SystemLog> getErrorLogs(LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    /**
     * Get security-related logs
     * @param start Optional start date
     * @param end Optional end date
     * @param pageable Pagination information
     * @return Page of security logs
     */
    Page<SystemLog> getSecurityLogs(LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    /**
     * Get logs for a specific user's activity
     * @param userId The user ID
     * @param start Optional start date
     * @param end Optional end date
     * @param pageable Pagination information
     * @return Page of user activity logs
     */
    Page<SystemLog> getUserActivityLogs(Long userId, LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    /**
     * Get logs for a specific parking lot
     * @param parkingLotId The parking lot ID
     * @param start Optional start date
     * @param end Optional end date
     * @param pageable Pagination information
     * @return Page of parking lot logs
     */
    Page<SystemLog> getParkingLotLogs(Integer parkingLotId, LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    /**
     * Get all available system components that have logs
     * @return List of component names
     */
    List<String> getAvailableComponents();
    
    /**
     * Get all available log levels
     * @return List of log levels
     */
    List<String> getAvailableLogLevels();
    
    /**
     * Delete a specific log entry
     * @param id The log ID to delete
     */
    void deleteLog(Long id);
    
    /**
     * Delete all logs older than a specific date
     *
     * @param date Date threshold (logs before this date will be deleted)
     * @return Number of deleted logs
     */
    int deleteLogsBefore(LocalDateTime date);
    
    /**
     * Export logs with optional filters
     * @param start Optional start date
     * @param end Optional end date
     * @param logLevel Optional log level filter
     * @param component Optional component filter
     * @param format Export format (CSV, JSON, XML)
     * @return Exported logs as byte array
     */
    byte[] exportLogs(LocalDateTime start, LocalDateTime end, String logLevel, String component, String format);
    
    /**
     * Create a new system log entry
     * @param level Log level
     * @param component Component name
     * @param message Log message
     * @param userId Optional user ID associated with the log
     * @return The created log entry
     */
    SystemLog createLog(String level, String component, String message, Long userId);

    List<SystemLog> getLogsByDateRange(LocalDateTime oneHourAgo, LocalDateTime now);

    List<SystemLog> getLogsByUserAndDateRange(Integer userId, LocalDateTime start, LocalDateTime end);
}