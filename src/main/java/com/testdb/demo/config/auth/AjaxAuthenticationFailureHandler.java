package com.testdb.demo.config.auth;

import com.alibaba.fastjson.JSON;
import com.testdb.demo.utils.response.Result;
import com.testdb.demo.utils.response.ResultStatus;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AjaxAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    @SneakyThrows
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        AuthenticationException e){

        httpServletResponse.setHeader("Content-Type", MediaType.APPLICATION_JSON.toString());
        httpServletResponse.getWriter().write(JSON.toJSONString(Result.failure(ResultStatus.WRONG_PARAMETERS
        .setMessage("用户名或密码错误！"))));

    }

}
