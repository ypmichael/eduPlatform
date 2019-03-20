package com.third.IntelPlat.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.third.IntelPlat.common.MediaTypes;
import com.third.IntelPlat.exception.RestException;
import com.third.IntelPlat.service.MarkRecordService;
import com.third.IntelPlat.service.MultiplicationService;

@RestController
@RequestMapping(value = "/rest")
public class MultiplicationManager 
{
	@Autowired
	private MultiplicationService multiplicationService;
	@Autowired
	private MarkRecordService markRecordService;
	
	/**
	 * 获取题目
	 * 
	 * map.put("sequence", score); 题目编号
		map.put("content", a); 题目
		
		或者
	multip.put("score",score); 分数
		
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/multiplication", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public Result getTitle() 
	{
		Map<Object,Object> titleMap = multiplicationService.getTitle();
		Result result = new Result();
		result.setStatus(result.STATUS_SUCCESS);
		result.setInfo("获取题目成功");
		result.setContent(titleMap);
		return result;
	}
	/**
	 * 提交答案
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/multiplication", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
	public Result saveAnswers(@RequestBody Map<String, Object> input) 
	{
		
		Result result = new Result();
		
		if ( !input.containsKey("sequence") || !input.containsKey("answer")) {
			
			throw new RestException("000001");
		}
		
		Object o1 = input.get("sequence");//部门名称
		Object o2 = input.get("answer");//父部门编号
		
		if (!(o1 instanceof Integer) || !(o2 instanceof String)) {

			throw new RestException("000002");
		}
		
		Integer sequence = (Integer)o1;
		String answer = (String)o2;
		
		markRecordService.addStudentAnswerToRedis(sequence, answer);
		
		result.setStatus(result.STATUS_SUCCESS);
		result.setInfo("乘法口诀提交成功");
		return result;
	}
}
