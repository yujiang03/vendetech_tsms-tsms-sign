package com.vendetech.project.system.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vendetech.common.constant.UserConstants;
import com.vendetech.common.exception.CustomException;
import com.vendetech.common.utils.SecurityUtils;
import com.vendetech.common.utils.StringUtils;
import com.vendetech.framework.aspectj.lang.annotation.DataScope;
import com.vendetech.framework.aspectj.lang.annotation.DataSource;
import com.vendetech.framework.aspectj.lang.enums.DataSourceType;
import com.vendetech.project.system.domain.SysPost;
import com.vendetech.project.system.domain.SysRole;
import com.vendetech.project.system.domain.SysUser;
import com.vendetech.project.system.domain.SysUserPost;
import com.vendetech.project.system.domain.SysUserRole;
import com.vendetech.project.system.mapper.SysPostMapper;
import com.vendetech.project.system.mapper.SysRoleMapper;
import com.vendetech.project.system.mapper.SysUserMapper;
import com.vendetech.project.system.mapper.SysUserPostMapper;
import com.vendetech.project.system.mapper.SysUserRoleMapper;
import com.vendetech.project.system.service.ISysConfigService;
import com.vendetech.project.system.service.ISysUserService;
import com.vendetech.project.tsms.user.controller.dto.CreateUserDTO;
import com.vendetech.project.tsms.user.controller.dto.UserNameDTO;
import com.vendetech.project.tsms.user.controller.util.GetRamdom;
import com.vendetech.project.tsms.utils.MailConstant;
import com.vendetech.project.tsms.utils.MailSentThread;
import com.vendetech.project.tsms.utils.MailUtils;

/**
 * 用户 业务层处理
 * 
 * @author vendetech
 */
@Service
public class SysUserServiceImpl implements ISysUserService
{
    private static final Logger log = LoggerFactory.getLogger(SysUserServiceImpl.class);

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

    @Autowired
    private SysPostMapper postMapper;

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysUserPostMapper userPostMapper;

    @Autowired
    private ISysConfigService configService;
    
//    @Autowired
//    private ISysConfigService configService2;
//    
//    @Autowired
//    private ISysConfigService configService3;

