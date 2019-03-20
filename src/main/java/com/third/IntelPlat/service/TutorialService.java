package com.third.IntelPlat.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.TutorialEntity;
import com.third.IntelPlat.exception.ServiceException;
import com.third.IntelPlat.exception.ServiceExceptionHandle;
import com.third.IntelPlat.repository.TutorialRepository;

@Component
public class TutorialService 
{
	private static Logger logger = LoggerFactory.getLogger(TutorialService.class);
	@Autowired
	private TutorialRepository tutorialRepository;
	@Autowired
	private ServiceExceptionHandle serviceExceptionHandle;

	/**
	 * 删除所有知识点数据
	 */
	public void deleteAll(Integer uid) 
	{
		List<TutorialEntity> tutorials = tutorialRepository.findTutorials(uid);
		
		for(TutorialEntity t : tutorials)
		{
			List<TutorialEntity> childrens = t.getChildren();
			
			tutorialRepository.delete(childrens);
		}
		
		tutorialRepository.delete(tutorials);
	}

	public TutorialEntity findByTutorialNo(Integer tutorialNo) 
	{
		TutorialEntity t = tutorialRepository.findOne(tutorialNo);
		
		if(t == null)
		{
			logger.error(serviceExceptionHandle.genInfoById("700001"));
			throw new ServiceException("700001");
		}
		return t;
	}

	public TutorialEntity findTutorial(String tutorialNo) {
		TutorialEntity t = tutorialRepository.findByTutorialNo(tutorialNo);
		
		if(t == null)
		{
			logger.error(serviceExceptionHandle.genInfoById("700001"));
			throw new ServiceException("700001");
		}
		return t;
	}

}
