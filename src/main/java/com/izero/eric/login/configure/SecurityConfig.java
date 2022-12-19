package com.izero.eric.login.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import com.izero.eric.login.security.ImageCodeValidateFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
        @Autowired
        private ImageCodeValidateFilter imageCodeValidateFilter; // 自定义过滤器（图形验证码校验）
        @Autowired
        @Qualifier("myUsersDetailService")
        private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /*
         * .authorizeRequests() // 定義哪些url需要被保護
         * .antMatchers("/user").hasRole("USER")// 定義匹配到"/user"底下的需要有USER的這個角色才能進去
         * .antMatchers("/admin").hasRole("ADMIN") // 定義匹配到"/admin"底下的需要有ADMIN的這個角色才能進去
         */
        /**
         * 
         */
        http.authenticationProvider(authenticationProvider());
        http.authorizeHttpRequests().requestMatchers(
                new OrRequestMatcher(new AntPathRequestMatcher("/login")
                ,new AntPathRequestMatcher("/code/image"),
                new AntPathRequestMatcher("/logout"),
                //new AntPathRequestMatcher("/index"),
                new AntPathRequestMatcher("/perform_login")))
                        .permitAll()
                        .and().
                        authorizeHttpRequests().anyRequest().authenticated().
                        and().
                formLogin()
                        .loginPage("/login").defaultSuccessUrl("/index",true)
                        .and().
                        rememberMe().rememberMeParameter("rememberme").key("12834843").tokenValiditySeconds(24*60*60).userDetailsService(userDetailsService)
                //http.logout((logout) -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout"));
                .and().logout().logoutUrl("/perform_logout").logoutSuccessUrl("/login?logout")
                /*http.csrf().csrfTokenRepository(new HttpSessionCsrfTokenRepository()).requireCsrfProtectionMatcher(httpServletReuqest->{
                        //POST非post等，其它放行
                        boolean notPost = StringUtils.equalsIgnoreCase(httpServletReuqest.getMethod(),"POST");
                        if(notPost)
                        {
                                return false;
                        }
                        for(String crsf : dgbSecurityProperties.getBrowser().getCsrf()){
                                if(pathMatcher.match(crsf, httpServletReuqest.getRequestURI())){
                                        return false;
                                }
                        }
                        return true;

                });*/
                
                .and().addFilterBefore(imageCodeValidateFilter, UsernamePasswordAuthenticationFilter.class);
                //http.httpBasic();
        return http.build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
            return authConfiguration.getAuthenticationManager();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

            authProvider.setUserDetailsService(userDetailsService);
            authProvider.setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());

            return authProvider;
    }
    /*@Bean
    public UserDetailsService userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails user = User
                .withUsername("username")
                .password(encoder.encode(
                        "password"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }*/
   
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
            return (web) -> web.ignoring().requestMatchers("/css/**", "/images/**", "/js/**", "/assets/**");
    }
    

    /**
     * 在代碼中配置一個bean，這個bean指定國際化資源文件
     * 
     * @return
     */
    /*@Bean
    public ReloadableResourceBundleMessageSource messageSource() {
            ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
            reloadableResourceBundleMessageSource.setBasename("classpath:messages");
            reloadableResourceBundleMessageSource.setDefaultEncoding("UTF-8");
            return reloadableResourceBundleMessageSource;
    }*/
}