    /**
     * 根据条件分页查询用户列表
     * 
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    @DataScope(deptAlias = "d", userAlias = "u")
    public List<SysUser> selectUserList(SysUser user)
    {
        return userMapper.selectUserList(user);
    }

    /**
     * 通过用户名查询用户
     * 
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName)
    {
        return userMapper.selectUserByUserName(userName);
    }

    /**
     * 通过用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId)
    {
        return userMapper.selectUserById(userId);
    }

    /**
     * 查询用户所属角色组
     * 
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName)
    {
        List<SysRole> list = roleMapper.selectRolesByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysRole role : list)
        {
            idsStr.append(role.getRoleName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString()))
        {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 查询用户所属岗位组
     * 
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String userName)
    {
        List<SysPost> list = postMapper.selectPostsByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysPost post : list)
        {
            idsStr.append(post.getPostName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString()))
        {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 校验用户名称是否唯一
     * 
     * @param userName 用户名称
     * @return 结果
     */
    @Override
    public String checkUserNameUnique(String userName)
    {
        int count = userMapper.checkUserNameUnique(userName);
        if (count > 0)
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkPhoneUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkEmailUnique(SysUser user)
    {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue())
        {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     * 
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user)
    {
        if (StringUtils.isNotNull(user.getUserId()) && user.isAdmin())
        {
            throw new CustomException("不允许操作超级管理员用户");
        }
    }

    /**
     * 新增保存用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUser(SysUser user)
    {
        // 新增用户信息
        int rows = userMapper.insertUser(user);
        // 新增用户岗位关联
        insertUserPost(user);
        // 新增用户与角色管理
        insertUserRole(user);
        return rows;
    }

    /**
     * 修改保存用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUser(SysUser user)
    {
        Long userId = user.getUserId();
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);
        // 删除用户与岗位关联
        userPostMapper.deleteUserPostByUserId(userId);
        // 新增用户与岗位管理
        insertUserPost(user);
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户状态
     * 
     * @param user 用户信息
     * @return 结果
     */
	@Override
	public int updateUserStatus(SysUser user) {
		return userMapper.updateUser(user);
	}

	@Override
	public List<HashMap<String, Object>> selectByUserList(String param) {
		return userMapper.selectByUserList(param);
	}

	@Override
	public List<SysUser> selectByUserName(String userName) {
		return userMapper.selectByUserName(userName);
	}

	@Override
	public List<HashMap<String, Object>> selectUserListByKeyword(String param) {
		return userMapper.selectUserListByKeyword(param);
	}

	@Override
    public int saveAccount(CreateUserDTO createUserDTO) {
        SysUser user = new SysUser();
        user.setEmployeeId(createUserDTO.getEmployeeId());
        user.setUserName(createUserDTO.getUserName());
        user.setNickName(createUserDTO.getNickName());
        user.setDeptId(createUserDTO.getDeptId());
        user.setEmail(createUserDTO.getEmail());
        user.setAvatar(createUserDTO.getAvatar());
        String password = GetRamdom.getRamdom();
        user.setPassword(SecurityUtils.encryptPassword(password));
        userMapper.saveAccount(user);
//        MailSentThread mailThread = new MailSentThread(user.getEmail(),password);
//        mailThread.start();
        MailUtils.sendSaveEmail(user.getEmail(),password);
        log.info("password: --------" + password);
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setRoleId(createUserDTO.getRoleId());
        sysUserRole.setUserId(user.getUserId());
        userRoleMapper.insertUserANDRole(sysUserRole);
        return 0;
    }
    
    @Override
    public List<HashMap<String, Object>> selectByRole(String nickName) {
        return userMapper.selectByRole(nickName);
    }
    
    @Override
    public int deleteByUserId(Long userId) {
        return userMapper.updateByStatus(userId);
    }
    
    @Override
    public int updateRoleByUserId(CreateUserDTO createUserDTO) {
        SysUserRole user = new SysUserRole();
        user.setUserId(createUserDTO.getUserId());
        user.setRoleId(createUserDTO.getRoleId());
        userRoleMapper.updateRoleByUserId(user);
        SysUser sysUser = new SysUser();
        sysUser.setEmail(createUserDTO.getEmail());
        sysUser.setUserId(createUserDTO.getUserId());        
        return userMapper.updateUser(sysUser);
    }
    
    /**
     * 修改用户基本信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户头像
     * 
     * @param userId 用户ID
     * @param avatar 头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar)
    {
        return userMapper.updateUserAvatar(userName, avatar) > 0;
    }

    /**
     * 重置用户密码
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(SysUser user)
    {
        return userMapper.updateUser(user);
    }

    /**
     * 重置用户密码
     * 
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(String userName, String password)
    {
        return userMapper.resetUserPwd(userName, password);
    }

    /**
     * 新增用户角色信息
     * 
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user)
    {
        Long[] roles = user.getRoleIds();
        if (StringUtils.isNotNull(roles))
        {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roles)
            {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.getUserId());
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0)
            {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    /**
     * 新增用户岗位信息
     *
     * @param user 用户对象
     */
    public void insertUserPost(SysUser user)
    {
        Long[] posts = user.getPostIds();
        if (StringUtils.isNotNull(posts))
        {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<SysUserPost>();
            for (Long postId : posts)
            {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size() > 0)
            {
                userPostMapper.batchUserPost(list);
            }
        }
    }

    /**
     * 通过用户ID删除用户
     * 
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteUserById(Long userId)
    {
        // 删除用户与角色关联
        userRoleMapper.deleteUserRoleByUserId(userId);
        // 删除用户与岗位表
        userPostMapper.deleteUserPostByUserId(userId);
        return userMapper.deleteUserById(userId);
    }

    /**
     * 批量删除用户信息
     * 
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    public int deleteUserByIds(Long[] userIds)
    {
        for (Long userId : userIds)
        {
            checkUserAllowed(new SysUser(userId));
        }
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * 导入用户数据
     * 
     * @param userList 用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(userList) || userList.size() == 0)
        {
            throw new CustomException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String password = configService.selectConfigByKey("sys.user.initPassword");
        for (SysUser user : userList)
        {
            try
            {
                // 验证是否存在这个用户
                SysUser u = userMapper.selectUserByUserName(user.getUserName());
                if (StringUtils.isNull(u))
                {
                    user.setPassword(SecurityUtils.encryptPassword(password));
                    user.setCreateBy(operName);
                    this.insertUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    user.setUpdateBy(operName);
                    this.updateUser(user);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new CustomException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }
    
    
    /**
	 * @param username
	 * @return
	 */
    @Override
	public int getErrorCount(String username){
    	return userMapper.getErrorCount(username);
	}
    
    /**
	 * @param param
	 */
    @Override
	public void updateErrorCount(Map<String, Object> param){
		userMapper.updateErrorCount(param);
	}
	
	@Override
    public List<UserNameDTO> getByUserName(String loginName) {
        List<UserNameDTO> byUserName = userMapper.getByUserName(loginName);
        return byUserName;
    }

    @Override
    public int updateDelFlag(CreateUserDTO createUserDTO) {
        SysUser user = new SysUser();
        user.setEmployeeId(createUserDTO.getEmployeeId());
        user.setUserId(createUserDTO.getUserId());
        user.setUserName(createUserDTO.getUserName());
        user.setNickName(createUserDTO.getNickName());
        user.setDeptId(createUserDTO.getDeptId());
        user.setEmail(createUserDTO.getEmail());
        String password = GetRamdom.getRamdom();
        user.setPassword(SecurityUtils.encryptPassword(password));
        userMapper.updateDelFlag(user);
		MailSentThread mailThread = new MailSentThread(user.getEmail(), MailConstant.Send_Subject,
				MailConstant.Send_Msg + password);
		mailThread.start();
        log.info("password: --------" + password);

        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setRoleId(createUserDTO.getRoleId());
        sysUserRole.setUserId(createUserDTO.getUserId());
        userRoleMapper.updateRoleByUserId(sysUserRole);
        return 0;
    }

    @Override
    public List<SysUser> selectDelFlag(String userName) {
        return userMapper.selectDelFlag(userName);
    }

    /**
	 * 
	 */
    @Override
    @DataSource(DataSourceType.MASTER)
	public List<SysUser> getMasterUser(){
    	SysUser user = new SysUser();
    	return userMapper.selectUserList(user); 
	}

	/**
	 * 
	 */
    @Override
    @DataSource(DataSourceType.SLAVE)
	public List<SysUser> getSlaveUser(){
    	SysUser user = new SysUser();
    	return userMapper.selectUserList(user);
	}
    
    /**
	 * @return
	 */
    @Override
	public List<SysUser> getUser(){
		SysUser user = new SysUser();
    	return userMapper.selectUserList(user);
	}
	
	/**
	 * 
	 */
//	@Transactional	
    @Override
	public void inserUserTestA(){
		SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setRoleId(0L);
        sysUserRole.setUserId(100L);
        userRoleMapper.insertUserANDRole(sysUserRole); 
        
//        int i = 3/0;
        
//        ExecutorService es = Executors.newCachedThreadPool();
//        UserJobCallableThread ct = new UserJobCallableThread();
//        FutureTask<String> f = new FutureTask<String>(ct);
//        es.submit(f);
//        es.shutdown();    
//        
//        try {
//			String str = f.get();
//			System.out.println("job 结束" + str);
//		} catch (Exception e) {
//			
//		}
//        UserJobThread thread = new UserJobThread();
//        thread.start();
        /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
			Date date1 = simpleDateFormat.parse("2019-123123");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			throw new RuntimeException(); 
		}
        sysUserRole.setRoleId(0L);
        sysUserRole.setUserId(200L);
        userRoleMapper.insertUserANDRole(sysUserRole);    */
        System.out.println("A 结束");
	}

	/**
	 * 
	 */
//	@Transactional
	@Async
	@Override
	public void inserUserTestB() {
		SysUserRole sysUserRole = new SysUserRole();
		sysUserRole.setRoleId(0L);
		sysUserRole.setUserId(101L);
		userRoleMapper.insertUserANDRole(sysUserRole);
		this.inserUserTestA();
	}

	/**
	 * 
	 */
	@Override
	@Transactional
	public void inserUserTestC() {
		SysUserRole sysUserRole = new SysUserRole();
		sysUserRole.setRoleId(0L);
		sysUserRole.setUserId(102L);
		userRoleMapper.insertUserANDRole(sysUserRole);
		System.out.println("C 插入结束");

		sysUserRole.setUserId(103L);
		configService.insertUserANDRole(sysUserRole);

//        int i = 3/0;

//        sysUserRole.setUserId(104L);
//        configService.insertUserANDRole(sysUserRole);   
//        
//        sysUserRole.setUserId(105L);
//        configService.insertUserANDRole(sysUserRole);   
		System.out.println("C 方法结束");
	}

}
