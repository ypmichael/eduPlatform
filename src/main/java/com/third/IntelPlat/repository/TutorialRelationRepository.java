package com.third.IntelPlat.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.TutorialRelationEntity;


@Component
public interface TutorialRelationRepository extends CrudRepository<TutorialRelationEntity, Integer>
{

	TutorialRelationEntity findByTutorialNo(String tutorialNo);

}
