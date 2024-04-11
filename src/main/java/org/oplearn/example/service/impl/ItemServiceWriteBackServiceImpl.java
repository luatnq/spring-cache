package org.oplearn.example.service.impl;

import org.oplearn.example.configuration.ItemWriteBackCacheProperties;
import org.oplearn.example.entity.Item;
import org.oplearn.example.repository.ItemRepository;
import org.oplearn.example.service.ItemServiceWriteBackService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceWriteBackServiceImpl implements ItemServiceWriteBackService {
  private final ItemWriteBackCacheProperties itemWriteBackCacheProperties;
  private final RedisTemplate<String, Item> itemRedisTemplate;
  private final ItemRepository itemRepository;


  @Override
  @Transactional
  public Item createItem(String name) {
    log.info("Creating item for name: {}", name);
    final Item item = new Item(UUID.randomUUID().toString(), name);
    this.cacheableItem(item);

    log.info("Item cached key: {}, value: {}", item.getId(), item);

    return item;
  }

  public Item getItem(String id) {
    log.info("Fetching item for id: {}", id);
    final Item itemOnCache = itemRedisTemplate.boundValueOps(id).get();
    if (itemOnCache != null) {
      log.info("Item found in cache for id: {}", id);
      return itemOnCache;
    }
    log.info("Item not found in cache for id: {}", id);

    final Item item = itemRepository.findById(id)
          .orElseThrow(RuntimeException::new);

    this.cacheableItem(item);

    return item;
  }

  private void cacheableItem(Item item) {
    itemRedisTemplate.boundValueOps(item.getId()).set(item);
    itemRedisTemplate.boundSetOps(itemWriteBackCacheProperties.getKey()).add(item);
  }

  public void printProperties() {
    log.info("Key: {}", itemWriteBackCacheProperties.getKey());
    log.info("Rate: {}", itemWriteBackCacheProperties.getRate());
  }
}
