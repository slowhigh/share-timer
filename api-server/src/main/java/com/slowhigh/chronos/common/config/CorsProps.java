package com.slowhigh.chronos.common.config;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cors")
@RequiredArgsConstructor
@Getter
public class CorsProps {

  private final List<String> allowedOrigins;

}