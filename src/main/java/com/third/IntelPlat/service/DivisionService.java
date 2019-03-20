package com.third.IntelPlat.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.DivisionEntity;
import com.third.IntelPlat.entity.DivisionRoleEntity;
import com.third.IntelPlat.entity.RoleEntity;
import com.third.IntelPlat.exception.ServiceException;
import com.third.IntelPlat.repository.DivisionRepository;
import com.third.IntelPlat.repository.DivisionRoleRepository;
import com.third.IntelPlat.repository.RoleRepository;
import com.third.IntelPlat.rest.Result;


@Component
public class DivisionService {
	
	@Autowired
	private DivisionRepository divisionRepository;
	@Autowired
	private DivisionRoleRepository divisionRoleRepository;
	@Autowired
	private RoleRepository roleRepository;
	
	private static Logger logger = LoggerFactory.getLogger(DivisionService.class);

	/**
	 * 新增部门
	 * @param divisionName
	 * @param parentDivisionUid
	 * @param description
	 * @return
	 */
	public DivisionEntity insert(String divisionName, Integer parentDivisionUid, String description) 
	{
		DivisionEntity parent = null;
		
		if(parentDivisionUid != null)//增加子部门
		{
			//判断父部门是否存在
			parent = existsParentDivision(parentDivisionUid);
			
			if(parent == null)
			{
				throw new ServiceException("300002");
			}
			
		}
		
		//判断部门名称是否存在
		DivisionEntity divisionInfo = existsDivisionName(divisionName);
		
		if(divisionInfo != null)
		{
			throw new ServiceException("300003");
		}
		
		DivisionEntity d = new DivisionEntity();
		d.setDivisionName(divisionName);
		d.setParent(parent);
		d.setCreateTime(new Date());
		
		d = divisionRepository.save(d);
		
		return d;
	}

	/**
	 * 判断部门名称是否存在
	 */
	private DivisionEntity existsDivisionName(String divisionName) 
	{
		DivisionEntity d = divisionRepository.findByDivisionName(divisionName);
		return d;
	}

	/**
	 * 判断是否存在父部门
	 * @param parentDivisionUid 
	 * @return
	 */
	private DivisionEntity existsParentDivision(Integer parentDivisionUid) 
	{
		DivisionEntity d = divisionRepository.findOne(parentDivisionUid);
		return d;
	}

	/**
	 * 修改部门信息
	 * @param uid
	 * @param divisionName
	 * @param parentDivisionUid
	 * @param description
	 * @return
	 */
	public void update(Integer uid, String divisionName, Integer parentDivisionUid, String description) {
		
		DivisionEntity division = existsByUidAndDivisionName(uid, divisionName);
		
		if(division != null)
		{
			throw new ServiceException("300003");
		}
		
		DivisionEntity d = divisionRepository.findOne(uid);
		
		if(d == null)
		{
			throw new ServiceException("300001");
		}
		
		DivisionEntity parent = null;
		
		if(parentDivisionUid != null)//增加子部门
		{
			//判断父部门是否存在
			parent = existsParentDivision(parentDivisionUid);
		}
		
		d.setDivisionName(divisionName == null ? d.getDivisionName() : divisionName);
		d.setParent(parent);
		d.setDescription(description == null ? d.getDescription(): description);
		
		divisionRepository.save(d);
	}

	/**
	 * 通过部门编号与部门名称判断部门是否存在
	 * @param divisionName 
	 * @param uid 
	 */
	private DivisionEntity existsByUidAndDivisionName(Integer uid, String divisionName) 
	{
		DivisionEntity d = divisionRepository.existsByUidAndDivisionName(uid, divisionName);
		return d;
	}

	/**
	 * 删除部门信息
	 * @param uid
	 * @return
	 */
	public void delete(Integer uid) 
	{
		DivisionEntity d = divisionRepository.findOne(uid);
		
		if(d == null)
		{
			throw new ServiceException("300001");
		}
		
		if(d.getUsers() == null || d.getUsers().size() == 0)
		{
			divisionRepository.delete(uid);
		}else
		{
			throw new ServiceException("300006");
		}
	}

	public DivisionEntity findByUid(Integer divisionId) {
		
		return divisionRepository.findOne(divisionId);
	}

	
	/**
	 * 部门分配角色
	 * @param divisionId
	 * @param roleIds
	 */
	public Result divisionAssignRole(Integer divisionId, List<Integer> roleIds) 
	{
		//TODO
		Result result = new Result();
		
		try {
			divisionRoleRepository.deleteByDivisionId(divisionId);
			
			DivisionEntity d = divisionRepository.findOne(divisionId);
			
			if(d == null)
			{
				throw new ServiceException("300001");
			}
			
			for(Integer roleId : roleIds)
			{
				RoleEntity r = roleRepository.findOne(roleId);
				
				if(r == null)
				{
					throw new ServiceException("200001");
				}
				
				DivisionRoleEntity userRole = new DivisionRoleEntity();
				userRole.setRoleDivision(r);
				userRole.setDivisionRole(d);
				
				divisionRoleRepository.save(userRole);
			}
		} catch (Exception e) {
			logger.error("系统异常", e.getMessage());
			throw new ServiceException("000004");
		}
		
		result.setStatus(1);
		result.setCode("");
		result.setInfo("部门分配角色成功");
		return result;
		
	}


	public List<DivisionEntity> getDivisions(Integer divisionId) {
		
		DivisionEntity d = divisionRepository.findOne(divisionId);
		
		if(d == null)
		{
			throw new ServiceException("300001");
		}
		
		return d.getChildren();
	}
	
	
	public List<DivisionEntity> findAll() 
	{
		List<DivisionEntity> divisions = (List<DivisionEntity>) divisionRepository.findAll();
		
		return divisions;
	}
	

	/**
	 * 获取所有部门
	 * @return
	 */
	public List<DivisionEntity> selectDivisions() 
	{
		List<DivisionEntity> divisions = divisionRepository.selectDivisions();
		
		for(DivisionEntity d : divisions)
		{
			getConverObj(d);
			
			d.setValue(d.getUid());
			d.setLabel(d.getDivisionName());
		}
		
		return divisions;
	}
	
	
	public static void getConverObj(DivisionEntity division) {
		
		List<DivisionEntity> divisions = division.getChildren();
		
		if (divisions != null && divisions.size() != 0) {
			
			for (DivisionEntity d : divisions) {
				
				d.setValue(d.getUid());
				d.setLabel(d.getDivisionName());
				
				if (d.getChildren() != null && d.getChildren().size() != 0) {
					
					getConverObj(d);
				} else
				{
					d.setChildren(null);
				}
			}

		}
	}
}
