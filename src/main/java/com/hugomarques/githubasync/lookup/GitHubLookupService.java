package com.hugomarques.githubasync.lookup;

import java.util.concurrent.CompletableFuture;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GitHubLookupService {

    private final RestTemplate restTemplate;
    private static final String GITHUB_API_ENDPOINT_TEMPLATE = "https://api.github.com/users/%s";

    public GitHubLookupService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Async
    public CompletableFuture<User> findUser(final String user) throws InterruptedException {
        log.info("Looking up user: " + user);
        final String userEndpoint = String.format(GITHUB_API_ENDPOINT_TEMPLATE, user);
        final User results = restTemplate.getForObject(userEndpoint, User.class);
        // Artificial delay of 1s for demonstration purposes
        Thread.sleep(1000L);
        return CompletableFuture.completedFuture(results);

    }

}
