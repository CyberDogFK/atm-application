package com.antp.atmapplication.service;

import com.antp.atmapplication.model.Role;

public interface RoleService {
    Role getRoleByName(Role.RoleName name);
}
