package com.bookstore.api.base;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class ApiBase {
    protected RequestSpecification setup(String baseUri) {
        return RestAssured
                .given()
                .filter(new AllureRestAssured())
                .baseUri(baseUri)
                .contentType("application/json")
                .accept("application/json");
    }
}