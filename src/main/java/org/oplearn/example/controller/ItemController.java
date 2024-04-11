package org.oplearn.example.controller;


import org.oplearn.example.entity.Item;
import org.oplearn.example.service.ItemService;
import org.oplearn.example.service.ItemServiceWriteBackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static org.oplearn.example.constant.Api.Item.BASE_URL;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class ItemController {
  private final ItemService itemService;
  private final ItemServiceWriteBackService itemServiceWriteBackService;

  @GetMapping(BASE_URL + "/{id}")
  public Item getItemById(@PathVariable String id) {
    log.info("Fetching item for id: {}", id);
    return itemService.getItemForId(id);
  }

  @GetMapping(BASE_URL)
  public Item getItemByName(@RequestParam String name) {
    log.info("Fetching item for name: {}", name);
    return itemService.getItemForName(name);
  }

  @PostMapping(BASE_URL)
  public Item saveItem(@RequestParam String name) {
    log.info("Saving item for name: {}", name);
    return itemService.saveItem(name);
  }

  @DeleteMapping(BASE_URL)
  public void deleteItem(@RequestParam String name) {
    log.info("Deleting item for name: {}", name);
    itemService.deleteItem(name);
  }

  @GetMapping(BASE_URL + "/print")
  public void printProperties() {
    itemServiceWriteBackService.printProperties();
  }


  @GetMapping(BASE_URL + "/write-back/{id}")
  public Item getItemByIdWriteBack(@PathVariable String id) {
    log.info("getItemByIdWriteBack item for id: {}", id);
    return itemServiceWriteBackService.getItem(id);
  }

  @PostMapping(BASE_URL + "/write-back")
  public Item saveItemWriteBack(@RequestParam String name) {
    log.info("saveItemWriteBack item for name: {}", name);
    return itemServiceWriteBackService.createItem(name);
  }
}
