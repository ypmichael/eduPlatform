package com.third.IntelPlat.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.GradeEntity;

@Component
public interface GradeRepository extends CrudRepository<GradeEntity, Integer>
{
	/**
	 * 新增年级信息
	 * @param gradeName
	 * @return
	 */
	GradeEntity findByGradeName(String gradeName);
	

}
