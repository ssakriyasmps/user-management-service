package com.services.user.management.service;

import com.services.user.management.exception.ErrorCode;
import com.services.user.management.exception.UserNotFound;
import com.services.user.management.model.Subscription;
import com.services.user.management.model.User;
import com.services.user.management.repository.UserEntity;
import com.services.user.management.repository.UserRepository;
import com.services.user.management.subscriptions.SubscriptionClient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional
public class UserManagementService {

    private Logger logger = LoggerFactory.getLogger(UserManagementService.class);

    @Autowired
    UserRepository repository;

    @Autowired
    SubscriptionClient subscriptionClient;

    public String save(final User user) {
        logger.info("start saving user & subscriptions {}", user);
        UserEntity userEntity = toEntity(user);
        UserEntity createdUserEntity = repository.save(userEntity);
        String userId = createdUserEntity.getId();
//        createdUserEntity.setEmail("ee@domain.com");
////        repository.updateUserEntity("ee@domain.com", createdUserEntity.getName());
//        repository.updateUserEntity1(createdUserEntity);
        if (!CollectionUtils.isEmpty(user.getSocialMedia())) {

            subscriptionClient.addSubscriptions(new Subscription(userId, user.getSocialMedia()));
        }
        logger.info("finished saving user & subscriptions {}", user);
        return userEntity.getId();
    }

    public void update(final User user, final String id) {
        logger.info("start updating user {}", user);
        UserEntity userEntity = toEntity(user);

        userEntity.setLastUpdatedDate(LocalDateTime.now());
        int updated = repository.updateUserEntity1(userEntity);
        logger.info("finished updating user {}", updated);
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

        Subscription subscription = subscriptionClient.getSubscriptionsByUserIdWebclient(userId);

        return toModel(repository.findById(userId).get(), subscription);
    }

    public List<User> getAll() {
        final List<User> users = new ArrayList<>();
        Iterable<UserEntity> userEntities = repository.findAll();
        List<String> userIds = new ArrayList<>();
        for(UserEntity user : userEntities){
            userIds.add(user.getId());
        }
        Map<String, Subscription> subscriptions = subscriptionClient.getSubscriptionsByUserIdsWebclient(userIds);
        for(UserEntity user : userEntities){
            if(subscriptions.containsKey(user.getId())) {
                users.add(toModel(user, subscriptions.get(user.getId())));
            }
        }

        userEntities.forEach(userEntity -> users.add(toModel(userEntity)));

        return users;
    }

    private UserEntity toEntity(User user){
        UserEntity userEntity = new UserEntity(user.getName(), user.getAge(),
                user.getEmail(), user.getPhone());
        userEntity.setLastUpdatedDate(LocalDateTime.now());

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
