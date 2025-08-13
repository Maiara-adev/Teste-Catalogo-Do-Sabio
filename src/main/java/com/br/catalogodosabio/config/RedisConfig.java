package com.br.catalogodosabio.config;

import com.br.catalogodosabio.infra.cache.PageSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisConfig {

    @Value("${app.cache.ttl.default:3600}")
    private long defaultTtl;

    @Value("${app.cache.ttl.books}")
    private long booksTtl;

    @Bean
    public ObjectMapper redisObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        SimpleModule module = new SimpleModule();
        module.addSerializer((Class<Page<?>>) (Class<?>) Page.class, new PageSerializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }

    @Bean
    public GenericJackson2JsonRedisSerializer jsonRedisSerializer(ObjectMapper objectMapper) {
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, GenericJackson2JsonRedisSerializer jsonRedisSerializer) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(defaultTtl))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonRedisSerializer));

        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put(CacheConstants.BOOKS, defaultConfig.entryTtl(Duration.ofSeconds(booksTtl)));
        cacheConfigurations.put(CacheConstants.BOOK_BY_ID, defaultConfig.entryTtl(Duration.ofSeconds(booksTtl)));
        cacheConfigurations.put(CacheConstants.BOOKS_BY_GENRE, defaultConfig.entryTtl(Duration.ofSeconds(booksTtl)));
        cacheConfigurations.put(CacheConstants.BOOKS_BY_AUTHOR, defaultConfig.entryTtl(Duration.ofSeconds(booksTtl)));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

    @Bean
    public RedisTemplate<String, Serializable> redisTemplate(RedisConnectionFactory connectionFactory, GenericJackson2JsonRedisSerializer jsonRedisSerializer) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(jsonRedisSerializer);
        return template;
    }
}