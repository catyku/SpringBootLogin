/*
 * Copyright 2020 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 */

package com.izero.eric.login.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.Principal;

import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.izero.eric.login.constans.Constants;
import com.izero.eric.login.model.CheckCode;

import jakarta.servlet.http.HttpSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.http.server.reactive.S;

@Controller
@Slf4j
@AllArgsConstructor
public class RootController {

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @GetMapping(value = { "/index", "/" })
    public String index(Authentication  authentication) {
        //System.out.println("123333333");
        UserModel userDetails = (UserModel)authentication.getPrincipal();
        String user = userDetails.getName();
        log.info("登入的使用者:"+user);
        return "index";
    }
    
    
    @GetMapping(value = {"/login"})
    public String Login() {
        return "login";
    }

    @GetMapping(value = { "/logout" })
    public String Logout() {
        return "logout";
    }


    /**
     * 認證碼image
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/code/image", method = RequestMethod.GET, produces = "image/jpg")
    public @ResponseBody byte[] imageCode(HttpSession session) throws IOException {
        // 创建验证码文本
        String capText = defaultKaptcha.createText();
        // 创建验证码图片
        BufferedImage image = defaultKaptcha.createImage(capText);

        // 将验证码文本放进 Session 中
        CheckCode code = new CheckCode(capText);
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, code);
        //log.info("-----------------"+image.getWidth());

        try {
    
            // Create a byte array output stream.
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
    
            // Write to output stream
            ImageIO.write(image, "jpg", bao);
    
            return bao.toByteArray();
        } catch (IOException e) {
            log.error("",e);
            throw new RuntimeException(e);
        }


    }

    
}
