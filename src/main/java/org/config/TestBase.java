package org.config;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.codeborne.selenide.testng.TextReport;
import io.qameta.allure.selenide.AllureSelenide;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;

@Listeners({TextReport.class})
public class TestBase {

    @BeforeTest
    public void config() {
        Configuration.baseUrl = "https://www.demoblaze.com";
        Configuration.browser = "chrome";
        Configuration.browserSize = "1280x920";
        Configuration.timeout = 10000;
        Configuration.savePageSource = false;
        Configuration.screenshots = true;

        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        Configuration.headless = headless;
        Configuration.holdBrowserOpen = !headless;

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(false));
    }

    @BeforeMethod
    public void openSite() {
        Selenide.open("/index.html");
    }

    @AfterMethod
    public void cleanWebDriver() {
        Selenide.clearBrowserCookies();
        Selenide.clearBrowserLocalStorage();
    }
}