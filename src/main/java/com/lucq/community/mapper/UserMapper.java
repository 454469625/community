package com.lucq.community.mapper;

import com.lucq.community.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void insertUser(User user);

    User findByToken(String token);

    User findById(Integer id);

    User findByAccountId(String accountId);

    void update(User dbUser);
}
