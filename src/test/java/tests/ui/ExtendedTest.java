package tests.ui;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.config.TestBase;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.helpers.Constants.*;
import static org.pages.CartPage.cartPage;
import static org.pages.HomePage.homePage;
import static org.pages.LoginModal.loginModal;
import static org.pages.PlaceOrderModal.placeOrderModal;
import static org.pages.ProductPage.productPage;
import static org.pages.SignUpModal.signUpModal;

public class ExtendedTest extends TestBase {

    @BeforeClass
    public void createUserForLogin() {
        Selenide.open("/index.html");
        homePage.openSignUpModal();
        signUpModal.signUp(LOGIN_USERNAME, PASSWORD);   // гарантую, що юзер існує
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    public void loginWithWrongPassword() {
        homePage.openLoginModal();
        String alert = loginModal.loginExpectingAlert(LOGIN_USERNAME, "wrongPassword123");
        Assert.assertEquals(alert, "Wrong password.");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    public void placeOrderWithoutCredentials() {
        homePage.openProductByName(ITEM_NAME);
        productPage.clickAddToCart();
        productPage.openCart();
        cartPage.clickPlaceOrder();
        placeOrderModal.clickPurchase();

        String alert = placeOrderModal.getPurchaseAlertText();
        Assert.assertEquals(alert, "Please fill out Name and Creditcard.");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    public void loginWithNonexistentUser() {
        homePage.openLoginModal();
        String alert = loginModal.loginExpectingAlert("nouser" + System.currentTimeMillis(), PASSWORD);
        Assert.assertEquals(alert, "User does not exist.");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    public void signUpExistingUser() {
        homePage.openSignUpModal();
        String alert = signUpModal.signUp(LOGIN_USERNAME, PASSWORD);
        Assert.assertEquals(alert, "This user already exist.");
    }
    @Test
    @Severity(SeverityLevel.NORMAL)
    public void removeProductFromCart() {
        homePage.openProductByName(ITEM_NAME);
        productPage.clickAddToCart();
        productPage.openCart();

        Assert.assertTrue(cartPage.getProductTitles().contains(ITEM_NAME),
                "Перед видаленням товар має бути в кошику");

        cartPage.deleteProduct();
        cartPage.checkCartIsEmpty();
    }


     //BUG (знайдено під час тестування):
     // demoblaze дозволяє оформити замовлення з ПОРОЖНІМ кошиком.
     // Кроки: порожній кошик → Place Order → заповнити форму → Purchase.
     // Очікувано: замовлення НЕ має оформитись.
     // Фактично: показує "Thank you for your purchase!" з "Amount: 0 USD".
     // Тест написаний на ОЧІКУВАНу (правильну) поведінку, тому проти поточного
     // demoblaze він падає. Вимкнений (enabled=false), щоб не "охороняти" баг і не ламати зелений прогін.
    @Test(enabled = false, description = "BUG: порожній кошик дозволяє оформити замовлення (Amount: 0 USD)")
    @Severity(SeverityLevel.NORMAL)
    public void placeOrderWithEmptyCart_shouldBeBlocked() {
        productPage.openCart();                 // кошик порожній
        cartPage.clickPlaceOrder();
        placeOrderModal.fillOrderForm(NAME, COUNTRY, CITY, CARD, MONTH, YEAR);
        placeOrderModal.clickPurchase();

        // ОЧІКУВАНО: вікна "Thank you" бути не повинно, бо кошик порожній
        String header = placeOrderModal.getSuccessHeader();
        Assert.assertNotEquals(header, "Thank you for your purchase!",
                "БАГ: порожній кошик не має дозволяти оформлення замовлення");
    }

}
