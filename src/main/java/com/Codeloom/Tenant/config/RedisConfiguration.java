package com.Codeloom.Tenant.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Map;

@Configuration
public class RedisConfiguration {
    @Bean
    public CacheManager redisCacheManager(
            RedisConnectionFactory redisConnectionFactory) {

        // Serializer for cached response objects.
        JacksonJsonRedisSerializer<Object> jsonSerializer =
                new JacksonJsonRedisSerializer<>(Object.class);

        // Base configuration shared by the caches.
        RedisCacheConfiguration defaultConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                        .disableCachingNullValues()
                        .serializeKeysWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(
                                                new StringRedisSerializer()
                                        )
                        )
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(jsonSerializer)
                        );

        // Configure each cache separately.
        Map<String, RedisCacheConfiguration> cacheConfigurations =
                Map.of(

                        "USER_CACHE",
                        defaultConfig.entryTtl(Duration.ofMinutes(10)),

                        "HOSTEL_CACHE",
                        defaultConfig.entryTtl(Duration.ofMinutes(10)),

                        "ROOM_CACHE",
                        defaultConfig.entryTtl(Duration.ofMinutes(5)),

                        "CARETAKER_CACHE",
                        defaultConfig.entryTtl(Duration.ofMinutes(10)),

                        "payments",
                        defaultConfig.entryTtl(Duration.ofMinutes(1))

                );

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
}
