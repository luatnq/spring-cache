package org.oplearn.example.repository;

import org.oplearn.example.entity.Item;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<Item, String> {

  Item findByName(String name);

  @Query("delete from Item i where i.name = ?1")
  @Modifying
  @Transactional
  void deleteAllByName(String name);
}
