package org.pages;

import com.codeborne.selenide.CollectionCondition;
import org.config.PageTools;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.visible;

public class HomePage extends PageTools {

    public static final HomePage homePage = new HomePage();
    private HomePage() {}

    private final String productName = "//h4[@class='card-title']";
    private final String productPrice = "//h5[contains(text(), '$')]";
    private final String productDescription = "//p[@id='article']";
    private final String productLinkByName = "//a[@class='hrefch' and text()='%s']";
    private final String signUpNavButton = "signin2";
    private final String loginNavButton = "login2";
    private final String welcomeLabel = "nameofuser";


    public List<Item> getItemsList() {
        List<Item> itemsList = new ArrayList<>();
        shouldCollection("xpath", CollectionCondition.size(9), productName);

        List<String> names = getElementsText("xpath", productName);
        List<String> prices = getElementsText("xpath", productPrice);
        List<String> descriptions = getElementsText("xpath", productDescription);

        for (int i = 0; i < names.size(); i++) {
            Item item = new Item();
            item.setName(names.get(i));
            item.setDescription(descriptions.get(i));
            item.setPrice(prices.get(i));
            itemsList.add(item);
        }
        return itemsList;
    }

    public void openProductByName(String name) {
        click("xpath", productLinkByName, name);
    }

    public void openSignUpModal() {
        click("id", signUpNavButton);
    }

    public void openLoginModal() {
        click("id", loginNavButton);
    }

    public String getWelcomeText() {
        should("id", visible, welcomeLabel);
        return getText("id", welcomeLabel);
    }
}