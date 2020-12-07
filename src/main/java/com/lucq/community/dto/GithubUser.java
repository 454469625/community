package com.lucq.community.dto;

import lombok.Data;

@Data
public class GithubUser {
    private String name;
    private String bio;
    private long id;
    private String avatarUrl;
}
