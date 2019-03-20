package com.third.IntelPlat.service;

import java.util.List;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.DivisionEntity;
import com.third.IntelPlat.entity.RoleEntity;
import com.third.IntelPlat.repository.RoleRepository;

@Component
public class InitDBService implements InitializingBean, DisposableBean{
	
	
	@Autowired
	private UserService userService;
		
	@Autowired
	private RoleService roleService;	
	
	@Autowired
	private DivisionService divisionService;
	
	@Autowired
	private RoleRepository roleRepository;

	@Override
	public void destroy() throws Exception {
		// do nothing
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		
		// add root division if need
		List<DivisionEntity> divisions = divisionService.findAll();
		
		if(divisions.size() == 0 || divisions.isEmpty()){
			
			String divisionName = "总部";
			divisionService.insert(divisionName, null, "");
		}
		
		// add root user if need
		if(userService.findAll().isEmpty()){
			String userName = "admin";
			String pass = "admin";
			String operator = "admin";
			
			userService.insertUser(userName, pass, operator, 1, "",0,null,null,null,null, null);
		}	
		
		// add role user if need
		if(roleService.findRoles(1, 1).isEmpty()){
			
			String roleName = "superadmin";
			String remark = "超级用户";
			
			RoleEntity r = roleRepository.findByRoleName(roleName);
			if(r == null)
			{
				roleService.insertRole(roleName, remark);
			}
			
		}
		
	}

}
