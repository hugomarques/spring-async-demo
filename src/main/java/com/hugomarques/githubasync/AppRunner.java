package com.hugomarques.githubasync;

import com.hugomarques.githubasync.lookup.GitHubLookupService;
import com.hugomarques.githubasync.lookup.User;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * This component is a CommandLineRunner. It's run once when the beans are initiated in the
 * spring context.
 * For details: https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/CommandLineRunner.html
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {

  private final GitHubLookupService gitHubLookupService;

  @Override
  public void run(String... args) throws Exception {
    // Start the clock
    long start = System.currentTimeMillis();

    // Kick of multiple, asynchronous lookups
    CompletableFuture<User> page1 = gitHubLookupService.findUser("PivotalSoftware");
    CompletableFuture<User> page2 = gitHubLookupService.findUser("CloudFoundry");
    CompletableFuture<User> page3 = gitHubLookupService.findUser("Spring-Projects");

    // Wait until they are all done
    CompletableFuture.allOf(page1, page2, page3).join();

    // Print results, including elapsed time
    log.info("Elapsed time: " + (System.currentTimeMillis() - start));
    log.info("--> " + page1.get());
    log.info("--> " + page2.get());
    log.info("--> " + page3.get());
  }
}
