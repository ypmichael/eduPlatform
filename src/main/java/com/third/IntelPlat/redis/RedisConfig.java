package com.third.IntelPlat.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

	private static Logger logger = LoggerFactory.getLogger(RedisConfig.class);

	@Value("${spring.redis.host}")
	private String host = null;

	@Value("${spring.redis.port}")
	private Integer port = null;

	@Value("${spring.redis.password}")
	private String password = null;

	@Value("${spring.redis.database}")
	private int database = 0;

	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {

		JedisConnectionFactory factory = new JedisConnectionFactory();

		if (this.host == null || this.port == null) {
			logger.info("host,post not null");

		}
		factory.setHostName(host);
		factory.setPort(port);
		factory.setPassword(password);
		factory.setDatabase(database);
		factory.setTimeout(100000);
		factory.setUsePool(true);
		
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(10);
		jedisPoolConfig.setMaxWaitMillis(65535);
		jedisPoolConfig.setTestWhileIdle(true);
		jedisPoolConfig.setMinEvictableIdleTimeMillis(60000);
		jedisPoolConfig.setTimeBetweenEvictionRunsMillis(30000);
		jedisPoolConfig.setNumTestsPerEvictionRun(-1);
		factory.setPoolConfig(jedisPoolConfig);
		return factory;
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());

		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new StringRedisSerializer());

		return redisTemplate;
	}

	@Bean
	public RedisCacheManager cacheManager() {
		RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate());
		return redisCacheManager;
	}

}
