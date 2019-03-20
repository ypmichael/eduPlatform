package com.third.IntelPlat.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.transaction.Transactional;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.common.ExcelUtil;
import com.third.IntelPlat.entity.ChoiceQstEntity;
import com.third.IntelPlat.entity.GradeEntity;
import com.third.IntelPlat.entity.TestLogEntity;
import com.third.IntelPlat.entity.TestObjectEntity;
import com.third.IntelPlat.entity.TutorialEntity;
import com.third.IntelPlat.entity.TutorialRelationEntity;
import com.third.IntelPlat.entity.UserEntity;
import com.third.IntelPlat.exception.ServiceException;
import com.third.IntelPlat.redis.RedisHelper;
import com.third.IntelPlat.repository.ChoiceQstRepository;
import com.third.IntelPlat.repository.GradeRepository;
import com.third.IntelPlat.repository.TutorialRepository;

@Component
public class ChoiceQstService 
{
	private static Logger logger = LoggerFactory.getLogger(ChoiceQstService.class);
	
	@Autowired
	private GradeRepository gradeRepository;
	@Autowired
	private TutorialRepository tutorialRepository;
	@Autowired
	private ChoiceQstRepository choiceQstRepository;
	@Autowired
	private TutorialService tutorialService;
	@Autowired
	private GradeService gradeService;
	@Autowired
	private TestLogService testLogService;
	@Autowired
	private UserService userService;
	@Autowired
	private TutorialRelationService tutorialRelationService;
	@Autowired
	private RedisHelper redisHelper;
	
