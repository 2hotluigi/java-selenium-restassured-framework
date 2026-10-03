package com.luisabrego.automation.pages;

import com.luisabrego.automation.utils.UserData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** "Enter Account Information" form shown after starting the signup. */
public class SignupPage extends BasePage {

    private static final By TITLE_MR = By.id("id_gender1");
    private static final By TITLE_MRS = By.id("id_gender2");
    private static final By PASSWORD = By.cssSelector("input[data-qa='password']");
    private static final By BIRTH_DAY = By.id("days");
    private static final By BIRTH_MONTH = By.id("months");
    private static final By BIRTH_YEAR = By.id("years");
    private static final By FIRST_NAME = By.id("first_name");
    private static final By LAST_NAME = By.id("last_name");
    private static final By COMPANY = By.id("company");
    private static final By ADDRESS_1 = By.id("address1");
    private static final By ADDRESS_2 = By.id("address2");
    private static final By COUNTRY = By.id("country");
    private static final By STATE = By.id("state");
    private static final By CITY = By.id("city");
    private static final By ZIPCODE = By.id("zipcode");
    private static final By MOBILE_NUMBER = By.id("mobile_number");
    private static final By CREATE_ACCOUNT_BUTTON = By.cssSelector("button[data-qa='create-account']");

    public SignupPage(WebDriver driver) {
        super(driver);
    }

    public SignupPage fillAccountInformation(UserData user) {
        jsClick("Mr".equals(user.title()) ? TITLE_MR : TITLE_MRS);
        type(PASSWORD, user.password());
        selectByValue(BIRTH_DAY, user.birthDay());
        selectByValue(BIRTH_MONTH, user.birthMonth());
        selectByValue(BIRTH_YEAR, user.birthYear());
        type(FIRST_NAME, user.firstName());
        type(LAST_NAME, user.lastName());
        type(COMPANY, user.company());
        type(ADDRESS_1, user.address1());
        type(ADDRESS_2, user.address2());
        selectByValue(COUNTRY, user.country());
        type(STATE, user.state());
        type(CITY, user.city());
        type(ZIPCODE, user.zipcode());
        type(MOBILE_NUMBER, user.mobileNumber());
        return this;
    }

    public AccountStatusPage createAccount() {
        click(CREATE_ACCOUNT_BUTTON);
        return new AccountStatusPage(driver);
    }
}
