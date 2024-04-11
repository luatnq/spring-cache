package org.oplearn.example.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Item implements Serializable {
  @Id
  private String id;
  private String name;

  public Item(String name) {
    this.name = name;
  }

  @PrePersist
  private void ensureId() {
    if (id == null) {
      id = java.util.UUID.randomUUID().toString();
    }
  }
}
