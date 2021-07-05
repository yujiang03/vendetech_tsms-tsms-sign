package com.vendetech.framework.security.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.vendetech.common.constant.Constants;
import com.vendetech.common.exception.CustomException;
import com.vendetech.common.exception.user.CaptchaException;
import com.vendetech.common.exception.user.CaptchaExpireException;
import com.vendetech.common.exception.user.UserPasswordNotMatchException;
import com.vendetech.common.utils.MessageUtils;
import com.vendetech.framework.manager.AsyncManager;
import com.vendetech.framework.manager.factory.AsyncFactory;
import com.vendetech.framework.redis.RedisCache;
import com.vendetech.framework.security.LoginUser;
import com.vendetech.project.system.domain.SysUser;
import com.vendetech.project.system.service.ISysUserService;

/**
 * 登录校验方法
 *
 * @author vendetech
 */
@Component
public class SysLoginService
{
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;
    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param captcha 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid)
    {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        int count = userService.getErrorCount(username);
        if(count == 5){
	        if (captcha == null)
	        {
	            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
	            throw new CaptchaExpireException();
	        }
	        if (!code.equalsIgnoreCase(captcha))
	        {
	            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
	            throw new CaptchaException();
	        }
        }
        // 用户验证
        Authentication authentication = null;
        try
        {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("username", username);
				if(count < 3){
				  count++;
				}
				param.put("count", count);
				userService.updateErrorCount(param);
				Integer[] countArray = {count};
                throw new UserPasswordNotMatchException(countArray);
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new CustomException(e.getMessage());
            }
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Map<String, Object> param = new HashMap<String, Object>();
	    param.put("username", username);
	    param.put("count", 0);
	    userService.updateErrorCount(param);
        // 生成token
        return tokenService.createToken(loginUser);
    }

	/**
	 * @param username
	 * @return
	 */
	public String loginByDingtalkScan(String username) {

		SysUser user = new SysUser();
        try
        {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
        	user = userService.selectUserByUserName(username);
        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                Integer[] countArray = {0};
                throw new UserPasswordNotMatchException(countArray);
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new CustomException(e.getMessage());
            }
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = new LoginUser(user, permissionService.getMenuPermission(user));
        // 生成token
        return tokenService.createToken(loginUser);

	}
}
