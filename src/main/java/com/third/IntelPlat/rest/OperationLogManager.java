package com.third.IntelPlat.rest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.third.IntelPlat.common.MediaTypes;
import com.third.IntelPlat.entity.LoginLogEntity;
import com.third.IntelPlat.entity.OperationLogEntity;
import com.third.IntelPlat.exception.RestException;
import com.third.IntelPlat.service.OperationLogService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/rest")
public class OperationLogManager {
	@Autowired
	private OperationLogService operationLogService;
	
	
	@CrossOrigin
	@RequestMapping(value = "/logs/operationlogs", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public Result findByUserUidWithTime(@RequestParam(value = "account", required = false) String account,
			@RequestParam(value = "type", required = false) String type, 
			@RequestParam(value = "model", required = false) String model, 
			@RequestParam(value = "startTime", required = false) Long startTime, 
			@RequestParam(value = "endTime", required = false) Long endTime, 
			@RequestParam(value = "pageNumber", required = true) Integer pageNumber, 
			@RequestParam(value = "limit", required = true) Integer limit){
		if(null == pageNumber || null == limit){
			throw new RestException("000001");
		}
		Date startTimeD = null;
		if(null != startTime){
			startTimeD = new Date(startTime * 1000);
		}
		Date endTimeD = null;
		if(null != endTime){
			endTimeD = new Date(endTime * 1000);
		}
		Page<OperationLogEntity> pageOperationLog = operationLogService.findByAccountWithTypeWithModelWithTime(account, type, model, startTimeD, endTimeD, pageNumber, limit);
		List<OperationLogEntity> operationLogList = pageOperationLog.getContent();
		Integer totalElements = (int) pageOperationLog.getTotalElements();
		
		Map<String,Object> contentMap = new HashMap<String,Object>();
		contentMap.put("operationLogList", operationLogList);
		contentMap.put("totalElements", totalElements);
		
		Result result = new Result();
		result.setStatus(result.STATUS_SUCCESS);
		result.setInfo("操作日志信息查询成功");
		Gson json = new Gson();
		result.setContent(json.toJson(contentMap));	
		return result;
	}
	
	/**
	 * 登录日志查询
	 * @param account
	 * @param startTime
	 * @param endTime
	 * @param pageNumber
	 * @param limit
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/logs/loginlogs", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public Result findLoginlogs(@RequestParam(value = "account", required = false) String account, 
			@RequestParam(value = "startTime", required = false) Long startTime, 
			@RequestParam(value = "endTime", required = false) Long endTime, 
			@RequestParam(value = "pageNumber", required = true) Integer pageNumber, 
			@RequestParam(value = "limit", required = true) Integer limit)
	{
		if(null == pageNumber || null == limit){
			throw new RestException("000001");
		}
		Date startTimeD = null;
		if(null != startTime){
			startTimeD = new Date(startTime * 1000);
		}
		Date endTimeD = null;
		if(null != endTime){
			endTimeD = new Date(endTime * 1000);
		}
		
		Page<LoginLogEntity> loginLogPage = operationLogService.findLoginlogs(account, startTimeD, endTimeD, pageNumber, limit);
		
		List<LoginLogEntity> LogList = loginLogPage.getContent();
		Integer totalElements = (int) loginLogPage.getTotalElements();
		
		Map<String,Object> contentMap = new HashMap<String,Object>();
		contentMap.put("operationLogList", LogList);
		contentMap.put("totalElements", totalElements);
		
		Result result = new Result();
		result.setStatus(result.STATUS_SUCCESS);
		result.setInfo("登录日志信息查询成功");
		Gson json = new Gson();
		result.setContent(json.toJson(contentMap));	
		return result;
	}
}
	
