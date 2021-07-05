package com.vendetech.project.tsms.user.service.impl;

import com.vendetech.common.utils.RandomUtil;
import com.vendetech.common.utils.SecurityUtils;
import com.vendetech.common.utils.StringUtils;
import com.vendetech.hr.service.impl.kit.BankCardKit;
import com.vendetech.project.system.domain.SysUser;
import com.vendetech.project.system.domain.SysUserRole;
import com.vendetech.project.system.mapper.SysUserMapper;
import com.vendetech.project.system.mapper.SysUserRoleMapper;
import com.vendetech.project.tsms.domain.Employee;
import com.vendetech.project.tsms.user.controller.dto.CreateUserDTO;
import com.vendetech.project.tsms.user.controller.dto.UserDTO;
import com.vendetech.project.tsms.user.controller.dto.UserNameDTO;
import com.vendetech.project.tsms.user.mapper.EmployeeMapper;
import com.vendetech.project.tsms.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private EmployeeMapper employeeMapper;

    /**
	 * @param param
	 * @return
	 */
	public List<Map> findduser(Map<String, Object> param){
		return null;
	}

    @Override
    public List<UserNameDTO> getByUserName(String loginName) {
        List<UserNameDTO> byUserName = sysUserMapper.getByUserName(loginName);
        return byUserName;
    }

    @Override
    public List<SysUser> selectByUserName(String userName) {
        return sysUserMapper.selectByUserName(userName);
    }

    @Override
    public List<HashMap<String, Object>> selectByUserList(String param) {
        return sysUserMapper.selectByUserList(param);
    }


    @Override
    public int resetPwd(SysUser user) {
        return sysUserMapper.resetPwd(user);
    }

    @Override
    public int saveAccount(CreateUserDTO createUserDTO) {
        SysUser user = new SysUser();
        user.setEmployeeId(createUserDTO.getEmployeeId());
        user.setUserName(createUserDTO.getUserName());
        user.setNickName(createUserDTO.getNickName());
        user.setDeptId(createUserDTO.getDeptId());
        user.setEmail(createUserDTO.getEmail());
        user.setStatus(createUserDTO.getStatus());
        sysUserMapper.saveAccount(user);
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setRoleId(createUserDTO.getRoleId());
        sysUserRole.setUserId(user.getUserId());
        sysUserRoleMapper.insertUserANDRole(sysUserRole);
        return 0;
    }

    @Override
    public int updateRoleByUserId(CreateUserDTO createUserDTO) {
        SysUserRole user = new SysUserRole();
        user.setUserId(createUserDTO.getUserId());
        user.setRoleId(createUserDTO.getRoleId());
        sysUserRoleMapper.updateRoleByUserId(user);
        SysUser sysUser = new SysUser();
        sysUser.setEmail(createUserDTO.getEmail());
        sysUser.setUserId(createUserDTO.getUserId());
        sysUserMapper.updateUser(sysUser);
        return 0;
    }

    @Override
    public int deleteByUserId(Long userId) {
        return sysUserMapper.updateByStatus(userId);
    }

    @Override
    public SysUserRole selectByUserId(Long userId) {
        return sysUserRoleMapper.selectByUserId(userId);
    }

    @Override
    public List<HashMap<String, Object>> selectByRole(String userName) {
        return sysUserMapper.selectByRole(userName);
    }

    @Override
    public SysUser selectByPassword(Long userId) {
        return sysUserMapper.selectByPassword(userId);
    }

    @Override
    public int updatePassword(UserDTO userDTO) {
        SysUser user = new SysUser();
        user.setUserId(userDTO.getUserId());
        user.setSalt(RandomUtil.randomStr(6));
        user.setPassword(SecurityUtils.encryptPassword(userDTO.getNewPassWord()));
        return sysUserMapper.updatePassword(user);
    }

    @Override
    public SysUser selectByEmail(Long userId) {
        return sysUserMapper.selectByEmail(userId);
    }

    @Override
    public List<Map<String, Object>> selectUserListByRole(Long roleId) {
    	return sysUserMapper.selectUserListByRole(roleId);
    }

    @Override
    public List<SysUser> selectUserListForBatchInsert() {
        return sysUserMapper.selectUserListForBatchInsert();
    }

    @Override
    public int updateSysUserByTask() {
        List<SysUser> notInSysUsers = new ArrayList<>();
        List<Employee> employees = employeeMapper.selectAllEmployee();
        List<SysUser> sysUsers = sysUserMapper.selectUserListForBatchInsert();

        for(SysUser sysUser : sysUsers) {
            employees = employees.stream().filter(employee ->
                    !(sysUser.getUserName().equals(employee.getEmployeeNum()))).collect(Collectors.toList());
        }

        for(Employee employee : employees) {
            SysUser user = new SysUser();

            if(StringUtils.isBlank(employee.getEmployeeNum())) {
                continue;
            }
            user.setEmployeeId(employee.getEmployeeId());
            user.setUserName(employee.getEmployeeNum());
            user.setNickName(employee.getEmployeeName());
            user.setDeptId(employee.getMainDeptId());
            user.setPhonenumber(employee.getMobile());
            user.setAvatar(employee.getAvatar());
            String sex = employee.getSex() == null ? "2" : String.valueOf(employee.getSex());
            user.setSex(sex);
            user.setStatus(String.valueOf(employee.getStatus()));
            String password = employee.getEmployeeNum().substring(3); // 截取后6位
            user.setPassword(SecurityUtils.encryptPassword(password));
            user.setCreateBy("systemTaskJob");
            user.setCreateTime(new Date());
            notInSysUsers.add(user);
        }

        if (notInSysUsers.isEmpty()) {
            logger.info("sys_user, sys_user_role, 没有需要更新的数据");
            return 0;
        }

        int userCount = sysUserMapper.batchInsertSysUser(notInSysUsers);
        logger.info("sys_user表成功更新 " + userCount + " 条数据");
        int roleCount = sysUserRoleMapper.batchUserRoleBySelect();
        logger.info("sys_user_role表成功更新 " + roleCount + " 条数据");

        return 0;
    }

    @Override
    public void resetUserErrorCount() {
        sysUserMapper.resetUserErrorCount();
    }


}
