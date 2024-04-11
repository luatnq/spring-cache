package org.oplearn.example.service.impl;


import org.oplearn.example.entity.Item;
import org.oplearn.example.repository.ItemRepository;
import org.oplearn.example.service.ItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oplearn.example.constant.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
  private final ItemRepository itemRepository;


  //cache aside pattern
  @Cacheable(Cache.Item.ITEM_CACHE_NAME)
  public Item getItemForId(String id) {
    log.info("Fetching item for id: {}", id);
    return itemRepository.findById(id)
          .orElseThrow(RuntimeException::new);
  }

  @Override
  @Cacheable(value = Cache.Item.ITEM_CACHE_NAME, key = "#name")
  public Item getItemForName(String name) {
    log.info("Fetching item for name: {}", name);
    return itemRepository.findByName(name);
  }

  //cache write through pattern
  @CachePut(value = Cache.Item.ITEM_CACHE_NAME, key = "#name")
  public Item saveItem(String name) {
    log.info("Saving item for name: {}", name);
    return itemRepository.save(new Item(name));
  }


  @Override
  @CacheEvict(value = Cache.Item.ITEM_CACHE_NAME, key = "#name")
  public void deleteItem(String name) {
    log.info("Deleting item for name: {}", name);
    itemRepository.deleteAllByName(name);
  }

}

