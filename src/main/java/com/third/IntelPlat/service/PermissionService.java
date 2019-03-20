package com.third.IntelPlat.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.PermissionEntity;
import com.third.IntelPlat.exception.ServiceException;
import com.third.IntelPlat.repository.PermissionRepository;


@Component
public class PermissionService 
{
	private static final Logger logger = LoggerFactory.getLogger(PermissionService.class);
	
	@Autowired
	private PermissionRepository permissionRepository;

	/**
	 * 查询角色对应的权限
	 * @param roleIds
	 * @return
	 */

	public Set<String> findPermissons(List<Integer> roleIds) {
		Set<String> permissons = permissionRepository.findPermissons(roleIds);
		
		logger.info("权限查询成功");
		return permissons;
	}

	
	/**
	 * 新增权限
	 * @param permissionName
	 * @param permissionIdentify
	 * @param remark
	 */
	public PermissionEntity insertPermission(String permissionName, String permissionIdentify, String remark) 
	{
		PermissionEntity p = null;
		try {
			//验证权限名称
			p = permissionRepository.findByPermissionName(permissionName);
			
			if(p != null)
			{
				throw new ServiceException("500002");
			}
			
			p = new PermissionEntity();
			p.setPermissionName(permissionName);
			p.setPermissionIdentify(permissionIdentify);
			p.setCreateTime(new Date());
			p.setRemark(remark);
			
			p = permissionRepository.save(p);
		} catch (Exception e) {
			logger.error("系统异常", e.getMessage());
			throw new ServiceException("000004");
		}
		
		return p;
	}


	/**
	 * 编辑权限
	 * @param uid
	 * @param permissionName
	 * @param permissionIdentify
	 * @param remark
	 */
	public void updatePermission(Integer uid, String permissionName, String permissionIdentify, String remark) 
	{
		
		try {
			PermissionEntity p = permissionRepository.findOne(uid);
			
			if(p == null)
			{
				throw new ServiceException("500000");
			}
			
			//验证权限名称是否存在
			PermissionEntity permission = permissionRepository.checkPermission(uid, permissionName);
			
			if(permission != null)
			{
				throw new ServiceException("500002");
			}
			
			p.setPermissionName(permissionName == null ? p.getPermissionName() : permissionName);
			p.setPermissionIdentify(permissionIdentify == null ? p.getPermissionIdentify() : permissionIdentify);
			p.setRemark(remark == null ? p.getRemark() : remark);
			p.setUpdateTime(new Date());
			
			permissionRepository.save(p);
		} catch (Exception e) {
			logger.error("系统异常", e.getMessage());
			throw new ServiceException("000004");
		}
		
	}


	/**
	 * 删除权限
	 * @param uid
	 */
	public void deletePermission(Integer uid) 
	{
		try {
			PermissionEntity p = permissionRepository.findOne(uid);
			
			if(p == null)
			{
				throw new ServiceException("500000");
			}
			
			permissionRepository.delete(uid);
		} catch (Exception e) {
			logger.error("系统异常", e.getMessage());
			throw new ServiceException("000004");
		}
		
	}


	/**
	 * 查询权限信息
	 * @param pageNumber
	 * @param limit
	 * @param sortField
	 * @param sortType
	 * @param keyword
	 */
	public Page<PermissionEntity> getPermissions(Integer pageNumber, Integer limit, String sortField, String sortType, String keyword) 
	{
		Page<PermissionEntity> permissionPage = null;
		
		try {
			
			if(sortField == null && keyword == null)
			{
				permissionPage = permissionRepository.findAll(new PageRequest(pageNumber, limit));
			}else if (sortField != null && keyword == null)
			{
				permissionPage = permissionRepository.findAll(new PageRequest(pageNumber, limit, new Sort(Direction.fromString(sortType), sortField)));
			}else if (sortField == null && keyword != null)
			{
				keyword = "%" + keyword + "%";
				permissionPage = permissionRepository.findByRoleLike(keyword, new PageRequest(pageNumber, limit));
			}else
			{
				keyword = "%" + keyword + "%";
				permissionPage = permissionRepository.findByRoleLike(keyword, new PageRequest(pageNumber, limit, new Sort(Direction.fromString(sortType), sortField)));
			}
			

		} catch (Exception e) {
			logger.error("系统异常", e.getMessage());
			throw new ServiceException("000004");
		}
		
		return permissionPage;
		
	}


	public Set<String> findAll() {
		return permissionRepository.findAllPermissions();
	}

}
