package com.third.IntelPlat.rest;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.third.IntelPlat.common.MediaTypes;
import com.third.IntelPlat.exception.RestException;
import com.third.IntelPlat.exception.ServiceExceptionHandle;
import com.third.IntelPlat.service.QuestionTypeService;

@RestController
@RequestMapping(value = "/rest")
public class QuestionTypeManager 
{
	private static Logger logger = LoggerFactory.getLogger(QuestionTypeManager.class);
	
	@Autowired
	private QuestionTypeService questionTypeService;
	
	@Autowired
	private ServiceExceptionHandle serviceExceptionHandle;

	/**
	 * 增加知识点信息
	 * @param input
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/questionType", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
	public Result saveQuestionType(@RequestBody Map<String, Object> input) 
	{
		if ( !input.containsKey("questionName") || !input.containsKey("questionLevel") || !input.containsKey("gradeId")) 
		{
			throw new RestException("000001");
		}
		
		Object o1 = input.get("questionName");
		Object o2 = input.get("questionLevel");
		Object o3 = input.get("gradeId");
		
		if ( ! (o1 instanceof String) || ! (o2 instanceof Integer) || ! (o3 instanceof Integer)) 
		{
			throw new RestException("000002");
		}
		
		String questionName = (String)o1;
		Integer questionLevel = (Integer)o2;
		Integer gradeId = (Integer)o3;
		Integer parentId = null;
		
		if(input.containsKey("parentId") && (input.get("parentId") instanceof Integer))
		{
			parentId = (Integer)input.get("parentId");
		}
		
		questionTypeService.saveQuestionType(questionName, questionLevel, parentId, gradeId);
		
		Result result = new Result();
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("700000");
		result.setInfo(serviceExceptionHandle.genInfoById("700000"));
		logger.info(result.getInfo());
		return result;
	}
	
}
