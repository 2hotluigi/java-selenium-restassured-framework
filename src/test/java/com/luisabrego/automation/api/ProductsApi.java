package com.luisabrego.automation.api;

import static com.luisabrego.automation.api.ApiClient.request;

import io.restassured.response.Response;

public final class ProductsApi {

    private ProductsApi() {
    }

    public static Response getAllProducts() {
        return request().get("/productsList");
    }

    public static Response postToProductsList() {
        return request().post("/productsList");
    }

    public static Response getAllBrands() {
        return request().get("/brandsList");
    }

    public static Response searchProduct(String term) {
        return request().formParam("search_product", term).post("/searchProduct");
    }

    public static Response searchProductWithoutParameter() {
        return request().post("/searchProduct");
    }
}
