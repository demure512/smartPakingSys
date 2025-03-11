package com.smartparking.api.repository;

import com.smartparking.api.model.GlobalConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalConfigurationRepository extends JpaRepository<GlobalConfiguration, String> {
} 