package com.services.user.management.subscriptions;

import com.services.user.management.model.Subscription;

public interface SubscriptionClient {
    Subscription getSubscriptionsByUserId(String userId);
    void addSubscriptions(Subscription subscription);
}
