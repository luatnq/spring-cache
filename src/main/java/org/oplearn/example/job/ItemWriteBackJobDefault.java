package org.oplearn.example.job;


import org.oplearn.example.configuration.ItemWriteBackCacheProperties;
import org.oplearn.example.entity.Item;
import org.oplearn.example.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemWriteBackJobDefault implements ItemWriteBackJob {
  private final ItemWriteBackCacheProperties itemWriteBackCacheProperties;
  private final RedisTemplate<String, Item> itemRedisTemplate;
  private final ItemRepository itemRepository;


  @Override
  @Transactional
  @Scheduled(fixedRateString = "${application.cache.item.write-back.rate}")
  public void writeBack() {
    final Long amountOfItemsToPersist = itemRedisTemplate.boundSetOps(itemWriteBackCacheProperties.getKey()).size();
    log.info("Amount of items to persist: {}", amountOfItemsToPersist);
    if (Objects.isNull(amountOfItemsToPersist) || amountOfItemsToPersist == 0) {
      return;
    }

    final BoundSetOperations<String, Item> itemBoundSetOperations = itemRedisTemplate.boundSetOps(
          itemWriteBackCacheProperties.getKey()
    );
    final ScanOptions scanOptions = ScanOptions.scanOptions().build();

    try (final Cursor<Item> cursor = itemBoundSetOperations.scan(scanOptions)) {
      assert cursor != null;

      final List<Item> items = new ArrayList<>();
      while (cursor.hasNext()) {
        final Item item = cursor.next();
        itemRepository.save(item);
        log.info("Item persisted in the database (item={})", item);

        itemRedisTemplate.boundSetOps(itemWriteBackCacheProperties.getKey()).remove(item);
        log.info("Item removed from the cache (item={})", item);
      }
      log.info("Items persisted in the database (items={})", items);
    } catch (RuntimeException exception) {
      log.error("Error while persisting items in the database", exception);
    }
  }
}
