package com.third.IntelPlat.rest;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.third.IntelPlat.common.MediaTypes;
import com.third.IntelPlat.entity.GradeEntity;
import com.third.IntelPlat.exception.RestException;
import com.third.IntelPlat.exception.ServiceExceptionHandle;
import com.third.IntelPlat.service.GradeService;

@RestController
@RequestMapping(value = "/rest")
public class GradeManager 
{
	private static Logger logger = LoggerFactory.getLogger(GradeManager.class);
	
	@Autowired
	private ServiceExceptionHandle serviceExceptionHandle;
	
	@Autowired
	private GradeService gradeService;
	
	/**
	 * 新增年级信息
	 * @param input
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/grade", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
	public Result saveGrade(@RequestBody Map<String, Object> input) 
	{
		if ( !input.containsKey("gradeName")) {
			
			logger.error("insert grade info :: " + serviceExceptionHandle.genInfoById("000001"));
			throw new RestException("000001");
		}
		
		Object o1 = input.get("gradeName");//部门名称
		
		if (!(o1 instanceof String)) {

			logger.error("insert grade info :: " + serviceExceptionHandle.genInfoById("000002"));
			throw new RestException("000002");
		}

		String gradeName = (String) o1;
		
		gradeService.saveGrade(gradeName);
		
		Result result = new Result();
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("600002");
		result.setInfo(serviceExceptionHandle.genInfoById("600002"));
		return result;
	}
	
	/**
	 * 更新年级信息
	 * @param input
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/grade", method = RequestMethod.PUT, produces = MediaTypes.JSON_UTF_8)
	public Result updateGrade(@RequestBody Map<String, Object> input) 
	{
		if (!input.containsKey("uid")) {
			
			logger.error("update grade info :: " + serviceExceptionHandle.genInfoById("000001"));
			throw new RestException("000001");
		}
		
		Object o1 = input.get("uid");
		
		if (!(o1 instanceof Integer)) {

			logger.error("update grade info :: " + serviceExceptionHandle.genInfoById("000002"));
			throw new RestException("000002");
		}
		
		Integer uid = (Integer) o1;
		String gradeName = null;
		
		if(input.containsKey("gradeName") && (input.get("gradeName") instanceof String))
		{
			gradeName = (String) input.get("gradeName");
		}
		
		gradeService.updateGrade(uid, gradeName);
		
		Result result = new Result();
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("600004");
		result.setInfo(serviceExceptionHandle.genInfoById("600004"));
		return result;
	}
	
	/**
	 * 删除年级信息
	 * @param uid
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/grade/{uid}", method = RequestMethod.DELETE, produces = MediaTypes.JSON_UTF_8)
	public Result deleteGrade(@PathVariable("uid") Integer uid) 
	{
		gradeService.deleteGrade(uid);
		
		Result result = new Result();
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("600005");
		result.setInfo(serviceExceptionHandle.genInfoById("600005"));
		return result;
	}
	
	/**
	 * 查询所有年级信息
	 * @param uid
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/grades", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public Result findGrades() 
	{
		List<GradeEntity> grades = gradeService.findGrades();
		
		Result result = new Result();
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("600007");
		result.setContent(grades);
		result.setInfo(serviceExceptionHandle.genInfoById("600007"));
		return result;
		
	}

}
