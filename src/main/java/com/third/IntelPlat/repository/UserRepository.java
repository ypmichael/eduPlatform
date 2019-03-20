package com.third.IntelPlat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.UserEntity;

@Component
public interface UserRepository extends CrudRepository<UserEntity, Integer> {

	UserEntity findByUserName(String account);

	@Query("select u from UserEntity u where u.uid = :userId and u.status != 0 ")
	UserEntity findByUid(@Param("userId") Integer userId);

	/**
	 * 判断用户名是否存在
	 * @param userId
	 * @param userName
	 * @return
	 */
	@Query("select u from UserEntity u where u.uid <> :userId and u.userName = :userName and u.status !=0 ")
	UserEntity checkUserAccount(@Param("userId") Integer userId, @Param("userName") String userName);

	@Query("select u from UserEntity u where u.status != 0 and  u.division.uid = :divisionUid ")
	Page<UserEntity> findByDivisionUid(@Param("divisionUid") Integer divisionUid, Pageable pageable);

	@Query( value = "select u from UserEntity u where u.division.uid = ?1 and u.status !=0 and u.userName like concat('%', ?2, '%')")
	Page<UserEntity> findByAccountLike(@Param("divisionUid") Integer divisionUid, @Param("keyword") String keyword, Pageable pageable);

	@Query("select u from UserEntity u where u.status != 0 ")
	Page<UserEntity> findByDivisionUid(Pageable pageable);

	@Query( value = "select u from UserEntity u where u.status !=0 and u.userName like concat('%', ?1, '%')")
	Page<UserEntity> findByAccountLike(String keyword, Pageable pageable);
	
	@Query( value = "select distinct u from UserEntity u where u.status !=0 and u.token = ?1")
	UserEntity findByToken(String token);

}
