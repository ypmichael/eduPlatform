/**
 * 
 */
package com.third.IntelPlat.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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
import com.third.IntelPlat.common.OperationLog;
import com.third.IntelPlat.entity.RoleEntity;
import com.third.IntelPlat.entity.UserEntity;
import com.third.IntelPlat.exception.RestException;
import com.third.IntelPlat.exception.ServiceExceptionHandle;
import com.third.IntelPlat.service.PermissionService;
import com.third.IntelPlat.service.RoleService;
import com.third.IntelPlat.service.UserService;

@RestController
@RequestMapping(value = "/rest")
public class UserManager 
{
	private static Logger logger = LoggerFactory.getLogger(UserManager.class);
	
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private ServiceExceptionHandle serviceExceptionHandle;
	/**
	 * 获取授权信息
	 * @param input
	 * @param authToken
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/authorization", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public Result getAuthorizationInfo(@RequestHeader(value = "Token", required = false) String authToken) {
		UserEntity u = userService.getAuthorizationInfo(authToken);
		
		Set<String> permissons = new HashSet<>();
		
		if("admin".equals(u.getUserName()))
		{
			permissons = permissionService.findAll();
		}else
		{
			//查询用户所具有的的权限
			List<Integer> roleIds = roleService.findAllRoleIds(u.getUid(), u.getDivisionUid());
			if(! roleIds.isEmpty()){
				permissons = permissionService.findPermissons(roleIds);
			}
		}
		Map<String, Object> map = new HashMap<>();
		map.put("user", u.toJson());
		map.put("permissons", permissons);
		Result result = new Result();
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("100005");
		result.setInfo(serviceExceptionHandle.genInfoById("100005"));
		result.setContent(map);
		logger.info(result.getInfo());
		return result;
	}
	
	/**
	 * 新增用户
	 * @param input
	 * @param authToken
	 * @return
	 */
	@CrossOrigin
//	@RequiresPermissions("userInsert1")
	@OperationLog(operation = "新增用户", type = "新增", model = "用户模块")
	@RequestMapping(value="/user", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
	public Result insertUser(@RequestBody Map<String, Object> input,@RequestHeader(value="Authorization", required=false) String authToken) {
		
		if ( !input.containsKey("userName") || !input.containsKey("password") || !input.containsKey("userType") 
				|| !input.containsKey("sex") || !input.containsKey("contact") || !input.containsKey("cellPhone")
				|| !input.containsKey("email") || !input.containsKey("divisionUid")) 
		{
			throw new RestException("000001");
		}
		
		Object o1 = input.get("userName");
		Object o2 = input.get("password");
		Object o3 = input.get("userType");
		Object o4 = input.get("sex");
		Object o5 = input.get("contact");
		Object o6 = input.get("cellPhone");
		Object o7 = input.get("email");
		Object o8 = input.get("divisionUid");
		
		if ( ! (o1 instanceof String) || ! (o2 instanceof String) || ! (o3 instanceof Integer)
				 || ! (o4 instanceof Integer) || ! (o5 instanceof String) || ! (o6 instanceof String)
				 || ! (o7 instanceof String) || ! (o8 instanceof Integer)) {
			throw new RestException("000002");
		}
		
		if(input.get("remark") != null && !(input.get("remark") instanceof String))
		{
			throw new RestException("000002");
		}
		Subject subject = SecurityUtils.getSubject();

		String userName = (String) input.get("userName");
		String password = (String) input.get("password");
		Integer userType = (Integer) input.get("userType");
		Integer sex = (Integer) input.get("sex");
		String contact = (String) input.get("contact");
		String cellPhone = (String) input.get("cellPhone");
		String email = (String) input.get("email");
		String operator = (String) subject.getPrincipal();
		Integer divisionId = (Integer) input.get("divisionUid");
		String gradeName = input.get("gradeName") != null ? (String) input.get("gradeName") : null;
		String remark = input.get("remark") != null ? (String)input.get("remark") : null;
		
		Integer uid = userService.insertUser(userName, password, operator, divisionId, remark, userType, sex, contact, cellPhone, email, gradeName);
		Result result = new Result();
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("100006");
		result.setInfo(serviceExceptionHandle.genInfoById("100006"));
		result.setContent(uid);
		logger.info(result.getInfo());
		return result;
	}
	
	/**
	 * 更新用户信息
	 * @param input
	 * @param authToken
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/user", method = RequestMethod.PUT, produces = MediaTypes.JSON_UTF_8)
	public Result updateUser(@RequestBody Map<String, Object> input) {
		if ( !input.containsKey("uid")) {
			throw new RestException("000001");
		}
		
		if ( !(input.get("uid") instanceof Integer)) {
			throw new RestException("000002");
		}
		
		Subject subject = SecurityUtils.getSubject();
		Integer userId = (Integer) input.get("uid");
		String operator = (String) subject.getPrincipal();
		String userName = null;
		String password = null;
		Integer divisionId = null;
		String remark = null;
		
		Integer userType = null;
		Integer sex = null;
		String contact = null;
		String cellPhone = null;
		String email = null;
		
		if(input.containsKey("userName") && (input.get("userName") instanceof String))
		{
			userName = (String)input.get("userName");
		}
		
		if(input.containsKey("password") && (input.get("password") instanceof String))
		{
			password = (String)input.get("password");
		}
		
		if(input.containsKey("divisionId") && (input.get("divisionId") instanceof Integer))
		{
			divisionId = (Integer)input.get("divisionId");
		}
		
		if(input.containsKey("remark") && (input.get("remark") instanceof String))
		{
			remark = (String)input.get("remark");
		}
		
		if(input.containsKey("userType") && (input.get("userType") instanceof Integer))
		{
			userType = (Integer)input.get("userType");
		}
		
		if(input.containsKey("sex") && (input.get("sex") instanceof Integer))
		{
			sex = (Integer)input.get("sex");
		}
		
		if(input.containsKey("contact") && (input.get("contact") instanceof String))
		{
			contact = (String)input.get("contact");
		}
		
		if(input.containsKey("cellPhone") && (input.get("cellPhone") instanceof String))
		{
			cellPhone = (String)input.get("cellPhone");
		}
		
		if(input.containsKey("email") && (input.get("email") instanceof String))
		{
			email = (String)input.get("email");
		}
		
		userService.updateUser(userId, userName, password, operator, divisionId, remark, userType, sex, contact, cellPhone, email);
		Result result = new Result();
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("100007");
		result.setInfo(serviceExceptionHandle.genInfoById("100007"));
		logger.info(result.getInfo());
		return result;
	}
	
	
	/**
	 * 删除用户信息
	 * @param input
	 * @param authToken
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/user/{uid}", method = RequestMethod.DELETE, produces = MediaTypes.JSON_UTF_8)
	public  Result deleteUser(@PathVariable("uid") Integer uid) {
		if ( uid == null ) {
			throw new RestException("000001");
		}
		userService.updateUserStatus(uid, UserEntity.STATUS_DELETE);
		Result result = new Result();
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("100008");
		result.setInfo(serviceExceptionHandle.genInfoById("100008"));
		logger.info(result.getInfo());
		return result;
	}
	
	/**
	 * 禁用用户信息
	 * @param uid
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/user/disable", method = RequestMethod.PUT, produces = MediaTypes.JSON_UTF_8)
	public  Result disableUser(@PathVariable("uid") Integer uid) {
		if ( uid == null ) {
			throw new RestException("000001");
		}
		
		userService.updateUserStatus(uid, UserEntity.STATUS_DISABLE);
		Result result = new Result();
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("100009");
		result.setInfo(serviceExceptionHandle.genInfoById("100009"));
		logger.info(result.getInfo());
		
		return result;
	}
	
	
	/**
	 * 启用用户信息
	 * @param uid
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/user/enable", method = RequestMethod.PUT, produces = MediaTypes.JSON_UTF_8)
	public  Result enableUser(@PathVariable("uid") Integer uid) {
		if ( uid == null ) {
			throw new RestException("000001");
		}
		
		userService.updateUserStatus(uid, UserEntity.STATUS_ACTIVATE);
		Result result = new Result();
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("100010");
		result.setInfo(serviceExceptionHandle.genInfoById("100010"));
		logger.info(result.getInfo());
		return result;
	}
	
	/**
	 * 根据用户编号查询用户信息
	 * @param uid
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/user/{uid}", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public Result findByUid(@PathVariable("uid") Integer uid) {
		if ( uid == null ) {
			throw new RestException("000001");
		}
		UserEntity user = userService.findByUid(uid);
		Result result = new Result();
		result.setStatus(Result.STATUS_SUCCESS);
		result.setContent(user.toJson());
		result.setCode("100011");
		result.setInfo(serviceExceptionHandle.genInfoById("100011"));
		logger.info(result.getInfo());
		return result;
	}
	
	/**
	 * 查询所有的用户信息
	 * @param input
	 * @param authToken
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/users", method = RequestMethod.GET, produces = MediaTypes.JSON_UTF_8)
	public  Result users(@RequestParam(value="divisionUid", required=false) Integer divisionUid,
			@RequestParam(value="pageNumber", required=true) Integer pageNumber, 
			@RequestParam(value="limit", required=true) Integer limit,
			@RequestParam(value="keyword", required=false) String keyword, 
			@RequestParam(value="sortField", required=false) String sortField,
			@RequestParam(value="sortType", required=false) String sortType ) {
		
			Page<UserEntity> userPage = userService.findUsers(divisionUid, pageNumber, limit, sortField, sortType, keyword);
			List<UserEntity> userEntityContent = userPage.getContent();
			
			List<UserEntity> users = new ArrayList<UserEntity>();
			
			for(UserEntity u : userEntityContent)
			{
				
				List<RoleEntity> roles = roleService.findRoles(u.getUid(), u.getDivision() == null ? null : u.getDivision().getUid());
				
				users.add(new UserEntity(u.getUid(), u.getUserName(), u.getRemark(), u.getStatus(), 
						u.getCreateTime(), u.getUpdateTime(), u.getOperator(), u.getLoginTime(), u.getToken(), roles, u.getDivision() == null ? null :u.getDivision().getUid(), u.getDivision() == null ? null : u.getDivision().getDivisionName(),
						u.getUserType(), u.getSex(), u.getContact(), u.getCellPhone(), u.getEmail()));
			}
			Map<String, Object> map = new HashMap<>();
			map.put("users", users);
			map.put("totalPages", userPage.getTotalPages());
			map.put("totalElements", userPage.getTotalElements());
			map.put("numberOfCurrentPage", userPage.getNumberOfElements());
			if ( sortField != null && sortType != null ) {
				map.put("sortField", sortField);
				map.put("sortType", sortType.toUpperCase());
			} else {
				map.put("sortField", null);
				map.put("sortType", null);
			}
			map.put("limit", userPage.getSize());
			map.put("pageNumber", userPage.getNumber());
			map.put("isFirstPage", userPage.isFirst());
			map.put("isLastPage", userPage.isLast());
			Result result = new Result();
			result.setStatus(Result.STATUS_SUCCESS);
			result.setContent(map);
			result.setCode("100012");
			result.setInfo(serviceExceptionHandle.genInfoById("100012"));
			logger.info(result.getInfo());
			
			return result;
	}
	
	
	/**
	 * 用户重置密码
	 * @param input
	 * @param authToken
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/user/passwd", method = RequestMethod.PUT, produces = MediaTypes.JSON_UTF_8)
	public Result resetPwd(@RequestBody Map<String, Object> input,@RequestHeader(value="Authorization", required=false) String authToken) {
		if ( !input.containsKey("userName") || !input.containsKey("password")  || !input.containsKey("newPassword")) {
			throw new RestException("000001");
		}
		Object o1 = input.get("userName");
		Object o2 = input.get("password");
		Object o3 = input.get("newPassword");
		if ( ! (o1 instanceof String) || ! (o2 instanceof String) || ! (o3 instanceof String) ) {
			throw new RestException("000002");
		}
		String account = (String) o1;
		String passwd = (String) o2;
		String newPasswd = (String) o3;
		userService.resetPwd(account, passwd, newPasswd);
		Result result = new Result();
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("100013");
		result.setInfo(serviceExceptionHandle.genInfoById("100013"));
		logger.info(result.getInfo());
		return result;
	}
	
	
	/**
	 * 用户分配角色
	 * @param input
	 * @param authToken
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value="/user/userAssignRole", method = RequestMethod.POST, produces = MediaTypes.JSON_UTF_8)
	public Result userAssignRole(@RequestBody Map<String, Object> input,@RequestHeader(value="Authorization", required=false) String authToken) {
		if ( !input.containsKey("userId") || !input.containsKey("roleIds")) {
			throw new RestException("000001");
		}
		
		Object o1 = input.get("userId");
		Object o2 = input.get("roleIds");
		
		if ( ! (o1 instanceof Integer)) {
			throw new RestException("000002");
		}
		
		Integer userId = (Integer) input.get("userId");
		List<Integer> roleIds = o2 != null ? (List<Integer>) input.get("roleIds") : null;
		
		userService.userAssignRole(userId, roleIds);
		Result result = new Result();
		result.setStatus(Result.STATUS_SUCCESS);
		result.setCode("100014");
		result.setInfo(serviceExceptionHandle.genInfoById("100014"));
		logger.info(result.getInfo());
		return result;
	}
	
}
