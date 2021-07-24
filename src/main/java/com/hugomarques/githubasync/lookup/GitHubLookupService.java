package com.hugomarques.githubasync.lookup;

import java.util.concurrent.CompletableFuture;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GitHubLookupService {

    private final RestTemplate restTemplate;
    private final MeterRegistry meterRegistr;
    private static final String GITHUB_API_ENDPOINT_TEMPLATE = "https://api.github.com/users/%s";
    private Counter githubCounter;

    public GitHubLookupService(RestTemplateBuilder restTemplateBuilder, final MeterRegistry meterRegistr) {
        this.restTemplate = restTemplateBuilder.build();
        this.meterRegistr = meterRegistr;
        this.initOrderCounters();
    }

    private void initOrderCounters() {
        githubCounter = Counter.builder("github.calls")    // 2 - create a counter using the fluent API
                                 .tag("API", "user")
                                 .description("The number of calls made to " + GITHUB_API_ENDPOINT_TEMPLATE)
                                 .register(this.meterRegistr);
    }

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
