package org.pages;

import com.codeborne.selenide.CollectionCondition;
import org.config.PageTools;

import java.util.List;


public class CartPage extends PageTools {

    public static final CartPage cartPage = new CartPage();

    private CartPage() {}

    private final String productTitles = "//tr[@class='success']/td[2]";
    private final String placeOrderButton = "//button[@data-target='#orderModal']";
    private final String deleteLink = "//a[text()='Delete']";

    public List<String> getProductTitles() {
        shouldCollection("xpath", CollectionCondition.sizeGreaterThan(0), productTitles);
        return getElementsText("xpath", productTitles);
    }
    public void clickPlaceOrder() {
        click("xpath", placeOrderButton);
    }
    public void deleteProduct() {
        click("xpath", deleteLink);
    }

    public void checkCartIsEmpty() {
        shouldCollection("xpath", CollectionCondition.size(0), productTitles);
    }
}
