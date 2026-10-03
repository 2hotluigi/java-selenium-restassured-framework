package com.luisabrego.automation.utils;

/**
 * Test user used by both the UI (signup form) and the API (createAccount).
 * birthMonth is the numeric value ("1".."12") used by the site.
 */
public record UserData(
        String name,
        String email,
        String password,
        String title,
        String birthDay,
        String birthMonth,
        String birthYear,
        String firstName,
        String lastName,
        String company,
        String address1,
        String address2,
        String country,
        String state,
        String city,
        String zipcode,
        String mobileNumber) {
}
