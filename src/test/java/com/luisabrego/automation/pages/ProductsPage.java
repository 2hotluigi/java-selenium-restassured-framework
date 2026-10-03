package com.luisabrego.automation.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ProductsPage extends BasePage {

    private static final By SEARCH_INPUT = By.id("search_product");
    private static final By SEARCH_BUTTON = By.id("submit_search");
    private static final By SECTION_TITLE = By.cssSelector(".features_items h2.title");
    private static final By PRODUCT_NAMES = By.cssSelector(".features_items .productinfo p");
    private static final By CART_MODAL = By.cssSelector("#cartModal .modal-content");
    private static final By CONTINUE_SHOPPING = By.cssSelector("#cartModal button.close-modal");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public ProductsPage open() {
        openPath("/products");
        return this;
    }

    public ProductsPage search(String term) {
        type(SEARCH_INPUT, term);
        click(SEARCH_BUTTON);
        return this;
    }

    public String sectionTitle() {
        return textOf(SECTION_TITLE);
    }

    public List<String> productNames() {
        return allVisible(PRODUCT_NAMES).stream()
                .map(WebElement::getText)
                .map(String::trim)
                .toList();
    }

    public ProductsPage addToCart(String productName) {
        By addButton = By.xpath(String.format(
                "//div[contains(@class,'productinfo')][p[normalize-space()='%s']]//a[contains(@class,'add-to-cart')]",
                productName));
        click(addButton);
        visible(CART_MODAL);
        click(CONTINUE_SHOPPING);
        waitUntilGone(CART_MODAL);
        return this;
    }
}
