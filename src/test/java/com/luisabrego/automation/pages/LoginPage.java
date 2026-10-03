package com.luisabrego.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** The /login page contains both the login form and the "New User Signup!" form. */
public class LoginPage extends BasePage {

    private static final By LOGIN_FORM = By.cssSelector("form[action='/login']");
    private static final By LOGIN_EMAIL = By.cssSelector("input[data-qa='login-email']");
    private static final By LOGIN_PASSWORD = By.cssSelector("input[data-qa='login-password']");
    private static final By LOGIN_BUTTON = By.cssSelector("button[data-qa='login-button']");
    private static final By LOGIN_ERROR = By.cssSelector("form[action='/login'] p");

    private static final By SIGNUP_NAME = By.cssSelector("input[data-qa='signup-name']");
    private static final By SIGNUP_EMAIL = By.cssSelector("input[data-qa='signup-email']");
    private static final By SIGNUP_BUTTON = By.cssSelector("button[data-qa='signup-button']");
    private static final By SIGNUP_ERROR = By.cssSelector("form[action='/signup'] p");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        openPath("/login");
        return this;
    }

    public boolean isLoaded() {
        return currentUrl().contains("/login") && isVisible(LOGIN_FORM);
    }

    public HomePage loginAs(String email, String password) {
        type(LOGIN_EMAIL, email);
        type(LOGIN_PASSWORD, password);
        click(LOGIN_BUTTON);
        return new HomePage(driver);
    }

    public String loginError() {
        return textOf(LOGIN_ERROR);
    }

    public SignupPage startSignup(String name, String email) {
        type(SIGNUP_NAME, name);
        type(SIGNUP_EMAIL, email);
        click(SIGNUP_BUTTON);
        return new SignupPage(driver);
    }

    public String signupError() {
        return textOf(SIGNUP_ERROR);
    }
}
