package com.third.IntelPlat.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.LoginLogEntity;
import com.third.IntelPlat.entity.OperationLogEntity;
import com.third.IntelPlat.repository.LoginLogRepository;
import com.third.IntelPlat.repository.OperationLogRepository;


@Component
public class OperationLogService {
	@Autowired
	private OperationLogRepository operationLogRepository;
	@Autowired
	private LoginLogRepository loginLogRepository;
	/**
	 * 添加操作日志信息
	 * @param operationUid
	 * @param userUid
	 * @param userAccount
	 * @param operationTime
	 * @param requestUri
	 * @param operationName
	 * @param success
	 * @return
	 */
	public Integer insertOperationLog(String account, Date time, String operation, String type,
			String model){
		OperationLogEntity operationLog = new OperationLogEntity(account, time, operation, type, model);
		operationLogRepository.save(operationLog);
		return operationLog.getUid();
	}
	
	
	public Specification<OperationLogEntity> driverSpec(final String account, final String type,
			final String model, final Date startTime, final Date endTime){
		return new Specification<OperationLogEntity>(){

			@Override
			public Predicate toPredicate(Root<OperationLogEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				List<Predicate> predicates = new ArrayList<Predicate>(); 
				if(!"".equals(account) && null != account)
				{
					predicates.add(cb.equal(root.<String>get("account"), account));
				}	
				if(!"".equals(type) && null != type)
				{
					predicates.add(cb.equal(root.<String>get("type"), type));
				}
				if(!"".equals(model) && null != model)
				{
					predicates.add(cb.equal(root.<String>get("model"), model));
				}
				if(null != startTime)
				{
					predicates.add(cb.greaterThanOrEqualTo(root.<Date>get("time"), startTime));
				}
				if(null != endTime)
				{
					predicates.add(cb.lessThanOrEqualTo(root.<Date>get("time"), endTime));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
			
		};
		
	}
	/**
	 * 根据登录账号、操作类型、操作模型、操作时间查询操作日志信息(分页)
	 * @param account
	 * @param type
	 * @param model
	 * @param startTime
	 * @param endTime
	 * @param pageNumber
	 * @param limit
	 * @return
	 */
	public Page<OperationLogEntity> findByAccountWithTypeWithModelWithTime(String account, String type,
			String model, Date startTime, Date endTime, Integer pageNumber,Integer limit){
		Sort sort = new Sort(Direction.DESC, "time");
		Pageable pageable = new PageRequest(pageNumber, limit, sort);
		Page<OperationLogEntity> appVersionPage = operationLogRepository.findAll(driverSpec(account, type, model, startTime, endTime), pageable);
		return appVersionPage;
	}

	/**
	 * 添加登录日志
	 * @param uid
	 * @param account
	 */
	public void inserLoginLog(Integer uid, String account) 
	{
		LoginLogEntity loginLog = new LoginLogEntity();
		
		loginLog.setUserId(uid);
		loginLog.setUserName(account);
		loginLog.setLoginTime(new Date());
		loginLog.setCreateTime(new Date());
		
		loginLogRepository.save(loginLog);
	}

	/**
	 * 查询登录日志
	 * @param account
	 * @param startTime
	 * @param endTime
	 * @param pageNumber
	 * @param limit
	 */
	public Page<LoginLogEntity> findLoginlogs(String account, Date startTime, Date endTime, Integer pageNumber, Integer limit) 
	{
		Sort sort = new Sort(Direction.DESC, "userName");
		Pageable pageable = new PageRequest(pageNumber, limit, sort);
		Page<LoginLogEntity> loginLogPage = loginLogRepository.findAll(LoginlogSpec(account, startTime, endTime), pageable);
		return loginLogPage;
		
	}

	/**
	 * 登录日志查询条件
	 * @param account
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	private Specification<LoginLogEntity> LoginlogSpec(final String account, final Date startTime, final Date endTime) 
	{
		return new Specification<LoginLogEntity>(){

			@Override
			public Predicate toPredicate(Root<LoginLogEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				List<Predicate> predicates = new ArrayList<Predicate>(); 
				if(!"".equals(account) && null != account)
				{
					predicates.add(cb.equal(root.<String>get("userName"), account));
				}	
				if(null != startTime)
				{
					predicates.add(cb.greaterThanOrEqualTo(root.<Date>get("loginTime"), startTime));
				}
				if(null != endTime)
				{
					predicates.add(cb.lessThanOrEqualTo(root.<Date>get("loginTime"), endTime));
				}
				return cb.and(predicates.toArray(new Predicate[predicates.size()]));
			}
			
		};
	}
}