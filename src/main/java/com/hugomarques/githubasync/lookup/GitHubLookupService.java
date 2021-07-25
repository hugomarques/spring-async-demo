package com.hugomarques.githubasync.lookup;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Class responsible for fetching data from github API.
 */
@Slf4j
@Service
public class GitHubLookupService {

  private static final String GITHUB_API_ENDPOINT_TEMPLATE = "https://api.github.com/users/%s";
  private final RestTemplate restTemplate;
  private final MeterRegistry meterRegistr;
  private Counter githubCounter;

  /**
   * GithubLookupService constructor.
   *
   * @param restTemplateBuilder This is injected automatically by Spring boot context. The build is
   *                            user the build the RestTemplate which is the client to the github
   *                            API.
   * @param meterRegistr This is injected automatically by Spring boot context. It is used by Spring
   *                     micrometer library to log metrics.
   */
  public GitHubLookupService(RestTemplateBuilder restTemplateBuilder,
                             final MeterRegistry meterRegistr) {
    this.restTemplate = restTemplateBuilder.build();
    this.meterRegistr = meterRegistr;
    this.initOrderCounters();
  }

  private void initOrderCounters() {
    githubCounter = Counter.builder("github.calls")
      .tag("API", "user")
      .description("The number of calls made to " + GITHUB_API_ENDPOINT_TEMPLATE)
      .register(this.meterRegistr);
  }

  /**
   * This method is executed in a separate thread through the @Async annotation.
   */
  @Async
  public CompletableFuture<User> findUser(final String user) throws InterruptedException {
    log.info("Looking up user: " + user);
    final String userEndpoint = String.format(GITHUB_API_ENDPOINT_TEMPLATE, user);
    final User results = restTemplate.getForObject(userEndpoint, User.class);
    githubCounter.increment();
    // Artificial delay of 1s for demonstration purposes
    Thread.sleep(1000L);
    return CompletableFuture.completedFuture(results);

  }

}
