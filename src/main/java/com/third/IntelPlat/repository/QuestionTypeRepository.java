package com.third.IntelPlat.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.TutorialEntity;

@Component
public interface QuestionTypeRepository extends CrudRepository<TutorialEntity, Integer>
{
	

}
