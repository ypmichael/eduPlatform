package com.third.IntelPlat.repository;

import com.third.IntelPlat.entity.ChoiceQstEntity;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public abstract interface ChoiceQstRepository
  extends CrudRepository<ChoiceQstEntity, Integer>
{
  @Query(value="SELECT * FROM CHOICE_QST WHERE TUTORIAL_UID= ?1 AND QUESTION_NO <> ?2  LIMIT 5 ", nativeQuery=true)
  public abstract List<ChoiceQstEntity> randomQuery(Integer paramInteger, String paramString);
  
  @Modifying
  @Transactional
  @Query("delete from ChoiceQstEntity c where c.tutorial.uid in ?1 ")
  public abstract void deleteAll(List<Integer> paramList);
  
  @Query(value="select * from CHOICE_QST order by rand() LIMIT 1 ", nativeQuery=true)
  public abstract ChoiceQstEntity randomQueryQst();
  
  @Query(value="SELECT count(*) FROM CHOICE_QST WHERE TUTORIAL_UID= ?1", nativeQuery=true)
  public abstract int findByTutorialUid(Integer paramInteger);
}
