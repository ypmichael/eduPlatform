package com.third.IntelPlat.security;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.third.IntelPlat.entity.RoleEntity;
import com.third.IntelPlat.entity.UserEntity;
import com.third.IntelPlat.exception.ServiceExceptionHandle;
import com.third.IntelPlat.repository.RoleRepository;
import com.third.IntelPlat.rest.Result;
import com.third.IntelPlat.service.OperationLogService;
import com.third.IntelPlat.service.PermissionService;
import com.third.IntelPlat.service.RoleService;
import com.third.IntelPlat.service.UserService;

public class ShiroDbRealm extends AuthorizingRealm {

	private static Logger logger = LoggerFactory.getLogger(ShiroDbRealm.class);

	@Autowired
	UserService userService;
	@Autowired
	RoleService roleService;
	@Autowired
	PermissionService permissionService;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private OperationLogService operationLogService;
	
	@Autowired
	private ServiceExceptionHandle serviceExceptionHandle;
	
	public ShiroDbRealm() {
		setAuthenticationTokenClass(BearerToken.class);
	}
	
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {

		BearerToken token = (BearerToken) authcToken;

		Gson json = new Gson();
		
		Result result = new Result();

		if (token.isEmptyToken()) {
			result.setStatus(Result.STATUS_FAILED);
			result.setInfo(serviceExceptionHandle.genInfoById("400001"));
			
			logger.error(result.getInfo());

			throw new AuthenticationException(json.toJson(result));
		}

		// check session Id, if valid, don't need query by db
		if (token.isAuthed()) {
			// 超时检查
			if (token.isTimeout()) {

				result.setStatus(Result.STATUS_FAILED);
				result.setInfo(serviceExceptionHandle.genInfoById("400002"));
				logger.error(result.getInfo());

				throw new AuthenticationException(json.toJson(result));
			}

			SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(token.getPrincipal(), token.getCredentials(),
					getName());
			return info;
		} else {

			UserEntity user = userService.findByAccount((String) token.getPrincipal());

			// 用户信息不存在
			if (user == null) {
				result.setStatus(Result.STATUS_FAILED);
				result.setInfo(serviceExceptionHandle.genInfoById("400003"));
				logger.error(result.getInfo());
				throw new AuthenticationException(json.toJson(result));
			}

			// 用户被禁用
			if (user.getStatus() != UserEntity.STATUS_ACTIVATE) {
				result.setStatus(Result.STATUS_FAILED);
				result.setInfo(serviceExceptionHandle.genInfoById("400004"));
				logger.error(result.getInfo());
				throw new AuthenticationException(json.toJson(result));
			}

			// 密码校验
			if (user.getPassword() == null || !user.getPassword().equals(token.getCredentials())) {
				result.setStatus(Result.STATUS_FAILED);
				result.setInfo(serviceExceptionHandle.genInfoById("400005"));
				logger.error(result.getInfo());
				throw new AuthenticationException(json.toJson(result));
			}
			
			user.setToken(token.genToken());
			user.setLoginTime(new Date());
			
			userService.save(user);
			
			operationLogService.inserLoginLog(user.getUid(), user.getUserName());
			
			SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user.getUserName(), user.getPassword(), getName());
			return info;
		}
	}
	
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

		String userName = (String) principals.getPrimaryPrincipal();
		Result result = new Result();
		UserEntity user = userService.findByAccount(userName);

		if (user == null) {
			result.setStatus(Result.STATUS_FAILED);
			result.setInfo(serviceExceptionHandle.genInfoById("400007"));
			logger.error(result.getInfo());
			throw new AuthenticationException(new Gson().toJson(result));
		}
		
		//查询用户关联的角色与权限
		List<RoleEntity> roles = roleService.findRoles(user.getUid(), user.getDivision().getUid());
		
		Set<Integer> roleIds = new HashSet<Integer>(); 
		
		Set<String> roleIdNames = new HashSet<String>();
		
		for( RoleEntity role : roles )
		{
			roleIds.add(role.getUid());
			roleIdNames.add(role.getRoleName());
		}
		
		List<Integer> roleId = roleRepository.findAllRoleIds(user.getUid(), user.getDivision().getUid());
		
		//查询角色对应的权限
		Set<String> permissons = permissionService.findPermissons(roleId);

		// get roles and permissions from cache
		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(permissons);
		info.setRoles(roleIdNames);
		return info;
	}
	
}
