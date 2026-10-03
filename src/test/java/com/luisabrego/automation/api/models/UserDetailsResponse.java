package com.luisabrego.automation.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserDetailsResponse(int responseCode, User user) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record User(
            int id,
            String name,
            String email,
            @JsonProperty("first_name") String firstName,
            @JsonProperty("last_name") String lastName,
            String company,
            String country,
            String state,
            String city,
            String zipcode) {
    }
}
