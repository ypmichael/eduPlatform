package com.third.IntelPlat.rest;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.third.IntelPlat.common.ExcelHeader;
import com.third.IntelPlat.common.MediaTypes;
import com.third.IntelPlat.common.OperationLog;
import com.third.IntelPlat.entity.ChoiceQstEntity;
import com.third.IntelPlat.entity.GradeEntity;
import com.third.IntelPlat.exception.RestException;
import com.third.IntelPlat.exception.ServiceExceptionHandle;
import com.third.IntelPlat.service.ChoiceQstService;

@RestController
@RequestMapping(value = "/rest")
public class ChoiceQstManager 
{
	private static Logger logger = LoggerFactory.getLogger(ChoiceQstManager.class);
	
	@Autowired
	private ChoiceQstService choiceQstService;
	
	@Autowired
	private ServiceExceptionHandle serviceExceptionHandle;

	/**
	 * 题目入库
	 * @param request
	 * @param response
	 * @return
	 */
	@OperationLog(operation = "题目入库", type = "批量导入", model = "题库模块")
	@CrossOrigin
	@RequestMapping(value="/qst/import", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
	public Result choiceQstImport(HttpServletRequest request,HttpServletResponse response) 
	{
		Result result = new Result();
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartRequest.getFile("file");
		
		 //取得当前上传文件的文件名称  
        String fileName = file.getOriginalFilename();  
        
        if(fileName != null && !"".equals(fileName))
        {
        	if(!fileName.endsWith(ExcelHeader.FILE_TYPE_XLSX) && !fileName.endsWith(ExcelHeader.FILE_TYPE_XLS))
        	{
				throw new RestException("文件格式错误");
        	}
        }
        
        List<Map<String, Object>> list = null;
        
        try {
        	list = choiceQstService.choiceQstImport(file.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("700000");
		result.setInfo(serviceExceptionHandle.genInfoById("700000"));
		logger.info(result.getInfo());
		return result;
	}
	
	
	@CrossOrigin
	@RequestMapping(value="/qsts", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public void testGrade() 
	{
		choiceQstService.testChoiceQstImport(null);
		
	}
	
	/**
	 * 获取题目
	 * @return
	 */
	@OperationLog(operation = "获取题目", type = "查询", model = "题库模块")
	@CrossOrigin
	@RequestMapping(value="/find/qst", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public Result findQst() 
	{
		Result result = new Result();
		
		ChoiceQstEntity choiceQst = choiceQstService.findQst();
		
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("800000");
		result.setContent(choiceQst);
		result.setInfo(serviceExceptionHandle.genInfoById("800000"));
		logger.info(result.getInfo());
		return result;
	}
	
	/**
	 * 答案提交
	 * @param input
	 * @return
	 */
	@OperationLog(operation = "答案提交", type = "新增", model = "题库模块")
	@CrossOrigin
	@RequestMapping(value="/submit/qst", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
	public Result submitQst(@RequestBody Map<String, Object> input) 
	{
		if ( !input.containsKey("choiceContent") 
				|| !input.containsKey("result") || !input.containsKey("answ")
				|| !input.containsKey("studentOptAnsw") || !input.containsKey("duration")
				|| !input.containsKey("startTime")|| !input.containsKey("endTime") || !input.containsKey("questionNo")) 
		{
			throw new RestException("000001");
		}
		
		Object o1 = input.get("tutorialUid");
		Object o2 = input.get("choiceContent");
		Object o3 = input.get("picture");
		Object o4 = input.get("radio");
		Object o5 = input.get("result");
		Object o6 = input.get("answ");
		Object o7 = input.get("studentOptAnsw");
		Object o8 = input.get("duration");
		Object o9 = input.get("startTime");
		Object o10 = input.get("endTime");
		Object o11 = input.get("questionNo");
		Object o12 = input.get("questionType");
		
		Integer tutorialUid = o1 == null ? null : (Integer) o1;
		String choiceContent = (String) o2;
		String picture = o3 != null ? (String) o3 : null;
		String radio = o4 != null ? (String) o4 : null;
		Integer result = (Integer) o5;
		String answ = (String) o6;
		String studentOptAnsw = (String) o7;
		String duration = o8 == null ? null : (String) o8;
		String startTime = o9 == null ? null : (String) o9;
		String endTime = o10 == null ? null : (String) o10;
		Integer questionNo = (Integer) o11;
		String questionType = o12 == null ? null : (String) o12;
		
		choiceQstService.submitQst(tutorialUid, choiceContent, picture, radio, result, answ, studentOptAnsw, duration, startTime, endTime, questionNo, questionType);
		
		Result res = new Result();
		
		res.setStatus(Result.STATUS_SUCCESS);
		res.setCode("800000");
		res.setInfo(serviceExceptionHandle.genInfoById("800001"));
		logger.info(res.getInfo());
		return res;
	}
	
	/**
	 * 查询题库信息
	 * @return
	 */
	@OperationLog(operation = "查询题库信息", type = "查询", model = "题库模块")
	@CrossOrigin
	@RequestMapping(value="/query/qsts", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public Result queryQsts() 
	{
		List<GradeEntity> grades = choiceQstService.queryQsts();
		
		Result res = new Result();
		res.setStatus(Result.STATUS_SUCCESS);
		res.setCode("800002");
		res.setContent(grades);
		res.setInfo(serviceExceptionHandle.genInfoById("800002"));
		logger.info(res.getInfo());
		return res;
	}
	
	/**
	 * 获取学生正确答题数目
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/query/correctAnswer", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public Result queryCorrectAnswer() 
	{
		Integer correctAnswerNum = choiceQstService.queryCorrectAnswer();
		
		Result res = new Result();
		res.setStatus(Result.STATUS_SUCCESS);
		res.setContent(correctAnswerNum);
		res.setInfo("获取学生正确答题数据成功");
		logger.info(res.getInfo());
		return res;
	}
	
	/**
	 * 清除学生分数
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/delete/studentScore", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
	public Result deleteStudentScore(@RequestBody Map<String, Object> input) 
	{
		List<String> userNames = (List)input.get("users");
		
		choiceQstService.deleteStudentScore(userNames);
		
		Result res = new Result();
		res.setStatus(Result.STATUS_SUCCESS);
		res.setInfo("清除学生分数成功");
		logger.info(res.getInfo());
		return res;
	}
	
}
