package com.third.IntelPlat.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.PermissionEntity;

@Component
public interface PermissionRepository  extends CrudRepository<PermissionEntity, Integer>
{

	/**
	 * 查询角色对应的权限
	 * @param roleIds
	 * @return
	 */
	
	@Query(value="select PERMISSION_IDENTIFY from PERMISSION where UID in ( select PERMISSION_UID from ROLE_PERMISSION where ROLE_UID in (:roleIds) )", nativeQuery=true)
	Set<String> findPermissons(@Param("roleIds") List<Integer> roleIds);

	/**
	 * 通过权限名称查询权限信息
	 * @param permissionName
	 * @return
	 */
	PermissionEntity findByPermissionName(String permissionName);

	@Query("select p from PermissionEntity p where p.uid <> :uid and p.permissionName = :permissionName ")
	PermissionEntity checkPermission(@Param("uid") Integer uid, @Param("permissionName")  String permissionName);

	Page<PermissionEntity> findAll(Pageable pageable);

	@Query( value = "select p from PermissionEntity p where p.permissionName like concat('%', ?1, '%')")
	Page<PermissionEntity> findByRoleLike(String keyword, Pageable pageable);

	@Query(value="select * from PERMISSION where UID in ( select PERMISSION_UID from ROLE_PERMISSION where ROLE_UID = :uid )", nativeQuery=true)
	List<PermissionEntity> getPermissions(@Param("uid") Integer uid);

	@Query(value="select PERMISSION_IDENTIFY from PERMISSION", nativeQuery=true)
	Set<String> findAllPermissions();
	
}
