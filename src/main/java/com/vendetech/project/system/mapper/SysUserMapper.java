package com.vendetech.project.system.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.vendetech.project.system.domain.SysUser;
import com.vendetech.project.tsms.user.controller.dto.UserNameDTO;

/**
 * 用户表 数据层
 * 
 * @author vendetech
 */
public interface SysUserMapper
{
    /**
     * 根据条件分页查询用户列表
     * 
     * @param sysUser 用户信息
     * @return 用户信息集合信息
     */
    public List<SysUser> selectUserList(SysUser sysUser);

    /**
     * 通过用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象信息
     */
    public SysUser selectUserByUserName(String userName);

    /**
     * 通过用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象信息
     */
    public SysUser selectUserById(Long userId);

    /**
     * 新增用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int insertUser(SysUser user);

    /**
     * 修改用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    public int updateUser(SysUser user);

    /**
     * 修改用户头像
     * 
     * @param userName 用户名
     * @param avatar 头像地址
     * @return 结果
     */
    public int updateUserAvatar(@Param("userName") String userName, @Param("avatar") String avatar);

    /**
     * 重置用户密码
     * 
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    public int resetUserPwd(@Param("userName") String userName, @Param("password") String password);

    /**
     * 通过用户ID删除用户
     * 
     * @param userId 用户ID
     * @return 结果
     */
    public int deleteUserById(Long userId);

    /**
     * 批量删除用户信息
     * 
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    public int deleteUserByIds(Long[] userIds);

    /**
     * 校验用户名称是否唯一
     * 
     * @param userName 用户名称
     * @return 结果
     */
    public int checkUserNameUnique(String userName);

    /**
     * 校验手机号码是否唯一
     *
     * @param phonenumber 手机号码
     * @return 结果
     */
    public SysUser checkPhoneUnique(String phonenumber);

    /**
     * 校验email是否唯一
     *
     * @param email 用户邮箱
     * @return 结果
     */
    public SysUser checkEmailUnique(String email);

	/**
	 * @param loginName
	 * @return
	 */
	public List<UserNameDTO> getByUserName(String loginName);

	/**
	 * @param userName
	 * @return
	 */
	public List<SysUser> selectByUserName(String userName);

	/**
	 * @param param
	 * @return
	 */
	public List<HashMap<String, Object>> selectByUserList(@Param("keyword") String param);

	/**
	 * @param param
	 * @return
	 */
	public List<HashMap<String, Object>> selectUserListByKeyword(@Param("keyword") String param);
	
	/**
	 * @param user
	 * @return
	 */
	public int resetPwd(SysUser user);

	/**
	 * @param userId
	 * @return
	 */
	public int updateByStatus(Long userId);

	/**
	 * @param nickName
	 * @return
	 */
	public List<HashMap<String, Object>> selectByRole(@Param("nickName") String nickName);

	/**
	 * @param userId
	 * @return
	 */
	public SysUser selectByPassword(Long userId);

	/**
	 * @param userId
	 * @return
	 */
	public SysUser selectByEmail(Long userId);

	/**
	 * @param user
	 * @return
	 */
	public int updatePassword(SysUser user);

	/**
	 * @param user
	 */
	public void saveAccount(SysUser user);

	/**
	 * @param username
	 * @return
	 */
	public int getErrorCount(String username);

	/**
	 * @param param
	 */
	public void updateErrorCount(Map<String, Object> param);

	public int updateDelFlag(SysUser sysUser);

	List<SysUser> selectDelFlag(String userName);
	
	public List<Map<String, Object>> selectUserListByRole(@Param("roleId") Long roleId);

	/**
	 * 查询批量数据，用于插入去重
	 */
	public List<SysUser> selectUserListForBatchInsert();

	int batchInsertSysUser(List<SysUser> notInSysUsers);

    void resetUserErrorCount();
}
