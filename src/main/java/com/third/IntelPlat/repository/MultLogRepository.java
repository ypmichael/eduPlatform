package com.third.IntelPlat.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.MultLog;
@Component
public interface MultLogRepository extends CrudRepository<MultLog, Integer>{

	@Query(value="select * from  MULT_LOG  a where a.USER_ID = ?1 and a.SEQUENCE = ?2", nativeQuery = true)
	MultLog getMarkByUserIdAndSequence(Integer userId, Integer sequence);
	

}
