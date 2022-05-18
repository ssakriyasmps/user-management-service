package com.services.user.management;

import org.apache.commons.io.IOUtils;
import org.mockserver.client.MockServerClient;
import org.mockserver.client.initialize.PluginExpectationInitializer;
import org.mockserver.model.MediaType;

import java.nio.charset.Charset;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class MockInitializer implements PluginExpectationInitializer {

        @Override
        public void initializeExpectations(MockServerClient mockServerClient) {
            String response = "";
            String request = "";
            try{
                request =  IOUtils.toString(getClass().getResourceAsStream("/req.json"), Charset.defaultCharset());
                response =  IOUtils.toString(getClass().getResourceAsStream("/res.json"), Charset.defaultCharset());
            }catch(Exception ex){

            }
            mockServerClient
                    .when(
                            request()
                                    .withMethod("GET")
                                    .withPath("/simpleFirst")
                                    .withBody(request)
                    )
                    .respond(
                            response()
                                    .withBody(response)
                                    .withContentType(MediaType.APPLICATION_JSON)
                    );
            mockServerClient
                    .when(
                            request()
                                    .withPath("/simpleSecond")
                    )
                    .respond(
                            response()
                                    .withBody("some second response")
                    );
        }

}
