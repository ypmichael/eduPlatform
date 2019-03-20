package com.third.IntelPlat.repository;

import com.third.IntelPlat.entity.TestLogEntity;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface TestLogRepository extends CrudRepository<TestLogEntity, Integer> {
	@Query(value = "select count(*) from (select * from TEST_LOG WHERE USER_NAME = ?1 order by CREATE_TIME DESC limit 2)  as t WHERE t.RESULT = 1  ", nativeQuery = true)
	public int checkWrongTopic(String paramString);

	@Query(value = "select * from TEST_LOG WHERE USER_NAME = ?1 order by CREATE_TIME DESC limit 1 ", nativeQuery = true)
	public TestLogEntity findTopic(String paramString);

	@Query(value = "select count(UID) from TEST_LOG WHERE USER_UID = ?1 and RESULT = 1", nativeQuery = true)
	public Integer queryCorrectAnswer(Integer paramInteger);

	@Query(value = "select count(*) from (select * from TEST_LOG WHERE USER_NAME = ?1 order by CREATE_TIME DESC limit 2)  as t WHERE t.TUTORIAL_UID = ?2  ", nativeQuery = true)
	public int checkTitleNumber(String paramString, Integer paramInteger);

	@Query(value = "select * from TEST_LOG WHERE USER_UID = ?1 order by CREATE_TIME DESC limit 3 ", nativeQuery = true)
	public List<TestLogEntity> findByUserUid(Integer paramInteger);

	@Query(value = "select * from TEST_LOG WHERE USER_NAME = ?1 and RESULT = 0 order by CREATE_TIME DESC limit 10 ", nativeQuery = true)
	public List<TestLogEntity> findByUserName(String paramString);

	@Transactional
	@Modifying
	@Query("update TestLogEntity t set t.result = 2 where t.userName in ?1 and t.result = 1")
	public void deleteStudentScore(List<String> userNames);
}
