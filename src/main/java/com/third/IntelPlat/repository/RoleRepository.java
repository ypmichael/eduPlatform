package com.third.IntelPlat.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.RoleEntity;

@Component
public interface RoleRepository extends CrudRepository<RoleEntity, Integer>
{

	/**
	 * 查询用户关联的角色与权限
	 * @param userId
	 * @param divisionId
	 */
	@Query(value="select new RoleEntity(r.uid, r.roleName) from RoleEntity r where r.uid in ?1 ")
	List<RoleEntity> findRoles(List<Integer> roleIds);

	RoleEntity findByRoleName(String roleName);

	@Query("select r from RoleEntity r where r.uid <> :uid and r.roleName = :roleName ")
	RoleEntity checkRoleNameByUid(@Param("uid") Integer uid, @Param("roleName") String roleName);

	@Query( value = "select r from RoleEntity r where r.roleName like concat('%', ?1, '%')")
	Page<RoleEntity> findByRoleLike(String keyword, Pageable pageable);

	@Query( value = "select r from RoleEntity r ")
	Page<RoleEntity> findRoles(Pageable pageable);

	@Query( value = "select new RoleEntity(r.uid, r.roleName) from RoleEntity r where r.uid = :uid ")
	RoleEntity findRoleByUid(@Param("uid") Integer uid);

	@Query( value = "(select ur.ROLE_UID from USER_ROLE ur where ur.USER_UID = :userId) UNION (select d.ROLE_UID from DIVISION_ROLE d where d.DIVISION_UID = :divisionId)", nativeQuery=true)
	List<Integer> findAllRoleIds(@Param("userId") Integer userId, @Param("divisionId") Integer divisionId);

}
