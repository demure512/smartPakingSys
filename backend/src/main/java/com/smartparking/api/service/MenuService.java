package com.smartparking.api.service;

import com.smartparking.api.model.Menu;
import java.util.List;
import java.util.Optional;

public interface MenuService {
    /**
     * Get all menus
     * @return List of all menus
     */
    List<Menu> getAllMenus();
    
    /**
     * Get menu by ID
     * @param id The menu ID
     * @return The menu if found
     */
    Optional<Menu> getMenuById(Integer id);
    
    /**
     * Create a new menu
     * @param menu The menu to create
     * @return The created menu
     */
    Menu createMenu(Menu menu);
    
    /**
     * Update an existing menu
     * @param menu The menu to update
     * @return The updated menu
     */
    Menu updateMenu(Menu menu);
    
    /**
     * Delete a menu
     * @param id The menu ID
     */
    void deleteMenu(Integer id);
    
    /**
     * Get root menus (menus without parent)
     * @return List of root menus
     */
    List<Menu> getRootMenus();
    
    /**
     * Get child menus of a parent menu
     * @param parentId The parent menu ID
     * @return List of child menus
     */
    List<Menu> getChildMenus(Integer parentId);
    
    /**
     * Get menus by role
     * @param roleId The role ID
     * @return List of menus accessible by the role
     */
    List<Menu> getMenusByRole(Integer roleId);
    
    /**
     * Assign menus to a role
     * @param roleId The role ID
     * @param menuIds List of menu IDs to assign
     */
    void assignMenusToRole(Integer roleId, List<Integer> menuIds);
} 