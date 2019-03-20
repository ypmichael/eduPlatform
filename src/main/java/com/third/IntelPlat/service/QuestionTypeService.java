package com.third.IntelPlat.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.exception.ServiceExceptionHandle;
import com.third.IntelPlat.repository.GradeRepository;
import com.third.IntelPlat.repository.QuestionTypeRepository;

@Component
public class QuestionTypeService 
{
	private static Logger logger = LoggerFactory.getLogger(QuestionTypeService.class);
	
	@Autowired
	private QuestionTypeRepository questionTypeRepository;
	
	@Autowired
	private GradeRepository gradeRepository;
	
	@Autowired
	private ServiceExceptionHandle serviceExceptionHandle;

	/**
	 * 增加知识点信息
	 * @param questionName
	 * @param questionLevel
	 * @param parentId
	 * @param gradeId 
	 */
	public void saveQuestionType(String questionName, Integer questionLevel, Integer parentId, Integer gradeId) 
	{
	}

}
