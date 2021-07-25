package com.hugomarques.githubasync;

import java.util.concurrent.Executor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Main class. It starts the application with @SpringBootApplication and it enables Async operations
 * to be run in a separate thread with @EnableAsync config.
 * For details see: https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/annotation/EnableAsync.html
 */
@SpringBootApplication
@EnableAsync
public class GithubAsyncApplication {

  public static void main(String[] args) {
    SpringApplication.run(GithubAsyncApplication.class, args);
  }

  /**
   * Configures a default task executor to be used by Spring Boot @EnableAsync config.
   */
  @Bean
  public Executor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(2);
    executor.setMaxPoolSize(2);
    executor.setQueueCapacity(500);
    executor.setThreadNamePrefix("GithubLookup-");
    executor.initialize();
    return executor;
  }
}
