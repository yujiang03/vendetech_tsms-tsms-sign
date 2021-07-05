package com.vendetech.framework.security.handle;

import java.io.IOException;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.vendetech.common.utils.http.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.vendetech.common.constant.HttpStatus;
import com.vendetech.common.utils.ServletUtils;
import com.vendetech.common.utils.StringUtils;
import com.vendetech.framework.web.domain.AjaxResult;

/**
 * 认证失败处理类 返回未授权
 *
 * @author vendetech
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -8970718410437077606L;

    @Value("${portal.host.api}")
    String portalHostApi;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {

        // String token = request.getHeader("Authorization");
        String ret = HttpUtils.sendGet(portalHostApi + "/api/loginUrl", "", null);
        JSONObject retJson = JSONObject.parseObject(ret);

        int code;
        String msg;
        if ("200".equals(retJson.getString("code"))) {
            code = HttpStatus.SEE_OTHER;
            msg = retJson.getString("msg");
        } else {
            code = HttpStatus.UNAUTHORIZED;
            msg = StringUtils.format("请求访问：{}，认证失败，无法访问系统资源", request.getRequestURI());
        }
        ServletUtils.renderString(request, response, JSON.toJSONString(AjaxResult.error(code, msg)));
    }

    // @Override
    // public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
    //         throws IOException
    // {
    //     int code = HttpStatus.UNAUTHORIZED;
    //     String msg = StringUtils.format("请求访问：{}，认证失败，无法访问系统资源", request.getRequestURI());
    //     ServletUtils.renderString(request,response, JSON.toJSONString(AjaxResult.error(code, msg)));
    // }
}
