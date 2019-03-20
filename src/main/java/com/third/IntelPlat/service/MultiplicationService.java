package com.third.IntelPlat.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.MultiplicationEntity;
import com.third.IntelPlat.entity.UserEntity;
import com.third.IntelPlat.redis.RedisHelper;
import com.third.IntelPlat.repository.MultiplicationRepository;

@Component
public class MultiplicationService 
{
	@Autowired
	private RedisHelper redisHelper;
	@Autowired
	private UserService userService;
	
	@Value("${redis.key.multipliTable}")
	public String multipliTable;
	
	@Autowired
	private MultiplicationRepository multiplicationRepository;
	
	
	/**
	 * //取题并返回
	 * 
	 *  去redis中获取   如果是没有   去数据库中取 10道题,并且放到redis中  
	 * 	标识 是  1 - 45  是 顺序取题 , 如果标识  0  就要 随机去取
	 * @return
	 */

	public Map<Object, Object> getTitle() {
		String userId = this.getUserId();
		// 1 直接去在redis zset 中获取
		Map<Object, Object> multip = this.getTitleToRedis(userId);
		
		String flag = this.getUseridentify(userId);// 根据 userId 在 redis中 获取用户 的 标识

		if (null == multip) { // redis中存在时 直接返回;
			String score = null;
			if (null == flag) { // 第一次去取
				multip = this.initTitle(0, userId);
				
			} else if (flag.equals("0")) {
				// 随机取 10 题 放入redis,然后在redis中再取 第一条
				 this.getRandomByNum2(10, userId);
				 
				 multip = new HashMap<Object, Object>();
				 score = this.getScore(userId);
				 multip.put("score",score);
				
			} else {
				// 顺序取 取 10 题 放入redis
				Integer sequence = Integer.valueOf(flag);
				 this.initTitle2(sequence, userId);


				 multip = new HashMap<Object, Object>  ();
				 score = this.getScore(userId);
				 multip.put("score",score);
			}
		}
		
		return multip;
	}
	
	/**
	 * 根据上个 题的   序号,顺序取题   ,不够10 条继续在错题中取, 不够就继续 随机取
	 * @param sequence
	 * @return
	 */
	public Map<Object,Object> initTitle(Integer sequence ,String userId)  
	{
		initTitle2(sequence,userId);
		Map<Object,Object> multip = this.getTitleToRedis(userId);  //再次到redis中 去取
		return multip ;
		
	}
	

	/**
	 * 只做初始化
	 * 
	 * 根据上个 题的   序号,顺序取题   ,不够10 条继续在错题中取, 不够就继续 随机取
	 * @param sequence
	 * @return
	 */
	public void initTitle2(Integer sequence ,String userId)  
	{

		List<MultiplicationEntity>  titleByOrder = multiplicationRepository.getTitleBySequence(sequence);
		
		if(titleByOrder != null && titleByOrder.size() != 10){  //不够 10 条  首先 取错题  5 道
			Integer uId = Integer.valueOf(userId);
			List<MultiplicationEntity> listMulti  = multiplicationRepository.getCuoTile(uId ,10 - titleByOrder.size());
			if(null != listMulti && listMulti.size()>=1 ){
				for (MultiplicationEntity  multi : listMulti) {
					titleByOrder.add(multi);
				}
			}
			if(titleByOrder.size() != 10){   //如果 取完错题仍然不够  10道题  再  随机取   10-size
				List<MultiplicationEntity> listRandom  =  multiplicationRepository.getRandomByNum( 10 - titleByOrder.size() );
				if(null != listRandom && listRandom.size()>=1 ){
					for (MultiplicationEntity  multi : listRandom) {
						titleByOrder.add(multi);
					}
				}
			}
			
		}
		
		//从数据库中取到  同时放到 redis中;
		Long batch = redisHelper.zAddBatch(userId, titleByOrder);
	}
	/**
	 * 随机取  number 个  乘法口诀
	 * @param number
	 * @return
	 */
	public Map<Object,Object> getRandomByNum(Integer number ,String userId)  
	{
		this.getRandomByNum2(number,userId);
		Map<Object,Object> titleToRedis2 = this.getTitleToRedis(userId);  //再次到redis中 去取
		return titleToRedis2 ;
	 
		
	}
	/**
	 * 只做初始化,不取题
	 * 
	 * 随机取  number 个  乘法口诀  
	 * @param number
	 * @return
	 */
	public void getRandomByNum2(Integer number ,String userId)  
	{
		List<MultiplicationEntity> listRandom  = multiplicationRepository.getRandomByNum( number );
		
		Long batch = redisHelper.zAddBatch(userId, listRandom);
		
	} 
	/**
	 *   获取String类型的   userId
	 * @return
	 */
	public String getUserId() 
	{
		Subject subject = SecurityUtils.getSubject();
		String userName = (String) subject.getPrincipal();
		
		UserEntity u = userService.findByAccount(userName);
		String valueOf = String.valueOf(u.getUid());
		return  valueOf;
	}
	/**
	 * 根据userId 在redis中 获取String类型的    题目 
	 * @param userId
	 * @return
	 */
	public Map<Object,Object> getTitleToRedis(String userId ) 
	{
		
		Set<TypedTuple<Object>> zRange = redisHelper.zRange(userId, 0, 0);//始终获取第一个 
		if(null !=  zRange  && zRange.size()>=1){  //redis中存在时
			for (TypedTuple<Object> typedTuple : zRange) {
				String params =  (String)typedTuple.getValue();
				Long score = typedTuple.getScore().longValue();
				 
				String[] datas = params.split(";", -1);
				String a  = datas[0];
				Map<Object,Object> map = new HashMap<Object,Object>();
				map.put("sequence", score);
				map.put("content", a);
				return map;
			}
		}
		return null;
	}
	
	
	/**
	 * 获取  用户标识
	 * @param userId
	 */
	public String getUseridentify(String userId) {
		
		if (redisHelper.hashExist("studentLogo", userId)) {
			String logo = redisHelper.hashGet("studentLogo", userId);
			return logo;
		}

		return null;
	}
	
	/**
	 * 保存用户分数
	 * @param userId
	 */
	public void addScore(String userId ,String score) 
	{
		if(null != score){
				String scoreBefor = redisHelper.hashGet("studentScore", userId);//保存 分数
			 
				//表示顺序取题还没到最后一个
				Integer inte = Integer.valueOf(scoreBefor);
				inte = inte + 10;
				String str  = String.valueOf(inte);
				redisHelper.hashSet("studentScore", userId ,str);//保存分数
		}else{
			redisHelper.hashSet("studentScore", userId ,"10");//第一次  保存 分数
		}
	}
	

	/**
	 * 获取  用户分数
	 * @param userId
	 */
	public String getScore(String userId) 
	{
		String Score = redisHelper.hashGet("studentScore", userId );//得到以前的 分数 
		
		if(Score != null)
		{
			redisHelper.hashSet("studentScore", userId, "0");
		}
		
		return Score;
	}
	
}
