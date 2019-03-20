package com.third.IntelPlat.redis;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

import com.third.IntelPlat.entity.MultiplicationEntity;

@Component
public class RedisHelper {

	@Autowired
	private RedisTemplate<String, Object> template;

	public void hashSet(String key, String member, String value) {
		HashOperations<String, Object, Object> ops = template.opsForHash();
		ops.put(key, member, value);
	}

	public String hashGet(String key, String member) {
		HashOperations<String, Object, Object> ops = template.opsForHash();
		return (String) ops.get(key, member);
	}

	public void hashDel(String key, Object... members) {
		HashOperations<String, Object, Object> ops = template.opsForHash();
		ops.delete(key, members);

	}

	public boolean hashExist(String key, Object member) {
		HashOperations<String, Object, Object> ops = template.opsForHash();
		return ops.hasKey(key, member);
	}

	public Cursor<Map.Entry<Object, Object>> hashScan(String key) {
		HashOperations<String, Object, Object> ops = template.opsForHash();
		return ops.scan(key, ScanOptions.NONE);
	}

	public Cursor<Map.Entry<Object, Object>> hashScan(String key, String pattern, long limit) {
		HashOperations<String, Object, Object> ops = template.opsForHash();
		return ops.scan(key, ScanOptions.scanOptions().match(pattern).count(limit).build());
	}

	public void zAdd(String key, String member, Double score) {
		ZSetOperations<String, Object> ops = template.opsForZSet();
		ops.add(key, member, score);
	}


	public void zIncrement(String key, String member, Double deltScore) {
		ZSetOperations<String, Object> ops = template.opsForZSet();
		ops.incrementScore(key, member, deltScore);
	}

	public Set<TypedTuple<Object>> zRange(String key, long start, long end) {
		ZSetOperations<String, Object> ops = template.opsForZSet();
		return ops.rangeWithScores(key, start, end);
	}

	public Set<TypedTuple<Object>> zReverseRange(String key, long start, long end) {
		ZSetOperations<String, Object> ops = template.opsForZSet();
		return ops.reverseRangeWithScores(key, start, end);
	}

	public Set<Object> zRangeByScore(String key, Double minScore, Double maxScore) {
		ZSetOperations<String, Object> ops = template.opsForZSet();
		return ops.rangeByScore(key, minScore, maxScore);
	}

	public Set<Object> zReverseRangeByScore(String key, Double minScore, Double maxScore) {
		ZSetOperations<String, Object> ops = template.opsForZSet();
		return ops.reverseRangeByScore(key, minScore, maxScore);
	}

	public Double zScore(String key, String member) {
		ZSetOperations<String, Object> ops = template.opsForZSet();

		return ops.score(key, member);
	}

	public Long zRemove(String key, String member) {
		ZSetOperations<String, Object> ops = template.opsForZSet();
		return ops.remove(key, member);
	}
	
	public Long zRangeRemove(String key, long start, long end) {
		ZSetOperations<String, Object> ops = template.opsForZSet();
		return ops.removeRange(key, start, end);
	}

	public Cursor<ZSetOperations.TypedTuple<Object>> zScan(String key) {
		ZSetOperations<String, Object> ops = template.opsForZSet();
		return ops.scan(key, ScanOptions.NONE);
	}

	public Cursor<ZSetOperations.TypedTuple<Object>> zScan(String key, String pattern, long limit) {
		ZSetOperations<String, Object> ops = template.opsForZSet();
		return ops.scan(key, ScanOptions.scanOptions().match(pattern).count(limit).build());
	}

	public void publish(String key, String value, Long expiration) {
		ListOperations<String, Object> ops = template.opsForList();
		ops.leftPush(key, value);

		if (expiration != null) {
			template.expire(key, expiration, TimeUnit.SECONDS);
		}
	}

	public boolean keyExist(String key) {
		return template.hasKey(key);
	}

	public void deleteKey(String key, Long seconds) {
		if (seconds == null) {
			template.delete(key);
		} else {
			template.expire(key, seconds, TimeUnit.SECONDS);
		}
	}

	public Set<String> keysMatch(String pattern) {
		return template.keys(pattern);
	}

	public void deleteKeys(Set<String> keys) {
		template.delete(keys);
	}
	
	public long increment(String clientId){
		ValueOperations<String, Object> valueOperation = template.opsForValue();
		
		return valueOperation.increment(clientId, 1);
	}
	
	public void set(String key, String status){
		ValueOperations<String, Object> valueOperation = template.opsForValue();
		valueOperation.set(key, status);
	}
	
	public String get(String key){
		ValueOperations<String, Object> valueOperation = template.opsForValue();
		return (String) valueOperation.get(key);
	}
	
	/**
	 * 批量操作  zset  Object  必须是   乘法口诀表对象  MultiplicationEntity
	 * @param userId
	 * @param objCollection
	 */
	public Long zAddBatch(String userId,List<MultiplicationEntity> multiplications) {
		ZSetOperations<String, Object> ops = template.opsForZSet();

        Set<TypedTuple<Object>> tuples = new HashSet<TypedTuple<Object>>();
        
        for(MultiplicationEntity obj : multiplications)
        {
        	String contentAndAnswer  = obj.getContent()+";"+obj.getAnswer();
        	Integer sequence = obj.getSequence();
            TypedTuple<Object> tuple = new DefaultTypedTuple<Object>(contentAndAnswer, Double.valueOf(String.valueOf(sequence)));  
            tuples.add(tuple);
        }
        Long add = ops.add(userId, tuples);
        return add;
	}

}
