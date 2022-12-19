package com.izero.eric.login.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.izero.eric.login.model.UserModel;
import com.izero.eric.login.service.MyUsersDetailService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private MyUsersDetailService userDetailsService;

    
    

    @Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String account = authentication.getName();
		String password = String.valueOf(authentication.getCredentials());
		
  
        	/*UserModel memberAccount = new UserModel();
        	memberAccount.setId(account);
        	memberAccount.setPwd(password);
            log.info("來了!!!"+password);
        	memberAccount = userDetailsService.login(memberAccount);*/
            UserModel memberAccount = userDetailsService.loadUserByUsername(account);
        	if(memberAccount.getId()==null)        	
                throw new UsernameNotFoundException("用戶不存在!");
        	Collection<? extends GrantedAuthority> authorities = memberAccount.getAuthorities();
            // 密碼加密
            //String encoderPassword = passwordEncoder.encode(password);
            //log.info("登入密碼比對...."+"輸入密碼:"+password +"----加密後:"+encoderPassword + ";"+memberAccount.getPassword());
            //log.info(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password));
            // 比對密碼
            if (!memberAccount.getPassword().equals(password)) {
                throw new BadCredentialsException("帳號或密碼不正確!");
            }


            
    		return new UsernamePasswordAuthenticationToken(memberAccount, password,authorities);	
        
	}

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
