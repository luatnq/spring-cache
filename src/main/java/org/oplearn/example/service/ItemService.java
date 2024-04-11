package org.oplearn.example.service;

import org.oplearn.example.entity.Item;

public interface ItemService {
  Item getItemForId(String id);

  Item getItemForName(String name);

  Item saveItem(String name);

  void deleteItem(String name);
}
