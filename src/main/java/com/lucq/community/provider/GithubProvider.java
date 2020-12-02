package com.lucq.community.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lucq.community.dto.AccessTokenDTO;
import com.lucq.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
/*
@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON.toJSONString(accessTokenDTO),mediaType,);
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token?client_id=d94836f5caf2ffb764c1&client_secret=d79b2947d5e1ed6c1c0c14841df793f4394a438f&code=\"+accessTokenDTO.getCode()+\"&redirect_uri=http://localhost:8080/callback&state=1")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            System.out.println("string0 = "+string);
            String token = string.split("&")[0].split("=")[1];
            System.out.println("token = "+token);
            return token;

//            return string;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GithubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .header("Authorization","token "+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            System.out.println("string1 = "+string);
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
*/
@Component
public class GithubProvider {

    public static final MediaType mediaType = MediaType.get("application/json; charset=utf-8");
    public static final String logUrl = "https://github.com/login/oauth/access_token";
    public static final String userUrl = "https://api.github.com/user";


    OkHttpClient client = new OkHttpClient();

    public String getAccessToken(AccessTokenDTO accessTokenDTO) throws IOException {

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .url(logUrl)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String token = response.body().string();
            return token;
        }
    }

    public GithubUser getUserInfo(String token) throws IOException {
        JSONObject jsonObject = JSON.parseObject(token);
        String stringToken = (String) jsonObject.get("access_token");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .header("Authorization", "token " + stringToken)
                .url(userUrl)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String userInfo = response.body().string();
            JSONObject userInfoObj = JSON.parseObject(userInfo);
            GithubUser gitHubUserDTO = new GithubUser();
            gitHubUserDTO.setId(((Number) userInfoObj.get("id")).longValue());
            gitHubUserDTO.setName((String) userInfoObj.get("name"));
            gitHubUserDTO.setBio((String) userInfoObj.get("bio"));
            gitHubUserDTO.setAvatarUrl((String) userInfoObj.get("avatar_url"));
            return gitHubUserDTO;
        }
    }
}