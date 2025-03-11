package com.smartparking.api.controller;

import com.smartparking.api.model.SystemLog;
import com.smartparking.api.service.SystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/system-logs")
@PreAuthorize("hasRole('ADMIN')")
public class SystemLogController {

    @Autowired
    private SystemLogService systemLogService;

    @GetMapping
    public Page<SystemLog> getAllLogs(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) String logLevel,
            @RequestParam(required = false) String component,
            Pageable pageable) {
        return systemLogService.getAllLogs(start, end, logLevel, component, pageable);
    }

    @GetMapping("/errors")
    public Page<SystemLog> getErrorLogs(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            Pageable pageable) {
        return systemLogService.getErrorLogs(start, end, pageable);
    }

    @GetMapping("/security")
    public Page<SystemLog> getSecurityLogs(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            Pageable pageable) {
        return systemLogService.getSecurityLogs(start, end, pageable);
    }

    @GetMapping("/user-activity/{userId}")
    public Page<SystemLog> getUserActivityLogs(
            @PathVariable Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            Pageable pageable) {
        return systemLogService.getUserActivityLogs(userId, start, end, pageable);
    }

    @GetMapping("/parking-lot/{parkingLotId}")
    public Page<SystemLog> getParkingLotLogs(
            @PathVariable Integer parkingLotId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            Pageable pageable) {
        return systemLogService.getParkingLotLogs(parkingLotId, start, end, pageable);
    }

    @GetMapping("/components")
    public List<String> getAvailableComponents() {
        return systemLogService.getAvailableComponents();
    }

    @GetMapping("/log-levels")
    public List<String> getAvailableLogLevels() {
        return systemLogService.getAvailableLogLevels();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLog(@PathVariable Long id) {
        systemLogService.deleteLog(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/before")
    public ResponseEntity<?> deleteLogsBefore(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        systemLogService.deleteLogsBefore(date);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/export")
    public ResponseEntity<byte[]> exportLogs(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) String logLevel,
            @RequestParam(required = false) String component,
            @RequestParam(defaultValue = "CSV") String format) {
        
        byte[] exportedLogs = systemLogService.exportLogs(start, end, logLevel, component, format);
        
        String filename = "system_logs_" + LocalDateTime.now().toString() + "." + format.toLowerCase();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + filename)
                .body(exportedLogs);
    }
} 