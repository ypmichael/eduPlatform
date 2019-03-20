package com.third.IntelPlat.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.third.IntelPlat.common.MediaTypes;
import com.third.IntelPlat.entity.PermissionEntity;
import com.third.IntelPlat.exception.RestException;
import com.third.IntelPlat.exception.ServiceExceptionHandle;
import com.third.IntelPlat.service.PermissionService;

@RestController
@RequestMapping(value = "/rest")
public class PermissionManager {
	
	private static Logger logger = LoggerFactory.getLogger(PermissionManager.class);
	
	@Autowired
	PermissionService permissionService;
	
	@Autowired
	private ServiceExceptionHandle serviceExceptionHandle;
	
	
	/**
	 * 新增权限
	 * @param input
	 * @param authToken
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/permission", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
	public Result insertPermission(@RequestBody Map<String, Object> input,@RequestHeader(value="Authorization", required=false) String authToken) {
   
		Result result = new Result();

		if ( !input.containsKey("permissionName") || !input.containsKey("permissionIdentify")) {
			
			logger.error("permission insert:: " + serviceExceptionHandle.genInfoById("000001"));
			
			throw new RestException("000001");
		}
		
		if ( ! (input.get("permissionName") instanceof String) && ! (input.get("permissionIdentify") instanceof String)) {
			
			logger.error("permission insert:: " + serviceExceptionHandle.genInfoById("000002"));
			
			throw new RestException("000002");
		}
		
		
		String permissionName = (String) input.get("permissionName");
		String permissionIdentify = (String) input.get("permissionIdentify");
		String remark = null;
		
		
		if(input.containsKey("remark") && (input.get("remark") instanceof String))
		{
			remark = (String) input.get("remark");
		}
		
		PermissionEntity p = permissionService.insertPermission(permissionName, permissionIdentify, remark);
		
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("500003");
		result.setContent(new JSONObject().put("uid", p.getUid()).toString());
		result.setInfo(serviceExceptionHandle.genInfoById("500003"));
		logger.info(result.getInfo());
		return result;
	}
	
	/**
	 * 编辑权限
	 * @param input
	 * @param authToken
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/permission", method = RequestMethod.PUT, produces = MediaTypes.JSON_UTF_8)
	public Result updatePermission(@RequestBody Map<String, Object> input,@RequestHeader(value="Authorization", required=false) String authToken) {
   
		Result result = new Result();
		
		if ( !input.containsKey("uid") || !input.containsKey("permissionName") || !input.containsKey("permissionIdentify")) {
			
			logger.error("permission update:: " + serviceExceptionHandle.genInfoById("000001"));
			throw new RestException("000001");
		}
		
		
		if ( ! (input.get("uid") instanceof Integer)) {
			
			logger.error("permission update:: " + serviceExceptionHandle.genInfoById("000002"));
			throw new RestException("000002");
		}
		
		Integer uid = (Integer) input.get("uid");
		String permissionName = null;
		String permissionIdentify = null;
		String remark = null;
		
		if(input.containsKey("permissionName") && (input.get("permissionName") instanceof String))
		{
			permissionName = (String) input.get("permissionName");
		}
		
		if(input.containsKey("permissionIdentify") && (input.get("permissionIdentify") instanceof String))
		{
			permissionIdentify = (String) input.get("permissionIdentify");
		}
		
		if(input.containsKey("remark") && (input.get("remark") instanceof String))
		{
			remark = (String) input.get("remark");
		}
		
		
		permissionService.updatePermission(uid, permissionName, permissionIdentify, remark);
		
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("500004");
		result.setInfo(serviceExceptionHandle.genInfoById("500004"));
		logger.info(result.getInfo());
		return result;
	}
	
	
	/**
	 * 删除权限
	 * @param input
	 * @param authToken
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/permission/{uid}", method = RequestMethod.DELETE, produces = MediaTypes.JSON_UTF_8)
	public Result deletePermission(@PathVariable("uid") Integer uid, @RequestHeader(value="Authorization", required=false) String authToken) {
   
		if ( uid == null ) {
			
			logger.error("permission delete:: " + serviceExceptionHandle.genInfoById("000001"));
			throw new RestException("000001");
		}
		
		permissionService.deletePermission(uid);
		
		Result result = new Result();
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("500005");
		result.setInfo(serviceExceptionHandle.genInfoById("500005"));
		logger.info(result.getInfo());
		return result;
	}
	
	/**
	 * 查询所有权限信息
	 * @param input
	 * @param authToken
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/permissions", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public Result getPermissions(@RequestParam(value="pageNumber", required=true) Integer pageNumber, 
			@RequestParam(value="limit", required=true) Integer limit,
			@RequestParam(value="keyword", required=false) String keyword, 
			@RequestParam(value="sortField", required=false) String sortField,
			@RequestParam(value="sortType", required=false) String sortType, @RequestHeader(value="Authorization", required=false) String authToken) {
   
		Page<PermissionEntity> permissionPage = permissionService.getPermissions(pageNumber, limit, sortField, sortType, keyword);
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<PermissionEntity> permissionContent = permissionPage.getContent();
		
		map.put("permissions", permissionContent);
		map.put("totalPages", permissionPage.getTotalPages());
		map.put("totalElements", permissionPage.getTotalElements());
		map.put("numberOfCurrentPage", permissionPage.getNumberOfElements());
		if ( sortField != null && sortType != null ) {
			map.put("sortField", sortField);
			map.put("sortType", sortType.toUpperCase());
		} else {
			map.put("sortField", null);
			map.put("sortType", null);
		}
		map.put("limit", permissionPage.getSize());
		map.put("pageNumber", permissionPage.getNumber());
		map.put("isFirstPage", permissionPage.isFirst());
		map.put("isLastPage", permissionPage.isLast());
		
		Result result = new Result();
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("500006");
		result.setContent(map);
		result.setInfo(serviceExceptionHandle.genInfoById("500006"));
		logger.info(result.getInfo());
		return result;
		
	}

}
