package com.smartparking.api.controller;

import com.smartparking.api.model.GlobalConfiguration;
import com.smartparking.api.payload.ApiResponse;
import com.smartparking.api.service.GlobalConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/config")
@PreAuthorize("hasRole('ADMIN')")
public class GlobalConfigurationController {

    @Autowired
    private GlobalConfigurationService configService;

    @GetMapping
    public List<GlobalConfiguration> getAllConfigurations() {
        return configService.getAllConfigurations();
    }

    @GetMapping("/{key}")
    public ResponseEntity<Optional<GlobalConfiguration>> getConfiguration(@PathVariable String key) {
        return ResponseEntity.ok(configService.getConfiguration(key));
    }

    @GetMapping("/category/{category}")
    public List<GlobalConfiguration> getConfigurationsByCategory(@PathVariable String category) {
        return configService.getConfigurationsByCategory(category);
    }

    @PostMapping
    public ResponseEntity<GlobalConfiguration> createConfiguration(
            @Valid @RequestBody GlobalConfiguration configuration) {
        return ResponseEntity.ok(configService.createConfiguration(configuration));
    }

    @PutMapping("/{key}")
    public ResponseEntity<GlobalConfiguration> updateConfiguration(
            @PathVariable String key,
            @Valid @RequestBody GlobalConfiguration configuration) {
        return ResponseEntity.ok(configService.updateConfiguration(key, configuration));
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<?> deleteConfiguration(@PathVariable String key) {
        configService.deleteConfiguration(key);
        return ResponseEntity.ok(new ApiResponse(true, "Configuration deleted successfully"));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<GlobalConfiguration>> updateBatchConfigurations(
            @Valid @RequestBody Map<String, String> configurations) {
        return ResponseEntity.ok(configService.updateBatchConfigurations(configurations));
    }

    @GetMapping("/categories")
    public List<String> getAllCategories() {
        return configService.getAllCategories();
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetToDefaults() {
        configService.resetToDefaults();
        return ResponseEntity.ok(new ApiResponse(true, "Configurations reset to defaults successfully"));
    }

    @GetMapping("/export")
    public ResponseEntity<Map<String, String>> exportConfigurations() {
        return ResponseEntity.ok(configService.exportConfigurations());
    }

    @PostMapping("/import")
    public ResponseEntity<?> importConfigurations(@RequestBody Map<String, String> configurations) {
        configService.importConfigurations(configurations);
        return ResponseEntity.ok(new ApiResponse(true, "Configurations imported successfully"));
    }

    @GetMapping("/validate")
    public ResponseEntity<Map<String, String>> validateConfigurations() {
        return ResponseEntity.ok(configService.validateConfigurations());
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshConfigurations() {
        configService.refreshConfigurations();
        return ResponseEntity.ok(new ApiResponse(true, "Configurations refreshed successfully"));
    }
} 