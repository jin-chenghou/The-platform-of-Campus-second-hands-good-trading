package com.second.hand.trading.server.controller;


import com.second.hand.trading.server.utils.VerifyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 随机4位数的验证码
 */

@RestController
@CrossOrigin
@RequestMapping("/code")
public class VerifyController {


    @RequestMapping("/verify")
    public void Verify(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        VerifyCode code = new VerifyCode();
        BufferedImage image = code.createImage();

        //验证码
        System.err.println(code.getText());
        //保存验证码到Redis,一分钟有效期
        session.setAttribute("verify:"+code.getText().toLowerCase(),code.getText());
        session.setMaxInactiveInterval(60);
        //验证码图片格式
        ImageIO.write(image,"jpg",response.getOutputStream());

    }


}
