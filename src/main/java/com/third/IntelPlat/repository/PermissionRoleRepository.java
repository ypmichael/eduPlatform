package com.third.IntelPlat.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.PermissionRoleEntity;


@Component
public interface PermissionRoleRepository extends CrudRepository<PermissionRoleEntity, Integer>
{

	@Transactional
	@Modifying
	@Query("delete from PermissionRoleEntity p where p.rolePermission.uid = :roleId")
	void deleteByRoleId(@Param("roleId") Integer roleId);
	
	

}
