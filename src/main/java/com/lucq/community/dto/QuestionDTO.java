package com.lucq.community.dto;

import com.lucq.community.model.User;
import lombok.Data;

@Data
public class QuestionDTO {
    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer likeCount;
    private Integer commentCount;
    private Integer viewCount;
    private User user;
}
