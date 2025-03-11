package com.smartparking.api.repository;

import com.smartparking.api.model.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Integer> {
    List<Partner> findByNameContaining(String name);
    
    List<Partner> findByRelationshipType(String relationshipType);
} 