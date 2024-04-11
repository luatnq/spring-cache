package org.oplearn.example.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application.cache.item.write-back")
public class ItemWriteBackCacheProperties {
  private String key;
  private long rate;
}
