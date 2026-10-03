package com.luisabrego.automation.api;

import io.restassured.mapper.ObjectMapperType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

/**
 * automationexercise.com always answers HTTP 200 with a text/html content type and puts the
 * real status in a "responseCode" field of the JSON body. These helpers read the body as JSON
 * regardless of the content type.
 */
public final class ApiResponses {

    private ApiResponses() {
    }

    public static int responseCode(Response response) {
        return JsonPath.from(response.asString()).getInt("responseCode");
    }

    public static String message(Response response) {
        return JsonPath.from(response.asString()).getString("message");
    }

    public static <T> T as(Response response, Class<T> type) {
        return response.as(type, ObjectMapperType.JACKSON_2);
    }
}
