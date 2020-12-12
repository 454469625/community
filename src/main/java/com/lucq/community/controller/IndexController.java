package com.lucq.community.controller;

import com.lucq.community.dto.PaginationDTO;
import com.lucq.community.dto.QuestionDTO;
import com.lucq.community.mapper.QuestionMapper;
import com.lucq.community.mapper.UserMapper;
import com.lucq.community.model.Question;
import com.lucq.community.model.User;
import com.lucq.community.service.QuestionService;
import com.sun.xml.internal.ws.resources.HttpserverMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    UserMapper userMapper;



//    @Autowired
//    QuestionMapper questionMapper

    @Autowired
    QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page",defaultValue = "1") Integer page,
                        @RequestParam(name = "size",defaultValue = "3") Integer size) {

        //获取cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    //根据获取到的token查询数据库
                    User user = userMapper.findByToken(token);
                    if (user != null) {
                        //获取用户名，显示在index页面中
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }

        //要将question类装入QuestionDTO中，引入service层，对question进行包装
//        List<QuestionDTO> questionList = questionMapper.List();
        PaginationDTO pagination = questionService.List(page,size);
        model.addAttribute("pagination", pagination);
        return "index";
    }
}
