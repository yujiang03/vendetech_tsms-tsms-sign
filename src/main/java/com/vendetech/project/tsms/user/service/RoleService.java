package com.vendetech.project.tsms.user.service;

import java.util.List;

import com.vendetech.project.system.domain.SysRole;

public interface RoleService {
    List<SysRole> selectRoleList();
}
