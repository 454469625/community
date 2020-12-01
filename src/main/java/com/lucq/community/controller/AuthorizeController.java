package com.lucq.community.controller;

import com.lucq.community.dto.AccessTokenDTO;
import com.lucq.community.dto.GithubUser;
import com.lucq.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri("http://localhost:8080/callback");
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id("6b25d6489c2c90816920");
        accessTokenDTO.setClient_secret("b6c0cd9435a77f1e66ab9c2a2b1085741c28f27d ");
//        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        githubProvider.getAccessToken(accessTokenDTO);
//        GithubUser user = githubProvider.getUser(accessToken);
//        System.out.println(user.getName());
        return "index";
    }
}
