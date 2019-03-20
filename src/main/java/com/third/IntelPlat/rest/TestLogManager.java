package com.third.IntelPlat.rest;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.third.IntelPlat.common.MediaTypes;
import com.third.IntelPlat.entity.TestLogEntity;
import com.third.IntelPlat.service.TestLogService;

@RestController
@RequestMapping(value = "/rest")
public class TestLogManager 
{
	private static Logger logger = LoggerFactory.getLogger(TestLogManager.class);
	
	@Autowired
	private TestLogService testLogService;
	
	/**
	 * 查询学生当前测试日志
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/query/qstLog", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public Result queryQstLog() 
	{
		Result result = new Result();
		
		List<TestLogEntity> testLog = testLogService.queryQstLog();
		
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("800000");
		result.setContent(testLog);
		result.setInfo("当前学生日志查询成功");
		logger.info(result.getInfo());
		return result;
	}
	
	
	/**
	 * 导出日志
	 * @return
	 * @throws FileNotFoundException 
	 */
	@CrossOrigin
	@RequestMapping(value="/export/log", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public Result exportTextLog(HttpServletRequest request) throws FileNotFoundException 
	{
		Result result = new Result();
		
		String url = testLogService.exportTextLog(request);
		
		result.setStatus(Result.STATUS_SUCCESS);
		result.setContent(url);
		result.setInfo("日志下载成功");
		logger.info(result.getInfo());
		return result;
	}
	
	
	@CrossOrigin
	  @RequestMapping(value={"/export/pdfLog"}, method={org.springframework.web.bind.annotation.RequestMethod.POST}, produces={"application/json; charset=UTF-8"})
	  public Result exportPdfLog(@RequestBody Map<String, Object> input, HttpServletRequest request)
	    throws Exception
	  {
	    Result result = new Result();
	    
	    List<String> userNames = (List)input.get("users");
	    
	    String url = this.testLogService.exportPdfLog(userNames, request);
	    
	    result.setStatus(1);
	    result.setContent(url);
	    result.setInfo("日志下载成功");
	    logger.info(result.getInfo());
	    return result;
	  }
	  
	  @CrossOrigin
	  @RequestMapping(value={"/query/logs"}, method={org.springframework.web.bind.annotation.RequestMethod.POST}, produces={"application/json; charset=UTF-8"})
	  public Result queryQstLogs(@RequestBody Map<String, Object> input, HttpServletRequest request)
	    throws FileNotFoundException
	  {
	    Result result = new Result();
	    
	    List<String> userNames = (List)input.get("users");
	    
	    String url = this.testLogService.queryQstLogs(userNames, request);
	    
	    result.setStatus(1);
	    result.setContent(url);
	    result.setInfo("日志下载成功");
	    logger.info(result.getInfo());
	    return result;
	  }

}
