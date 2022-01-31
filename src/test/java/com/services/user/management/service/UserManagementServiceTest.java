package com.services.user.management.service;

import com.services.user.management.model.User;
import com.services.user.management.repository.UserRepository;
import com.services.user.management.repository.UserEntity;
import com.services.user.management.subscriptions.SubscriptionClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserManagementServiceTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private SubscriptionClient subscriptionClient;

    @InjectMocks
    private UserManagementService userManagementService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSave(){
        UserEntity userEntity = new UserEntity("Joy", 20,
                "joy@domain.com", "123123");
        when(userRepository.save(ArgumentMatchers.any())).thenReturn(userEntity);
        userManagementService.save(new User("Joy", 20, "joy@domain.com", "123123", null));
        verify(userRepository).save(ArgumentMatchers.eq(userEntity));
    }

    @Test
    public void testSaveAll(){
        userManagementService.saveAll(getAllUsers());
        verify(userRepository).saveAll(ArgumentMatchers.eq(getAllUserEntities()));
    }

    @Test
    public void testFinalAll(){
        when(userRepository.findAll()).thenReturn(getAllUserEntities());

        List<User> actualUsers = userManagementService.getAll();

        assertEquals(getAllUsers(), actualUsers);
    }

    private List<UserEntity> getAllUserEntities(){

        List<UserEntity> entities = new ArrayList<>();
        entities.add(new UserEntity("Joy", 20, "joy@domain.com", "123123"));
        entities.add(new UserEntity("John", 21, "john@domain.com", "771823132"));

        return entities;
    }


    private List<User> getAllUsers(){

        List<User> users = new ArrayList<>();
        users.add(new User("Joy", 20, "joy@domain.com", "123123", null));
        users.add(new User("John", 21, "john@domain.com", "771823132", null));

        return users;
    }

}
