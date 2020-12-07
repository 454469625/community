package com.lucq.community.mapper;

import com.lucq.community.model.Question;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QuestionMapper {

    void create(Question question);

    List<Question> List();
}
