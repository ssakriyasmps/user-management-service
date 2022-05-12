package com.services.user.management.subscriptions;

import com.services.user.management.model.Subscription;

import java.util.List;
import java.util.Map;

public interface SubscriptionClient {
    Subscription getSubscriptionsByUserId(String userId);
    void addSubscriptions(Subscription subscription);
    Subscription getSubscriptionsByUserIdWebclient(String userId);
    Map<String, Subscription> getSubscriptionsByUserIdsWebclient(List<String> userId);
}
