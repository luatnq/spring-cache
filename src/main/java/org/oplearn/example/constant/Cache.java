package org.oplearn.example.constant;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Cache {

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Item {
    public static final String ITEM_CACHE_NAME = "itemCache";
  }
}
