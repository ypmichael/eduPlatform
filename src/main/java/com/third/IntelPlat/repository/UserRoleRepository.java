package com.third.IntelPlat.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.UserRoleEntity;

@Component
public interface UserRoleRepository extends CrudRepository<UserRoleEntity, Integer> {

	@Transactional
	@Modifying
	@Query("delete from UserRoleEntity u where u.userRole.uid = :userId")
	void deleteByUserId(@Param("userId") Integer userId);


}
