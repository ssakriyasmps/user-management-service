package com.services.user.management.service;

import com.services.user.management.exception.ErrorCode;
import com.services.user.management.exception.UserNotFound;
import com.services.user.management.model.Subscription;
import com.services.user.management.repository.UserRepository;
import com.services.user.management.model.User;
import com.services.user.management.repository.UserEntity;
import com.services.user.management.subscriptions.SubscriptionClient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserManagementService {

    private Logger logger = LoggerFactory.getLogger(UserManagementService.class);

    @Autowired
    UserRepository repository;

    @Autowired
    SubscriptionClient subscriptionClient;

    public String save(final User user) {
        logger.info("start saving user & subscriptions {}", user);
        UserEntity userEntity = toEntity(user);
        String userId = repository.save(userEntity).getId();
        if (!CollectionUtils.isEmpty(user.getSocialMedia())) {
            subscriptionClient.addSubscriptions(new Subscription(userId, user.getSocialMedia()));
        }
        logger.info("finished saving user & subscriptions {}", user);
        return userEntity.getId();
    }

    public void saveAll(final List<User> users) {
        List<UserEntity> entities = new ArrayList<>();
        for(User user : users){
            entities.add(toEntity(user));
        }

        repository.saveAll(entities);
    }

    public User getById(String userId) {

        if(!repository.findById(userId).isPresent()) {
            throw new UserNotFound(ErrorCode.ERTCUM0001);
        }

        Subscription subscription = subscriptionClient.getSubscriptionsByUserId(userId);

        return toModel(repository.findById(userId).get(), subscription);
    }

    public List<User> getAll() {
        final List<User> users = new ArrayList<>();
        repository.findAll().forEach(userEntity -> users.add(toModel(userEntity)));
        return users;
    }

    private UserEntity toEntity(User user){
        UserEntity userEntity = new UserEntity(user.getName(), user.getAge(),
                user.getEmail(), user.getPhone());

        return userEntity;
    }

    private User toModel(UserEntity userEntity, Subscription subscription){
        return new User(userEntity.getName(), userEntity.getAge(),
                userEntity.getEmail(), userEntity.getPhone(), subscription.getSocialMedia());
    }
    private User toModel(UserEntity userEntity){
        return new User(userEntity.getName(), userEntity.getAge(),
                userEntity.getEmail(), userEntity.getPhone(), null);
    }
}
