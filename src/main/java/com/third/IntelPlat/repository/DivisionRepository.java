package com.third.IntelPlat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.DivisionEntity;


@Component
public interface DivisionRepository extends CrudRepository<DivisionEntity, Integer>
{

	/**
	 * 判断部门名称是否存在
	 * @param divisionName
	 * @return
	 */
	DivisionEntity findByDivisionName(String divisionName);

	/**
	 * 通过部门编号与部门名称判断部门是否存在
	 * @param uid
	 * @param divisionName
	 * @return
	 */
	@Query(value="select d from DivisionEntity d where d.divisionName = :divisionName and d.uid <> :uid")
	DivisionEntity existsByUidAndDivisionName(@Param("uid") Integer uid, @Param("divisionName") String divisionName);

	@Query(value = "select * from DIVISION d WHERE d.PARENT_DIVISION_UID IS NULL ", nativeQuery = true)
	List<DivisionEntity> selectDivisions();

}
