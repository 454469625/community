package com.lucq.community.controller;

import com.lucq.community.mapper.UserMapper;
import com.lucq.community.model.User;
import com.sun.xml.internal.ws.resources.HttpserverMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    UserMapper userMapper;

    @GetMapping("/")
    public String index(HttpServletRequest request) {

        //获取cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    //根据获取到的token查询数据库
                    User user = userMapper.findByToken(token);
                    if (user != null) {
                        System.out.println(user);
                        //获取用户名，显示在index页面中
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }
        
        return "index";
    }
}
