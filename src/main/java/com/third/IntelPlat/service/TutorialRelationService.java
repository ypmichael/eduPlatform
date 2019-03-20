package com.third.IntelPlat.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.TutorialRelationEntity;
import com.third.IntelPlat.repository.TutorialRelationRepository;

@Component
public class TutorialRelationService 
{
	private static Logger logger = LoggerFactory.getLogger(TutorialRelationService.class);
	
	@Autowired
	private TutorialRelationRepository tutorialRelationRepository;

	public TutorialRelationEntity findByTutorialNo(String tutorialNo) 
	{
		TutorialRelationEntity tutorialRelation = tutorialRelationRepository.findByTutorialNo(tutorialNo);
		return tutorialRelation;
	}

	public List<TutorialRelationEntity> findAll() 
	{
		List<TutorialRelationEntity> tutorialRelations = (List<TutorialRelationEntity>) tutorialRelationRepository.findAll();
		return tutorialRelations;
	}
	
	
	

}
