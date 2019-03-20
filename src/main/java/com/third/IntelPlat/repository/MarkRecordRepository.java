package com.third.IntelPlat.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.MarkRecordEntity;
@Component
public interface MarkRecordRepository extends CrudRepository<MarkRecordEntity, Integer>{

}
