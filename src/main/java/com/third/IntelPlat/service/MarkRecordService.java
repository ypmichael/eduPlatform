package com.third.IntelPlat.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.third.IntelPlat.entity.MarkRecordEntity;
import com.third.IntelPlat.entity.UserEntity;
import com.third.IntelPlat.exception.ServiceException;
import com.third.IntelPlat.redis.RedisHelper;
import com.third.IntelPlat.repository.MarkRecordRepository;
import com.third.IntelPlat.repository.MultLogRepository;

@Component
public class MarkRecordService {
	@Autowired
	private RedisHelper redisHelper;
	@Autowired
	private UserService userService;
	@Autowired
	private MarkRecordRepository markRecordRepository;
	
	@Autowired
	private MultLogService multLogService;
	/**
	 * 点击提交以后 把 题目的序号和  学生做的 答案 保存在redis中
	 * @param userId
	 */
	public void addStudentAnswerToRedis(Integer sequence, String answer) 
	{
		UserEntity userEntity = userService.getUserEntity();
		String userName = userEntity.getUserName();// 用户名
		Integer uid = userEntity.getUid();
		String stringUserId = String.valueOf(uid);
		Set<TypedTuple<Object>> zRange = redisHelper.zRange(stringUserId, 0, 0);// 获取第一个
																				// 初始化的
																				// 题目

		if(zRange == null || zRange.size() != 1)
		{
			throw new ServiceException("缓存中没有找到相应的题目");
		}
		
		for (TypedTuple<Object> typedTuple : zRange) {
			String params = (String) typedTuple.getValue(); // 这是题目
			Double score = typedTuple.getScore();
			String[] datas = params.split(";", -1);
			String a = datas[1]; // 拿到题答案

			if (a.equals(answer)) {
				// 如果是正确答案
				if (redisHelper.hashExist("studentScore", stringUserId)) {
					String beforScore = redisHelper.hashGet("studentScore", stringUserId);// 得到以前的
																							// 分数

					String score2 = String.valueOf(Integer.valueOf(beforScore) + 10);

					redisHelper.hashSet("studentScore", stringUserId, score2); // 保存
																				// 新
																				// 分数
				} else {
					redisHelper.hashSet("studentScore", stringUserId, "10"); // 第一次保存
																				// 新
																				// 分数
				}

				// 如果是正确的
				multLogService.addRightResult(uid, sequence);
			} else {
				// 如果是错误答案 去记录
				multLogService.addErrorResult(uid, sequence);
			}
			String member = sequence + ";" + answer; // 题号和答案
			redisHelper.zAdd(userName, member, score); // 保存 记录
			redisHelper.zRangeRemove(stringUserId, 0, 0);
			
			this.addLogo(stringUserId, String.valueOf(score.longValue()));// 添加 logo
			
			
			
		}
	}
	
	
	/**
	 * 保存用户 标识
	 * @param userId
	 */
	public void addLogo(String userId ,String logo) 
	{
		if(null != logo){
			if(logo.equals("0")){  //要随机去取 没有必要 进行修改标识
				
			}else if(logo.equals("64")){
				//表示顺序取题已近结束,
				redisHelper.hashSet("studentLogo", userId ,"0");//保存 标识
			}else{
				//表示顺序取题还没到最后一个
				Integer inte = Integer.valueOf(logo);
				inte  = inte +1;
				String str  = String.valueOf(inte);
				redisHelper.hashSet("studentLogo", userId ,str);//保存 标识
			}
		}else{
				//第一次进来 没有标识
			redisHelper.hashSet("studentLogo", userId ,"1");//保存 标识
		}
	}
	
	
	
	/**
	 * 点击提交以后   如果在redis没有获取到新题目     就要保存记录日志的10条记录并  获取分数
	 * @param userId
	 */
	public String addCuoTileLog() 
	{
		
		UserEntity userEntity = userService.getUserEntity();
		String userName = userEntity.getUserName();//用户名
		Integer uid = userEntity.getUid();
		String stringUserId = String.valueOf(uid);
		Map<Object,Object> cuowu = new  HashMap<Object,Object>();
		Set<TypedTuple<Object>> zRange = redisHelper.zRange(userName, 0, -1);//获取所有的 记录 
		if(null !=  zRange  && zRange.size()>=1){  //redis中存在时
			for (TypedTuple<Object> typedTuple : zRange) {
				String params =   (String)typedTuple.getValue();  // 这是题目编号 +学生答案;
				String[] datas = params.split(";", -1);
				String a  = datas[0];  //拿到题目编号  
				String b  = datas[1];  //拿到题目答案  
				 cuowu.put(a, b);
			}
		}
		Gson gson = new Gson();
		String content = gson.toJson(cuowu);  //内容 就是  map转换的  jison
		MarkRecordEntity  mark  = new MarkRecordEntity();
		mark.setUserId(uid);
		mark.setContent(content);
		
		String beforScore = redisHelper.hashGet("studentScore", stringUserId );//得到以前的 分数
		mark.setScore(beforScore);
		MarkRecordEntity save = markRecordRepository.save(mark);
		
		return beforScore;
	}
	
	/**
	 * 判断是否存在题目  如果没有就  返回结果  
	 * @param userId
	 */
/*	public void getBiaoShi(String userId ,String logo) 
	{
		Set<TypedTuple<Object>> zRange2 = redisHelper.zRange(stringUserId, 0, -1);
	}
	*/
}
