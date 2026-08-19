package org.pages;

import com.codeborne.selenide.Selenide;
import org.config.PageTools;

import static com.codeborne.selenide.Condition.visible;

public class PlaceOrderModal extends PageTools {
    public static final PlaceOrderModal placeOrderModal = new PlaceOrderModal();
    private PlaceOrderModal() {}

    private final String name = "name";
    private final String country = "country";
    private final String city = "city";
    private final String card = "card";
    private final String month = "month";
    private final String year = "year";
    private final String purchaseButton = "//button[contains(text(), 'Purchase')]";
    private final String successMessage = "//h2[contains(text(), 'Thank you')]";

    public void fillOrderForm(String nameValue, String countryValue, String cityValue,
                              String cardValue, String monthValue, String yearValue) {
        type("id", nameValue, name);
        type("id", countryValue, country);
        type("id", cityValue, city);
        type("id", cardValue, card);
        type("id", monthValue, month);
        type("id", yearValue, year);
    }

    public void clickPurchase() {
        click("xpath", purchaseButton);
    }

    public void checkSuccessMessage() {
        should("xpath", visible, successMessage);   // чекає появу "Thank you", інакше тест падає
    }
    private final String confirmationText =
            "//div[contains(@class,'sweet-alert')]//p[contains(@class,'lead')]";

    public String getSuccessHeader() {
        should("xpath", visible, successMessage);   // чекаємо появу вікна, тоді читаємо
        return getText("xpath", successMessage);
    }

    public String getConfirmationText() {
        return getText("xpath", confirmationText);
    }
    public String getPurchaseAlertText() {
        org.openqa.selenium.Alert alert = Selenide.switchTo().alert();
        String text = alert.getText();
        alert.accept();
        return text;
    }
}
