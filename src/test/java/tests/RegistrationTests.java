package tests;

import io.restassured.http.ContentType;
import models.pojo.RegistrationBodyPojoModel;
import models.pojo.RegistrationResponsePojoModel;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationTests {

    Faker faker = new Faker();
    String username = faker.name().firstName();
    String password = faker.name().lastName();

    @Test
    public void successfulRegistrationTests_with_pojo() {
        RegistrationBodyPojoModel data = new RegistrationBodyPojoModel();
        data.setUsername(username);
        data.setPassword(password);

        RegistrationResponsePojoModel registrationResponse = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(RegistrationResponsePojoModel.class);

        assertEquals(username, registrationResponse.getUsername());
    }

    @Test
    public void existingUser400RegistrationTests() {
        RegistrationBodyPojoModel data = new RegistrationBodyPojoModel();
        data.setUsername(username);
        data.setPassword(password);

        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register/")
                .then()
                .log().all()
                .statusCode(400);
    }

    @Test
    public void unsupportedMediaType301RegistrationTests() {
        RegistrationBodyPojoModel data = new RegistrationBodyPojoModel();
        data.setUsername(username);
        data.setPassword(password);

        given()
                .log().all()
                .body(data)
                .when()
                .post("https://book-club.qa.guru/api/v1/users/register")
                .then()
                .log().all()
                .statusCode(301);
    }
}
