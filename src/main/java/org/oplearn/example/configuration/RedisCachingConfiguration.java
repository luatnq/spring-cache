package org.oplearn.example.configuration;


import org.oplearn.example.entity.Item;
import org.oplearn.example.constant.Cache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Duration;

@EnableCaching
@EnableScheduling
@Configuration
public class RedisCachingConfiguration {
  @Value("${spring.data.redis.host}")
  private String redisHostName;
  @Value("${spring.data.redis.port}")
  private int redisPort;

  @Bean
  public JedisConnectionFactory redisConnectionFactory() {

    final RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHostName, redisPort);
    return new JedisConnectionFactory(config);
  }

  @Bean
  public RedisCacheConfiguration cacheConfiguration() {
    return RedisCacheConfiguration.defaultCacheConfig()
          .entryTtl(Duration.ofMinutes(5))
          .disableCachingNullValues()
          .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
  }

  @Bean
  public RedisCacheManager redisCacheManager() {

    return RedisCacheManager.builder(redisConnectionFactory())
          .cacheDefaults(cacheConfiguration())
          .withCacheConfiguration(Cache.Item.ITEM_CACHE_NAME,
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10)))
          .withCacheConfiguration("customerCache",
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5))).build();
  }

  @Bean
  public RedisTemplate<String, Item> itemRedisTemplate() {
    final RedisTemplate<String, Item> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory());
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Item.class));
    return template;
  }
}
