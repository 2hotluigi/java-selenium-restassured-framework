package com.luisabrego.automation.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CartPage extends BasePage {

    private static final By PRODUCT_NAMES = By.cssSelector("#cart_info_table td.cart_description h4 a");
    private static final By EMPTY_CART = By.id("empty_cart");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public CartPage open() {
        openPath("/view_cart");
        return this;
    }

    public List<String> productNames() {
        return allVisible(PRODUCT_NAMES).stream()
                .map(WebElement::getText)
                .map(String::trim)
                .toList();
    }

    public String priceOf(String productName) {
        return textOf(cell(productName, "cart_price", "p"));
    }

    public String quantityOf(String productName) {
        return textOf(cell(productName, "cart_quantity", "button"));
    }

    public CartPage remove(String productName) {
        By row = row(productName);
        click(cell(productName, "cart_delete", "a"));
        waitUntilGone(row);
        return this;
    }

    public boolean isEmpty() {
        return isVisible(EMPTY_CART);
    }

    private static By row(String productName) {
        return By.xpath(String.format(
                "//table[@id='cart_info_table']//tr[.//td[@class='cart_description']//a[normalize-space()='%s']]",
                productName));
    }

    private static By cell(String productName, String cellClass, String tag) {
        return By.xpath(String.format(
                "//table[@id='cart_info_table']//tr[.//td[@class='cart_description']//a[normalize-space()='%s']]"
                        + "/td[contains(@class,'%s')]//%s",
                productName, cellClass, tag));
    }
}
