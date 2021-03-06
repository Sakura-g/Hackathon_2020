package com.testdb.demo.service;

import com.testdb.demo.entity.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@Component("RbacAuthorityService")
public class RbacAuthorityService {

    @SuppressWarnings("unchecked")
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {

        Object userInfo = authentication.getPrincipal();
        boolean hasPermission = false;

        if (userInfo instanceof UserDetails) {

            //密码擦除
            User user = (User)userInfo;
            user.setPassword(null);

            // 禁用未认证账户
            if(!user.isEnabled()){ return false; }
            // 拦截密码更新用户
            if(user.getBeRefresh()) {return false;}

            //获取资源
            Set<String> urls = new HashSet();
            urls.add("/**"); // 这些 url 都是要登录后才能访问，且其他的 url 都不能访问！

            AntPathMatcher antPathMatcher = new AntPathMatcher();
            for (String url : urls) {
                if (antPathMatcher.match(url, request.getRequestURI())) {
                    hasPermission = true;
                    break;
                }
            }

            return hasPermission;
        } else {
            return false;
        }
    }

    public boolean test(Authentication authentication){
        System.out.println("23333");
        return false;
    }
}