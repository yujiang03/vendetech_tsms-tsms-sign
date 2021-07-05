package com.vendetech.framework.security.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.vendetech.common.constant.HttpStatus;
import com.vendetech.common.utils.http.HttpUtils;
import com.vendetech.project.system.domain.SysUser;
import com.vendetech.project.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.vendetech.common.utils.SecurityUtils;
import com.vendetech.common.utils.StringUtils;
import com.vendetech.framework.security.LoginUser;
import com.vendetech.framework.security.service.TokenService;

/**
 * token过滤器 验证token有效性
 *
 * @author vendetech
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ISysUserService userService;


    @Value("${portal.host.api}")
    String portalHostApi;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String token = request.getHeader("Authorization");
        String ret = HttpUtils.sendGet(portalHostApi + "/api/isLoggedIn", "", token);
        JSONObject retJson = JSONObject.parseObject(ret);

        if("200".equals(retJson.getString("code"))) {
            JSONObject loginUserData = retJson.getJSONObject("data");
            LoginUser loginUser = new LoginUser();

            if (StringUtils.isNotNull(loginUserData) && StringUtils.isNull(SecurityUtils.getAuthentication())) {
                // tokenService.verifyToken(loginUserData);
                JSONObject sysUserData = loginUserData.getJSONObject("user");

                SysUser sysUser = userService.selectUserByUserName(sysUserData.getString("userName"));

                // sysUser.setUserId(sysUserData.getLong("userId"));
                // sysUser.setAvatar(sysUserData.getString("avatar"));
                // sysUser.setUserName(sysUserData.getString("userName"));
                // sysUser.setPassword(sysUserData.getString("password"));
                // sysUser.setNickName(sysUserData.getString("nickName"));

                loginUser.setUser(sysUser);
                loginUser.setToken(loginUserData.getString("token"));

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(loginUser, null, null);

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        chain.doFilter(request, response);
    }

    // @Override
    // protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    //         throws ServletException, IOException {
    //     LoginUser loginUser = tokenService.getLoginUser(request);
    //     if (StringUtils.isNotNull(loginUser) && StringUtils.isNull(SecurityUtils.getAuthentication())) {
    //         tokenService.verifyToken(loginUser);
    //         UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
    //         authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    //         SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    //     }
    //     chain.doFilter(request, response);
    // }

}
