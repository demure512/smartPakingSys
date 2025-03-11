package com.smartparking.api.service.impl;

import com.smartparking.api.model.GlobalConfiguration;
import com.smartparking.api.repository.GlobalConfigurationRepository;
import com.smartparking.api.service.GlobalConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GlobalConfigurationServiceImpl implements GlobalConfigurationService {

    private final GlobalConfigurationRepository configurationRepository;

    @Autowired
    public GlobalConfigurationServiceImpl(GlobalConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    @Override
    public List<GlobalConfiguration> getAllConfigurations() {
        return configurationRepository.findAll();
    }

    @Override
    public Optional<GlobalConfiguration> getConfiguration(String key) {
        return configurationRepository.findById(key);
    }

    @Override
    @Transactional
    public GlobalConfiguration setConfiguration(String key, String value) {
        GlobalConfiguration config = configurationRepository.findById(key)
                .orElse(new GlobalConfiguration());
        config.setKey(key);
        config.setValue(value);
        return configurationRepository.save(config);
    }

    @Override
    @Transactional
    public void deleteConfiguration(String key) {
        configurationRepository.deleteById(key);
    }

    @Override
    public String getConfigurationValue(String key, String defaultValue) {
        return configurationRepository.findById(key)
                .map(GlobalConfiguration::getValue)
                .orElse(defaultValue);
    }

    @Override
    public Integer getConfigurationValueAsInteger(String key, Integer defaultValue) {
        String value = getConfigurationValue(key, null);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    public Boolean getConfigurationValueAsBoolean(String key, Boolean defaultValue) {
        String value = getConfigurationValue(key, null);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }
    
    @Override
    public List<GlobalConfiguration> getConfigurationsByCategory(String category) {
        return configurationRepository.findAll().stream()
                .filter(config -> category.equals(config.getCategory()))
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public GlobalConfiguration createConfiguration(GlobalConfiguration configuration) {
        if (configuration.getKey() == null || configuration.getKey().isEmpty()) {
            throw new IllegalArgumentException("Configuration key cannot be empty");
        }
        
        if (configurationRepository.existsById(configuration.getKey())) {
            throw new IllegalArgumentException("Configuration with key '" + configuration.getKey() + "' already exists");
        }
        
        return configurationRepository.save(configuration);
    }
    
    @Override
    @Transactional
    public GlobalConfiguration updateConfiguration(String key, GlobalConfiguration configuration) {
        if (!configurationRepository.existsById(key)) {
            throw new IllegalArgumentException("Configuration with key '" + key + "' does not exist");
        }
        
        configuration.setKey(key); // Ensure the key is set correctly
        return configurationRepository.save(configuration);
    }
    
    @Override
    @Transactional
    public List<GlobalConfiguration> updateBatchConfigurations(Map<String, String> configurations) {
        List<GlobalConfiguration> updatedConfigs = new ArrayList<>();
        
        configurations.forEach((key, value) -> {
            GlobalConfiguration config = configurationRepository.findById(key)
                    .orElse(new GlobalConfiguration());
            config.setKey(key);
            config.setValue(value);
            updatedConfigs.add(configurationRepository.save(config));
        });
        
        return updatedConfigs;
    }
    
    @Override
    public List<String> getAllCategories() {
        return configurationRepository.findAll().stream()
                .map(GlobalConfiguration::getCategory)
                .filter(category -> category != null && !category.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void resetToDefaults() {
        // Define your default configurations here
        Map<String, String> defaultConfigs = new HashMap<>();
        defaultConfigs.put("parking.fee.hourly", "2.00");
        defaultConfigs.put("parking.fee.daily", "20.00");
        defaultConfigs.put("parking.fee.monthly", "200.00");
        defaultConfigs.put("system.theme", "light");
        defaultConfigs.put("system.language", "en");
        
        // Clear existing configurations
        configurationRepository.deleteAll();
        
        // Create default configurations
        defaultConfigs.forEach((key, value) -> {
            GlobalConfiguration config = new GlobalConfiguration();
            config.setKey(key);
            config.setValue(value);
            config.setCategory(key.split("\\.")[0]); // Set category based on key prefix
            configurationRepository.save(config);
        });
    }
    
    @Override
    public Map<String, String> exportConfigurations() {
        Map<String, String> exportedConfigs = new HashMap<>();
        
        configurationRepository.findAll().forEach(config -> 
            exportedConfigs.put(config.getKey(), config.getValue())
        );
        
        return exportedConfigs;
    }
    
    @Override
    @Transactional
    public void importConfigurations(Map<String, String> configurations) {
        configurations.forEach(this::setConfiguration);
    }
    
    @Override
    public Map<String, String> validateConfigurations() {
        Map<String, String> validationErrors = new HashMap<>();
        
        // Validate numeric configurations
        List<String> numericKeys = List.of("parking.fee.hourly", "parking.fee.daily", "parking.fee.monthly");
        
        for (String key : numericKeys) {
            String value = getConfigurationValue(key, null);
            if (value != null) {
                try {
                    Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    validationErrors.put(key, "Value must be a valid number");
                }
            }
        }
        
        // Add other validation rules as needed
        
        return validationErrors;
    }
    
    @Override
    @Transactional
    public void refreshConfigurations() {
        // This method would typically reload configurations from a source
        // such as a database or configuration files
        // For now, we'll just reload from the database
        
        // You could also clear any caches here if you had them
    }
} 