package com.vendetech.framework.security.handle;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.vendetech.common.utils.http.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import com.alibaba.fastjson.JSON;
import com.vendetech.common.constant.Constants;
import com.vendetech.common.constant.HttpStatus;
import com.vendetech.common.utils.ServletUtils;
import com.vendetech.common.utils.StringUtils;
import com.vendetech.framework.manager.AsyncManager;
import com.vendetech.framework.manager.factory.AsyncFactory;
import com.vendetech.framework.security.LoginUser;
import com.vendetech.framework.security.service.TokenService;
import com.vendetech.framework.web.domain.AjaxResult;

/**
 * 自定义退出处理类 返回成功
 *
 * @author vendetech
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    @Autowired
    private TokenService tokenService;
    @Value("${portal.host.api}")
    String portalHostApi;

    /**
     * 退出处理
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        // LoginUser loginUser = tokenService.getLoginUser(request);
        // if (StringUtils.isNotNull(loginUser)) {
        //     String userName = loginUser.getUsername();
        //     // 删除用户缓存记录
        //     tokenService.delLoginUser(loginUser.getToken());
        //     // 记录用户退出日志
        //     AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT, "退出成功"));
        // }

        // String token = request.getHeader("Authorization");
        String ret = HttpUtils.sendGet(portalHostApi + "/api/loginUrl", "", null);
        JSONObject retJson = JSONObject.parseObject(ret);

        int code;
        String msg;
        if ("200".equals(retJson.getString("code"))) {
            code = HttpStatus.SUCCESS;
            msg = retJson.getString("msg");
        } else {
            code = HttpStatus.ERROR;
            msg = "网络访问错误，请稍后重试";
        }
        ServletUtils.renderString(request, response, JSON.toJSONString(AjaxResult.error(code, msg)));
    }
}
