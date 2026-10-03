package com.luisabrego.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** Confirmation page shown after creating or deleting an account. */
public class AccountStatusPage extends BasePage {

    private static final By HEADING = By.cssSelector("h2[data-qa^='account-']");
    private static final By CONTINUE_BUTTON = By.cssSelector("a[data-qa='continue-button']");

    public AccountStatusPage(WebDriver driver) {
        super(driver);
    }

    public String heading() {
        return textOf(HEADING);
    }

    public HomePage continueToHome() {
        click(CONTINUE_BUTTON);
        return new HomePage(driver);
    }
}
