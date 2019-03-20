package com.third.IntelPlat.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.DivisionRoleEntity;


@Component
public interface DivisionRoleRepository extends CrudRepository<DivisionRoleEntity, Integer>
{

	/**
	 * 通过部门部门编号删除部门相关的角色
	 * @param divisionId
	 */
	
	@Transactional
	@Modifying
	@Query("delete from DivisionRoleEntity d where d.divisionRole.uid = :divisionId")
	void deleteByDivisionId(@Param("divisionId") Integer divisionId);

}
