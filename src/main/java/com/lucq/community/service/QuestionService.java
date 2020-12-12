package com.lucq.community.service;

import com.lucq.community.dto.PaginationDTO;
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

    public PaginationDTO List(Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.count();
        paginationDTO.setPagination(totalCount, page, size);

        if (page > paginationDTO.getTotalPage()) {
            page = paginationDTO.getTotalPage();
        }
        if (page < 1) {
            page = 1;
        }
        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.List(offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            //此处其实可以用多表查询来代替，就不用封装得这么麻烦
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //将question对象所有属性拷贝到questionDTO中
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestionDTOS(questionDTOList);
        return paginationDTO;
    }

    public PaginationDTO listByUserId(Integer userId, Integer page, Integer size) {

        PaginationDTO paginationDTO = new PaginationDTO();
        //根据id获取用户问题数
        Integer totalCount = questionMapper.countByUserId(userId);
        paginationDTO.setPagination(totalCount, page, size);

        if (page > paginationDTO.getTotalPage()) {
            page = paginationDTO.getTotalPage();
        }
        if (page < 1) {
            page = 1;
        }

        Integer offset = size * (page - 1);
        //获取该用户所提出的问题
        List<Question> questions = questionMapper.listByUserId(userId, offset, size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question question : questions) {
            //此处其实可以用多表查询来代替，就不用封装得这么麻烦
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //将question对象所有属性拷贝到questionDTO中
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestionDTOS(questionDTOList);
        return paginationDTO;
    }
}
