package com.services.user.management.subscriptions;

import com.services.user.management.configuration.AppProperties;
import com.services.user.management.model.Subscription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class DefaultSubscriptionClient implements SubscriptionClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppProperties appProperties;

    private final String getSubscriptionsURI = "/subscriptions/{userId}";
    private final String addSubscriptionURI = "/subscriptions";

    @Override
    public Subscription getSubscriptionsByUserId(String userId) {

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);

        ResponseEntity<Subscription> response = restTemplate.exchange(
                appProperties.getSubscriptionManagementServiceUrl() + getSubscriptionsURI,
                    HttpMethod.GET,
                    entity,
                    Subscription.class,
                    params);

       return response.getBody();
    }


    @Override
    public void addSubscriptions(Subscription subscription) {

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Subscription> entity = new HttpEntity<>(subscription, headers);

        ResponseEntity<Subscription> response = restTemplate.exchange(
                appProperties.getSubscriptionManagementServiceUrl()+addSubscriptionURI,
                    HttpMethod.POST,
                    entity,
                    Subscription.class);

        log.info("Added subscription {}", subscription);
    }
}
