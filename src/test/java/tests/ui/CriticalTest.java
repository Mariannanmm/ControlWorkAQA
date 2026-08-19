package tests.ui;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.config.TestBase;
import org.pages.Item;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.helpers.Constants.*;
import static org.pages.CartPage.cartPage;
import static org.pages.HomePage.homePage;
import static org.pages.LoginModal.loginModal;
import static org.pages.PlaceOrderModal.placeOrderModal;
import static org.pages.ProductPage.productPage;
import static org.pages.SignUpModal.signUpModal;

public class CriticalTest extends TestBase {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    public void checkHomeItemMatchesDetail() {

        List<Item> homeItems = homePage.getItemsList();
        Item homeItem = homeItems.get(0);           // беремо перший товар

        homePage.openProductByName(homeItem.getName());
        Item detailItem = productPage.getDetailedItem();

        Assert.assertEquals(detailItem.getName(), homeItem.getName(),
                "Назва має збігатися з головною");
        Assert.assertTrue(detailItem.getPrice().contains(homeItem.getPrice()),
                "Ціна має містити ціну з головної. Home: "
                        + homeItem.getPrice() + ", Detail: " + detailItem.getPrice());
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    public void addProductToCart() {
        homePage.openProductByName(ITEM_NAME);   // ITEM_NAME = "Nokia lumia 1520"
        productPage.clickAddToCart();
        productPage.openCart();

        Assert.assertTrue(cartPage.getProductTitles().contains(ITEM_NAME),
                "Кошик має містити доданий товар: " + ITEM_NAME);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    public void placeOrder() {
        homePage.openProductByName(ITEM_NAME);
        productPage.clickAddToCart();
        productPage.openCart();
        cartPage.clickPlaceOrder();
        placeOrderModal.fillOrderForm(NAME, COUNTRY, CITY, CARD, MONTH, YEAR);
        placeOrderModal.clickPurchase();

        Assert.assertEquals(placeOrderModal.getSuccessHeader(),
                "Thank you for your purchase!",
                "Заголовок вікна підтвердження не збігається");


        String confirmation = placeOrderModal.getConfirmationText();
        Assert.assertTrue(confirmation.contains(NAME),
                "Підтвердження має містити ім'я: " + NAME + ", а було: " + confirmation);
        Assert.assertTrue(confirmation.contains(CARD),
                "Підтвердження має містити картку: " + CARD + ", а було: " + confirmation);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    public void signUpNewUser() {
        String username = "marianna" + System.currentTimeMillis();   // унікальний щоразу

        homePage.openSignUpModal();
        String alertText = signUpModal.signUp(username, PASSWORD);

        Assert.assertEquals(alertText, "Sign up successful.");
    }
    @BeforeClass
    public void createUserForLogin() {
        Selenide.open("/index.html");
        homePage.openSignUpModal();
        signUpModal.signUp(LOGIN_USERNAME, PASSWORD);   // якщо вже існує — алерт просто приймається
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    public void loginExistingUser() {
        homePage.openLoginModal();
        loginModal.login(LOGIN_USERNAME, PASSWORD);

        Assert.assertTrue(homePage.getWelcomeText().contains(LOGIN_USERNAME),
                "Навбар має показати Welcome " + LOGIN_USERNAME);
    }
}
