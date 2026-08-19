package org.helpers;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebElementCondition;

public class CustomConditions {
    public static WebElementCondition inputCondition =
            Condition.and("input condition", Condition.empty, Condition.visible);
}
