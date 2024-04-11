package org.oplearn.example.service;

import org.oplearn.example.entity.Item;

public interface ItemServiceWriteBackService {
  Item createItem(String name);

  Item getItem(String id);

  void printProperties();
}
