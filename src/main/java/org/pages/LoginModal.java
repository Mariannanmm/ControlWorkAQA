package org.pages;

import com.codeborne.selenide.Selenide;
import org.config.PageTools;

import static com.codeborne.selenide.Condition.visible;

public class LoginModal extends PageTools {

    public static final LoginModal loginModal = new LoginModal();
    private LoginModal() {}

    private final String usernameInput = "loginusername";
    private final String passwordInput = "loginpassword";
    private final String loginButton = "//button[@onclick='logIn()']";

    public void login(String username, String password) {
        should("id", visible, usernameInput);
        type("id", username, usernameInput);
        type("id", password, passwordInput);
        click("xpath", loginButton);
    }
    public String loginExpectingAlert(String username, String password) {
        should("id", visible, usernameInput);
        type("id", username, usernameInput);
        type("id", password, passwordInput);
        click("xpath", loginButton);

        org.openqa.selenium.Alert alert = Selenide.switchTo().alert();
        String text = alert.getText();
        alert.accept();
        return text;
    }
}