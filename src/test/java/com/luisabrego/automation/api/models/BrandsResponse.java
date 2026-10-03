package com.luisabrego.automation.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BrandsResponse(int responseCode, List<Brand> brands) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Brand(int id, String brand) {
    }
}
