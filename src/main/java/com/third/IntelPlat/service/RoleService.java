package com.third.IntelPlat.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.PermissionEntity;
import com.third.IntelPlat.entity.PermissionRoleEntity;
import com.third.IntelPlat.entity.RoleEntity;
import com.third.IntelPlat.exception.ServiceException;
import com.third.IntelPlat.repository.PermissionRepository;
import com.third.IntelPlat.repository.PermissionRoleRepository;
import com.third.IntelPlat.repository.RoleRepository;


@Component
public class RoleService 
{
	private static final Logger logger = LoggerFactory.getLogger(RoleService.class);
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PermissionRoleRepository permissionRoleRepository;
	
	@Autowired
	private PermissionRepository permissionRepository;


	/**
	 * 查询用户关联的角色与权限
	 * @param uid
	 * @param uid2
	 * @return
	 */
	public List<RoleEntity> findRoles(Integer userId, Integer divisionId) {
		
		List<Integer> roleIds = roleRepository.findAllRoleIds(userId, divisionId);
		
		List<RoleEntity> roleList = new ArrayList<RoleEntity>();
		
		if(roleIds.size() != 0)
		{
			roleList = roleRepository.findRoles(roleIds);
			
		}
		return roleList;
	}
	/**
	 * 
	 * @param userId
	 * @param divisionId
	 * @return
	 */
	public List<Integer> findAllRoleIds(Integer userId, Integer divisionId){
		List<Integer> roleIds = roleRepository.findAllRoleIds(userId, divisionId);
		return roleIds;
	}

	/**
	 * 新增角色信息
	 * @param roleName
	 * @param remark
	 */
	public RoleEntity insertRole(String roleName, String remark) 
	{
		//验证角色名称是否存在
		RoleEntity r = null;
		
		try {
			r = roleRepository.findByRoleName(roleName);
			
			if(r != null)
			{
				throw new ServiceException("200002");
			}
			
			RoleEntity role = new RoleEntity();
			
			role.setRoleName(roleName);
			role.setRemark(remark);
			role.setCreateTime(new Date());
			
			r = roleRepository.save(role);
		} catch (Exception e) {
			logger.error("系统异常", e.getMessage());
			throw new ServiceException("000004");
		}
		
		return r;
	}


	/**
	 * 更新角色信息
	 * @param uid
	 * @param roleName
	 * @param remark
	 */
	public void updateRole(Integer uid, String roleName, String remark) {
		
		try {
			//验证角色信息是否存在
			RoleEntity r = roleRepository.findOne(uid);
			
			if(r == null)
			{
				throw new ServiceException("200001");
			}
			
			//验证角色的名称不能存在
			RoleEntity role = roleRepository.checkRoleNameByUid(uid, roleName);
			
			if(role != null)
			{
				throw new ServiceException("200002");
			}
			
			r.setRoleName(roleName == null ? r.getRoleName() : roleName);
			r.setRemark(remark == null ? r.getRemark() : remark);
			r.setUpdateTime(new Date());
			
			roleRepository.save(r);
		} catch (Exception e) {
			logger.error("系统异常", e.getMessage());
			throw new ServiceException("000004");
		}
		
	}


	/**
	 * 删除角色信息
	 * @param uid 
	 */
	public void deleteRole(Integer uid) 
	{
		
		try {
			RoleEntity r = roleRepository.findOne(uid);
			
			if(r == null){
				throw new ServiceException("200001");
			}
			
			if(r.getRolePermissions() != null && r.getRolePermissions().size() > 0)
			{
				throw new ServiceException("200005");
			}
			
			roleRepository.delete(r.getUid());
			
		} catch (Exception e) {
			logger.error("系统异常", e.getMessage());
			throw new ServiceException("000004");
		}
	}


	/**
	 * 按条件获取角色信息
	 * @param pageNumber
	 * @param limit
	 * @param sortField
	 * @param sortType
	 * @param keyword
	 */
	public Page<RoleEntity> getRoles(Integer pageNumber, Integer limit, String sortField, String sortType, String keyword) 
	{
		
		Page<RoleEntity> rolePage = null;
		try {
			
			if(sortField == null && keyword == null)
			{
				rolePage = roleRepository.findRoles(new PageRequest(pageNumber, limit));
			}else if (sortField != null && keyword == null)
			{
				rolePage = roleRepository.findRoles(new PageRequest(pageNumber, limit, new Sort(Direction.fromString(sortType), sortField)));
			}else if (sortField == null && keyword != null)
			{
				keyword = "%" + keyword + "%";
				rolePage = roleRepository.findByRoleLike(keyword, new PageRequest(pageNumber, limit));
			}else
			{
				keyword = "%" + keyword + "%";
				rolePage = roleRepository.findByRoleLike(keyword, new PageRequest(pageNumber, limit, new Sort(Direction.fromString(sortType), sortField)));
			}
			
		} catch (Exception e) {
			logger.error("系统异常", e.getMessage());
			throw new ServiceException("000004");
		}
		
		return rolePage;
		
	}


	/**
	 * 角色分配权限
	 * @param roleId
	 * @param permissionIds
	 */
	public void roleAssignPermission(Integer roleId, List<Integer> permissionIds) 
	{
		
		permissionRoleRepository.deleteByRoleId(roleId);
		
		RoleEntity r = roleRepository.findOne(roleId);
		
		if(r == null)
		{
			throw new ServiceException("200001");
		}
		
		for(Integer id : permissionIds)
		{
			PermissionEntity p = permissionRepository.findOne(id);
			
			if(p == null)
			{
				throw new ServiceException("500000");
			}
			
			PermissionRoleEntity pr = new PermissionRoleEntity();
			
			pr.setRolePermission(r);
			pr.setPermissionRole(p);
			
			permissionRoleRepository.save(pr);
			
		}
	}


	/**
	 * 根据角色编号查询权限信息
	 * @param uid
	 */
	public List<PermissionEntity> getPermissions(Integer uid) 
	{
		try {
			RoleEntity r = roleRepository.findRoleByUid(uid);
			
			if(r == null)
			{
				throw new ServiceException("200001");
			}
			
			List<PermissionEntity> permissions = permissionRepository.getPermissions(uid);
			
			for(PermissionEntity p : permissions)
			{
				p.setPermissionRoles(null);
			}
			
			return permissions;
		} catch (Exception e) {
			logger.error("系统异常", e.getMessage());
			throw new ServiceException("000004");
		}
	}
}
