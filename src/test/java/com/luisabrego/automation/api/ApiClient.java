package com.luisabrego.automation.api;

import com.luisabrego.automation.config.ConfigReader;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

/** Base request specification shared by every API client. */
public final class ApiClient {

    private ApiClient() {
    }

    public static RequestSpecification request() {
        return RestAssured.given()
                .spec(new RequestSpecBuilder()
                        .setBaseUri(ConfigReader.get("api.base.url"))
                        // Every request/response is attached to the Allure report.
                        .addFilter(new AllureRestAssured())
                        .build());
    }
}
