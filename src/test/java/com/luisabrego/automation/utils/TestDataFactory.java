package com.luisabrego.automation.utils;

import java.util.Locale;
import java.util.UUID;
import net.datafaker.Faker;

/**
 * Builds unique, realistic test data so scenarios can run in parallel without collisions.
 */
public final class TestDataFactory {

    private static final ThreadLocal<Faker> FAKER = ThreadLocal.withInitial(() -> new Faker(Locale.US));

    private TestDataFactory() {
    }

    public static UserData newUser() {
        Faker faker = FAKER.get();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String unique = UUID.randomUUID().toString().substring(0, 8);
        String emailName = firstName.toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "");

        return new UserData(
                firstName,
                "qa." + emailName + "." + unique + "@example.com",
                "Qa!" + unique,
                faker.bool().bool() ? "Mr" : "Mrs",
                String.valueOf(faker.number().numberBetween(1, 29)),
                String.valueOf(faker.number().numberBetween(1, 13)),
                String.valueOf(faker.number().numberBetween(1970, 2005)),
                firstName,
                lastName,
                faker.company().name(),
                faker.address().streetAddress(),
                faker.address().secondaryAddress(),
                "United States",
                faker.address().state(),
                faker.address().city(),
                faker.address().zipCode(),
                faker.number().digits(10));
    }
}
