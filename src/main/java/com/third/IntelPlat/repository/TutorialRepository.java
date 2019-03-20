package com.third.IntelPlat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.TutorialEntity;

@Component
public interface TutorialRepository extends CrudRepository<TutorialEntity, Integer>
{
	@Query("select t from TutorialEntity t where t.parent = null and t.grade.uid = ?1 order by t.uid asc ")
	List<TutorialEntity> findTutorials(Integer uid);

	TutorialEntity findByTutorialNo(String tutorialNo);
	
	@Query(value="select t.UID from TUTORIAL t where t.GRADE_UID = ?1", nativeQuery=true)
	List<Integer> findByGradeId(Integer uid);

	@Query("select t from TutorialEntity t where t.parent = null order by t.uid asc ")
	List<TutorialEntity> findTutorial();

	@Query("select t from TutorialEntity t where t.parent = null and t.grade.uid >= ?1 order by t.uid asc ")
	List<TutorialEntity> findTutorialList(Integer uid);
	

}
