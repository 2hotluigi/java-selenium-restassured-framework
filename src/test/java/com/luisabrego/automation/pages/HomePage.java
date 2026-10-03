package com.luisabrego.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    private static final By SLIDER = By.id("slider");
    private static final By SIGNUP_LOGIN_LINK = By.cssSelector(".shop-menu a[href='/login']");
    private static final By PRODUCTS_LINK = By.cssSelector(".shop-menu a[href='/products']");
    private static final By CART_LINK = By.cssSelector(".shop-menu a[href='/view_cart']");
    private static final By LOGGED_IN_AS = By.xpath("//a[contains(normalize-space(), 'Logged in as')]/b");
    private static final By DELETE_ACCOUNT_LINK = By.cssSelector(".shop-menu a[href='/delete_account']");
    private static final By LOGOUT_LINK = By.cssSelector(".shop-menu a[href='/logout']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage open() {
        openPath("/");
        return this;
    }

    public boolean isLoaded() {
        return isVisible(SLIDER);
    }

    public LoginPage goToSignupLogin() {
        click(SIGNUP_LOGIN_LINK);
        return new LoginPage(driver);
    }

    public ProductsPage goToProducts() {
        click(PRODUCTS_LINK);
        return new ProductsPage(driver);
    }

    public CartPage goToCart() {
        click(CART_LINK);
        return new CartPage(driver);
    }

    public String loggedInUsername() {
        return textOf(LOGGED_IN_AS);
    }

    public AccountStatusPage deleteAccount() {
        click(DELETE_ACCOUNT_LINK);
        return new AccountStatusPage(driver);
    }

    public LoginPage logout() {
        click(LOGOUT_LINK);
        return new LoginPage(driver);
    }
}
