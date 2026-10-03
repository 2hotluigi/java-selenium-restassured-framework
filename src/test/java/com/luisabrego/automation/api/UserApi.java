package com.luisabrego.automation.api;

import static com.luisabrego.automation.api.ApiClient.request;

import com.luisabrego.automation.utils.UserData;
import io.restassured.response.Response;
import java.util.LinkedHashMap;
import java.util.Map;

public final class UserApi {

    private UserApi() {
    }

    public static Response createAccount(UserData user) {
        Map<String, String> form = new LinkedHashMap<>();
        form.put("name", user.name());
        form.put("email", user.email());
        form.put("password", user.password());
        form.put("title", user.title());
        form.put("birth_date", user.birthDay());
        form.put("birth_month", user.birthMonth());
        form.put("birth_year", user.birthYear());
        form.put("firstname", user.firstName());
        form.put("lastname", user.lastName());
        form.put("company", user.company());
        form.put("address1", user.address1());
        form.put("address2", user.address2());
        form.put("country", user.country());
        form.put("state", user.state());
        form.put("city", user.city());
        form.put("zipcode", user.zipcode());
        form.put("mobile_number", user.mobileNumber());
        return request().formParams(form).post("/createAccount");
    }

    public static Response verifyLogin(String email, String password) {
        return request()
                .formParam("email", email)
                .formParam("password", password)
                .post("/verifyLogin");
    }

    public static Response verifyLoginWithoutEmail(String password) {
        return request().formParam("password", password).post("/verifyLogin");
    }

    public static Response deleteVerifyLogin() {
        return request().delete("/verifyLogin");
    }

    public static Response getUserDetailByEmail(String email) {
        return request().queryParam("email", email).get("/getUserDetailByEmail");
    }

    public static Response deleteAccount(String email, String password) {
        return request()
                .formParam("email", email)
                .formParam("password", password)
                .delete("/deleteAccount");
    }
}
