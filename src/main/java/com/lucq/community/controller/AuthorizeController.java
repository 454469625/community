package com.lucq.community.controller;

import com.lucq.community.dto.AccessTokenDTO;
import com.lucq.community.dto.GithubUser;
import com.lucq.community.mapper.UserMapper;
import com.lucq.community.model.User;
import com.lucq.community.provider.GithubProvider;
import com.lucq.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@PropertySource({"classpath:application.properties"})
@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;


    @Autowired
    UserService userService;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletResponse response) throws Exception {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        //获取token
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        //获取github账户，用户名，id等
        GithubUser githubUser = githubProvider.getUserInfo(accessToken);
        //获取成功
        if (githubUser != null) {
            User user = new User();
            //生成一个新的token(账户标识)之后写入cookie中，用于查询数据库
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setAvatarUrl(githubUser.getAvatarUrl());
            //将账户写入数据库
            userService.createOrUpdate(user);
//            System.out.println(user);
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }else{
            return "index";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response,HttpServletRequest request){
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
