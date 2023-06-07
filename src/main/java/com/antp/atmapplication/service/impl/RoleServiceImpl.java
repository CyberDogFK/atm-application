package com.antp.atmapplication.service.impl;

import com.antp.atmapplication.exception.DataProcessingException;
import com.antp.atmapplication.model.Role;
import com.antp.atmapplication.repository.RoleRepository;
import com.antp.atmapplication.service.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getRoleByName(Role.RoleName name) {
        return roleRepository.findRoleByName(name)
                .orElseThrow(() -> new DataProcessingException("Can't find role with name: " + name));
    }
}