	@Value("${redis.key.tutorialRelation}")
	public String tutorial2Relation;

	
	/**
	 * 题目入库
	 * @param inputStream
	 * @return
	 */
	@Transactional
	public List<Map<String, Object>> choiceQstImport(InputStream inputStream) 
	{
		List<Object> list = ExcelUtil.readExcelToObjectByPath(inputStream, TestObjectEntity.class, 0, 0);
		
//		deleteChoiceQst();
		
		if(list == null && list.size() == 0)
		{
			throw new ServiceException("Excel为空");
		}
		
		GradeEntity g = null;
		TutorialEntity t = null;
		TutorialEntity t1 = null;
		String topic = null;
		String questionType = null;
		
		for(Object obj : list)
		{
			TestObjectEntity testObject = (TestObjectEntity)obj;
			
			topic = testObject.getModuleName() != null && !"".equals(testObject.getModuleName()) ? testObject.getModuleName() : topic;
			
			if(testObject.getGradeId() != null && !"".equals(testObject.getGradeId().trim()))
			{
				g = new GradeEntity();
				g.setGradeName(String.valueOf(testObject.getGradeId()));
				g.setCreateTime(new Date());
				
				g = gradeRepository.save(g);
			}

			if(testObject.getTutorialNo() != null && !"".equals(testObject.getTutorialNo().trim()))
			{
				t = new TutorialEntity();
				t.setTutorialNo(testObject.getTutorialNo());
				t.setGrade(g);
				t.setTutorialName(testObject.getTutorialName());
				t = tutorialRepository.save(t);
				
			}
			
			if(testObject.getQuestionType() != null)
			{
				questionType = testObject.getQuestionType();
			}
			
			questionType = testObject.getQuestionType() != null && !"".equals(testObject.getQuestionType()) ? testObject.getQuestionType() : questionType;
			
			if(testObject.getSubtutorialNo() != null && !"".equals(testObject.getSubtutorialNo().trim()))
			{
				t1 = new TutorialEntity();
				t1.setTutorialNo(testObject.getSubtutorialNo());
				t1.setGrade(g);
				t1.setParent(t);
				t1.setTutorialName(testObject.getSubtutorialName());
				t1 = tutorialRepository.save(t1);
			}
			
			if(testObject.getQuestionNo() != null)
			{
				ChoiceQstEntity c = new ChoiceQstEntity();
				c.setTutorial(t1);
				c.setQuestionNo(testObject.getQuestionNo());
				c.setChoiceContent(testObject.getQuenstionContent());
				c.setPicture(testObject.getPicture() != null ? "/pictures/" + testObject.getPicture() + ".png": null);
				c.setRadio(testObject.getPicture() != null ? "/read/" + testObject.getPicture() + ".mp3": null);
				c.setChoiceA(testObject.getChoiceA() != null ? testObject.getChoiceA() : null);
				c.setChoiceB(testObject.getChoiceB() != null ? testObject.getChoiceB() : null);
				c.setChoiceC(testObject.getChoiceC() != null ? testObject.getChoiceC() : null);
				c.setQuestionType(questionType);
				c.setTopic(topic);
				c.setChoiceAnsw(testObject.getAnswer());
				c.setCreateTime(new Date());
				
				choiceQstRepository.save(c);
			}
		}
		
		return null;
	}
	
	
	@Transactional
	public List<Map<String, Object>> testChoiceQstImport(InputStream inputStream) 
	{
		List<Object> list = null;
		try {
			list = ExcelUtil.readExcelToObjectByPath(new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\excel123\\k9.xlsx")), TestObjectEntity.class, 0, 0);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		if(list == null && list.size() == 0)
		{
			throw new ServiceException("Excel为空");
		}
		
		GradeEntity g = null;
		TutorialEntity t = null;
		TutorialEntity t1 = null;
		String topic = null;
		String questionType = null;
		
		TestObjectEntity tObj = (TestObjectEntity)list.get(0);
		
		if(tObj != null && tObj.getGradeId() != null && !"".equals(tObj.getGradeId()))
		{
			String gradeId = tObj.getGradeId();
			
			deleteChoiceQst(gradeId);
		}
		
		for(Object obj : list)
		{
			TestObjectEntity testObject = (TestObjectEntity)obj;
			
			topic = testObject.getModuleName() != null && !"".equals(testObject.getModuleName()) ? testObject.getModuleName() : topic;
			
			if(testObject.getGradeId() != null && !"".equals(testObject.getGradeId().trim()))
			{
				g = new GradeEntity();
				g.setGradeName(String.valueOf(testObject.getGradeId()));
				g.setCreateTime(new Date());
				
				g = gradeRepository.save(g);
			}

			if(testObject.getTutorialNo() != null && !"".equals(testObject.getTutorialNo().trim()))
			{
				t = new TutorialEntity();
				t.setTutorialNo(testObject.getTutorialNo());
				t.setGrade(g);
				t.setTutorialName(testObject.getTutorialName());
				t = tutorialRepository.save(t);
				
			}
			
			if(testObject.getQuestionType() != null)
			{
				questionType = testObject.getQuestionType();
			}
			
			questionType = testObject.getQuestionType() != null && !"".equals(testObject.getQuestionType()) ? testObject.getQuestionType() : questionType;
			
			if(testObject.getSubtutorialNo() != null && !"".equals(testObject.getSubtutorialNo().trim()))
			{
				t1 = new TutorialEntity();
				t1.setTutorialNo(testObject.getSubtutorialNo());
				t1.setGrade(g);
				t1.setParent(t);
				t1.setTutorialName(testObject.getSubtutorialName());
				t1 = tutorialRepository.save(t1);
			}
			
			if(testObject.getQuestionNo() != null)
			{
				ChoiceQstEntity c = new ChoiceQstEntity();
				c.setTutorial(t1);
				c.setQuestionNo(testObject.getQuestionNo());
				c.setChoiceContent(testObject.getQuenstionContent());
				c.setPicture((testObject.getPicture() != null) && (!"".equals(testObject.getPicture())) ? "/pictures/" + testObject.getPicture() + ".png": null);
				c.setRadio((testObject.getPicture() != null && !"".equals(testObject.getPicture())) ? "/read/" + testObject.getPicture() + ".mp3": null);
				c.setChoiceA((testObject.getChoiceA() != null && !"".equals(testObject.getChoiceA())) ? testObject.getChoiceA() : null);
				c.setChoiceB((testObject.getChoiceB() != null && !"".equals(testObject.getChoiceB())) ? testObject.getChoiceB() : null);
				c.setChoiceC((testObject.getChoiceC() != null && !"".equals(testObject.getChoiceC())) ? testObject.getChoiceC() : null);
				c.setQuestionType(questionType);
				c.setTopic(topic);
				c.setChoiceAnsw(testObject.getAnswer());
				c.setCreateTime(new Date());
				
				choiceQstRepository.save(c);
			}
		}
		
		return null;
	}
	
	
	@Transactional
	public void deleteChoiceQst(String gradeId) 
	{
		GradeEntity grade = gradeService.findByGradeName(gradeId);
		
		if(grade != null)
		{
			//通过年级唯一编号查询所有知识点
			Integer uid = grade.getUid();
			
			List<Integer> tutorials = tutorialRepository.findByGradeId(uid);
			if(tutorials != null && tutorials.size() != 0)
			{
				choiceQstRepository.deleteAll(tutorials);
			}
			
			tutorialService.deleteAll(uid);
			
			gradeRepository.delete(uid);
			
		}
		
	}

	/**
	 * 查询题目
	 */
	public ChoiceQstEntity findQst() 
	{
		
		Subject subject = SecurityUtils.getSubject();
		String userName = (String) subject.getPrincipal();
		
		UserEntity u = userService.findByAccount(userName);
		
		String gradeName = "k";
		
		if(u != null && u.getGrade() != null)
		{
			gradeName = u.getGrade();
		}
		
		GradeEntity grade = gradeService.findByGradeName(gradeName);
		
		if(grade == null)
		{
			throw new ServiceException("该年级没有相关的练习题");
		}
		
		TestLogEntity testLog = findTopic(userName);
		
		ChoiceQstEntity choiceQst = null;
		
		
		if(testLog == null)
		{
			choiceQst = initChoiceQst(grade);
			
			TutorialEntity tutorial = tutorialService.findByTutorialNo(choiceQst.getTutorialUid());
			
			choiceQst.setTutorialName(tutorial.getTutorialName());
			choiceQst.setTutorialNo(tutorial.getTutorialNo());
			
			boolean flag = testLogService.checkFileExists(choiceQst.getQuestionNo() + ".mp3");
			if(flag)
			{
				choiceQst.setRadio("/read/"+choiceQst.getQuestionNo() + ".mp3");
			}else
			{
				choiceQst.setRadio(null);
			}
			
			return choiceQst;
		}
		
		TutorialEntity tutorial = tutorialService.findByTutorialNo(testLog.getTutorialUid());
		
		//1. 判断同一个知识点是否连续做两道题以上
 		boolean isTrue = checkTitleNumber(userName, testLog.getTutorialUid());
		
		if(!isTrue)
		{
			choiceQst = randomQuery(tutorial.getUid(), testLog.getQuestionNumber(), grade);
			
			TutorialEntity t = tutorialService.findByTutorialNo(choiceQst.getTutorialUid());
			choiceQst.setTutorialName(t.getTutorialName());
			choiceQst.setTutorialNo(t.getTutorialNo());
			boolean flag = testLogService.checkFileExists(choiceQst.getQuestionNo() + ".mp3");
			if(flag)
			{
				choiceQst.setRadio("/read/"+choiceQst.getQuestionNo() + ".mp3");
			}else
			{
				choiceQst.setRadio(null);
			}
			
			return choiceQst;
		}
		
		//2. 判断当前知识点的错误率
//		boolean flag = checkWrongTopic(userName);
		
		//3. 判断是否有跳转的知识点
		Integer tUid = checkSkipTutorial(tutorial, userName);
		
		if(tUid != null)
		{
			//通过知识点获取题目
//			choiceQst = findSkipQst(tUid);
			
			TutorialEntity t = tutorialService.findByTutorialNo(tUid);
			
			List<ChoiceQstEntity> choiceQsts = this.choiceQstRepository.randomQuery(tUid, testLog.getQuestionNumber());
			
			if(choiceQsts == null)
			{
				choiceQsts = t.getChoiceQsts();
			}
			
			if(choiceQsts != null && choiceQsts.size() != 0)
			{
				Collections.shuffle(choiceQsts);
				
				ChoiceQstEntity qst = choiceQsts.get(0);
				qst.setTutorialUid(tUid);
				qst.setTutorialName(t.getTutorialName());
				qst.setTutorialNo(t.getTutorialNo());
				boolean flag = testLogService.checkFileExists(qst.getQuestionNo() + ".mp3");
				if(flag)
				{
					qst.setRadio("/read/"+qst.getQuestionNo() + ".mp3");
				}else
				{
					qst.setRadio(null);
				}
				
				return qst;
			}
		}
		
//		if(flag)//向下查找
//		{
//			TutorialEntity t = upperTutorial(grade, tutorial, 1);
//			
//			choiceQst = randomQuery(t.getUid(), testLog.getQuestionNo(),grade);
//		}else
//		{//向上查找
//			TutorialEntity t = upperTutorial(grade, tutorial, 0);
//			
//			choiceQst = randomQuery(t.getUid(), testLog.getQuestionNo(),grade);
//		}
		
		TutorialEntity t = upperTutorial(grade, tutorial, 1);
		
		choiceQst = randomQuery(t.getUid(), testLog.getQuestionNumber(), grade);
		
		TutorialEntity t1 = tutorialService.findByTutorialNo(choiceQst.getTutorialUid());
		
		choiceQst.setTutorialName(t1.getTutorialName());
		choiceQst.setTutorialNo(t1.getTutorialNo());
		boolean flag = testLogService.checkFileExists(choiceQst.getQuestionNo() + ".mp3");
		if(flag)
		{
			choiceQst.setRadio("/read/"+choiceQst.getQuestionNo() + ".mp3");
		}else
		{
			choiceQst.setRadio(null);
		}
		
		return choiceQst;
	}
	
	/**
	 * 查询跳转知识点
	 * @param tUid
	 * @return
	 */
	private ChoiceQstEntity findSkipQst(Integer tUid) {
		
		TutorialEntity tutorial = tutorialService.findByTutorialNo(tUid);
		
		List<TutorialEntity> subTutorials = tutorial.getChildren();
	    
		if(subTutorials != null)
		{
			Iterator<TutorialEntity> subIter = subTutorials.iterator();
			
			while(subIter.hasNext()){
				
				TutorialEntity subTutorial = subIter.next();
				
				List<ChoiceQstEntity> choiceQsts = subTutorial.getChoiceQsts();
				
				if(choiceQsts != null && choiceQsts.size() != 0)
				{
					ChoiceQstEntity choiceQst = choiceQsts.get(0);
					choiceQst.setTutorialUid(subTutorial.getUid());
					
					return choiceQst;
				}
			}
		}
		return null;
		
	}
	
	private Integer checkSkipTutorial(TutorialEntity tutorial, String userName) 
	{
		if(redisHelper.hashExist(tutorial2Relation, userName))
		{
			String relations = redisHelper.hashGet(tutorial2Relation, userName);
			
			String[] relationArray = relations.split(",", -1);
			
			TreeSet<String> relationList = new TreeSet<String>();
			
			for(String a : relationArray)
			{
				relationList.add(a);
			}
			
			boolean flag = checkWrongTopic(userName);
			
			if(flag)
			{
				if(relationList.higher(tutorial.getTutorialNo()) != null)
				{
					String tutorialNo = relationList.higher(tutorial.getTutorialNo());
					
					TutorialEntity t = tutorialService.findTutorial(tutorialNo);
					
					List<ChoiceQstEntity> choiceQsts = t.getChoiceQsts();
					
					if(choiceQsts == null || choiceQsts.size() == 0)
					{
						return checkSkipTutorial(t, userName);
					}
					
					return t.getUid();
				}else
				{
					//删除缓存
					redisHelper.hashDel(tutorial2Relation, userName);
				}
			}else
			{
				if(relationList.lower(tutorial.getTutorialNo()) != null)
				{
					String tutorialNo = relationList.lower(tutorial.getTutorialNo());
					
					TutorialEntity t = tutorialService.findTutorial(tutorialNo);
					
					List<ChoiceQstEntity> choiceQsts = t.getChoiceQsts();
					
					if(choiceQsts == null || choiceQsts.size() == 0)
					{
						return checkSkipTutorial(t, userName);
					}
					
					return t.getUid();
				}
				
				return tutorial.getUid();
			}
		}else
		{
			//3.1 判断当前知识点是否存在相关联知识点
			TutorialRelationEntity tutorialRelation = tutorialRelationService.findByTutorialNo(tutorial.getTutorialNo());
			
			if(tutorialRelation != null)
			{
				boolean flag = checkWrongTopic(userName);
				
				if(flag)
				{
					return null;
				}
				
				String[] relations = tutorialRelation.getTutorialRelation().split(",", -1);
				
				TreeSet<String> relationList = new TreeSet<String>();
				
				for(String a : relations)
				{
					relationList.add(a);
				}
				
				relationList.add(tutorial.getTutorialNo());
				
				redisHelper.hashSet(tutorial2Relation, userName, tutorialRelation.getTutorialRelation() + "," + tutorial.getTutorialNo());
				
				String tutorialNo = relationList.lower(tutorial.getTutorialNo());
				
				TutorialEntity t = tutorialService.findTutorial(tutorialNo);
				
				List<ChoiceQstEntity> choiceQsts = t.getChoiceQsts();
				
				if(choiceQsts == null || choiceQsts.size() == 0)
				{
					return checkSkipTutorial(t, userName);
				}
				
				return t.getUid();
			}
		}
		return null;
		
	}

//	private Integer checkSkipTutorial(TutorialEntity tutorial, String userName) 
//	{
//		
//		if(redisHelper.hashExist(tutorial2Relation, userName))
//		{
//			String relations = redisHelper.hashGet(tutorial2Relation, userName);
//			
//			String[] relationArray = relations.split(",", -1);
//			
//			TreeSet<Integer> relationList = new TreeSet<Integer>();
//			
//			for(String a : relationArray)
//			{
//				relationList.add(Integer.valueOf(a));
//			}
//			
//			boolean flag = checkWrongTopic(userName);
//			
//			if(flag)
//			{
//				if(relationList.higher(tutorial.getUid()) != null)
//				{
//					int tutorialUid = relationList.higher(tutorial.getUid());
//					
//					TutorialEntity t = tutorialService.findByTutorialNo(tutorialUid);
//					
//					if(t.getChildren() == null || t.getChildren().size() == 0)
//					{
//						return checkSkipTutorial(t, userName);
//					}
//					
//					return tutorialUid;
//				}else
//				{
//					//删除缓存
//					redisHelper.hashDel(tutorial2Relation, userName);
//				}
//			}else
//			{
//				if(relationList.lower(tutorial.getUid()) != null)
//				{
//					int tutorialUid = relationList.lower(tutorial.getUid());
//					
//					TutorialEntity t = tutorialService.findByTutorialNo(tutorialUid);
//					
//					if(t.getChildren() == null || t.getChildren().size() == 0)
//					{
//						return checkSkipTutorial(t, userName);
//					}
//					
//					return tutorialUid;
//				}
//			}
//		}else
//		{
//			
//			//3.1 判断当前知识点是否存在相关联知识点
//			TutorialRelationEntity tutorialRelation = tutorialRelationService.findByTutorialUid(tutorial.getUid());
//			
//			if(tutorialRelation != null)
//			{
//				boolean flag = checkWrongTopic(userName);
//				
//				if(flag)
//				{
//					return null;
//				}
//				
//				String[] relations = tutorialRelation.getTutorialRelation().split(",", -1);
//				
//				TreeSet<Integer> relationList = new TreeSet<Integer>();
//				
//				for(String a : relations)
//				{
//					relationList.add(Integer.valueOf(a));
//				}
//				
//				relationList.add(tutorial.getUid());
//				
//				redisHelper.hashSet(tutorial2Relation, userName, tutorialRelation.getTutorialRelation() + "," + tutorial.getUid());
//				
//				int tutorialUid = relationList.lower(tutorial.getUid());
//				
//				TutorialEntity t = tutorialService.findByTutorialNo(tutorialUid);
//				
//				if(t.getChildren() == null || t.getChildren().size() == 0)
//				{
//					return checkSkipTutorial(t, userName);
//				}
//				
//				return tutorialUid;
//			}
//		}
//		return null;
//		
//	}


	private boolean checkTitleNumber(String userName, Integer tutorialUid) {
		
		//判断当前知识点对应题目的数量
		int qstNum = choiceQstRepository.findByTutorialUid(tutorialUid);
		
		if(qstNum < 2)
		{
			return true;
		}
		
		int num = testLogService.checkTitleNumber(userName, tutorialUid);
		
		if(num >= 2)
		{
			return true;
		}
		
		return false;
	}


	/**
	 * 检查错误题目数量
	 * @return
	 */
	public boolean checkWrongTopic(String userName) 
	{
		int num = testLogService.checkWrongTopic(userName);
		
		if(num >= 2)
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * 获取最近一道题目的信息
	 * @return
	 */
	public TestLogEntity findTopic(String userName) 
	{
		TestLogEntity TestLogEntity = testLogService.findTopic(userName);
		
		return TestLogEntity;
	}
	
	/**
	 * 查询相关的知识点
	 * @param tutorial
	 * @param status
	 * @return
	 */
	public TutorialEntity upperTutorial(GradeEntity grade, TutorialEntity tutorial, Integer status) 
	{
		List<TutorialEntity> tutorials = tutorialRepository.findTutorialList(grade.getUid());
		
//		List<TutorialEntity> tutorials = tutorialRepository.findTutorial();
		
		for(TutorialEntity t : tutorials)
		{
			List<TutorialEntity> subTutorials = t.getChildren();
			
			if(subTutorials.contains(tutorial))
			{
				TreeSet<TutorialEntity> set = new TreeSet<TutorialEntity>();
				
				set.addAll(subTutorials);
				
				if(status == 0)//向上查找,1向下查找
				{
					TutorialEntity tutorialUp = (TutorialEntity) set.lower(tutorial);
					
					if(tutorialUp == null)
					{
						TutorialEntity paentT = tutorial.getParent();
						
						set = new TreeSet<TutorialEntity>();
						set.addAll(tutorials);
						tutorialUp = checkSubTutorial(set, paentT, status);
					}
					
					if(tutorialUp != null)
					{
						return tutorialUp;
					}
					
					return tutorial;
					
				}else
				{
					TutorialEntity higTutorial = (TutorialEntity) set.higher(tutorial);
					
					List<ChoiceQstEntity> qsts = higTutorial == null ? new ArrayList<ChoiceQstEntity>() : higTutorial.getChoiceQsts();
					
					if(higTutorial == null || qsts.size() == 0)
					{
						TutorialEntity parentTutorial = tutorial.getParent();
						
						set = new TreeSet<TutorialEntity>();
						set.addAll(tutorials);
						higTutorial = checkSubTutorial(set, parentTutorial, status);
					}
					
					if(higTutorial != null)
					{
						return higTutorial;
					}
					
					return tutorial;
				}
			}
		}
		
		return tutorial;
	}
	
	/**
	 * 验证子节点
	 * @param tutorialEntity
	 * @return
	 */
	public TutorialEntity checkSubTutorial(TreeSet<TutorialEntity> tutorials, TutorialEntity parentTutorial, Integer status) 
	{
		
		if(tutorials.contains(parentTutorial))
		{
			if(status == 0)//向上查找
			{
				TutorialEntity parentTutorialUp = (TutorialEntity) tutorials.lower(parentTutorial);
				
				if(parentTutorialUp == null)
				{
					return null;
				}
				
				List<TutorialEntity> subTutorials = parentTutorialUp.getChildren();
				
				if(subTutorials != null && subTutorials.size() != 0)
				{
					TreeSet<TutorialEntity> set = new TreeSet<TutorialEntity>();
					set.addAll(subTutorials);
					
					TutorialEntity t = set.last();
					
					if(t.getChoiceQsts() != null && t.getChoiceQsts().size() != 0)
					{
						return t;
					}else
					{
						boolean flag = true;
						
						while (flag) {
							
							t = set.lower(t);
							
							if(t != null)
							{
								if(t.getChildren() != null && t.getChildren().size() != 0)
								{
									flag = false;
								}
							}else
							{
								return checkSubTutorial(tutorials, parentTutorialUp, status);
							}
				        }
						
						return t;
					}
					
				}else
				{
					return checkSubTutorial(tutorials, parentTutorialUp, status);
				}
			}else
			{
				TutorialEntity parentTutorialDown = (TutorialEntity) tutorials.higher(parentTutorial);
				
				if(parentTutorialDown == null)
				{
					return null;
				}
				
				List<TutorialEntity> subTutorials = parentTutorialDown.getChildren();
				
				if(subTutorials != null && subTutorials.size() != 0)
				{
					TreeSet<TutorialEntity> set = new TreeSet<TutorialEntity>();
					set.addAll(subTutorials);
					
					TutorialEntity t = set.first();
					
					if(t.getChoiceQsts() != null && t.getChoiceQsts().size() != 0)
					{
						return t;
					}else
					{
						boolean flag = true;
						
						while (flag) {
							
							t = set.higher(t);
							
							if(t != null)
							{
								if(t.getChildren() != null && t.getChildren().size() != 0)
								{
									flag = false;
								}
							}else
							{
								return checkSubTutorial(tutorials, parentTutorialDown, status);
							}
				        }
						
						return t;
					}
					
				}else
				{
					return checkSubTutorial(tutorials, parentTutorialDown, status);
				}
			}
		}
		
		return null;
	}
	
	
	
	/**
	 * 随机取题
	 * @param tutorialUid
	 * @param integer 
	 * @return
	 */
	public ChoiceQstEntity randomQuery(Integer tutorialUid, String questionNo, GradeEntity grade)
	{
		
		logger.info("tutorialUid::::" + tutorialUid);
		
		List<ChoiceQstEntity> choiceQsts = choiceQstRepository.randomQuery(tutorialUid, questionNo);
		
		if(choiceQsts == null || choiceQsts.size() == 0)
		{
			choiceQsts = choiceQstRepository.randomQuery(tutorialUid, questionNo);
		}
		
		Collections.shuffle(choiceQsts);
		
		ChoiceQstEntity choiceQst = null;
		
		if(choiceQsts!= null && choiceQsts.size() != 0)
		{
			choiceQst = choiceQsts.get(0);
			choiceQst.setTutorialUid(tutorialUid);
		}else
		{
			choiceQst = initChoiceQst(grade);
		}
		
		logger.info("Query Qst::::::::::::" + choiceQst);
		
		return choiceQst;
	}
	
	public ChoiceQstEntity initChoiceQst(GradeEntity grade) 
	{
		
		List<TutorialEntity> tutorials = tutorialRepository.findTutorials(grade.getUid());
		
		if(tutorials != null)
		{
			Iterator<TutorialEntity> iter = tutorials.iterator();
			while(iter.hasNext()){
				
				TutorialEntity tutorial = iter.next();
				
				List<TutorialEntity> subTutorials = tutorial.getChildren();
			    
				if(subTutorials != null)
				{
					Iterator<TutorialEntity> subIter = subTutorials.iterator();
					
					while(subIter.hasNext()){
						
						TutorialEntity subTutorial = subIter.next();
						
						List<ChoiceQstEntity> choiceQsts = subTutorial.getChoiceQsts();
						
						if(choiceQsts != null && choiceQsts.size() != 0)
						{
							ChoiceQstEntity choiceQst = choiceQsts.get(0);
							choiceQst.setTutorialUid(subTutorial.getUid());
							
							return choiceQst;
						}
					}
				}else
				{
					List<ChoiceQstEntity> choiceQsts = tutorial.getChoiceQsts();
					
					if(choiceQsts != null)
					{
						ChoiceQstEntity choiceQst = choiceQsts.get(0);
						choiceQst.setTutorialUid(tutorial.getUid());
						return choiceQst;
					}
				}
			}
		}else
		{
			throw new ServiceException("该年级没有相关的练习题");
		}
		return null;
		
	}

	/**
	 * 答案提交
	 * @param tutorialUid
	 * @param choiceContent
	 * @param picture
	 * @param radio
	 * @param result
	 * @param answ
	 * @param studentOptAnsw
	 * @param duration
	 * @param startTime
	 * @param endTime
	 * @param questionNo 
	 */
	@Transactional
	public void submitQst(Integer tutorialUid, String choiceContent, String picture, String radio, Integer result,
			String answ, String studentOptAnsw, String duration, String startTime, String endTime, Integer questionNo, String questionType) 
	{
		Subject subject = SecurityUtils.getSubject();
		
		String userName = (String) subject.getPrincipal();
		
		UserEntity u = userService.findByAccount(userName);
		
		if(u == null)
		{
			throw new ServiceException("100003");
		}
		
		TutorialEntity t = tutorialService.findByTutorialNo(tutorialUid);
		
		if(t == null)
		{
			throw new ServiceException("700002");
		}
		
		ChoiceQstEntity choiceQst = (ChoiceQstEntity)this.choiceQstRepository.findOne(questionNo);
		
	    TestLogEntity testLog = new TestLogEntity();
	    testLog.setUserUid(u.getUid());
	    testLog.setUserName(userName);
	    testLog.setTutorialUid(t.getUid());
	    testLog.setTutorialName(t.getTutorialName());
	    testLog.setTutorialNo(t.getTutorialNo());
	    testLog.setChoiceContent(choiceContent);
	    testLog.setPicture(picture);
	    testLog.setRadio(radio);
	    testLog.setResult(result.intValue());
	    testLog.setAnsw(answ);
	    testLog.setStudentOptAnsw(studentOptAnsw);
	    testLog.setDuration(Long.valueOf(Long.parseLong(duration)));
	    testLog.setStartTime(new Date(Long.parseLong(startTime)));
	    testLog.setEndTime(new Date(Long.parseLong(endTime)));
	    testLog.setCreateTime(new Date());
	    testLog.setQuestionNo(questionNo);
	    testLog.setQuestionNumber(choiceQst == null ? null : choiceQst.getQuestionNo());
	    testLog.setQuestionType(questionType);
		
		testLogService.save(testLog);
		
	}

	/**
	 * 查询题库信息
	 * @return
	 */
	public List<GradeEntity> queryQsts() 
	{
		List<GradeEntity> grades = gradeService.findAlls();
		
		return grades;
		
	}

	/**
	 * 获取学生正确答题数据
	 * @return
	 */
	public Integer queryCorrectAnswer() 
	{
		Subject subject = SecurityUtils.getSubject();
		
		String userName = (String) subject.getPrincipal();
		
		UserEntity u = userService.findByAccount(userName);
		
		Integer correctAnswerNum = testLogService.queryCorrectAnswer(u.getUid());
		
		return correctAnswerNum;
	}

	/**
	 * 清除学生分数
	 * @param userNames
	 */
	public void deleteStudentScore(List<String> userNames) 
	{
		testLogService.deleteStudentScore(userNames);
		
	}
}
