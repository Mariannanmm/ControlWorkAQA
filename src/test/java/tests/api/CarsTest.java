package tests.api;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.api.pojos.Car;
import org.api.pojos.CarPaginated;
import org.api.pojos.Token;
import org.api.pojos.User;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.helpers.Specifications.getSpecifications;
import static org.helpers.Specifications.requestSpecification;
import static org.helpers.Specifications.responseSpecification;

public class CarsTest {

    private String token;

    @BeforeClass
    public void getToken() {
        String username = "user" + System.currentTimeMillis();
        String password = "Password123!";

        getSpecifications(requestSpecification("/users"), responseSpecification(201));
        given().body(new User(null, username, password)).when().post().then();

        getSpecifications(requestSpecification("/auth"), responseSpecification(200));
        token = given().body(new User(null, username, password))
                .when().post().then()
                .extract().as(Token.class).getAccess();
    }

    private Car createSampleCar() {
        getSpecifications(requestSpecification("/cars"), responseSpecification(201));
        return given()
                .header("Authorization", "Bearer " + token)
                .body(new Car(null, "Honda", 20000, 2021, null))
                .when().post().then()
                .extract().as(Car.class);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    public void getCarsList() {
        getSpecifications(requestSpecification("/cars"), responseSpecification(200));
        CarPaginated cars = given()
                .header("Authorization", "Bearer " + token)
                .when().get().then().log().all()
                .extract().as(CarPaginated.class);

        Assert.assertNotNull(cars.getItems());
        Assert.assertTrue(cars.getTotalItems() >= 0);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    public void createCar() {
        getSpecifications(requestSpecification("/cars"), responseSpecification(201));
        Car created = given()
                .header("Authorization", "Bearer " + token)
                .body(new Car(null, "Toyota", 25000, 2022, null))
                .when().post().then().log().all()
                .extract().as(Car.class);

        Assert.assertNotNull(created.getId());
        Assert.assertEquals(created.getBrand(), "Toyota");
        Assert.assertEquals(created.getPrice().intValue(), 25000);
        Assert.assertEquals(created.getYear().intValue(), 2022);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    public void getCarById() {
        Car created = createSampleCar();

        getSpecifications(requestSpecification("/cars/" + created.getId()), responseSpecification(200));
        Car fetched = given()
                .header("Authorization", "Bearer " + token)
                .when().get().then().log().all()
                .extract().as(Car.class);

        Assert.assertEquals(fetched.getId(), created.getId());
        Assert.assertEquals(fetched.getBrand(), created.getBrand());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    public void updateCar() {
        Car created = createSampleCar();

        getSpecifications(requestSpecification("/cars/" + created.getId()), responseSpecification(200));
        Car updated = given()
                .header("Authorization", "Bearer " + token)
                .body(new Car(null, "Mazda", 30000, 2023, null))
                .when().put().then().log().all()
                .extract().as(Car.class);

        Assert.assertEquals(updated.getBrand(), "Mazda");
        Assert.assertEquals(updated.getPrice().intValue(), 30000);
        Assert.assertEquals(updated.getYear().intValue(), 2023);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    public void partialUpdateCar() {              // PATCH
        Car created = createSampleCar();

        getSpecifications(requestSpecification("/cars/" + created.getId()), responseSpecification(200));
        Car patched = given()
                .header("Authorization", "Bearer " + token)
                .body(new Car(null, null, 35000, null, null))
                .when().patch().then().log().all()
                .extract().as(Car.class);

        Assert.assertEquals(patched.getPrice().intValue(), 35000);
        Assert.assertEquals(patched.getBrand(), created.getBrand());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    public void deleteCar() {
        Car created = createSampleCar();

        getSpecifications(requestSpecification("/cars/" + created.getId()), responseSpecification(204));
        given().header("Authorization", "Bearer " + token)
                .when().delete().then().log().all();

        getSpecifications(requestSpecification("/cars/" + created.getId()), responseSpecification(404));
        given().header("Authorization", "Bearer " + token)
                .when().get().then().log().all();
    }
}