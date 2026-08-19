package org.config;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.WebElementCondition;
import com.codeborne.selenide.WebElementsCondition;

import java.util.List;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selectors.byCssSelector;
import static com.codeborne.selenide.Selectors.byId;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PageTools {
    public String locatorFormatter(String locator, Object... args) {
        return String.format(locator, args);
    }


    public void should(String locatorType, WebElementCondition condition, String locator, Object... args) {
        switch(locatorType) {
            case "xpath":
                $(byXpath(locatorFormatter(locator, args))).shouldBe(condition);
                break;
            case "id":
                $(byId(locatorFormatter(locator, args))).shouldBe(condition);
                break;
            case "name":
                $(byName(locatorFormatter(locator, args))).shouldBe(condition);
                break;
            case "css":
                $(byCssSelector(locatorFormatter(locator, args))).shouldBe(condition);
            default:
                throw new IllegalArgumentException("Invalid locator Type: " + locatorType);

        }

    }

    public void shouldCollection(String locatorType, WebElementsCondition condition, String locator, Object... args) {
        switch(locatorType) {
            case "xpath":
                $$(byXpath(locatorFormatter(locator, args))).shouldBe(condition);
                break;
            case "id":
                $$(byId(locatorFormatter(locator, args))).shouldBe(condition);
                break;
            case "name":
                $$(byName(locatorFormatter(locator, args))).shouldBe(condition);
                break;
            default:
                throw new IllegalArgumentException("Invalid locator Type: " + locatorType);

        }
    }

    public void click(String locatorType, String locator, Object... args) {
        switch(locatorType) {
            case "xpath":
                $(byXpath(locatorFormatter(locator, args))).click();
                break;
            case "id":
                $(byId(locatorFormatter(locator, args))).click();
                break;
            case "name":
                $(byName(locatorFormatter(locator, args))).click();
                break;
            default:
                throw new IllegalArgumentException("Invalid locator Type: " + locatorType);
        }
    }
    public void type(String locatorType, String text, String locator, Object... args) {
        switch(locatorType) {
            case "xpath":
                $(byXpath(locatorFormatter(locator, args))).append(text);
                break;
            case "id":
                $(byId(locatorFormatter(locator, args))).append(text);
                break;
            case "name":
                $(byName(locatorFormatter(locator, args))).append(text);
                break;
            case "css":
                $(byCssSelector(locatorFormatter(locator, args))).append(text);
            default:
                throw new IllegalArgumentException("Invalid locator Type: " + locatorType);
        }
    }
    public String getText(String locatorType, String locator, Object... args) {
        switch (locatorType) {
            case "xpath":
                return $(byXpath(locatorFormatter(locator, args))).getText();
            case "id":
                return $(byId(locatorFormatter(locator, args))).getText();
            case "name":
                return $(byName(locatorFormatter(locator, args))).getText();
            default:
                throw new IllegalArgumentException("Invalid locator Type: " + locatorType);
        }
    }
    public boolean isCondition(String locatorType, WebElementCondition condition, String locator, Object... args) {
        switch (locatorType) {
            case "xpath":
                return $(byXpath(locatorFormatter(locator, args))).is(condition);
            case "id":
                return $(byId(locatorFormatter(locator, args))).is(condition);
            case "name":
                return $(byName(locatorFormatter(locator, args))).is(condition);
            default:
                throw new IllegalArgumentException("Invalid locator Type: " + locatorType);
        }
    }
    public ElementsCollection getElements(String locatorType, String locator, Object... args) {
        switch (locatorType) {
            case "xpath":
                return $$(byXpath(locatorFormatter(locator, args)));
            case "id":
                return $$(byId(locatorFormatter(locator, args)));
            case "name":
                return $$(byName(locatorFormatter(locator, args)));
            default:
                throw new IllegalArgumentException("Invalid locator Type: " + locatorType);
        }
    }
    public List<String> getElementsText(String locatorType, String locator, Object... args) {
        switch (locatorType) {
            case "xpath":
                return $$(byXpath(locatorFormatter(locator, args))).texts();
            case "id":
                return $$(byId(locatorFormatter(locator, args))).texts();
            case "name":
                return $$(byName(locatorFormatter(locator, args))).texts();
            default:
                throw new IllegalArgumentException("Invalid locator Type: " + locatorType);
        }
    }
}
