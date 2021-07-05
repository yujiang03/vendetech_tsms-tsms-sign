package com.vendetech.project.tsms.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vendetech.project.system.domain.SysUser;
import com.vendetech.project.system.domain.SysUserRole;
import com.vendetech.project.tsms.domain.DdHrmDataSign;
import com.vendetech.project.tsms.user.controller.dto.CreateUserDTO;
import com.vendetech.project.tsms.user.controller.dto.UserDTO;
import com.vendetech.project.tsms.user.controller.dto.UserNameDTO;

public interface UserService {
	List<HashMap<String, Object>> selectByUserList(String param);

	int resetPwd(SysUser user);

	int saveAccount(CreateUserDTO createUserDTO);

	int updateRoleByUserId(CreateUserDTO createUserDTO);

	int deleteByUserId(Long userId);

	SysUserRole selectByUserId(Long userId);

	List<HashMap<String, Object>> selectByRole(String userName);

	SysUser selectByPassword(Long userId);

	int updatePassword(UserDTO userDTO);

	SysUser selectByEmail(Long userId);

	/**
	 * @param param
	 * @return
	 */
	List<Map> findduser(Map<String, Object> param);

	List<UserNameDTO> getByUserName(String loginName);

	List<SysUser> selectByUserName(String userName);

	List<Map<String, Object>> selectUserListByRole(Long roleId);

	public List<SysUser> selectUserListForBatchInsert();

    int updateSysUserByTask();

    void resetUserErrorCount();
}
