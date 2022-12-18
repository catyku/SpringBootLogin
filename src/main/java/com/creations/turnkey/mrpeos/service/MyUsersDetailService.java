package com.creations.turnkey.mrpeos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MyUsersDetailService implements UserDetailsService {



    
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        // 權限(固定用法,role是還沒用到所以拿來暫時先隨便亂取）
        List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("role");
        // return 用戶名稱,密碼,用戶角色（權限），用戶角色不可以為Null!
        

        /*Optional<User> userInfoOpt = Optional.ofNullable(new User("username", PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("abc123"), auths));
        User user = userInfoOpt.orElseThrow(() -> new UsernameNotFoundException("帳號或密碼錯誤!"));
        log.info("根據使用者名稱:{}查詢使用者成功", user.getUsername());
        */
        log.debug("password encode :"+PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("abc123"));
        return new User("username", PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("abc123"), auths);

       
    }
}
