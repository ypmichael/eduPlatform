package com.third.IntelPlat.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.GradeEntity;
import com.third.IntelPlat.exception.ServiceException;
import com.third.IntelPlat.exception.ServiceExceptionHandle;
import com.third.IntelPlat.repository.GradeRepository;

@Component
public class GradeService 
{
	private static Logger logger = LoggerFactory.getLogger(GradeService.class);
	
	@Autowired
	private GradeRepository gradeRepository;
	
	@Autowired
	private ServiceExceptionHandle serviceExceptionHandle;
	
	/**
	 * 新增年级信息
	 * @param gradeName
	 */
	public void saveGrade(String gradeName) 
	{
		GradeEntity grade = gradeRepository.findByGradeName(gradeName);
		
		if(grade != null)
		{
			logger.error(serviceExceptionHandle.genInfoById("600001"));
			throw new ServiceException("600001");
		}
		
		grade = new GradeEntity();
		
		grade.setGradeName(gradeName);
		grade.setCreateTime(new Date());
		
		gradeRepository.save(grade);
	}

	
	/**
	 * 更新年级信息
	 * @param uid
	 * @param gradeName
	 */
	public void updateGrade(Integer uid, String gradeName) 
	{
		GradeEntity grade = gradeRepository.findByGradeName(gradeName);
		
		if(grade == null)
		{
			logger.error(serviceExceptionHandle.genInfoById("600003"));
			throw new ServiceException("600003");
		}
		
		grade.setGradeName(gradeName == null ? grade.getGradeName() : gradeName);
		grade.setUpdateTime(new Date());
		
		gradeRepository.save(grade);
	}

	/**
	 * 删除年级信息
	 * @param uid
	 */
	public void deleteGrade(Integer uid) 
	{
		GradeEntity grade = gradeRepository.findOne(uid);
		
		if(grade == null)
		{
			logger.error(serviceExceptionHandle.genInfoById("600003"));
			throw new ServiceException("600003");
		}
		
		gradeRepository.delete(grade);
		
	}

	/**
	 * 查询所有年级信息
	 * @return
	 */
	public List<GradeEntity> findGrades() 
	{
		List<GradeEntity> grades = (List<GradeEntity>) gradeRepository.findAll();
		return grades;
	}

	/**
	 * 
	 * @param gradeName
	 */
	public GradeEntity findByGradeName(String gradeName) 
	{
		GradeEntity grade = gradeRepository.findByGradeName(gradeName);
		
		return grade;
	}

	/**
	 * 查询题库信息
	 * @return
	 */
	public List<GradeEntity> findAlls() 
	{
		List<GradeEntity> grades = (List<GradeEntity>) gradeRepository.findAll();
		return grades;
	}

	/**
	 * 根据年级编号查询年级信息
	 * @param gradeId
	 */
	public GradeEntity findByGradeId(Integer gradeId) 
	{
		return gradeRepository.findOne(gradeId);
	}

}
