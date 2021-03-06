package com.testdb.demo.service.user;

import com.testdb.demo.mapper.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@CacheConfig(cacheNames = "UserService")
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * 仅用于认证，其他用户业务写在UserService
     * @Autuor: 刘镇
     */

    @Autowired
    private UserMapper um;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = null;

        try {
             user = um.selectUserAndRoles(username);
        } catch (Exception e) {
            throw e;
        }
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }

}
