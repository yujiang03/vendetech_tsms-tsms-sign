package com.vendetech.project.tsms.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vendetech.project.system.domain.SysRole;
import com.vendetech.project.system.mapper.SysRoleMapper;
import com.vendetech.project.tsms.user.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public List<SysRole> selectRoleList() {
        return sysRoleMapper.selectByRoleList();
    }
}
