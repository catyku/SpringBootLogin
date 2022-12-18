package com.creations.turnkey.mrpeos.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.creations.turnkey.mrpeos.constans.Constants;
import com.creations.turnkey.mrpeos.exception.ValidateCodeException;
import com.creations.turnkey.mrpeos.model.CheckCode;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@Component
public class ImageCodeValidateFilter extends OncePerRequestFilter {

    private String codeParamter = "imageCode"; // 前端输入的图形验证码参数名

    @Autowired
    private CustomAuthenticationFailureHandler authenticationFailureHandler; // 自定义认证失败处理器

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 非 POST 方式的表单提交请求不校验图形验证码
        if ("/login".equals(request.getRequestURI()) & "POST".equals(request.getMethod())) {
            try {
                // 校验图形验证码合法性
                validate(request);
            } catch (ValidateCodeException e) {
                // 手动捕获图形验证码校验过程抛出的异常，将其传给失败处理器进行处理
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }

        // 放行请求，进入下一个过滤器
        filterChain.doFilter(request, response);
    }

    // 判断验证码的合法性
    private void validate(HttpServletRequest request) {

      


        // 获取用户传入的图形验证码值
        String requestCode = request.getParameter(this.codeParamter);
        if (requestCode == null) {
            requestCode = "";
        }
        requestCode = requestCode.trim();

        // 获取 Session
        HttpSession session = request.getSession();
        // 获取存储在 Session 里的验证码值
        CheckCode savedCode = (CheckCode) session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (savedCode != null) {
            // 随手清除验证码，无论是失败，还是成功。客户端应在登录失败时刷新验证码
            session.removeAttribute(Constants.KAPTCHA_SESSION_KEY);
        }

        // 校验出错，抛出异常
        if (StringUtils.isBlank(requestCode)) {
            throw new ValidateCodeException("驗證碼的值不可為空白");
        }

        if (savedCode == null) {
            throw new ValidateCodeException("驗證碼不存在");
        }

        if (savedCode.isExpried()) {
            throw new ValidateCodeException("驗證碼過期");
        }

        if (!requestCode.equalsIgnoreCase(savedCode.getCode())) {
            throw new ValidateCodeException("驗證碼錯誤");
        }
    }

    
}
