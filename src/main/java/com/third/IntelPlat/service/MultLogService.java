package com.third.IntelPlat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.MultLog;
import com.third.IntelPlat.repository.MultLogRepository;

@Component
public class MultLogService {

	@Autowired
	private MultLogRepository multLogRepository;

	/**
	 * 学生答题 正确 要去数据库记录题 答对答错次数;
	 * 
	 * @param userId
	 */
	public void addRightResult(Integer userId, Integer sequence) {
		MultLog mult = multLogRepository.getMarkByUserIdAndSequence(userId, sequence);

		if (mult == null) {
			return;
		} else {
			int c = mult.getCount() - 2;

			if (c <= 0) {
				multLogRepository.delete(mult);
			} else {
				mult.setCount(c);
				multLogRepository.save(mult);
			}
		}
	}

	/**
	 * 学生答题 错误 要去数据库记录题 答对答错次数;
	 * 
	 * @param userId
	 */
	public void addErrorResult(Integer userId, Integer sequence) {
		MultLog mult = multLogRepository.getMarkByUserIdAndSequence(userId, sequence);
		if (null == mult) {
			MultLog mark = new MultLog();
			mark.setCount(1);
			mark.setUserId(userId);
			mark.setSequence(sequence);
			multLogRepository.save(mark);
		} else {
			mult.setCount(mult.getCount() + 1);
			multLogRepository.save(mult);
		}

	}
}
