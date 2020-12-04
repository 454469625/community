package com.lucq.community.provider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lucq.community.dto.AccessTokenDTO;
import com.lucq.community.dto.GithubUser;
import okhttp3.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
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
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        System.out.println("获取token");
        try (Response response = client.newCall(request).execute()) {
            String token = response.body().string();
            System.out.println("token = "+token);
            return token;
        }
    }

    public GithubUser getUserInfo(String token) throws IOException {
        System.out.println("getUserInfo中token="+token);
        JSONObject jsonObject = JSON.parseObject(token);
        String stringToken = (String) jsonObject.get("access_token");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .header("Authorization", "token " + stringToken)
                .url("https://api.github.com/user")
                .build();
        System.out.println("request = "+request);
        try (Response response = client.newCall(request).execute()) {
            String userInfo = response.body().string();
            JSONObject userInfoObj = JSON.parseObject(userInfo);
            GithubUser gitHubUserDTO = new GithubUser();
            gitHubUserDTO.setId(((Number) userInfoObj.get("id")).longValue());
            gitHubUserDTO.setName((String) userInfoObj.get("name"));
            gitHubUserDTO.setAvatarUrl((String) userInfoObj.get("avatar_url"));
            return gitHubUserDTO;
        }
    }

    public GithubUser getUser(String accessToken) throws Exception {
        System.out.println("getUser中token="+accessToken);
        int timeout = 60;
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(timeout * 1000)
                .setConnectTimeout(timeout * 1000)
                .setConnectionRequestTimeout(timeout * 1000)
                .build();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet("https://api.github.com/user");
        httpget.addHeader(new BasicHeader("Authorization", "token "+accessToken));
        httpget.setProtocolVersion(HttpVersion.HTTP_1_0);
        httpget.addHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
        httpget.setConfig(defaultRequestConfig);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            GithubUser user = JSON.parseObject(String.valueOf(entity.getContent()),GithubUser.class);
            EntityUtils.consume(entity);
            response.close();
            return  user;
        } catch (Exception e){
            throw e;
        } finally{
            if(response != null){
                response.close();
            }
            if(httpclient!=null){
                httpclient.close();
            }
        }
    }
}