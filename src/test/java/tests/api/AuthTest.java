package tests.api;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.api.pojos.Token;
import org.api.pojos.User;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.helpers.Specifications.getSpecifications;
import static org.helpers.Specifications.requestSpecification;
import static org.helpers.Specifications.responseSpecification;

public class AuthTest {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    public void authFlow() {
        String username = "user" + System.currentTimeMillis();
        String password = "Password123!";

        //юзер
        getSpecifications(requestSpecification("/users"), responseSpecification(201));
        User createdUser = given()
                .body(new User(null, username, password))
                .when()
                .post()
                .then()
                .log().all()
                .extract().as(User.class);
        Assert.assertEquals(createdUser.getUsername(), username);
        Assert.assertNotNull(createdUser.getId());

        //токен
        getSpecifications(requestSpecification("/auth"), responseSpecification(200));
        Token token = given()
                .body(new User(null, username, password))
                .when()
                .post()
                .then()
                .log().all()
                .extract().as(Token.class);
        Assert.assertNotNull(token.getAccess());

        //auth/me з Bearer-токеном
        getSpecifications(requestSpecification("/auth/me"), responseSpecification(200));
        User me = given()
                .header("Authorization", "Bearer " + token.getAccess())
                .when()
                .get()
                .then()
                .log().all()
                .extract().as(User.class);
        Assert.assertEquals(me.getUsername(), username);
    }
}