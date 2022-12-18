package com.test.framework.tests;

import com.google.gson.Gson;
import io.restassured.response.Response;

public class BaseAPITest {

    public static <T> T deserializeJSON(Response response, Class<T> clazz) {
        // Get the response body as a string
        String responseBody = response.getBody().asString();

        Gson gson = new Gson();
        return gson.fromJson(responseBody, clazz);
    }

    public void assertStatusCode(Response response, int statusCode) {
        response.then().statusCode(statusCode);
    }
}
