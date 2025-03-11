package com.smartparking.api.service.impl;

import com.smartparking.api.model.Menu;
import com.smartparking.api.model.Role;
import com.smartparking.api.repository.MenuRepository;
import com.smartparking.api.repository.RoleRepository;
import com.smartparking.api.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public MenuServiceImpl(MenuRepository menuRepository, RoleRepository roleRepository) {
        this.menuRepository = menuRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    @Override
    public Optional<Menu> getMenuById(Integer id) {
        return menuRepository.findById(id);
    }

    @Override
    @Transactional
    public Menu createMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    @Override
    @Transactional
    public Menu updateMenu(Menu menu) {
        if (!menuRepository.existsById(menu.getId())) {
            throw new IllegalArgumentException("Menu not found with ID: " + menu.getId());
        }
        return menuRepository.save(menu);
    }

    @Override
    @Transactional
    public void deleteMenu(Integer id) {
        menuRepository.deleteById(id);
    }

    @Override
    public List<Menu> getRootMenus() {
        return menuRepository.findRootMenus();
    }

    @Override
    public List<Menu> getChildMenus(Integer parentId) {
        return menuRepository.findByParentId(parentId);
    }

    @Override
    public List<Menu> getMenusByRole(Integer roleId) {
        return menuRepository.findByRoleId(roleId);
    }

    @Override
    @Transactional
    public void assignMenusToRole(Integer roleId, List<Integer> menuIds) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found with ID: " + roleId));

        Set<Menu> menus = menuIds.stream()
                .map(menuId -> menuRepository.findById(menuId)
                        .orElseThrow(() -> new IllegalArgumentException("Menu not found with ID: " + menuId)))
                .collect(Collectors.toSet());

        role.setMenus(menus);
        roleRepository.save(role);
    }
} 