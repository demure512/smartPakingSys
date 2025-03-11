package com.smartparking.api.service;

import com.smartparking.api.model.GlobalConfiguration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GlobalConfigurationService {
    /**
     * Get all configurations
     * @return List of all configurations
     */
    List<GlobalConfiguration> getAllConfigurations();
    
    /**
     * Get configuration by key
     * @param key The configuration key
     * @return The configuration value if found
     */
    Optional<GlobalConfiguration> getConfiguration(String key);
    
    /**
     * Set configuration value
     * @param key The configuration key
     * @param value The configuration value
     * @return The updated configuration
     */
    GlobalConfiguration setConfiguration(String key, String value);
    
    /**
     * Delete configuration
     * @param key The configuration key
     */
    void deleteConfiguration(String key);
    
    /**
     * Get configuration value as String
     * @param key The configuration key
     * @param defaultValue Default value if not found
     * @return The configuration value or default value
     */
    String getConfigurationValue(String key, String defaultValue);
    
    /**
     * Get configuration value as Integer
     * @param key The configuration key
     * @param defaultValue Default value if not found
     * @return The configuration value or default value
     */
    Integer getConfigurationValueAsInteger(String key, Integer defaultValue);
    
    /**
     * Get configuration value as Boolean
     * @param key The configuration key
     * @param defaultValue Default value if not found
     * @return The configuration value or default value
     */
    Boolean getConfigurationValueAsBoolean(String key, Boolean defaultValue);
    
    /**
     * Get configurations by category
     * @param category The category
     * @return List of configurations in the specified category
     */
    List<GlobalConfiguration> getConfigurationsByCategory(String category);
    
    /**
     * Create a new configuration
     * @param configuration The configuration to create
     * @return The created configuration
     */
    GlobalConfiguration createConfiguration(GlobalConfiguration configuration);
    
    /**
     * Update an existing configuration
     * @param key The key of the configuration to update
     * @param configuration The updated configuration
     * @return The updated configuration
     */
    GlobalConfiguration updateConfiguration(String key, GlobalConfiguration configuration);
    
    /**
     * Update a batch of configurations
     * @param configurations Map of key-value pairs to update
     * @return List of updated configurations
     */
    List<GlobalConfiguration> updateBatchConfigurations(Map<String, String> configurations);
    
    /**
     * Get all unique categories
     * @return List of all categories
     */
    List<String> getAllCategories();
    
    /**
     * Reset configurations to default values
     */
    void resetToDefaults();
    
    /**
     * Export all configurations as key-value pairs
     * @return Map of configuration key-value pairs
     */
    Map<String, String> exportConfigurations();
    
    /**
     * Import configurations from a key-value map
     * @param configurations Map of configuration key-value pairs
     */
    void importConfigurations(Map<String, String> configurations);
    
    /**
     * Validate all configurations
     * @return Map of validation errors (if any)
     */
    Map<String, String> validateConfigurations();
    
    /**
     * Refresh configurations (reload from data source)
     */
    void refreshConfigurations();
} 