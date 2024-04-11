package org.oplearn.example.constant;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Api {

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Item {
    public static final String BASE_URL = "/items";
    public static final String GET_ITEM_BY_ID = BASE_URL.concat("/{id}");
  }

}
