package com.third.IntelPlat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.MultiplicationEntity;

@Component
public interface MultiplicationRepository extends CrudRepository<MultiplicationEntity, Integer>
{
	/**
	 * 根据上一个 题目的  编号  顺序取题
	 * @param sequence
	 * @return   SELECT *,RAND() as r FROM address ORDER BY r LIMIT 0,10
	 */
	@Query(value="select * from MULTIPLICATION ORDER BY sequence asc LIMIT ?1,  10", nativeQuery = true)
	List<MultiplicationEntity> getTitleBySequence(Integer sequence);
	
	/**
	 * 随机 乘法口诀表中取  num 个 乘法口诀  
	 * @param num
	 * @return
	 */
	@Query(value="select *,RAND() as r from MULTIPLICATION ORDER BY r LIMIT ?1 ", nativeQuery = true)
	List<MultiplicationEntity> getRandomByNum(Integer num);
	
	
	/**
	 * 在错题记录表中取  num 个 乘法口诀
	 * @param userId num
	 * @return
	 */
	@Query(value="select  b.* from MULT_LOG r left  JOIN  MULTIPLICATION b on r.SEQUENCE = b.SEQUENCE where r.USER_ID= ?1 ORDER BY r.COUNT DESC LIMIT ?2 ", nativeQuery = true)
	List<MultiplicationEntity> getCuoTile(@Param("userId") Integer   userId  ,@Param("num")  Integer num);
	
	
}
