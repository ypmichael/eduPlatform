package com.third.IntelPlat.repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.OperationLogEntity;

@Component
public interface OperationLogRepository extends CrudRepository<OperationLogEntity, Integer>, JpaSpecificationExecutor<OperationLogEntity>{
}
