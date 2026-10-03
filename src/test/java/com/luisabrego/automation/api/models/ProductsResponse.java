package com.luisabrego.automation.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductsResponse(int responseCode, List<Product> products) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Product(int id, String name, String price, String brand, Category category) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Category(UserType usertype, String category) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record UserType(String usertype) {
    }
}
