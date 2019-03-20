package com.third.IntelPlat.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.LoginLogEntity;


@Component
public interface LoginLogRepository extends CrudRepository<LoginLogEntity, Integer>, JpaSpecificationExecutor<LoginLogEntity>
{
	


}
