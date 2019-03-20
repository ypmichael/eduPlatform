package com.third.IntelPlat.rest;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.third.IntelPlat.common.MediaTypes;
import com.third.IntelPlat.entity.DivisionEntity;
import com.third.IntelPlat.exception.RestException;
import com.third.IntelPlat.exception.ServiceExceptionHandle;
import com.third.IntelPlat.service.DivisionService;

@RestController
@RequestMapping(value = "/rest")
public class DivisionManager 
{	
	private static Logger logger = LoggerFactory.getLogger(DivisionManager.class);
	
	@Autowired
	private DivisionService divisionService;
	
	@Autowired
	private ServiceExceptionHandle serviceExceptionHandle;
	
	/**
	 * 增加部门
	 * @param input
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/division", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
	public Result insertDivision(@RequestBody Map<String, Object> input) 
	{
		if ( !input.containsKey("divisionName")) {
			
			logger.error("insert division :: " + serviceExceptionHandle.genInfoById("000001"));
			throw new RestException("000001");
		}
		
		Object o1 = input.get("divisionName");//部门名称
		Object o2 = input.get("parentDivisionUid");//父部门编号
		
		if (!(o1 instanceof String)) {

			logger.error("insert division :: " + serviceExceptionHandle.genInfoById("000002"));
			throw new RestException("000002");
		}
		
		String divisionName = (String) o1;
		Integer parentDivisionUid = (Integer) o2;
		String description = null;
		
		if(input.containsKey("parentDivisionUid") && (input.get("parentDivisionUid") instanceof Integer))
		{
			parentDivisionUid = (Integer) input.get("parentDivisionUid");
		}
		
		if(input.containsKey("description") && (input.get("description") instanceof String))
		{
			description = (String) input.get("description");
		}
		
		//新增部门
		DivisionEntity d = divisionService.insert(divisionName, parentDivisionUid, description);
		
		Result result = new Result();
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("300004");
		result.setInfo(serviceExceptionHandle.genInfoById("300004"));
		result.setContent(new JSONObject().put("uid", d.getUid()).toString());
		return result;
	}
	
	/**
	 * 修改部门
	 * @param input
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/division", method = RequestMethod.PUT, produces = MediaTypes.JSON_UTF_8)
	public Result updateDivision(@RequestBody Map<String, Object> input) 
	{
		if (!input.containsKey("uid")) {
			
			logger.error("update division :: " + serviceExceptionHandle.genInfoById("000001"));
			throw new RestException("000001");
		}
		
		Object o1 = input.get("uid");//
		
		if (!(o1 instanceof Integer)) {

			logger.error("update division :: " + serviceExceptionHandle.genInfoById("000002"));
			throw new RestException("000002");
		}
		
		Integer uid = (Integer) o1;
		Integer parentDivisionUid = null;
		String divisionName = null;
		String description = null;
		
		if(input.containsKey("parentDivisionUid") && (input.get("parentDivisionUid") instanceof Integer))
		{
			parentDivisionUid = (Integer) input.get("parentDivisionUid");
		}
		
		if(input.containsKey("divisionName") && (input.get("divisionName") instanceof String))
		{
			divisionName = (String) input.get("divisionName");
		}
		
		if(input.containsKey("description") && (input.get("description") instanceof String))
		{
			description = (String) input.get("description");
		}
		
		//修改部门信息
		divisionService.update(uid, divisionName, parentDivisionUid, description);
		
		Result result = new Result();
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("300005");
		result.setInfo(serviceExceptionHandle.genInfoById("300005"));
		return result;
	}
	
	/**
	 * 删除部门
	 * @param input
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/division/{uid}", method = RequestMethod.DELETE, produces = MediaTypes.JSON_UTF_8)
	public Result deleteDivision(@PathVariable("uid") Integer uid) 
	{
		if (uid == null) {
			
			logger.error("delete division :: " + serviceExceptionHandle.genInfoById("000001"));
			throw new RestException("000001");
		}
		
		//删除部门信息
		divisionService.delete(uid);
		
		Result result = new Result();
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("300007");
		result.setInfo(serviceExceptionHandle.genInfoById("300007"));
		return result;
	}
	
	
	/**
	 * 获取所有部门
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/divisions", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public String selectDivisions() 
	{
		logger.info("Find all divisions");
		
		Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                // 排除指定的属性
            	if(f.getName().equals("uid") || f.getName().equals("divisionName") || f.getName().equals("description") || f.getName().equals("parent") 
            			|| f.getName().equals("users") || f.getName().equals("divisionRoles") || f.getName().equals("createTime") || f.getName().equals("updateTime")
            			 || f.getName().equals("divisions")  || f.getName().equals("roles")  || f.getName().equals("version"))
            		return true;
            	else
            		return false;
            }
            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                // 排除指定的类
                return false;
            }
        }).create();
		
		
		List<DivisionEntity> divisions = divisionService.selectDivisions();
		return gson.toJson(divisions);
	}
	
	/**
	 * 部门分配角色
	 * @param input
	 * @param authToken
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/division/divisionAssignRole", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
	public ResponseEntity<String> divisionAssignRole(@RequestBody Map<String, Object> input,@RequestHeader(value="Authorization", required=false) String authToken) {
   
		ResponseEntity<String> entity = null;
		
		Result result = new Result();
		
		Gson json = new Gson();

		if ( !input.containsKey("divisionId") || !input.containsKey("roleIds")) {
			logger.error("division Assign Role :: " + serviceExceptionHandle.genInfoById("000001"));
			throw new RestException("000001");
		}
		
		Object o1 = input.get("divisionId");
		Object o2 = input.get("roleIds");
		
		if ( ! (o1 instanceof Integer)) {
			
			logger.error("division Assign Role :: " + serviceExceptionHandle.genInfoById("000002"));
			throw new RestException("000002");
		}
		
		Integer divisionId = (Integer) input.get("divisionId");
		List<Integer> roleIds = o2 != null ? (List<Integer>) input.get("roleIds") : null;
		
		result = divisionService.divisionAssignRole(divisionId, roleIds);
		
		entity = new ResponseEntity<String>(json.toJson(result), HttpStatus.OK);
		
		if ( result.getStatus() != 0 ) {
			logger.info("division Assign Role:: successfully ");
		} else {
			logger.error("division Assign Role:: failed Err: " + result.getInfo());
		}
		return entity;
		
	}
	
	
	/**
	 * 根据父部门编号查询子部门信息
	 * @param input
	 * @param authToken
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/division/{divisionId}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public Result getDivisions(@PathVariable("divisionId") Integer divisionId, @RequestHeader(value="Authorization", required=false) String authToken) {
   
		List<DivisionEntity> divisions = divisionService.getDivisions(divisionId);
		
		Result result = new Result();
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("300009");
		result.setContent(divisions);
		result.setInfo(serviceExceptionHandle.genInfoById("300009"));
		return result;
	}
}
