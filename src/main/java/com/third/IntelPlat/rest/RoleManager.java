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
import com.third.IntelPlat.entity.RoleEntity;
import com.third.IntelPlat.exception.RestException;
import com.third.IntelPlat.exception.ServiceExceptionHandle;
import com.third.IntelPlat.service.RoleService;

@RestController
@RequestMapping(value = "/rest")
public class RoleManager {
	
	private static Logger logger = LoggerFactory.getLogger(RoleManager.class);
	
	@Autowired
	private RoleService roleServicel;
	
	@Autowired
	private ServiceExceptionHandle serviceExceptionHandle;
	/**
	 * 新增角色
	 * @param input
	 * @param authToken
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/role", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
	public Result insertRole(@RequestBody Map<String, Object> input,@RequestHeader(value="Authorization", required=false) String authToken) {
   
		if ( !input.containsKey("roleName") ) 
		{
			throw new RestException("000001");
		}
		
		if ( ! (input.get("roleName") instanceof String) ) {
			
			throw new RestException("000002");
		}
		
		String roleName = (String) input.get("roleName");
		String remark = null;
		
		if(input.containsKey("remark") && (input.get("remark") instanceof String))
		{
			remark = (String) input.get("remark");
		}
		
		RoleEntity r = roleServicel.insertRole(roleName, remark);
		
		Result result = new Result();
		result.setStatus(Result.STATUS_SUCCESS);
		result.setContent(new JSONObject().put("uid", r.getUid()).toString());
		result.setCode("200003");
		result.setInfo(serviceExceptionHandle.genInfoById("100013"));
		logger.info(result.getInfo());
		return result;
	}
	
	/**
	 * 编辑角色信息
	 * @param input
	 * @param authToken
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/role", method = RequestMethod.PUT, produces = MediaTypes.JSON_UTF_8)
	public Result updateRole(@RequestBody Map<String, Object> input,@RequestHeader(value="Authorization", required=false) String authToken) {
   
		if ( !input.containsKey("uid")) {
			throw new RestException("000001");
		}
		
		
		if ( !(input.get("uid") instanceof Integer)) {
			throw new RestException("000002");
		}
		
		Integer uid = (Integer) input.get("uid");
		String roleName = null;
		String remark = null;
		
		
		if(input.containsKey("roleName") && (input.get("roleName") instanceof String))
		{
			roleName = (String) input.get("roleName");
		}
		
		if(input.containsKey("remark") && (input.get("remark") instanceof String))
		{
			remark = (String) input.get("remark");
		}
		
		roleServicel.updateRole(uid, roleName, remark);
		
		Result result = new Result();
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("200004");
		result.setInfo(serviceExceptionHandle.genInfoById("200004"));
		logger.info(result.getInfo());
		return result;
	}
	
	
	/**
	 * 删除角色
	 * @param input
	 * @param authToken
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/role/{uid}", method = RequestMethod.DELETE, produces = MediaTypes.JSON_UTF_8)
	public Result deleteRole(@PathVariable("uid") Integer uid, @RequestHeader(value="Authorization", required=false) String authToken) {
   
		if ( uid == null) {
			throw new RestException("000001");
		}
		
		roleServicel.deleteRole(uid);
		
		Result result = new Result();
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("200006");
		result.setInfo(serviceExceptionHandle.genInfoById("200006"));
		logger.info(result.getInfo());
		return result;
		
	}
	
	
	/**
	 * 获取角色列表信息
	 * @param input
	 * @param authToken
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/roles", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public Result getRoles(@RequestParam(value="pageNumber", required=true) Integer pageNumber, 
			@RequestParam(value="limit", required=true) Integer limit,
			@RequestParam(value="keyword", required=false) String keyword, 
			@RequestParam(value="sortField", required=false) String sortField,
			@RequestParam(value="sortType", required=false) String sortType,@RequestHeader(value="Authorization", required=false) String authToken) {
	
		Page<RoleEntity> rolePage = roleServicel.getRoles(pageNumber, limit, sortField, sortType, keyword);
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<RoleEntity> roleContent = rolePage.getContent();
		List<String> roles = new ArrayList<String>();
		
		map.put("roles", roleContent);
		map.put("totalPages", rolePage.getTotalPages());
		map.put("totalElements", rolePage.getTotalElements());
		map.put("numberOfCurrentPage", rolePage.getNumberOfElements());
		if ( sortField != null && sortType != null ) {
			map.put("sortField", sortField);
			map.put("sortType", sortType.toUpperCase());
		} else {
			map.put("sortField", null);
			map.put("sortType", null);
		}
		map.put("limit", rolePage.getSize());
		map.put("pageNumber", rolePage.getNumber());
		map.put("isFirstPage", rolePage.isFirst());
		map.put("isLastPage", rolePage.isLast());
		
		Result result = new Result();
		result.setStatus(Result.STATUS_SUCCESS);
		result.setContent(map);
		result.setCode("100012");
		result.setInfo(serviceExceptionHandle.genInfoById("100012"));
		logger.info(result.getInfo());
		
		return result;
	}
	
	
	/**
	 * 角色分配权限
	 * @param input
	 * @param authToken
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/role/roleAssignPermission", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
	public Result roleAssignPermission(@RequestBody Map<String, Object> input,@RequestHeader(value="Authorization", required=false) String authToken) {
   
		if ( !input.containsKey("roleId") || !input.containsKey("permissionIds")) {
			
			logger.error("role Assign Permission :: " + serviceExceptionHandle.genInfoById("000001"));
			throw new RestException("000001");
		}
		
		Object o1 = input.get("roleId");
		Object o2 = input.get("permissionIds");
		
		if ( ! (o1 instanceof Integer)) {
			
			logger.error("role Assign Permission  :: " + serviceExceptionHandle.genInfoById("000002"));
			throw new RestException("000002");
		}
		
		Integer roleId = (Integer) input.get("roleId");
		List<Integer> permissionIds = o2 != null ? (List<Integer>) input.get("permissionIds") : null;
		
		roleServicel.roleAssignPermission(roleId, permissionIds);
		
		Result result = new Result();
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("200008");
		result.setInfo(serviceExceptionHandle.genInfoById("200008"));
		logger.info(result.getInfo());
		
		return result;
	}
	
	
	/**
	 * 根据角色编号查询权限信息
	 * @param input
	 * @param authToken
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/role/{uid}/permissions", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public Result getPermissions(@PathVariable("uid") Integer uid, @RequestHeader(value="Authorization", required=false) String authToken) {
		
		if ( uid == null ) {
			
			logger.error("get permissions by roleId :: " + serviceExceptionHandle.genInfoById("000001"));
			throw new RestException("000001");
		}
		
		List<PermissionEntity> permissions = roleServicel.getPermissions(uid);
		
		Result result = new Result();
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("500001");
		result.setContent(permissions);
		result.setInfo(serviceExceptionHandle.genInfoById("500001"));
		logger.info(result.getInfo());
		
		return result;
	}

}
