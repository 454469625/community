package com.lucq.community.service;

import com.lucq.community.dto.QuestionDTO;
import com.lucq.community.mapper.QuestionMapper;
import com.lucq.community.mapper.UserMapper;
import com.lucq.community.model.Question;
import com.lucq.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;

    public List<QuestionDTO> List() {
        List<Question> questions = questionMapper.List();
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for(Question question : questions){
            //此处其实可以用多表查询来代替，就不用封装得这么麻烦
            System.out.println("creator = "+question.getCreator());
            User user = userMapper.findById(question.getCreator());
            System.out.println(user);
            QuestionDTO questionDTO = new QuestionDTO();
            //将question对象所有属性拷贝到questionDTO中
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }
}
