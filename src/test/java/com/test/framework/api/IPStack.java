package com.test.framework.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static com.test.framework.api.Config.BASE_URL;
import static com.test.framework.constants.APIConstants.ACCES_KEY;
import static com.test.framework.util.ResourceUtil.getProperty;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class IPStack {
    public static void setBaseUrl() {
        baseURI = BASE_URL;
    }

    public static Response getLocation(String ip) {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(ACCES_KEY, getProperty(ACCES_KEY));
        return given().contentType(ContentType.JSON).queryParams(queryParams).get(ip);
    }

}
