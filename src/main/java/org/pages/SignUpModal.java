package org.pages;

import com.codeborne.selenide.Selenide;
import org.config.PageTools;

import static com.codeborne.selenide.Condition.visible;

public class SignUpModal extends PageTools {
    public static final SignUpModal signUpModal = new SignUpModal();
    private SignUpModal() {}

    private final String usernameInput = "sign-username";
    private final String passwordInput = "sign-password";
    private final String signUpButton = "//button[@onclick='register()']";

    public String signUp(String username, String password) {
        should("id", visible, usernameInput);
        type("id", username, usernameInput);
        type("id", password, passwordInput);
        click("xpath", signUpButton);

        // відповідЬ JS-алертом читаю і приймаю
        org.openqa.selenium.Alert alert = Selenide.switchTo().alert();
        String alertText = alert.getText();
        alert.accept();
        return alertText;
    }
}
