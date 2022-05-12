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
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public Subscription getSubscriptionsByUserIdWebclient(String userId) {

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        final Map<String, String> context = new HashMap<>();
        Map<String, String> params = new HashMap<>();
        params.put("userId", userId);

        List<String> socialMediaList = new ArrayList<>();
        List<Mono<Subscription>> socialMediaList1 = new ArrayList<>();
//        Flux.range(1,5)
//                .flatMap(i -> getSubscription(i, userId, context))
//                .doOnTerminate(() -> System.out.println(context))
//                .blockLast();
        Flux.range(1,5)
                .parallel()
                .flatMap(i -> getSubscription(i, userId, context))
                .sequential()
                .doOnNext(subscription -> {
                    socialMediaList.addAll(subscription.getSocialMedia());
                })
                .blockLast();

        System.out.println("Now 5: "+context);

        Subscription subscription1 = new Subscription(userId, socialMediaList);

        return subscription1;
    }

    public Map<String, Subscription> getSubscriptionsByUserIdsWebclient(List<String> userId) {
        Map<String, Subscription> subscriptionList =  new HashMap<>();
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        final Map<String, String> context = new HashMap<>();

//        Flux.range(1,5)
//                .flatMap(i -> getSubscription(i, userId, context))
//                .doOnTerminate(() -> System.out.println(context))
//                .blockLast();
        Flux.fromIterable(userId)
                .parallel()
                .flatMap(id -> getSubscription(id, context))
                .sequential()
                .doOnNext(subscription -> {
                    subscriptionList.put(subscription.getUserId(), subscription);
                })
                .blockLast();

        System.out.println("Got [" +userId.size()+ "] [" +subscriptionList.size()+ ", response received ["+ context.size() +" ]: "+context);


        return subscriptionList;
    }

    private Mono<Subscription> getSubscription(int i, String userId, Map<String, String> context){
        WebClient client = WebClient.create(appProperties.getSubscriptionManagementServiceUrl());
        Mono<Subscription> subscriptionMono =  client.get()
                .uri(getSubscriptionsURI, userId)
                .retrieve()
                .bodyToMono(Subscription.class)
                .doOnSuccess(subscription -> {
                    context.put(""+i, "Success");
                    System.out.println("Result: Success"+i);
                })
                .retry(2)
                .onErrorResume((ex) -> {
//                    log.error("Error processing [ "+i+" ] :", ex);
                    context.put(""+i, "Failed");
                    return Mono.empty();
                });

        return subscriptionMono;
    }

    private Mono<Subscription> getSubscription(String userId, Map<String, String> context){
        WebClient client = WebClient.create(appProperties.getSubscriptionManagementServiceUrl());
        Mono<Subscription> subscriptionMono =  client.get()
                .uri(getSubscriptionsURI, userId)
                .retrieve()
                .bodyToMono(Subscription.class)
                .doOnSuccess(subscription -> {
                    context.put(""+userId, "Success");
                    System.out.println("Result: Success"+userId);
                })
                .retry(2)
                 .onErrorResume((ex) -> {
//                    log.error("Error processing [ "+userId+" ] :", ex);
                    context.put(""+userId, "Failed");
                    return Mono.empty();
                });

        return subscriptionMono;
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
