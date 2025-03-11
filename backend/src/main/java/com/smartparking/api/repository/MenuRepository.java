package com.smartparking.api.repository;

import com.smartparking.api.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    List<Menu> findByParentId(Integer parentId);
    
    @Query("SELECT m FROM Menu m WHERE m.parent IS NULL")
    List<Menu> findRootMenus();
    
    @Query("SELECT m FROM Menu m JOIN m.roles r WHERE r.id = :roleId")
    List<Menu> findByRoleId(Integer roleId);
} 