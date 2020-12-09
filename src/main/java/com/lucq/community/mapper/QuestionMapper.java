package com.lucq.community.mapper;

import com.lucq.community.model.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QuestionMapper {

    void create(Question question);

    List<Question> List(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    Integer count();
}
