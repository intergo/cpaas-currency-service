package com.intergotelecom.service;

import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.spi.InjectionPoint;
import java.lang.reflect.ParameterizedType;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Dependent
@RequiredArgsConstructor
public class RedisService<T> {
  private ValueCommands<String, T> valueCommands;

  private final RedisDataSource redisDataSource;
  private final InjectionPoint injectionPoint;

  @PostConstruct
  protected void init() {
    var type = (ParameterizedType) injectionPoint.getType();
    var typeParameter = (Class<T>) type.getActualTypeArguments()[0];

    valueCommands = redisDataSource.value(typeParameter);
    log.info("[REDIS_SERVICE] Initializing RedisService [OK]");
  }

  public Optional<T> getCachedObject(String objectCacheKey) {
    try {
      T cachedProduct = valueCommands.get(objectCacheKey);
      return Optional.ofNullable(cachedProduct);

    } catch (Exception e) {
      log.error("[REDIS_SERVICE] Error while getting cached object for key: {} [FAIL]", objectCacheKey, e);
    }

    return Optional.empty();
  }

  public void cacheObject(String key, Duration duration, T object) {
    if (duration.isNegative() || duration.isZero()) {
      log.warn("[REDIS_SERVICE] Cache duration is negative or zero for key: {}, [FAIL]", key);
      return;
    }

    try {
      log.info("[REDIS_SERVICE] Caching object for key: {}, duration(s): {}", key, duration.getSeconds());
      valueCommands.setex(key, duration.getSeconds(), object);
    } catch (Exception e) {
      log.error("[REDIS_SERVICE] Error while caching object for key: {} [FAIL]", key, e);
    }
  }
}
