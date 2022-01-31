package com.services.user.management.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.services.user.management.Application;
import com.services.user.management.model.Subscription;
import com.services.user.management.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(classes = Application.class,
        webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserManagement2IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rest;

    private String url;

    @RegisterExtension
    static WireMockExtension subscriptionManagementService = WireMockExtension.newInstance()
            .options(wireMockConfig().port(9112))
            .configureStaticDsl(true)
            .build();

    @BeforeEach
    public void setup() throws Exception{
        url = "http://localhost:" + port;

        String requestBodyJson = new String(Files.readAllBytes(Paths.get(getClass()
                .getResource("/testdata/request/subscription.json").toURI())));
        Subscription subscription = new Subscription(1, Arrays.asList("facebook", "instagram"));

        subscriptionManagementService.stubFor(get(urlMatching("/subscriptions/.*"))
                .willReturn(jsonResponse(subscription, HttpStatus.OK.value())));
        subscriptionManagementService.stubFor(post("/subscriptions")
                .withRequestBody(equalToJson(requestBodyJson))
                .willReturn(created()));
    }

    @Test
    public void givenAllUsersAreAvailable_WhenISendRequestToGetAllUsers_ThenIGetAllUsers() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<List<User>> response = rest.exchange(url+"/users",
                HttpMethod.GET, entity, new ParameterizedTypeReference<List<User>>() {
                });

        assertNotNull(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<User> actualusers = response.getBody();

        assertEquals(getUsers("/testdata/users.json"), actualusers);
    }

    @Test
    public void givenAllUsersAreAvailable_WhenISendRequestToGetOneUser_ThenIGetOneUser(WireMockRuntimeInfo wmRuntimeInfo)
            throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        ResponseEntity<User> response = rest.exchange(url+"/users/1",
                HttpMethod.GET, entity, new ParameterizedTypeReference<User>() {
                });

        assertNotNull(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        User actualUser = response.getBody();

        assertEquals(getUser("/testdata/request/user.json"), actualUser);
    }

    @Test
    public void givenAllUsersAreAvailable_WhenISendPostRequest_ThenNewUserGetsAdded() throws Exception {

        String requestBodyJson = new String(Files.readAllBytes(Paths.get(getClass()
                .getResource("/testdata/request/user.json").toURI())));
        User expecteduser =  new ObjectMapper().readValue(requestBodyJson, new TypeReference<User>() {});
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBodyJson,headers);

        ResponseEntity<String> response = rest.exchange(url+"/users",
                HttpMethod.POST, entity, new ParameterizedTypeReference<String>() {
                });

        headers.setContentType(MediaType.APPLICATION_JSON);
        entity = new HttpEntity<>(headers);
        ResponseEntity<User> actualResponse = rest.exchange(url+"/users/1",
                HttpMethod.GET, entity, new ParameterizedTypeReference<User>() {
                });

        assertNotNull(response.getBody());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains("Successfully created user");
        assertThat(actualResponse.getBody()).isEqualToComparingFieldByField(expecteduser);
    }

    private List<User> getUsers(String filePath) throws Exception {
        URL fileUrl = getClass().getResource(filePath);
        List<User> expectedUsers = new ObjectMapper().readValue(fileUrl, new TypeReference<List<User>>() {});
        return expectedUsers;
    }

    private User getUser(String filePath) throws Exception {
        URL fileUrl = getClass().getResource(filePath);
        User expectedUsers = new ObjectMapper().readValue(fileUrl, new TypeReference<User>() {});
        return expectedUsers;
    }

}
