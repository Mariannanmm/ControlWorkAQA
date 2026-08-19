package org.pages;

import com.codeborne.selenide.Selenide;
import org.config.PageTools;

public class ProductPage extends PageTools {
    public static final ProductPage productPage = new ProductPage();
    private ProductPage() {}
    private final String name = "//h2[@class='name']";
    private final String price = "//h3[@class='price-container']";
    private final String description = "//div[@id='more-information']/p";
    private final String addToCartButton = "//a[contains(text(), 'Add to cart')]";
    private final String cartNavButton = "cartur";   // id кнопки "Cart" у навбарі

    public Item getDetailedItem() {
        Item item = new Item();
        item.setName(getText("xpath", name));
        item.setDescription(getText("xpath", description));
        item.setPrice(getText("xpath", price));
        return item;
    }
    public void clickAddToCart() {
        click("xpath", addToCartButton);
        Selenide.switchTo().alert().accept();   // demoblaze показує alert — приймаємо
    }

    public void openCart() {
        click("id", cartNavButton);
    }
}
